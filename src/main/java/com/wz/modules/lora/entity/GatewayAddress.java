package com.wz.modules.lora.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: Cherry
 * @Date: 2021/1/29
 * @Desc: GatewayAddress
 */
@Data
@EqualsAndHashCode
public class GatewayAddress {

    private int id;

    private String ipAddress;

    private String name;

}
