package com.wz.front.dto;

import com.wz.modules.projectinfo.entity.ProjectInfoEntity;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: ProjectInfoDto
 */
public class ProjectInfoDto {

    private ProjectInfoEntity project;

    private int boxTotal = 0;

    private int switchOnlineTotal = 0;

    private int switchLeaveTotal = 0;

    private int alarmTotal = 0;

    private int imageHeight;

    private int imageWidth;

    private String gymName;

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

    public int getAlarmTotal() {
        return alarmTotal;
    }

    public void setAlarmTotal(int alarmTotal) {
        this.alarmTotal = alarmTotal;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public ProjectInfoEntity getProject() {
        return project;
    }

    public void setProject(ProjectInfoEntity project) {
        this.project = project;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }
}
