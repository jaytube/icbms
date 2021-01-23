package com.wz.modules.lora.dto;

import lombok.Data;

/**
 * @Author: Cherry
 * @Date: 2021/1/23
 * @Desc: AddDeviceDto
 */
@Data
public class AddDeviceDto {
    /**
     * 节点id
     */
    private int applicationId;
    /**
     * 终端编码
     */
    private String deviceSn;
    /**
     * 网关id
     */
    private int gatewayId;
    /**
     * 终端名称
     */
    private String name;
    /**
     * 终端类型
     */
    private String type;
    /**
     * 模板id
     */
    private int templateId;
    /**
     * 是否同步到lora,1：同步；0：不同步
     */
    private int toLora;
    /**
     * 终端类型名称
     */
    private String typeName;

}
