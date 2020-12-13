package com.wz.modules.devicelog.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wz.modules.common.utils.Utils;
import com.wz.modules.deviceinfo.entity.DeviceBoxInfoEntity;
import com.wz.modules.deviceinfo.service.DeviceBoxInfoService;
import com.wz.modules.devicelog.dao.DeviceSwitchInfoLogDao;
import com.wz.modules.devicelog.entity.DeviceSwitchInfoLogEntity;
import com.wz.modules.devicelog.service.DeviceSwitchInfoLogService;
import com.wz.modules.kk.entity.PageInfo;

@Service("deviceSwitchInfoLogService")
public class DeviceSwitchInfoLogServiceImpl implements DeviceSwitchInfoLogService {
	@Autowired
	private DeviceSwitchInfoLogDao deviceSwitchInfoLogDao;

	@Autowired
	private DeviceBoxInfoService deviceBoxInfoService;

	@Override
	public DeviceSwitchInfoLogEntity queryObject(String id) {
		return deviceSwitchInfoLogDao.queryObject(id);
	}

	@Override
	public List<DeviceSwitchInfoLogEntity> queryList(Map<String, Object> map) {
		return deviceSwitchInfoLogDao.queryList(map);
	}

	@Override
	public List<DeviceSwitchInfoLogEntity> queryListByBean(DeviceSwitchInfoLogEntity entity) {
		return deviceSwitchInfoLogDao.queryListByBean(entity);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return deviceSwitchInfoLogDao.queryTotal(map);
	}

	@Override
	public int save(DeviceSwitchInfoLogEntity deviceSwitchInfoLog) {
		deviceSwitchInfoLog.setId(Utils.uuid());
		return deviceSwitchInfoLogDao.save(deviceSwitchInfoLog);
	}

	@Override
	public int update(DeviceSwitchInfoLogEntity deviceSwitchInfoLog) {
		return deviceSwitchInfoLogDao.update(deviceSwitchInfoLog);
	}

	@Override
	public int delete(String id) {
		return deviceSwitchInfoLogDao.delete(id);
	}

	@Override
	public int deleteBatch(String[] ids) {
		return deviceSwitchInfoLogDao.deleteBatch(ids);
	}

	@Override
	public PageInfo<DeviceSwitchInfoLogEntity> queryAppPage(String projectId, String beginTime, String endTime,
			String page, String pageSize) {
		Map<String, String> boxMap = new HashMap<String, String>();
		List<DeviceBoxInfoEntity> boxList = deviceBoxInfoService.findDeviceBoxsInfoByProjectId(projectId);
		for (DeviceBoxInfoEntity box : boxList) {
			boxMap.put(box.getId(), box.getDeviceBoxName());
		}

		if (StringUtils.isBlank(page)) {
			page = "1";
		}
		if (StringUtils.isBlank(pageSize)) {
			pageSize = "5";
		}
		Integer offset = Integer.parseInt(page) * Integer.parseInt(pageSize);
		List<DeviceSwitchInfoLogEntity> result = this.deviceSwitchInfoLogDao.queryAppList(projectId, beginTime, endTime,
				offset, Integer.parseInt(pageSize));
		for (DeviceSwitchInfoLogEntity entity : result) {
			entity.setDeviceBoxName(boxMap.get(entity.getDeviceBoxId()));
		}
		int totalCount = this.deviceSwitchInfoLogDao.queryAppTotal(projectId, beginTime, endTime);
		int totalPage = totalCount / Integer.parseInt(pageSize);
		if (totalCount % Integer.parseInt(pageSize) != 0) {
			totalPage = totalPage + 1;
		}
		PageInfo<DeviceSwitchInfoLogEntity> pageInfo = new PageInfo<DeviceSwitchInfoLogEntity>();
		pageInfo.setTotal(String.valueOf(totalCount));
		pageInfo.setTotalPage(String.valueOf(totalPage));
		pageInfo.setPage(page);
		pageInfo.setDataList(result);
		return pageInfo;
	}

	@Override
	public int insertDeviceSwitchHis(String synDate) {
		return this.deviceSwitchInfoLogDao.insertDeviceSwitchHis(synDate);
	}

	@Override
	public int deleteDeviceSwitchLog(String synDate) {
		return this.deviceSwitchInfoLogDao.deleteDeviceSwitchLog(synDate);
	}

	public List<DeviceSwitchInfoLogEntity> getDeviceBoxHistory(String deviceBoxId, String projectId, String hours) {
		return this.deviceSwitchInfoLogDao.getDeviceBoxHistory(deviceBoxId, projectId, hours);
	}
}
