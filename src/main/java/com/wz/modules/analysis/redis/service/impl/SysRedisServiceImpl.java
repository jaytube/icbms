package com.wz.modules.analysis.redis.service.impl;

import com.wz.config.RedisConfig;
import com.wz.modules.analysis.redis.service.SysRedisService;
import com.wz.modules.common.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
public class SysRedisServiceImpl implements SysRedisService {

    private static Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private RedisUtil redisUtil;

    /*@Override
    public CommonResponse<List<RedisConfig>> list() {

        return CommonResponse.success(listAll());
    }

    public List<RedisConfig> listAll() {
        List<RedisConfig> list = new ArrayList<>();
        list.add(redisConfig);
        return list;
    }

    @Override
    public CommonResponse<List<Map<String, String>>> select() {
        List<RedisConfig> list = listAll();
        List<Map<String, String>> selectList = new ArrayList<>();
        list.forEach(redisConfig -> {
            Map<String, String> map = new HashMap<>();
            map.put("host", redisConfig.getHostName());
            map.put("name", redisConfig.getHostName());
            selectList.add(map);
        });
        return CommonResponse.success(selectList);
    }

    @Override
    public CommonResponse<List<SysRedis>> cacheList(CacheRedisQueryVo vo) {
        if (redisConfig == null) {
            return CommonResponse.error("配置信息不存在");
        }
        String match = "*";
        if (StringUtils.isNotBlank(vo.getKeyword())) {
            match = "*" + vo.getKeyword() + "*";
        }

        Jedis jedis = redisUtil.getJedis();
        ScanParams scanParams = new ScanParams();
        int count = 10000;
        scanParams.count(count);

        scanParams.match(match);
        int scanRet = 0;

        List<String> keyList = new ArrayList<>();
        do {
            ScanResult<String> scanResult = jedis.scan(scanRet, scanParams);
            scanRet = scanResult.getCursor();
            //scan 487439 MATCH * COUNT 10000
            log.info("scan {} MATCH {} COUNT {}", scanRet, match, count);

            keyList.addAll(scanResult.getResult());
        } while (scanRet != 0);

        if (!CollectionUtils.isEmpty(keyList)) {
            List<SysRedis> list = new ArrayList<>();
            int start = vo.getPageNo() == 1 ? 0 : (vo.getPageNo() - 1) * vo.getPageSize();
            for (int i = start, j = 0; i < keyList.size() && j < vo.getPageSize(); i++, j++) {
                String key = keyList.get(i);

                SysRedis base = baseInfo(key, jedis);
                list.add(base);
            }
            jedis.close();
            int total = keyList.size();
            // 清空加载出来全部
            keyList.clear();
            return CommonResponse.success(200, total, list);
        }
        return CommonResponse.success(200, 0, "暂无数据");
    }

    @Override
    public CommonResponse<SysRedisDto> cacheDetails(CacheRedisQueryVo vo) {
        String type = vo.getType();
        String key = vo.getKeyword();
        Long elCount = 0L;
        Jedis jedis = redisUtil.getJedis();
        Long expire = jedis.ttl(key);
        String expireStr = "";
        if (expire == -1) {
            expireStr = "永不过期";
        } else {
            expireStr = DateUtils.getTimeStrBySecond(expire);
        }
        SysRedisDto dto = new SysRedisDto();
        if ("string".equals(type)) {
            elCount = 1L;
            String value = jedis.get(key);
            dto.setRedisValue(value);
        } else if ("list".equals(type)) {
            elCount = jedis.llen(key);

            if (elCount > 0) {
                List<String> listValues = jedis.lrange(key, 0, elCount);
                if (listValues != null && listValues.size() > 0) {
                    List<Map<String, Object>> values = new ArrayList<>();
                    int no = 1;
                    for (String m : listValues) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("no", no);
                        map.put("svalue", m);
                        values.add(map);
                        no++;
                    }
                    dto.setValues(values);
                }
            }
        } else if ("hash".equals(type)) {
            elCount = jedis.hlen(key);
            Map<String, String> hgetAll = jedis.hgetAll(key);
            if (hgetAll != null && hgetAll.size() > 0) {
                List<Map<String, Object>> values = new ArrayList<>();
                int no = 1;
                for (String hk : hgetAll.keySet()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("no", no);
                    map.put("hkey", hk);
                    map.put("hvalue", hgetAll.get(hk));
                    values.add(map);
                    no++;
                }
                dto.setValues(values);
            }
        } else if ("set".equals(type)) {
            elCount = jedis.scard(key);
            Set<String> smembers = jedis.smembers(key);
            if (smembers != null && smembers.size() > 0) {
                List<Map<String, Object>> values = new ArrayList<>();
                int no = 1;
                for (String m : smembers) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("no", no);
                    map.put("svalue", m);
                    values.add(map);
                    no++;
                }
                dto.setValues(values);
            }
        } else if ("zset".equals(type)) {
            elCount = jedis.zcard(key);
            long zcard = jedis.zcard(key);
            Set<String> zset = jedis.zrange(key, 0, zcard);
            if (zset != null && zset.size() > 0) {
                List<Map<String, Object>> values = new ArrayList<>();
                int no = 1;
                for (String m : zset) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("no", no);
                    map.put("zvalue", m);
                    double zscore = jedis.zscore(key, m);
                    map.put("zscore", zscore);
                    values.add(map);
                    no++;
                }
                dto.setValues(values);
            }
        }

        if (dto.getValues() != null && dto.getValues().size() > 0) {
            // 过滤查询值列表关键字
            List<Map<String, Object>> valueAll = dto.getValues();
            List<Map<String, Object>> filterValueAll = new ArrayList<>();
            if (StringUtils.isNotBlank(vo.getCacheKeyword())) {
                for (int i = 0; i < valueAll.size(); i++) {
                    Map<String, Object> map = valueAll.get(i);
                    String hk = "";
                    if ("hash".equals(type)) {
                        hk = map.get("hkey").toString();
                    } else if ("zset".equals(type)) {
                        hk = map.get("zvalue").toString();
                    } else if ("set".equals(type)) {
                        hk = map.get("svalue").toString();
                    } else if ("list".equals(type)) {
                        hk = map.get("svalue").toString();
                    }
                    if (hk.contains(vo.getCacheKeyword())) {
                        filterValueAll.add(map);
                    }
                }
                elCount = Long.parseLong(filterValueAll.size() + "");
            } else {
                filterValueAll.addAll(valueAll);
            }

            // 分页
            List<Map<String, Object>> values = new ArrayList<>();
            int start = vo.getPageNo() == 1 ? 0 : (vo.getPageNo() - 1) * vo.getPageSize();
            for (int i = start, j = 0; i < filterValueAll.size() && j < vo.getPageSize(); i++, j++) {
                Map<String, Object> map = filterValueAll.get(i);

                values.add(map);
            }

            dto.setValues(values);
        }


        dto.setDataType(type);
        dto.setExpireStr(expireStr);
        dto.setElCount(elCount);
        dto.setRedisKey(key);
        dto.setOldRedisKey(key);
        jedis.close();
        return CommonResponse.success(dto);
    }

    @Override
    public CommonResponse cacheCreate(CacheRedisVo vo) {
        Jedis jedis = redisUtil.getJedis();
        String type = vo.getDataType();
        if ("string".equals(type)) {
            jedis.set(vo.getRedisKey(), vo.getRedisValue());
        } else if ("list".equals(type)) {
            // 原始value
            if (StringUtils.isNotBlank(vo.getOldRedisKey())) {
                jedis.lrem(vo.getRedisKey(), 1, vo.getOldRedisKey());
                log.info("list - {} 从 {} 移除", vo.getOldRedisKey(), vo.getRedisKey());
            }
            // 将一个值插入到已存在的列表头部
            jedis.lpush(vo.getRedisKey(), vo.getRedisValue());
//            try {
//                JSONArray array = JSON.parseArray(vo.getRedisValue());
//                for (int i = 0; i < array.size(); i++) {
//                    Object obj = array.get(i);
//                    jedis.lset(vo.getRedisKey(), i, JSON.toJSONString(obj));
//                }
//            } catch (Exception e) {
//                log.error("格式转换错误", e);
//                return Response.error("格式不正确");
//            }
        } else if ("hash".equals(type)) {
            if (StringUtils.isNotBlank(vo.getOldRedisKey())) {
                // 修改 删除以前的key
                jedis.hdel(vo.getRedisKey(), vo.getOldRedisKey());
                log.info("hash - {} 从 {} 移除", vo.getOldRedisKey(), vo.getRedisKey());
            }
            //新增
            jedis.hset(vo.getRedisKey(), vo.getRedisHKey(), vo.getRedisValue());
        } else if ("set".equals(type)) {
            if (StringUtils.isNotBlank(vo.getOldRedisKey())) {
                // 修改 删除以前的key
                jedis.srem(vo.getRedisKey(), vo.getOldRedisKey());
                log.info("set - {} 从 {} 移除", vo.getOldRedisKey(), vo.getRedisKey());
            }
            // 新增
            jedis.sadd(vo.getRedisKey(), vo.getRedisValue());
        } else if ("zset".equals(type)) {
            if (StringUtils.isNotBlank(vo.getOldRedisKey())) {
                // 修改 删除以前的key
                jedis.zrem(vo.getRedisKey(), vo.getOldRedisKey());
                log.info("zset - {} 从 {} 移除", vo.getOldRedisKey(), vo.getRedisKey());
            }
            // 新增
            jedis.zadd(vo.getRedisKey(), vo.getScore(), vo.getRedisValue());
        }

        boolean exists = jedis.exists(vo.getRedisKey());
        if (exists) {
            if (vo.getExpire() > 0) {
                if (StringUtils.isNotBlank(vo.getUnit())) {
                    int expire = DateUtils.getExpire(vo.getExpire(), vo.getUnit());
                    jedis.expire(vo.getRedisKey(), expire);
                } else {
                    jedis.expire(vo.getRedisKey(), vo.getExpire());
                }
            }
            jedis.close();
            return CommonResponse.success("添加成功");
        }
        jedis.close();
        return CommonResponse.error("添加失败");
    }

    @Override
    public CommonResponse cacheDelete(CacheRedisVo vo) {
        String type = vo.getDataType();
        Jedis jedis = redisUtil.getJedis();
        boolean exists = jedis.exists(vo.getRedisKey());
        if (exists) {
            long count = 0;
            if ("string".equalsIgnoreCase(type)) {
                count = jedis.del(vo.getRedisKey());
            } else if ("hash".equals(type)) {

                if (StringUtils.isNotBlank(vo.getRedisKey()) && StringUtils.isNotBlank(vo.getRedisValue())) {
                    count = jedis.hdel(vo.getRedisKey(), vo.getRedisHKey());
                    log.info("hash - 单个 {} 从 {} 中移除", vo.getRedisHKey(), vo.getRedisKey());
                } else {
                    Map<String, String> hgetAll = jedis.hgetAll(vo.getRedisKey());
                    if (hgetAll != null && hgetAll.size() > 0) {
                        for (String hk : hgetAll.keySet()) {
                            count += jedis.hdel(vo.getRedisKey(), hk);
                        }
                        log.info("hash - 移除全部");
                    }
                }

            } else if ("set".equals(type)) {
                if (StringUtils.isNotBlank(vo.getRedisValue())) {
                    jedis.srem(vo.getRedisKey(), vo.getRedisValue());
                    log.info("set - 单个 {} 从 {} 中移除", vo.getRedisValue(), vo.getRedisKey());
                } else {
                    // 删除全部
                    jedis.spop(vo.getRedisKey());
                    log.info("set - 移除全部");
                }
                count = 1;
            } else if ("zset".equals(type)) {
                if (StringUtils.isNotBlank(vo.getRedisValue())) {
                    jedis.zrem(vo.getRedisKey(), vo.getRedisValue());
                    log.info("zset - 单个 {} 从 {} 中移除", vo.getRedisValue(), vo.getRedisKey());
                    count = 1;
                } else {
                    long zcard = jedis.zcard(vo.getRedisKey());
                    Set<String> zset = jedis.zrange(vo.getRedisKey(), 0, zcard);
//                    log.info("zset size={}", zset.size());
                    for (String s : zset) {
                        jedis.zrem(vo.getRedisKey(), s);
                        count++;
                    }
                    log.info("zset - 移除全部");
                }
            } else if ("list".equals(type)) {
                // 删除单个
                if (StringUtils.isNotBlank(vo.getRedisValue())) {
                    count = 1;
                    jedis.lrem(vo.getRedisKey(), 1, vo.getRedisValue());
                    log.info("list - 单个 {} 从 {} 中移除", vo.getRedisValue(), vo.getRedisKey());
                } else {
                    long len = jedis.llen(vo.getRedisKey());
                    if (len > 0) {
                        List<String> lvalues = jedis.lrange(vo.getRedisKey(), 0, len);
                        for (String value : lvalues) {
                            jedis.lrem(vo.getRedisKey(), 1, value);
                            count++;
                        }
                        log.info("list - 移除全部");
                    }
                }
                // 删除全部
            }

            jedis.close();
            if (count > 0) {
                return CommonResponse.success(200, (int) count, "删除成功");
            }
        }
        return CommonResponse.error("删除失败");
    }

    @Override
    public CommonResponse cacheNameUpdate(CacheRedisVo vo) {
        String type = vo.getDataType();
        Jedis jedis = redisUtil.getJedis();
        boolean exists = jedis.exists(vo.getOldRedisKey());
        if (exists) {
//            if ("string".equalsIgnoreCase(type)) {
//            } else if ("hash".equalsIgnoreCase(type)) {
//            } else if ("set".equalsIgnoreCase(type)) {
//            } else if ("zset".equalsIgnoreCase(type)) {
//            }
            jedis.rename(vo.getOldRedisKey(), vo.getRedisKey());
            return CommonResponse.success("更新成功");
        } else {
            return CommonResponse.error(vo.getOldRedisKey() + " 不存在或已失效");
        }
    }

    @Override
    public CommonResponse cacheValueCreate(CacheRedisVo vo) {
//        String type = vo.getDataType();
        Jedis jedis = redisUtil.getJedis();
        boolean exists = jedis.exists(vo.getRedisKey());
        if (exists) {
            Long expire = jedis.ttl(vo.getRedisKey());
            vo.setExpire(Integer.parseInt(expire + ""));
            return cacheCreate(vo);
        } else {
            jedis.close();
            return CommonResponse.error(vo.getRedisKey() + " 不存在或已失效");
        }
    }

    @Override
    public CommonResponse cacheValueUpdate(CacheRedisVo vo) {
        String type = vo.getDataType();
        Jedis jedis = redisUtil.getJedis();
        boolean exists = jedis.exists(vo.getRedisKey());
        if (exists) {
            Long expire = jedis.ttl(vo.getRedisKey());
            vo.setExpire(Integer.parseInt(expire + ""));
            CommonResponse response = new CommonResponse();
            if ("string".equals(type)) {
                response = cacheCreate(vo);
            } else if ("list".equals(type)) {
                response = cacheCreate(vo);
            } else if ("hash".equals(type)) {
                response = cacheCreate(vo);
            } else if ("set".equals(type)) {
                response = cacheCreate(vo);
            } else if ("zset".equals(type)) {
                response = cacheCreate(vo);
            }

            if (response.getCode() == 200) {
                response.setMsg("更新成功");
            } else {
                response.setMsg("更新失败");
            }
            return response;
        } else {
            jedis.close();
            return CommonResponse.error(vo.getRedisKey() + " 不存在或已失效");
        }
    }

    *//**
     * 更新过期时间
     *
     * @param vo
     * @return
     *//*
    @Override
    public CommonResponse cacheExpireUpdate(CacheRedisVo vo) {
        Jedis jedis = redisUtil.getJedis();
        boolean exists = jedis.exists(vo.getRedisKey());
        if (exists) {
            if (vo.getExpire() == 0) {
                return CommonResponse.error("过期时间不能为0");
            }
            if (vo.getExpire() > 0) {
                if (StringUtils.isNotBlank(vo.getUnit())) {
                    int expire = DateUtils.getExpire(vo.getExpire(), vo.getUnit());
                    jedis.expire(vo.getRedisKey(), expire);
                } else {
                    jedis.expire(vo.getRedisKey(), vo.getExpire());
                }
            } else {
                // 如果小于0 就移除key的有效时间
                jedis.persist(vo.getRedisKey());
            }
            jedis.close();
            return CommonResponse.success("过期时间更新成功");
        }
        jedis.close();
        return CommonResponse.error("过期时间更新失败");
    }

    @Override
    public CommonResponse selectDb(String host) {
        log.info("HOST： {}", host);

        // 创建redis连接
        JedisShardInfo jedisShardInfo = new JedisShardInfo(host, redisConfig.getPort());
        if (StringUtils.isNotBlank(redisConfig.getPassword())) {
            jedisShardInfo.setPassword(redisConfig.getPassword());
        } else {
            jedisShardInfo.setPassword(null);
        }
        Jedis jedis = new Jedis(jedisShardInfo);

        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            log.info("SELECT {}", i);

            try {
                // 检查db是否连接扫描成功
                String selectDb = jedis.select(i);
                log.info(selectDb);
                Map<String, String> map = new HashMap<>(2);
                map.put("name", "db" + i);
                map.put("db", i + "");

                list.add(map);
            } catch (Exception e) {
                log.info(e.getMessage());
                if (e.getMessage().contains("NOAUTH Authentication required")) {
                    return CommonResponse.error("需要密码认证");
                }

                if (e.getMessage().contains("ERR DB index is out of range")) {
                    // 查询数据库下标越界时 跳出循环
                    break;
                }
            }
        }

        if (list.size() > 0) {
            return CommonResponse.success(list);
        }
        // 关闭连接
        jedis.close();
        // ERR DB index is out of range ：ERR数据库索引超出范围
        return CommonResponse.error("ERR数据库索引超出范围");
    }

    private SysRedis baseInfo(String key, Jedis jedis) {
        String type = jedis.type(key);
        Long expire = jedis.ttl(key);
        log.info("key={}，type={}", key, type);
        String expireStr = "";
        if (expire == -1) {
            expireStr = "永不过期";
        } else {
            expireStr = DateUtils.getTimeStrBySecond(expire);
        }
        SysRedis sysRedis = new SysRedis(type, key, null, expireStr);
        Long elCount = 0L;
        if ("string".equals(type)) {
            elCount = 1L;
        } else if ("list".equals(type)) {
            elCount = jedis.llen(key);
        } else if ("hash".equals(type)) {
            elCount = jedis.hlen(key);
        } else if ("set".equals(type)) {
            elCount = jedis.scard(key);
        } else if ("zset".equals(type)) {
            elCount = jedis.zcard(key);
        }
        sysRedis.setElCount(elCount);
        return sysRedis;
    }*/
}
