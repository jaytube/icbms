package com.wz.modules.lora.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: Cherry
 * @Date: 2021/1/23
 * @Desc: TerminalTypeDto
 */
@Data
public class TerminalTypeDto {

    private int id;
    private long createTime;
    private long updateTime;
    private String type;
    private int processIsShow;
    private String imgUrl;
    private String des;
    private String title;
    private String typeName;
    private String typeColor;
    private int version;
    private int createUserId;
    private String updateUserName;
    private int updateUserId;
    private int isDel;
    private List<Object> children;

}
