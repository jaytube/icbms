package com.wz.modules.lora.dto;

import lombok.Data;

/**
 * @Author: Cherry
 * @Date: 2021/1/23
 * @Desc: DeviceInfoDto
 */
@Data
public class DeviceInfoDto {
    private int id;
    private String createTime;
    private String updateTime;
    private int loraId;
    private int applicationId;
    private String applicationName;
    private int loraAppId;
    private int gatewayId;
    private String gatewayName;
    private String type;
    private String deviceSn;
    private String name;
    private String des;
    private String lot;
    private String lat;
    private String groups;
    private int templateId;
    private String typeName;
    private String typeColor;
    private int createUserId;
    private String updateUserName;
    private int updateUserId;
    private int isDel;
    private int pointNum;
    private String templateDes;
    private int toLora;
    private String imgUrl;
    private String mgrUrl;
    private boolean selected;
    private int cardId;
    private int deviceTemplateId;
}
