package com.wz.modules.devicelog.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wz.modules.common.utils.Utils;
import com.wz.modules.devicelog.dao.DeviceElectricityLogDao;
import com.wz.modules.devicelog.entity.DeviceElecStatEntity;
import com.wz.modules.devicelog.entity.DeviceElectricityLogEntity;
import com.wz.modules.devicelog.service.DeviceElectricityLogService;

@Service("deviceElectricityLogService")
public class DeviceElectricityLogServiceImpl implements DeviceElectricityLogService {
	@Autowired
	private DeviceElectricityLogDao deviceElectricityLogDao;

	@Override
	public DeviceElectricityLogEntity queryObject(String id) {
		return deviceElectricityLogDao.queryObject(id);
	}

	@Override
	public List<DeviceElectricityLogEntity> queryList(Map<String, Object> map) {
		return deviceElectricityLogDao.queryList(map);
	}

	@Override
	public List<DeviceElectricityLogEntity> queryListByBean(DeviceElectricityLogEntity entity) {
		return deviceElectricityLogDao.queryListByBean(entity);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return deviceElectricityLogDao.queryTotal(map);
	}

	@Override
	public int save(DeviceElectricityLogEntity deviceElectricityLog) {
		deviceElectricityLog.setId(Utils.uuid());
		return deviceElectricityLogDao.save(deviceElectricityLog);
	}

	@Override
	public int update(DeviceElectricityLogEntity deviceElectricityLog) {
		return deviceElectricityLogDao.update(deviceElectricityLog);
	}

	@Override
	public int delete(String id) {
		return deviceElectricityLogDao.delete(id);
	}

	@Override
	public int deleteBatch(String[] ids) {
		return deviceElectricityLogDao.deleteBatch(ids);
	}

	@Override
	public List<DeviceElecStatEntity> doStatDeviceElec(String projectId, String startDate) {
		return deviceElectricityLogDao.doStatDeviceElec(projectId, startDate);
	}

    @Override
    public List<DeviceElectricityLogEntity> doStatElectricCnt(String projectId, String statDate) {
        return deviceElectricityLogDao.doStatElectricCnt(projectId, statDate);
    }

    @Override
    public void doDeleteElectricCnt(String projectId, String statDate) {
        deviceElectricityLogDao.doDeleteElectricCnt(projectId, statDate);
    }

}