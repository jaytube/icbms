package com.wz.modules.migration.enums;

/**
 * @Author: Cherry
 * @Date: 2020/12/13
 * @Desc: LocationNodeType
 */
public enum LocationNodeType {

    ROOT("根节点"),
    VENUE("场馆"),
    EXHIBITION("展会"),
    DEVICE("设备/电箱");

    private String desc;

    LocationNodeType(String desc) {
        this.desc = desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
