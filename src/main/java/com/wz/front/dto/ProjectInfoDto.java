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

    private String projectIcon;

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

    public String getProjectIcon() {
        return projectIcon;
    }

    public void setProjectIcon(String projectIcon) {
        this.projectIcon = projectIcon;
    }

    public ProjectInfoEntity getProject() {
        return project;
    }

    public void setProject(ProjectInfoEntity project) {
        this.project = project;
    }
}
