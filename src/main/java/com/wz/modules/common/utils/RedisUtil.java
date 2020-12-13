package com.wz.modules.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

@Component
public class RedisUtil {

	private Logger log = Logger.getLogger(RedisUtil.class);

	@Autowired
	private JedisPool jedisPool;

	private Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis;
		} catch (JedisConnectionException e) {
			log.error("获取Redis 异常", e);
			throw e;
		}
	}

	protected <T> T execute(RedisCallback<T> callback, Object... args) {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			Object index = ((Object[]) args)[0];
			if (null != index && Integer.parseInt(index.toString()) > 0 && Integer.parseInt(index.toString()) < 16) {
				jedis.select(Integer.parseInt(index.toString()));
			} else {

			}
			return callback.call(jedis, args);
		} catch (JedisConnectionException e) {
			if (jedis != null) {
				this.releaseRedis(jedis);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			if (jedis != null) {
				this.releaseRedis(jedis);
			}
		}
		return null;
	}

	/**
	 * @Description: 获取Hash（哈希）
	 * @author Wanglei Date: 2015-8-12 上午9:29:38
	 * @param key
	 * @param field
	 * @return
	 * @version [版本号1.0.0]
	 */
	public String hget(int index, String key, String field) {
		return execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				String field = ((Object[]) parms)[2].toString();
				return jedis.hget(key, field);
			}
		}, index, key, field);
	}

	public Map<String, String> fuzzyGet(int index, String key, String field) {
		return execute(new RedisCallback<Map<String, String>>() {
			@Override
			public Map<String, String> call(Jedis jedis, Object params) {
				String key = ((Object[]) params)[1].toString();
				Map<String, String> maps = jedis.hgetAll(key);
				Map<String, String> newMaps = new HashMap<String, String>();
				for (String k : maps.keySet()) {
					if (k.startsWith(field)) {
						newMaps.put(k, maps.get(k));
					}
				}
				return newMaps;
			}
		}, index, key);
	}

	public Map<String, String> hgetAll(int index, String key) {
		return execute(new RedisCallback<Map<String, String>>() {
			@Override
			public Map<String, String> call(Jedis jedis, Object params) {
				String key = ((Object[]) params)[1].toString();
				return jedis.hgetAll(key);
			}
		}, index, key);
	}

	public void hset(int index, String key, String field, String value) {
		execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				String field = ((Object[]) parms)[2].toString();
				String value = ((Object[]) parms)[3].toString();
				jedis.hset(key, field, value);
				return null;
			}
		}, index, key, field, value);
	}

	public void hdel(int index, String key, String field) {
		execute(new RedisCallback<String>() {
			public String call(Jedis jedis, Object parms) {
				String key = ((Object[]) parms)[1].toString();
				String field = ((Object[]) parms)[2].toString();
				jedis.hdel(key, field);
				return null;
			}
		}, index, key, field);
	}

	/**
	 * 保存对象到Redis 对象不过期
	 *
	 * @param key
	 *            待缓存的key
	 * @param object
	 *            待缓存的对象
	 * @return 返回是否缓存成功
	 */
	public boolean setObject(String key, Object object) throws Exception {
		return setObject(key, object, -1);
	}

	/**
	 * 保存对象到Redis 并设置超时时间
	 *
	 * @param key
	 *            缓存key
	 * @param object
	 *            缓存对象
	 * @param timeout
	 *            超时时间
	 * @return 返回是否缓存成功
	 * @throws Exception
	 *             异常上抛
	 */
	public boolean setObject(String key, Object object, int timeout) throws Exception {
		String value = SerializeUtil.serialize(object);
		boolean result = false;
		try {
			// 为-1时不设置超时时间
			if (timeout != -1) {
				setString(key, value, timeout);
			} else {
				setString(key, value);
			}
			result = false;
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * 从Redis中获取对象
	 *
	 * @param key
	 *            待获取数据的key
	 * @return 返回key对应的对象
	 */
	public Object getObject(String key) throws Exception {
		Object object = null;
		try {
			String serializeObj = getString(key);
			if (null == serializeObj || serializeObj.length() == 0) {
				object = null;
			} else {
				object = SerializeUtil.deserialize(serializeObj);
			}
		} catch (Exception e) {
			throw e;
		}
		return object;
	}

	/**
	 * 缓存String类型的数据,数据不过期
	 *
	 * @param key
	 *            待缓存数据的key
	 * @param value
	 *            需要缓存的额数据
	 * @return 返回是否缓存成功
	 */
	public boolean setString(String key, String value) throws Exception {
		return setString(key, value, -1);
	}

	/**
	 * 缓存String类型的数据并设置超时时间
	 *
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * @param timeout
	 *            超时时间
	 * @return 返回是否缓存成功
	 */
	public boolean setString(String key, String value, int timeout) throws Exception {
		String result;
		Jedis jedis = null;
		try {
			jedis = getJedis();
			result = jedis.set(key, value);
			if (timeout != -1) {
				jedis.expire(key, timeout);
			}
			if ("OK".equals(result)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			releaseRedis(jedis);
		}
	}

	/**
	 * 获取String类型的数据
	 *
	 * @param key
	 *            需要获取数据的key
	 * @return 返回key对应的数据
	 */
	@SuppressWarnings("deprecation")
	public String getString(String key) throws Exception {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			return jedis.get(key);
		} catch (Exception e) {
			throw e;
		} finally {
			releaseRedis(jedis);
		}
	}

	/**
	 * Jedis 对象释放
	 * 
	 * @param jedis
	 */
	public void releaseRedis(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	/**
	 * 删除缓存中的数据
	 *
	 * @param key
	 *            需要删除数据的key
	 * @return 返回是否删除成功
	 */
	public boolean del(String key) throws Exception {
		Long num;
		Jedis jedis = null;
		Boolean result = false;
		try {
			jedis = getJedis();
			num = jedis.del(key);
			if (num.equals(1L)) {
				result = true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			releaseRedis(jedis);
		}
		return result;
	}

}
