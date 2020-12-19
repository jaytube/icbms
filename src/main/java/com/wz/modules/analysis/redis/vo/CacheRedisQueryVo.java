package com.wz.modules.analysis.redis.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class CacheRedisQueryVo implements Serializable {

    private String keyword;

    private String cacheKeyword;

    private String type;

    private int pageNo = 1;

    private int pageSize = 10;
}
