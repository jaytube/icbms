package com.wz.modules.lora.enums;

/**
 * @Author: Cherry
 * @Date: 2021/1/23
 * @Desc: LoRaCommand
 */
public enum LoRaCommand {

    /**
     * 单次查询指令
     */
    QUERY_CMD("000003EE00001E", "单次查询指令"),
    /**
     * 合闸指令
     */
    OPEN_CMD("000010EE020002040000FF00", "合闸指令"),
    /**
     * 分闸指令
     */
    CLOSE_CMD("000010EE0200020400000000", "分闸指令");

    LoRaCommand(String cmd, String desc) {
        this.cmd = cmd;
        this.desc = desc;
    }

    private String cmd;
    private String desc;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
