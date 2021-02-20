package com.wz.modules.deviceinfo.service.impl;

import com.wz.modules.common.utils.RedisUtil;
import com.wz.modules.common.utils.UserUtils;
import com.wz.modules.common.utils.Utils;
import com.wz.modules.deviceinfo.dao.DeviceBoxInfoDao;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.service.DeviceBoxInfoService;
import com.wz.modules.gen.utils.GenUtils;
import com.wz.modules.projectinfo.entity.DboxLocLinkEntity;
import com.wz.modules.projectinfo.entity.LocationInfoEntity;
import com.wz.modules.projectinfo.service.LocationInfoService;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.socket.utils.CommUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("deviceBoxInfoService")
public class DeviceBoxInfoServiceImpl implements DeviceBoxInfoService {

    private static Logger log = Logger.getLogger(GenUtils.class);

    @Autowired
    private DeviceBoxInfoDao deviceBoxInfoDao;

    @Autowired
    private LocationInfoService locationInfoService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public DeviceBoxInfoEntity queryObject(String id) {
        return deviceBoxInfoDao.queryObject(id);
    }

    @Override
    public List<DeviceBoxInfoEntity> queryList(Map<String, Object> map) {
        String locationId = (String) map.get("locationId");
        if (!StringUtils.isBlank(locationId)) {
            LocationInfoEntity location = locationInfoService.queryObject(locationId);
            map.put("root", location.getRoot() + "%");
        }
        return deviceBoxInfoDao.queryList(map);
    }

    @Override
    public List<DeviceBoxInfoEntity> queryListByBean(DeviceBoxInfoEntity entity) {
        return deviceBoxInfoDao.queryListByBean(entity);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        String locationId = (String) map.get("locationId");
        if (!StringUtils.isBlank(locationId)) {
            LocationInfoEntity location = locationInfoService.queryObject(locationId);
            map.put("root", location.getRoot());
        }
        return deviceBoxInfoDao.queryTotal(map);
    }

    @Override
    public int save(DeviceBoxInfoEntity deviceBoxInfo) {
        deviceBoxInfo.setId(Utils.uuid());
        UserEntity currentUser = UserUtils.getCurrentUser();
        if (null != currentUser) {
            deviceBoxInfo.setCreateId(currentUser.getId());
        }
        deviceBoxInfo.setDeviceBoxName(deviceBoxInfo.getStandNo());
        deviceBoxInfo.setCreateTime(new Date());
        int rtnInt = deviceBoxInfoDao.save(deviceBoxInfo);
        // 设置终端对应的项目
        String str = deviceBoxInfo.getDeviceBoxNum();
        if (str != null && !"".equals(str) && str.length() > 6) {
            String terminalId = CommUtils.getDeviceBoxAddress(str);
            if(str.startsWith("LY"))
                terminalId += "_LY";
            redisUtil.hset(0, "TERMINAL_CONFIG", terminalId, deviceBoxInfo.getProjectId());
        }

        // 这里检测是否选择了位置，如果选择了位置，顺便需要把电箱与位置关系配置进去
        if (StringUtils.isNotBlank(deviceBoxInfo.getLocationId())) {
            LocationInfoEntity location = locationInfoService.queryObject(deviceBoxInfo.getLocationId());
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("deviceBoxId", deviceBoxInfo.getId());
            List<DboxLocLinkEntity> dboxLocList = deviceBoxInfoDao.queryLinkListByBoxId(paramsMap);
            if (location != null) {
                if (dboxLocList != null && dboxLocList.size() > 0) {
                    // update
                    DboxLocLinkEntity dboxLoc = dboxLocList.get(0);
                    dboxLoc.setLocationInfo(location);
                    if (null != currentUser) {
                        dboxLoc.setCreateId(currentUser.getId());
                    }
                    dboxLoc.setUpdateTime(new Date());
                    deviceBoxInfoDao.updateBoxLocLink(dboxLoc);
                } else {
                    DboxLocLinkEntity dboxLoc = new DboxLocLinkEntity();
                    dboxLoc.setId(Utils.uuid());
                    dboxLoc.setLocationInfo(location);
                    dboxLoc.setDboxInfo(deviceBoxInfo);
                    if (null != currentUser) {
                        dboxLoc.setCreateId(currentUser.getId());
                    }
                    dboxLoc.setCreateTime(new Date());
                    deviceBoxInfoDao.saveBoxLocLink(dboxLoc);
                }
            }
        }
        return rtnInt;
    }

    @Override
    public int update(DeviceBoxInfoEntity deviceBoxInfo) {
        deviceBoxInfo.setDeviceBoxName(deviceBoxInfo.getStandNo());
        return deviceBoxInfoDao.update(deviceBoxInfo);
    }

    public int xyReset(DeviceBoxInfoEntity deviceBoxInfo) {
        return deviceBoxInfoDao.xyReset(deviceBoxInfo);
    }

    @Override
    public int delete(String id) {
        return deviceBoxInfoDao.delete(id);
    }

    @Override
    @Transactional
    public int deleteBatch(String[] ids, String type) {
        deviceBoxInfoDao.deleteBoxLocLinkBatch(ids);
        try {
            List<String> locations = this.deviceBoxInfoDao.findSingleBoxLocation(ids);
            if ("app".equals(type)) {
                // 当位置没有再关联电箱时,删除位置信息
                for (String l : locations) {
                    this.locationInfoService.delete(l);
                }
            }
        } catch (Exception e) {
            log.error("删除位置信息失败", e);
        }
        return deviceBoxInfoDao.deleteBatch(ids);
    }

    @Override
    public List<DeviceBoxInfoEntity> findDeviceBoxsInfoByLId(String projectId, String locationId) {
        return deviceBoxInfoDao.findDeviceBoxsInfoByLId(projectId, locationId);
    }

    @Override
    public List<DeviceBoxInfoEntity> findDeviceBoxsInfoByProjectId(String projectId) {
        return deviceBoxInfoDao.findDeviceBoxsInfoByProjectId(projectId);
    }

    @Override
    public List<DeviceBoxInfoEntity> findDeviceBoxInfosByProjectId(String projectId) {
        return deviceBoxInfoDao.findDeviceBoxInfoByProjectId(projectId);
    }

    @Override
    public List<DeviceBoxInfoEntity> findDeviceBoxsInfoByProjectIds(String[] projectIds) {
        return deviceBoxInfoDao.findDeviceBoxsInfoByProjectIds(projectIds);
    }

    @Override
    public List<DeviceBoxInfoEntity> findDeviceBoxsInfoByUserId(String userId) {
        return deviceBoxInfoDao.findDeviceBoxsInfoByUserId(userId);
    }

    @Override
    public void updateDeviceBoxOnline(String deviceMac, String online) {
        deviceBoxInfoDao.updateDeviceBoxOnline(deviceMac, online);
    }

    /**
     * 保存电箱和位置关系 此方法用于在配置电箱时候，顺便配置了电箱位置
     */
    @Override
    public void saveBoxLocLink(DboxLocLinkEntity dboxLocation) {
        deviceBoxInfoDao.saveBoxLocLink(dboxLocation);
    }

    /**
     * 根据电箱获取电箱和位置的配置关系
     */
    @Override
    public List<DboxLocLinkEntity> queryLinkListByBoxId(Map<String, String> paramsMap) {
        return deviceBoxInfoDao.queryLinkListByBoxId(paramsMap);
    }

    @Override
    @Transactional
    public List<DeviceBoxInfoEntity> saveBoxLocBatch(List<Map<String, String>> result, String projectId, UserEntity currentUser) {
        List<DeviceBoxInfoEntity> results = new ArrayList<>();
        Map<String, String> paramMap = new HashMap<String, String>();
        for (Map<String, String> map : result) {
            String firstLocId = "", secLocId = "", thirdLocId = "", forthLocId = "", fifthLocId = "";
            String deviceMac = "", secBoxGateway = "", standNo = "", boxCapacity = "", controlFlag = "", remark = "";
            String deviceBoxId = "";
            for (String key : map.keySet()) {
                String val = map.get(key);
                if (StringUtils.isNotBlank(val)) {
                    val = val.trim();
                }
                switch (key) {
                    case "firstLoc":
                        paramMap.put("projectId", projectId);
                        paramMap.put("locName", val);
                        paramMap.put("parentId", "0");
                        firstLocId = locationInfoService.findLocIdByLocName(paramMap);
                        if (StringUtils.isNotBlank(firstLocId)) {
                            // 该位置节点在数据库已经存在
                        } else if (StringUtils.isNotBlank(val)) {
                            // 插入新的位置节点,根节点，parentId为0
                            LocationInfoEntity locationInfo = new LocationInfoEntity();
                            firstLocId = Utils.uuid();
                            locationInfo.setId(firstLocId);
                            locationInfo.setName(val);
                            locationInfo.setParentId("0");
                            locationInfo.setProjectId(projectId);
                            locationInfo.setCreateId(null == currentUser ? null : currentUser.getId());
                            locationInfo.setCreateTime(new Date());
                            locationInfoService.save(locationInfo);
                        }
                        // 一级位置
                        break;
                    case "secLoc":
                        // 二级位置
                        paramMap.put("projectId", projectId);
                        paramMap.put("locName", val);
                        paramMap.put("parentId", firstLocId);
                        secLocId = locationInfoService.findLocIdByLocName(paramMap);
                        if (StringUtils.isNotBlank(secLocId)) {
                            // 该位置节点在数据库已经存在
                        } else if (StringUtils.isNotBlank(val)) {
                            // 插入新的位置节点,根节点，parentId为0
                            if (StringUtils.isBlank(firstLocId)) {
                                break;
                            }
                            LocationInfoEntity locationInfo = new LocationInfoEntity();
                            secLocId = Utils.uuid();
                            locationInfo.setId(secLocId);
                            locationInfo.setName(val);
                            locationInfo.setParentId(firstLocId);
                            locationInfo.setProjectId(projectId);
                            locationInfo.setCreateId(null == currentUser ? null : currentUser.getId());
                            locationInfo.setCreateTime(new Date());
                            locationInfoService.save(locationInfo);
                        }
                        break;
                    case "thirdLoc":
                        // 三级位置
                        paramMap.put("projectId", projectId);
                        paramMap.put("locName", val);
                        paramMap.put("parentId", secLocId);
                        thirdLocId = locationInfoService.findLocIdByLocName(paramMap);
                        if (StringUtils.isNotBlank(thirdLocId)) {
                            // 该位置节点在数据库已经存在
                        } else if (StringUtils.isNotBlank(val)) {
                            // 插入新的位置节点,根节点，parentId为0
                            if (StringUtils.isBlank(secLocId)) {
                                break;
                            }
                            LocationInfoEntity locationInfo = new LocationInfoEntity();
                            thirdLocId = Utils.uuid();
                            locationInfo.setId(thirdLocId);
                            locationInfo.setProjectId(projectId);
                            locationInfo.setName(val);
                            locationInfo.setParentId(secLocId);
                            locationInfo.setCreateId(null == currentUser ? null : currentUser.getId());
                            locationInfo.setCreateTime(new Date());
                            locationInfoService.save(locationInfo);
                        }
                        break;
                    case "forthLoc":
                        // 四级位置
                        paramMap.put("projectId", projectId);
                        paramMap.put("locName", val);
                        paramMap.put("parentId", thirdLocId);
                        forthLocId = locationInfoService.findLocIdByLocName(paramMap);
                        if (StringUtils.isNotBlank(forthLocId)) {
                            // 该位置节点在数据库已经存在
                        } else if (StringUtils.isNotBlank(val)) {
                            // 插入新的位置节点,根节点，parentId为0
                            if (StringUtils.isBlank(thirdLocId)) {
                                break;
                            }
                            LocationInfoEntity locationInfo = new LocationInfoEntity();
                            forthLocId = Utils.uuid();
                            locationInfo.setId(forthLocId);
                            locationInfo.setName(val);
                            locationInfo.setProjectId(projectId);
                            locationInfo.setParentId(thirdLocId);
                            locationInfo.setCreateId(null == currentUser ? null : currentUser.getId());
                            locationInfo.setCreateTime(new Date());
                            locationInfoService.save(locationInfo);
                        }
                        break;
                    case "fifthLoc":
                        // 五级位置
                        paramMap.put("projectId", projectId);
                        paramMap.put("locName", val);
                        paramMap.put("parentId", forthLocId);
                        fifthLocId = locationInfoService.findLocIdByLocName(paramMap);
                        if (StringUtils.isNotBlank(fifthLocId)) {
                            // 该位置节点在数据库已经存在
                        } else if (StringUtils.isNotBlank(val)) {
                            // 插入新的位置节点,根节点，parentId为0
                            if (StringUtils.isBlank(forthLocId)) {
                                break;
                            }
                            LocationInfoEntity locationInfo = new LocationInfoEntity();
                            fifthLocId = Utils.uuid();
                            locationInfo.setId(fifthLocId);
                            locationInfo.setName(val);
                            locationInfo.setProjectId(projectId);
                            locationInfo.setParentId(forthLocId);
                            locationInfo.setCreateId(null == currentUser ? null : currentUser.getId());
                            locationInfo.setCreateTime(new Date());
                            locationInfoService.save(locationInfo);
                        }
                        break;
                    case "deviceMac":
                        // 设备Mac
                        deviceMac = val;
                        break;
                    case "secBoxGateway":
                        // 二级电箱网关号
                        secBoxGateway = val;
                        break;
                    case "standNo":
                        // 展位号
                        standNo = val;
                        break;
                    case "controlFlag":
                        // 是否受控
                        controlFlag = ("是".equals(val) ? "1" : "否".equals(val) ? "0" : null);
                        break;
                    case "boxCapacity":
                        // 电箱容量
                        boxCapacity = val;
                        break;
                    case "remark":
                        // 备注
                        remark = val;
                        break;
                    case "deviceBoxId":
                        deviceBoxId = val;
                        break;
                }
            }
            String locId = "";
            if (StringUtils.isNotBlank(fifthLocId)) {
                locId = fifthLocId.trim();
            } else if (StringUtils.isNotBlank(forthLocId)) {
                locId = forthLocId.trim();
            } else if (StringUtils.isNotBlank(thirdLocId)) {
                locId = thirdLocId.trim();
            } else if (StringUtils.isNotBlank(secLocId)) {
                locId = secLocId.trim();
            }

            if (StringUtils.isNotBlank(locId)) {
                if (StringUtils.isNotBlank(deviceMac)) {
                    // 判断电箱是否存在
                    DeviceBoxInfoEntity tmpDeviceBox = null;
                    if (StringUtils.isNotBlank(deviceBoxId)) {
                        tmpDeviceBox = deviceBoxInfoDao.queryObject(deviceBoxId);
                    } else {
                        tmpDeviceBox = deviceBoxInfoDao.queryProjectDeviceBox(projectId, deviceMac);
                    }

                    if (tmpDeviceBox != null) {
                        LocationInfoEntity location = new LocationInfoEntity();
                        location.setId(locId);

                        Map<String, String> paramsMap = new HashMap<String, String>();
                        paramsMap.put("deviceBoxId", tmpDeviceBox.getId());
                        // 根据电箱查找电箱对应的位置关系
                        List<DboxLocLinkEntity> dboxLocList = deviceBoxInfoDao.queryLinkListByBoxId(paramsMap);
                        if (dboxLocList != null && dboxLocList.size() > 0) {
                            DboxLocLinkEntity dboxLoc = dboxLocList.get(0);
                            // 原位置
                            String originalLocation = dboxLoc.getLocationInfo().getId();

                            dboxLoc.setLocationInfo(location);
                            dboxLoc.setCreateId(null == currentUser ? null : currentUser.getId());
                            dboxLoc.setUpdateTime(new Date());
                            deviceBoxInfoDao.updateBoxLocLink(dboxLoc);

                            this.locationInfoService.deleteLocation(originalLocation);
                        } else {
                            DboxLocLinkEntity dboxLoc = new DboxLocLinkEntity();
                            dboxLoc.setId(Utils.uuid());
                            dboxLoc.setLocationInfo(location);
                            dboxLoc.setDboxInfo(tmpDeviceBox);
                            dboxLoc.setCreateId(null == currentUser ? null : currentUser.getId());
                            dboxLoc.setCreateTime(new Date());
                            deviceBoxInfoDao.saveBoxLocLink(dboxLoc);
                        }

                        // 根据电箱MAC地址更新电箱信息
                        tmpDeviceBox.setDeviceBoxName(CommUtils.getDeviceBoxAddress(deviceMac));
                        tmpDeviceBox.setDeviceBoxNum(deviceMac);
                        tmpDeviceBox.setUpdateId(null == currentUser ? null : currentUser.getId());
                        tmpDeviceBox.setUpdateTime(new Date());
                        tmpDeviceBox.setRemark(remark);
                        tmpDeviceBox.setSecBoxGateway(secBoxGateway);
                        tmpDeviceBox.setStandNo(standNo);
                        tmpDeviceBox.setControlFlag(controlFlag);
                        tmpDeviceBox.setBoxCapacity(boxCapacity);
                        tmpDeviceBox.setProjectId(projectId);
                        results.add(tmpDeviceBox);
                        deviceBoxInfoDao.update(tmpDeviceBox);
                    } else {
                        // 电箱不存在 新增电箱
                        DeviceBoxInfoEntity deviceBoxInfo = new DeviceBoxInfoEntity();
                        deviceBoxInfo.setId(Utils.uuid());
                        deviceBoxInfo.setDeviceBoxName(CommUtils.getDeviceBoxAddress(deviceMac));
                        deviceBoxInfo.setDeviceBoxNum(deviceMac);
                        deviceBoxInfo.setCreateId(null == currentUser ? null : currentUser.getId());
                        deviceBoxInfo.setCreateTime(new Date());
                        deviceBoxInfo.setProjectId(projectId);

                        deviceBoxInfo.setRemark(remark);
                        deviceBoxInfo.setSecBoxGateway(secBoxGateway);
                        deviceBoxInfo.setStandNo(standNo);
                        deviceBoxInfo.setControlFlag(controlFlag);
                        deviceBoxInfo.setBoxCapacity(boxCapacity);

                        deviceBoxInfoDao.save(deviceBoxInfo);
                        // 新增电箱和位置的关系
                        LocationInfoEntity location = new LocationInfoEntity();
                        location.setId(locId);
                        // 新增电箱和位置关系
                        DboxLocLinkEntity dboxLoc = new DboxLocLinkEntity();
                        dboxLoc.setId(Utils.uuid());
                        dboxLoc.setLocationInfo(location);
                        dboxLoc.setDboxInfo(deviceBoxInfo);
                        dboxLoc.setCreateId(null == currentUser ? null : currentUser.getId());
                        dboxLoc.setCreateTime(new Date());
                        deviceBoxInfoDao.saveBoxLocLink(dboxLoc);
                        results.add(deviceBoxInfo);
                        // 设置终端对应的项目
                        String str = deviceMac;
                        if (str != null && !"".equals(str) && str.length() > 6) {
                            String terminalId = CommUtils.getDeviceBoxAddress(str);
                            if(str.startsWith("LY"))
                                terminalId += "_LY";
                            redisUtil.hset(0, "TERMINAL_CONFIG", terminalId, projectId);
                        }
                    }
                }
            }
        }
        return results;
    }

    public DeviceBoxInfoEntity queryByMac(String deviceMac, String projectId) {
        return deviceBoxInfoDao.queryByMac(deviceMac, projectId);
    }

    public DeviceBoxInfoEntity queryProjectMac(String projectId, String deviceMac) {
        return deviceBoxInfoDao.queryProjectMac(projectId, deviceMac);
    }

    public List<DeviceBoxInfoEntity> findDeviceBoxsInfoLike(String projectId, String deviceMac) {
        return deviceBoxInfoDao.findDeviceBoxsInfoLike(projectId, deviceMac);
    }

    public int deleteProjectDeviceBox(String projectId) {
        return deviceBoxInfoDao.deleteProjectDeviceBox(projectId);
    }

    public int updateBoxPlacedFlag(String projectId, String deviceBoxMac, String placedFlag) {
        return deviceBoxInfoDao.updateBoxPlacedFlag(projectId, deviceBoxMac, placedFlag);
    }
}