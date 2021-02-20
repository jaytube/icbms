package com.wz.front.service.impl;

import com.wz.front.dto.ProjectAlarmTotalDto;
import com.wz.front.dto.ProjectBoxInfoCntDto;
import com.wz.front.dto.ProjectBoxStatusCntDto;
import com.wz.front.dto.ProjectInfoDto;
import com.wz.front.service.AppProjectInfoService;
import com.wz.front.service.CurrentUser;
import com.wz.front.util.FileUtils;
import com.wz.modules.common.utils.RedisUtil;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.service.DeviceBoxInfoService;
import com.wz.modules.devicelog.dao.DeviceAlarmInfoLogDao;
import com.wz.modules.kk.service.KkService;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;
import com.wz.modules.projectinfo.service.ProjectInfoService;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wz.socket.utils.Constant.TERMINAL_STATUS;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: AppProjectInfoServiceImpl
 */
@Service
@Slf4j
public class AppProjectInfoServiceImpl implements AppProjectInfoService {

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

    @Autowired
    private CurrentUser currentUser;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private FileUtils fileUtils;

    private static final String REDIS_GATEWAYADDRESS = "0";

    @Override
    public List<ProjectInfoEntity> getUserProjects() {
        String currentUser = this.currentUser.getCurrentUser();
        UserEntity user = userService.queryObject(currentUser);
        String projectIds = user.getProjectIds();
        List<ProjectInfoEntity> projectList = new ArrayList<>();
        if (StringUtils.isNotBlank(projectIds)) {
            String[] ids = projectIds.split(",");
            return projectInfoService.queryProjects(ids);
        }
        return projectList;
    }

    @Override
    public String[] getUserProjectIds() {
        String currentUser = this.currentUser.getCurrentUser();
        UserEntity user = userService.queryObject(currentUser);
        String projectIds = user.getProjectIds();
        List<ProjectInfoEntity> projectList = new ArrayList<>();
        if (StringUtils.isNotBlank(projectIds)) {
            return projectIds.split(",");
        }
        return new String[]{};
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
    public Map<String, ProjectBoxInfoCntDto> getProjectBoxInfoCntByIds(String[] projectIds) {
        List<DeviceBoxInfoEntity> allBoxInfoList = deviceBoxInfoService.findDeviceBoxsInfoByProjectIds(projectIds);
        return getAllProjectBoxInfoCnt(allBoxInfoList);
    }

    @Override
    public Map<String, ProjectBoxInfoCntDto> getProjectBoxInfoCntByUserId(String userId) {
        List<DeviceBoxInfoEntity> allBoxInfoList = deviceBoxInfoService.findDeviceBoxsInfoByUserId(userId);
        return getAllProjectBoxInfoCnt(allBoxInfoList);
    }

    @Override
    public ProjectBoxStatusCntDto getProjectBoxStatusInfoCnt(String projectId) {
        return null;
    }

    private Map<String, ProjectBoxInfoCntDto> getAllProjectBoxInfoCnt(List<DeviceBoxInfoEntity> allBoxInfoList) {
        if (CollectionUtils.isEmpty(allBoxInfoList)) {
            return new HashMap<>();
        }
        Map<String, List<DeviceBoxInfoEntity>> projectMap = new HashMap<>();
        for (DeviceBoxInfoEntity entity : allBoxInfoList) {
            String projectId = entity.getProjectId();
            if (projectMap.get(projectId) == null) {
                projectMap.put(projectId, new ArrayList<>());
            }
            projectMap.get(projectId).add(entity);
        }
        Map<String, ProjectBoxInfoCntDto> all = new HashMap<>();
        Map<String, String> redisStatus = redisUtil.hgetAll(Integer.parseInt(REDIS_GATEWAYADDRESS), TERMINAL_STATUS);
        for (Map.Entry<String, List<DeviceBoxInfoEntity>> entry : projectMap.entrySet()) {
            ProjectBoxInfoCntDto resultMap = new ProjectBoxInfoCntDto();
            Map<String, Integer> onlineMaps = this.kkService.statDeviceBoxOnline(entry.getValue(), redisStatus);
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
            all.put(entry.getKey(), resultMap);
        }
        return all;
    }

    private List<ProjectInfoDto> convert(List<ProjectInfoEntity> userProjects) {
        String currentUser = this.currentUser.getCurrentUser();
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
        Map<String, ProjectBoxInfoCntDto> projectBoxInfoCntDtoMap = getProjectBoxInfoCntByUserId(currentUser);
        for (ProjectInfoEntity entity : userProjects) {
            ProjectInfoDto dto = new ProjectInfoDto();
            String projectId = entity.getId();
            ProjectBoxInfoCntDto projectBoxInfoCnt = projectBoxInfoCntDtoMap.get(projectId);
            if (projectBoxInfoCnt == null) {
                dto.setBoxTotal(0);
                dto.setSwitchOnlineTotal(0);
                dto.setSwitchLeaveTotal(0);
            } else {
                dto.setBoxTotal(projectBoxInfoCnt.getBoxTotal());
                dto.setSwitchOnlineTotal(projectBoxInfoCnt.getSwitchOnlineTotal());
                dto.setSwitchLeaveTotal(projectBoxInfoCnt.getSwitchLeaveTotal());
            }
            dto.setProject(entity);
            dto.setAlarmTotal(MapUtils.getIntValue(totalMap, projectId, 0));
            setImageSize(entity, dto);
            projectInfoDtos.add(dto);
        }
        return projectInfoDtos;
    }

    private void setImageSize(ProjectInfoEntity entity, ProjectInfoDto dto) {
        String fileName = entity.getFileName();
        if (StringUtils.isBlank(fileName)) {
            return;
        }
        String fileUploadPath = fileUtils.getFileUploadPath();
        File image = new File(fileUploadPath + fileName);
        if (!image.exists()) {
            return;
        }
        try {
            BufferedImage imageBuf = ImageIO.read(image);
            dto.setImageHeight(imageBuf.getHeight());
            dto.setImageWidth(imageBuf.getWidth());
        } catch (IOException e) {
            log.error("获取展会图片宽高异常：", e);
        }
    }

}
