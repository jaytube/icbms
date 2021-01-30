package com.wz.modules.lora.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: Cherry
 * @Date: 2021/1/29
 * @Desc: GatewayDeviceMap
 */
@Data
@EqualsAndHashCode
public class GatewayDeviceMap {

    private int mapId;

    private int gatewayId;

    private String deviceSn;

    private int deviceId;

    private String deviceInfoId;

    private String projectId;

    private int gymId;
}
