package com.wz.front.service;

import com.wz.front.dto.ProjectBoxInfoCntDto;
import com.wz.front.dto.ProjectBoxStatusCntDto;
import com.wz.front.dto.ProjectInfoDto;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: AppProjectInfoService
 */
public interface AppProjectInfoService {

    List<ProjectInfoEntity> getUserProjects();

    String[] getUserProjectIds();

    List<ProjectInfoDto> listProjects();

    ProjectBoxInfoCntDto getProjectBoxInfoCnt(String projectId);

    Map<String, ProjectBoxInfoCntDto> getProjectBoxInfoCntByIds(String[] projectIds);

    Map<String, ProjectBoxInfoCntDto> getProjectBoxInfoCntByUserId(String userId);

    ProjectBoxStatusCntDto getProjectBoxStatusInfoCnt(String projectId);

}
