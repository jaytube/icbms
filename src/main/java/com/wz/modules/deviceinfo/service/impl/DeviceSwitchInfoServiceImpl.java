package com.wz.modules.deviceinfo.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wz.modules.common.utils.Utils;
import com.wz.modules.deviceinfo.dao.DeviceSwitchInfoDao;
import com.wz.modules.deviceinfo.entity.DeviceSwitchInfoEntity;
import com.wz.modules.deviceinfo.service.DeviceSwitchInfoService;



@Service("deviceSwitchInfoService")
public class DeviceSwitchInfoServiceImpl implements DeviceSwitchInfoService {
	@Autowired
	private DeviceSwitchInfoDao deviceSwitchInfoDao;
	
	@Override
	public DeviceSwitchInfoEntity queryObject(String id){
		return deviceSwitchInfoDao.queryObject(id);
	}
	
	@Override
	public List<DeviceSwitchInfoEntity> queryList(Map<String, Object> map){
		return deviceSwitchInfoDao.queryList(map);
	}

    @Override
    public List<DeviceSwitchInfoEntity> queryListByBean(DeviceSwitchInfoEntity entity) {
        return deviceSwitchInfoDao.queryListByBean(entity);
    }
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return deviceSwitchInfoDao.queryTotal(map);
	}
	
	@Override
	public int save(DeviceSwitchInfoEntity deviceSwitchInfo){
        deviceSwitchInfo.setId(Utils.uuid());
		return deviceSwitchInfoDao.save(deviceSwitchInfo);
	}
	
	@Override
	public int update(DeviceSwitchInfoEntity deviceSwitchInfo){
        return deviceSwitchInfoDao.update(deviceSwitchInfo);
	}
	
	@Override
	public int delete(String id){
        return deviceSwitchInfoDao.delete(id);
	}
	
	@Override
	public int deleteBatch(String[] ids){
        return deviceSwitchInfoDao.deleteBatch(ids);
	}
	
}
