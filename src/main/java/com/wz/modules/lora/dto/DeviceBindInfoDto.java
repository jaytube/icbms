package com.wz.modules.lora.dto;

import lombok.Data;

/**
 * @Author: Cherry
 * @Date: 2021/1/30
 * @Desc: DeviceBindInfoDto
 */
@Data
public class DeviceBindInfoDto {

    private int gymId;

    private String gymName;

    private String projectId;

    private String projectName;

    private String deviceBoxId;

    private String deviceBoxSn;

    private String deviceBoxNum;

    private int gatewayId;

    private String gatewayName;

    private String gatewayIp;

    private String standNo;

    private boolean isNew;

}
