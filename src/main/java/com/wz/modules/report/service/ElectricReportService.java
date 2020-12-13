package com.wz.modules.report.service;

import java.util.List;
import java.util.Map;

import com.wz.modules.report.entity.ElectricDailyEntity;
import com.wz.modules.report.entity.ElectricReportEntity;

public interface ElectricReportService {

    public List<ElectricReportEntity> queryList(Map<String, Object> map);

    public int queryTotal(Map<String, Object> map);

	public List<ElectricDailyEntity> queryDailyElectricCnt(Map<String, Object> map);

	public int queryDailyElectricCntTotal(Map<String, Object> map);
}
