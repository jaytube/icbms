package com.wz.front.dto;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: ProjectBoxInfoCntDto
 */
public class ProjectBoxInfoCntDto {

    private int boxTotal = 0;

    private int switchOnlineTotal = 0;

    private int switchLeaveTotal = 0;

    private String dailyElecTotal;

    public ProjectBoxInfoCntDto(int boxTotal, int switchOnlineTotal, int switchLeaveTotal) {
        this.boxTotal = boxTotal;
        this.switchOnlineTotal = switchOnlineTotal;
        this.switchLeaveTotal = switchLeaveTotal;
    }

    public ProjectBoxInfoCntDto() {
    }

    public int getBoxTotal() {
        return boxTotal;
    }

    public void setBoxTotal(int boxTotal) {
        this.boxTotal = boxTotal;
    }

    public int getSwitchOnlineTotal() {
        return switchOnlineTotal;
    }

    public void setSwitchOnlineTotal(int switchOnlineTotal) {
        this.switchOnlineTotal = switchOnlineTotal;
    }

    public int getSwitchLeaveTotal() {
        return switchLeaveTotal;
    }

    public void setSwitchLeaveTotal(int switchLeaveTotal) {
        this.switchLeaveTotal = switchLeaveTotal;
    }

    public String getDailyElecTotal() {
        return dailyElecTotal;
    }

    public void setDailyElecTotal(String dailyElecTotal) {
        this.dailyElecTotal = dailyElecTotal;
    }
}
