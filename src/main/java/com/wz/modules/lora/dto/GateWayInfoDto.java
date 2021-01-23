package com.wz.modules.lora.dto;

import lombok.Data;

/**
 * @Author: Cherry
 * @Date: 2021/1/23
 * @Desc: GateWayInfoDto
 */
@Data
public class GateWayInfoDto {

    private int id;
    private long createTime;
    private long updateTime;
    private Object loraId;
    private String name;
    private int applicationId;
    private String applicationName;
    private Object loraApplicatonId;
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

}
