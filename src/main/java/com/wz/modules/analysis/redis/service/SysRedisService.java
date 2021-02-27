package com.wz.modules.analysis.redis.service;

import com.wz.config.RedisConfig;
import com.wz.modules.analysis.redis.dto.SysRedisDto;
import com.wz.modules.analysis.redis.po.SysRedis;
import com.wz.modules.analysis.redis.vo.CacheRedisQueryVo;
import com.wz.modules.analysis.redis.vo.CacheRedisVo;
import com.wz.modules.common.utils.CommonResponse;

import java.util.List;
import java.util.Map;

public interface SysRedisService {

    /*CommonResponse<List<RedisConfig>> list();

    CommonResponse<List<Map<String, String>>> select();

    CommonResponse<List<SysRedis>> cacheList(CacheRedisQueryVo vo);

    CommonResponse<SysRedisDto> cacheDetails(CacheRedisQueryVo vo);

    CommonResponse cacheCreate(CacheRedisVo vo);

    CommonResponse cacheDelete(CacheRedisVo vo);

    CommonResponse cacheNameUpdate(CacheRedisVo vo);

    CommonResponse cacheValueCreate(CacheRedisVo vo);

    CommonResponse cacheValueUpdate(CacheRedisVo vo);

    CommonResponse cacheExpireUpdate(CacheRedisVo vo);

    CommonResponse selectDb(String host);*/
}
