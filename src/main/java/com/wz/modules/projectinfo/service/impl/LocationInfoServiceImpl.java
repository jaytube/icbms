package com.wz.modules.projectinfo.service.impl;

import com.wz.modules.common.utils.RedisUtil;
import com.wz.modules.common.utils.UserUtils;
import com.wz.modules.common.utils.Utils;
import com.wz.modules.deviceinfo.dao.DeviceBoxInfoDao;
import com.wz.modules.lora.dao.GymMasterDao;
import com.wz.modules.lora.entity.GymMaster;
import com.wz.modules.migration.enums.LocationNodeType;
import com.wz.modules.migration.util.LocationInfoUtil;
import com.wz.modules.projectinfo.dao.LocationInfoDao;
import com.wz.modules.projectinfo.entity.LocationInfoEntity;
import com.wz.modules.projectinfo.service.LocationInfoService;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.socket.utils.CommUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("locationInfoService")
public class LocationInfoServiceImpl implements LocationInfoService {
    @Autowired
    private LocationInfoDao locationInfoDao;

    @Autowired
    private DeviceBoxInfoDao deviceBoxInfoDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GymMasterDao gymMasterDao;

    @Override
    public LocationInfoEntity queryObject(String id) {
        return locationInfoDao.queryObject(id);
    }

    @Override
    public List<LocationInfoEntity> queryList(Map<String, Object> map) {
        return locationInfoDao.queryList(map);
    }

    @Override
    public List<LocationInfoEntity> queryListByBean(LocationInfoEntity entity) {
        return locationInfoDao.queryListByBean(entity);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return locationInfoDao.queryTotal(map);
    }

    @Override
    public String save(LocationInfoEntity locationInfo) {
        if (StringUtils.isBlank(locationInfo.getId())) {
            locationInfo.setId(Utils.uuid());
        }
        UserEntity currentUser = UserUtils.getCurrentUser();
        if (null != currentUser) {
            locationInfo.setCreateId(currentUser.getId());
        }
        locationInfo.setCreateTime(new Date());
        if (!StringUtils.isBlank(locationInfo.getParentId())) {
            if (!"0".equals(locationInfo.getParentId())) {
                LocationInfoEntity parentLocation = this.queryObject(locationInfo.getParentId());
                locationInfo.setRoot(parentLocation.getRoot() + "," + locationInfo.getId());
            } else {
                locationInfo.setRoot(locationInfo.getId());
            }
        }
        locationInfoDao.save(locationInfo);
        LocationInfoUtil.parseLocationNodeType(locationInfo);
        return locationInfo.getId();
    }

    @Override
    public int update(LocationInfoEntity locationInfo) {
        UserEntity currentUser = UserUtils.getCurrentUser();
        if (null != currentUser) {
            locationInfo.setUpdateId(currentUser.getId());
        }
        locationInfo.setUpdateTime(new Date());
        LocationInfoUtil.parseLocationNodeType(locationInfo);
        return locationInfoDao.update(locationInfo);
    }

    @Override
    public int delete(String id) {
        locationInfoDao.deleteLinkByLocId(id);
        return locationInfoDao.delete(id);
    }

    @Override
    public int deleteLocation(String id) {
        return locationInfoDao.delete(id);
    }

    @Override
    public int deleteBatch(String[] ids) {
        locationInfoDao.deleteBoxLocLinkBatch(ids);
        deviceBoxInfoDao.deleteBoxInfoByLocation(ids);
        return locationInfoDao.deleteBatch(ids);
    }

    @Override
    public List<LocationInfoEntity> findLocInfoByPId(String projectId) {
        List<LocationInfoEntity> locationList = locationInfoDao.findLocInfoByPId(projectId);
        // for (LocationInfoEntity loc : locationList) {
        // boolean hasAlarm = false;
        // List<DeviceBoxInfoEntity> deviceBoxs =
        // this.deviceBoxInfoDao.findDeviceBoxsInfoByLId(projectId,
        // loc.getId());
        // for (DeviceBoxInfoEntity box : deviceBoxs) {
        // if (hasAlarm) {
        // break;
        // }
        // DeviceBoxConfigEntity config =
        // deviceBoxConfigService.queryDeviceBoxConfig(box.getDeviceBoxNum());
        // if (null != config) {
        // String field = config.getGatewayAddress() + "_" +
        // config.getDeviceBoxAddress() + "_";
        // Map<String, String> result =
        // redisUtil.fuzzyGet(Integer.parseInt(config.getGatewayAddress()),
        // "ALARM_DATA", field);
        // for (String k : result.keySet()) {
        // String resultJson = result.get(k);
        // JSONObject jsonObj = JSONObject.fromObject(resultJson);
        // String alarmLevel = jsonObj.getString("alarmLevel");
        // if ("4".equals(alarmLevel)) {
        // hasAlarm = true;
        // break;
        // }
        // }
        // }
        // }
        // if (hasAlarm) {
        // loc.setHasAlarm("1");
        // } else {
        // loc.setHasAlarm("0");
        // }
        // }
        return locationList;
    }

    public List<LocationInfoEntity> findProjectLocationRel(String projectId) {
        List<LocationInfoEntity> locationList = locationInfoDao.findProjectLocationRel(projectId);
        return locationList;
    }

    @Override
    public List<LocationInfoEntity> queryListParentId(String parenId) {
        return locationInfoDao.queryListParentId(parenId);
    }

    public String findLocIdByLocName(Map<String, String> map) {
        return locationInfoDao.findLocIdByLocName(map);
    }

    public int delProjectLocationRel(String projectId) {
        return locationInfoDao.delProjectLocationRel(projectId);
    }

    public int delProjectLocation(String projectId) {
        return locationInfoDao.delProjectLocation(projectId);
    }

    @Override
    @Transactional
    public void saveLocationsForNewProject(String projectId, int gymId, String projectName) {
        Map<String, String> map = new HashMap<>();
        map.put("parentId", "0");
        map.put("projectId", projectId);
        map.put("locName", LocationNodeType.ROOT.getDesc());
        String locaId = locationInfoDao.findLocIdByLocName(map);
        if(StringUtils.isBlank(locaId)) {
            String rootId = Utils.uuid();
            LocationInfoEntity root = new LocationInfoEntity();
            root.setId(rootId);
            root.setProjectId(projectId);
            root.setName(LocationNodeType.ROOT.getDesc());
            root.setParentId("0");
            root.setType(LocationNodeType.ROOT.name());
            root.setCreateId(UserUtils.getCurrentUserId());
            root.setCreateTime(new Date());
            root.setUpdateId(UserUtils.getCurrentUserId());
            root.setUpdateTime(new Date());
            root.setRoot(rootId);
            locationInfoDao.save(root);

            LocationInfoEntity venue = new LocationInfoEntity();
            String venueId = Utils.uuid();
            venue.setId(venueId);
            venue.setProjectId(projectId);
            GymMaster gym = gymMasterDao.findById(gymId);
            venue.setName(gym.getName());
            venue.setParentId(rootId);
            venue.setType(LocationNodeType.VENUE.name());
            venue.setCreateId(UserUtils.getCurrentUserId());
            venue.setCreateTime(new Date());
            venue.setUpdateId(UserUtils.getCurrentUserId());
            venue.setUpdateTime(new Date());
            venue.setRoot(rootId + "," + venueId);
            locationInfoDao.save(venue);

            LocationInfoEntity exhibition = new LocationInfoEntity();
            String exId = Utils.uuid();
            exhibition.setId(exId);
            exhibition.setProjectId(projectId);
            exhibition.setName(projectName);
            exhibition.setParentId(venueId);
            exhibition.setType(LocationNodeType.EXHIBITION.name());
            exhibition.setCreateId(UserUtils.getCurrentUserId());
            exhibition.setCreateTime(new Date());
            exhibition.setUpdateId(UserUtils.getCurrentUserId());
            exhibition.setUpdateTime(new Date());
            exhibition.setRoot(rootId+","+venueId+","+exId);
            locationInfoDao.save(exhibition);
        }

    }
}