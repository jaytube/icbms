package com.wz.front.service.impl;

import com.wz.front.dto.ProjectAlarmTotalDto;
import com.wz.front.dto.ProjectBoxInfoCntDto;
import com.wz.front.dto.ProjectInfoDto;
import com.wz.front.service.ClientProjectInfoService;
import com.wz.modules.common.utils.ShiroUtils;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.service.DeviceBoxInfoService;
import com.wz.modules.devicelog.dao.DeviceAlarmInfoLogDao;
import com.wz.modules.kk.service.KkService;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;
import com.wz.modules.projectinfo.service.ProjectInfoService;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: ClientProjectInfoServiceImpl
 */
@Service
public class ClientProjectInfoServiceImpl implements ClientProjectInfoService {

    @Autowired
    private KkService kkService;

    @Autowired
    private DeviceBoxInfoService deviceBoxInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceAlarmInfoLogDao deviceAlarmInfoLogDao;

    @Autowired
    private ProjectInfoService projectInfoService;

    @Override
    public List<ProjectInfoEntity> getUserProjects() {
        UserEntity user = userService.queryObject(ShiroUtils.getUserId());
        String projectIds = user.getProjectIds();
        List<ProjectInfoEntity> projectList = new ArrayList<>();
        if (StringUtils.isNotBlank(projectIds)) {
            String[] ids = projectIds.split(",");
            return projectInfoService.queryProjects(ids);
        }
        return projectList;
    }

    @Override
    public List<ProjectInfoDto> listProjects() {
        return convert(getUserProjects());
    }

    @Override
    public ProjectBoxInfoCntDto getProjectBoxInfoCnt(String projectId) {
        ProjectBoxInfoCntDto resultMap = new ProjectBoxInfoCntDto();
        List<DeviceBoxInfoEntity> boxInfoList = deviceBoxInfoService.findDeviceBoxsInfoByProjectId(projectId);
        if (boxInfoList != null && boxInfoList.size() > 0) {
            Map<String, Integer> onlineMaps = this.kkService.statDeviceBoxOnline(projectId);
            resultMap.setBoxTotal(boxInfoList.size());
            try {
                Integer onlineNums = onlineMaps.get("onlineNums");
                int onlineNumsV = onlineNums == null ? 0 : onlineNums;
                resultMap.setSwitchOnlineTotal(onlineNumsV);
                resultMap.setSwitchLeaveTotal(boxInfoList.size() - onlineNums);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultMap;
    }

    @Override
    public List<ProjectBoxInfoCntDto> getProjectBoxInfoCntByIds(String[] projectIds) {
        List<DeviceBoxInfoEntity> allBoxInfoList = deviceBoxInfoService.findDeviceBoxsInfoByProjectIds(projectIds);
        if (CollectionUtils.isEmpty(allBoxInfoList)) {
            return new ArrayList<>();
        }
        Map<String, List<DeviceBoxInfoEntity>> projectMap = new HashMap<>();
        for (DeviceBoxInfoEntity entity : allBoxInfoList) {
            String projectId = entity.getProjectId();
            if (projectMap.get(projectId) == null) {
                projectMap.put(projectId, new ArrayList<>());
            }
            projectMap.get(projectId).add(entity);
        }

        List<ProjectBoxInfoCntDto> all = new ArrayList<>();
        for (Map.Entry<String, List<DeviceBoxInfoEntity>> entry : projectMap.entrySet()) {
            ProjectBoxInfoCntDto resultMap = new ProjectBoxInfoCntDto();
            Map<String, Integer> onlineMaps = this.kkService.statDeviceBoxOnline(entry.getKey());
            List<DeviceBoxInfoEntity> boxInfoList = entry.getValue();
            resultMap.setBoxTotal(boxInfoList.size());
            try {
                Integer onlineNums = onlineMaps.get("onlineNums");
                int onlineNumsV = onlineNums == null ? 0 : onlineNums;
                resultMap.setSwitchOnlineTotal(onlineNumsV);
                resultMap.setSwitchLeaveTotal(boxInfoList.size() - onlineNums);
            } catch (Exception e) {
                e.printStackTrace();
            }
            all.add(resultMap);
        }
        return all;
    }

    private List<ProjectInfoDto> convert(List<ProjectInfoEntity> userProjects) {
        if (CollectionUtils.isEmpty(userProjects)) {
            return new ArrayList<>();
        }
        List<ProjectInfoDto> projectInfoDtos = new ArrayList<>();

        int size = userProjects.size();
        String[] ids = new String[size];
        for (int i = 0; i < size; i++) {
            ids[i] = userProjects.get(i).getId();
        }
        List<ProjectAlarmTotalDto> projectAlarmTotalDtos = this.deviceAlarmInfoLogDao.queryProjectsTotal(ids);
        Map<String, Integer> totalMap = new HashMap<>();
        for (ProjectAlarmTotalDto dto : projectAlarmTotalDtos) {
            totalMap.put(dto.getProjectId(), dto.getAlarmTotal());
        }
        for (ProjectInfoEntity entity : userProjects) {
            ProjectInfoDto dto = new ProjectInfoDto();
            String projectId = entity.getId();
            ProjectBoxInfoCntDto projectBoxInfoCnt = getProjectBoxInfoCnt(projectId);
            dto.setBoxTotal(projectBoxInfoCnt.getBoxTotal());
            dto.setSwitchOnlineTotal(projectBoxInfoCnt.getSwitchOnlineTotal());
            dto.setSwitchLeaveTotal(projectBoxInfoCnt.getSwitchLeaveTotal());
            dto.setProject(entity);
            dto.setAlarmTotal(MapUtils.getIntValue(totalMap, projectId, 0));
            projectInfoDtos.add(dto);
        }
        return projectInfoDtos;
    }

}
