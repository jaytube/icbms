package com.wz.modules.analysis.redis.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysRedisDto implements Serializable {
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 原始的缓存key
     */
    private String oldRedisKey;
    /**
     * 缓存键
     */
    private String redisKey;
    /**
     * 缓存值
     */
    private String redisValue;
    /**
     * 过期时间
     */
    private String expire;

    private String expireStr;
    /**
     * 集合元素总数
     */
    private Long elCount;

    private List<Map<String, Object>> values;
    /**
     * list value
     */
//    private List<String> listValues;
}
