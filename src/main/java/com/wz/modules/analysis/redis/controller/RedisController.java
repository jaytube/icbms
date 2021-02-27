package com.wz.modules.analysis.redis.controller;

import com.wz.config.RedisConfig;
import com.wz.modules.analysis.redis.dto.SysRedisDto;
import com.wz.modules.analysis.redis.po.SysRedis;
import com.wz.modules.analysis.redis.service.SysRedisService;
import com.wz.modules.analysis.redis.vo.CacheRedisQueryVo;
import com.wz.modules.analysis.redis.vo.CacheRedisVo;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.common.utils.RedisUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/redis")
@Api(tags = "Redis数据管理接口")

public class RedisController {

    /*@Autowired
    private SysRedisService sysRedisService;

    @Autowired
    private RedisConfig redisConfig;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping(path = "/cache/query")
    public CommonResponse<List<SysRedis>> queryCache(@RequestBody CacheRedisQueryVo vo) {

        return sysRedisService.cacheList(vo);
    }

    @PostMapping(path = "/cache/details")
    public CommonResponse<SysRedisDto> queryCacheDetails(@RequestBody CacheRedisQueryVo vo) {

        return sysRedisService.cacheDetails(vo);
    }

    @PostMapping(path = "/cache/create")
    public CommonResponse cacheCreate(@RequestBody CacheRedisVo vo) {

        return sysRedisService.cacheCreate(vo);
    }

    @PostMapping(path = "/cache/name/update")
    public CommonResponse cacheNameUpdate(@RequestBody CacheRedisVo vo) {

        return sysRedisService.cacheNameUpdate(vo);
    }

    @PostMapping(path = "/cache/expire/update")
    public CommonResponse cacheExpireUpdate(@RequestBody CacheRedisVo vo) {

        return sysRedisService.cacheExpireUpdate(vo);
    }

    @PostMapping(path = "/cache/value/create")
    public CommonResponse cacheValueCreate(@RequestBody CacheRedisVo vo) {

        return sysRedisService.cacheValueCreate(vo);
    }

    @PostMapping(path = "/cache/value/update")
    public CommonResponse cacheValueUpdate(@RequestBody CacheRedisVo vo) {

        return sysRedisService.cacheValueUpdate(vo);
    }

    @PostMapping(path = "/cache/delete")
    public CommonResponse cacheDelete(@RequestBody CacheRedisVo vo) {

        return sysRedisService.cacheDelete(vo);
    }*/
}
