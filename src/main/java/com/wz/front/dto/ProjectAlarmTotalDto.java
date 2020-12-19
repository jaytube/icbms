package com.wz.front.dto;

/**
 * @Author: Cherry
 * @Date: 2020/12/20
 * @Desc: ProjectAlarmTotalDto
 */
public class ProjectAlarmTotalDto {

    private String projectId;

    private int alarmTotal;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getAlarmTotal() {
        return alarmTotal;
    }

    public void setAlarmTotal(int alarmTotal) {
        this.alarmTotal = alarmTotal;
    }
}
