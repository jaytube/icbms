package com.wz.modules.devicelog.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wz.modules.common.utils.Utils;
import com.wz.modules.devicelog.dao.DeviceAlarmFlowDao;
import com.wz.modules.devicelog.entity.DeviceAlarmFlowEntity;
import com.wz.modules.devicelog.service.DeviceAlarmFlowService;
import com.wz.modules.kk.entity.PageInfo;

@Service("deviceAlarmFlowService")
public class DeviceAlarmFlowServiceImpl implements DeviceAlarmFlowService {
	@Autowired
	private DeviceAlarmFlowDao deviceAlarmFlowDao;

	@Override
	public DeviceAlarmFlowEntity queryObject(String id) {
		return deviceAlarmFlowDao.queryObject(id);
	}

	@Override
	public List<DeviceAlarmFlowEntity> queryList(Map<String, Object> map) {
		return deviceAlarmFlowDao.queryList(map);
	}

	@Override
	public List<DeviceAlarmFlowEntity> queryListByBean(DeviceAlarmFlowEntity entity) {
		return deviceAlarmFlowDao.queryListByBean(entity);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return deviceAlarmFlowDao.queryTotal(map);
	}

	@Override
	public int save(DeviceAlarmFlowEntity deviceAlarmFlow) {
		deviceAlarmFlow.setId(Utils.uuid());
		return deviceAlarmFlowDao.save(deviceAlarmFlow);
	}

	@Override
	public int update(DeviceAlarmFlowEntity deviceAlarmFlow) {
		return deviceAlarmFlowDao.update(deviceAlarmFlow);
	}

	@Override
	public int delete(String id) {
		return deviceAlarmFlowDao.delete(id);
	}

	@Override
	public int deleteBatch(String[] ids) {
		return deviceAlarmFlowDao.deleteBatch(ids);
	}

	public PageInfo<DeviceAlarmFlowEntity> queryFlowPage(String projectId, String status, String alarmStartDate,
			String alarmEndDate, String deviceBoxMac, String standNo, String alarmType, String page, String pageSize) {
		if (StringUtils.isBlank(page)) {
			page = "1";
		}
		if (StringUtils.isBlank(pageSize)) {
			pageSize = "5";
		}
		int curPage = Integer.parseInt(page);
		Integer offset = (curPage - 1) * Integer.parseInt(pageSize);
		List<DeviceAlarmFlowEntity> result = this.deviceAlarmFlowDao.queryFlowPage(projectId, status, alarmStartDate,
				alarmEndDate, deviceBoxMac, standNo, alarmType, offset, Integer.parseInt(pageSize));
		int totalCount = this.deviceAlarmFlowDao.queryFlowTotal(projectId, status, alarmStartDate, alarmEndDate,
				deviceBoxMac, standNo, alarmType);
		int totalPage = totalCount / Integer.parseInt(pageSize);
		if (totalCount % Integer.parseInt(pageSize) != 0) {
			totalPage = totalPage + 1;
		}
		PageInfo<DeviceAlarmFlowEntity> pageInfo = new PageInfo<DeviceAlarmFlowEntity>();
		pageInfo.setTotal(String.valueOf(totalCount));
		pageInfo.setTotalPage(String.valueOf(totalPage));
		pageInfo.setPage(page);
		pageInfo.setDataList(result);
		return pageInfo;
	}
}