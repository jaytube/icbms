package com.wz.modules.common.utils;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

@Component
public class RedisUtil {

	private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
    private StringRedisTemplate redisTemplate;

	/*@Autowired
	private RedisTemplate<String, Object> objRedisTemplate;*/

	/**
	 * @Description: 获取Hash（哈希）
	 * @author Wanglei Date: 2015-8-12 上午9:29:38
	 * @param key
	 * @param field
	 * @return
	 * @version [版本号1.0.0]
	 */
	public String hget(int index, String key, String field) {
	    return hget(key, field);
	}

    public String hget(String key, String field) {
	    return (String) redisTemplate.opsForHash().get(key, field);
    }

	public Map<String, String> fuzzyGet(int index, String key, String field) {
		return fuzzyGet(key, field);
	}

	public Map<String, String> fuzzyGet(String key, String field) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if(MapUtils.isEmpty(entries))
            return new HashMap<>();

        Map<String, String> result = new HashMap<>();
        entries.entrySet().stream().forEach(e -> {
            Object k = e.getKey();
            if(k.toString().startsWith(field))
                result.put(k.toString(), e.getValue().toString());
        });

        return result;
    }

	public Map<String, String> hgetAll(int index, String key) {
	    return hgetAll(key);
	}

	public Map<String, String> hgetAll(String key) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        Map<String, String> result = new HashMap<>();
		map.entrySet().stream().forEach(e -> {
			result.put(e.getKey().toString(), e.getValue().toString());
		});
        return result;
	}

	public void hset(int index, String key, String field, String value) {
		hset(key, field, value);
	}

    public void hset(String key, String field, String value) {
	    redisTemplate.opsForHash().put(key, field, value);
    }

	public void hdel(int index, String key, String field) {
		hdel(key, field);
	}

    public void hdel(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
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
	/*public boolean setObject(String key, Object object) throws Exception {
		return setObject(key, object, -1);
	}*/

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
	/*public boolean setObject(String key, Object object, int timeout) throws Exception {
		objRedisTemplate.opsForValue().set(key, object, timeout);
		return true;
	}

	*//**
	 * 从Redis中获取对象
	 *
	 * @param key
	 *            待获取数据的key
	 * @return 返回key对应的对象
	 *//*
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
	}*/

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
	    redisTemplate.opsForValue().set(key, value, timeout);
	    return true;
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
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 删除缓存中的数据
	 *
	 * @param key
	 *            需要删除数据的key
	 * @return 返回是否删除成功
	 */
	public boolean del(String key) throws Exception {
		redisTemplate.delete(key);
		return true;
	}

}
