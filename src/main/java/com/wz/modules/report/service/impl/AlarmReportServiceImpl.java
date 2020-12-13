package com.wz.modules.report.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wz.modules.common.utils.Utils;
import com.wz.modules.report.dao.AlarmInfoReportDao;
import com.wz.modules.report.entity.AlarmInfoReportEntity;
import com.wz.modules.report.service.AlarmReportService;

@Service("alarmReportService")
public class AlarmReportServiceImpl implements AlarmReportService {
    @Autowired
    private AlarmInfoReportDao alarmInfoReportDao;


    @Override
    public AlarmInfoReportEntity queryObject(String id){
        return alarmInfoReportDao.queryObject(id);
    }

    @Override
    public List<AlarmInfoReportEntity> queryList(Map<String, Object> map){

        return alarmInfoReportDao.queryList(map);
    }

    @Override
    public List<AlarmInfoReportEntity> queryListByBean(AlarmInfoReportEntity entity) {
        return alarmInfoReportDao.queryListByBean(entity);
    }

    @Override
    public int queryTotal(Map<String, Object> map){
        return alarmInfoReportDao.queryTotal(map);
    }

    @Override
    public int save(AlarmInfoReportEntity deviceAlarmInfoLog){
        deviceAlarmInfoLog.setId(Utils.uuid());
        return alarmInfoReportDao.save(deviceAlarmInfoLog);
    }

    @Override
    public int update(AlarmInfoReportEntity deviceAlarmInfoLog){
        return alarmInfoReportDao.update(deviceAlarmInfoLog);
    }

    @Override
    public int delete(String id){
        return alarmInfoReportDao.delete(id);
    }

    @Override
    public int deleteBatch(String[] ids){
        return alarmInfoReportDao.deleteBatch(ids);
    }
}
