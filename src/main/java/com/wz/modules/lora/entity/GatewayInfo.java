package com.wz.modules.lora.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: Cherry
 * @Date: 2021/1/29
 * @Desc: GatewayInfo
 */
@Data
@EqualsAndHashCode
public class GatewayInfo implements Serializable {

    private int id;
    private int gatewayId;
    private long createTime;
    private long updateTime;
    private int loraId;
    private String name;
    private int applicationId;
    private String applicationName;
    private int loraApplicationId;
    private int sceneId;
    private String sceneName;
    private int loraSceneId;
    private String macAddress;
    private String des;
    private String mgrUrl;
    private int createUserId;
    private String updateUserName;
    private int updateUserId;
    private int isDel;
    private String ipAddress;
}
