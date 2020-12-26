package com.wz.front.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: ProjectBoxInfoCntDto
 */
@Data
@AllArgsConstructor
public class ProjectBoxStatusCntDto {

    private int normalCnt;

    private int offlineCnt;

    private int warnCnt;

    private int errorCnt;

}
