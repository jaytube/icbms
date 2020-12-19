package com.wz.modules.analysis.redis.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseVo implements Serializable {
    private String host;
    private String db;

}
