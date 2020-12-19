package com.wz.front.service;

import com.wz.front.dto.ProjectBoxInfoCntDto;
import com.wz.front.dto.ProjectInfoDto;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;

import java.util.List;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: ClientProjectInfoService
 */
public interface ClientProjectInfoService {

    List<ProjectInfoEntity> getUserProjects();

    List<ProjectInfoDto> listProjects();

    ProjectBoxInfoCntDto getProjectBoxInfoCnt(String projectId);

    List<ProjectBoxInfoCntDto> getProjectBoxInfoCntByIds(String[] projectIds);

}
