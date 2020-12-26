package com.wz.front.service;

import com.wz.front.dto.ProjectBoxInfoCntDto;
import com.wz.front.dto.ProjectBoxStatusCntDto;
import com.wz.front.dto.ProjectInfoDto;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: ClientProjectInfoService
 */
public interface ClientProjectInfoService {

    List<ProjectInfoEntity> getUserProjects();

    List<ProjectInfoDto> listProjects();

    ProjectBoxInfoCntDto getProjectBoxInfoCnt(String projectId);

    Map<String, ProjectBoxInfoCntDto> getProjectBoxInfoCntByIds(String[] projectIds);

    Map<String, ProjectBoxInfoCntDto> getProjectBoxInfoCntByUserId(String userId);

    ProjectBoxStatusCntDto getProjectBoxStatusInfoCnt(String projectId);

}
