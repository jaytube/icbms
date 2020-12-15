package com.wz.modules.analysis.redis.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysRedisCreateVo implements Serializable {

    private String host;

    private int port;

    private String password;

    private String name;

    private Boolean status;

    private String env;
}
