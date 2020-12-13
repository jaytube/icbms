package com.wz.modules.report.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wz.modules.report.dao.ElectricReportDao;
import com.wz.modules.report.entity.ElectricDailyEntity;
import com.wz.modules.report.entity.ElectricReportEntity;
import com.wz.modules.report.service.ElectricReportService;

@Service("electricReportService")
public class ElectricReportServiceImpl implements ElectricReportService {

    @Autowired
    private ElectricReportDao electricReportDao;

    @Override
    public List<ElectricReportEntity> queryList(Map<String, Object> map) {

        return electricReportDao.findElectricByDay(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return electricReportDao.findElectricByDayTotal(map);
    }

	@Override
	public List<ElectricDailyEntity> queryDailyElectricCnt(Map<String, Object> map) {

		return electricReportDao.doStatDailyElectricCnt(map);
	}

	@Override
	public int queryDailyElectricCntTotal(Map<String, Object> map) {
		return electricReportDao.doStatDailyElectricCntTotal(map);
	}


}
