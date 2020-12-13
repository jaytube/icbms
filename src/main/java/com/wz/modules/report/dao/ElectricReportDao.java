package com.wz.modules.report.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.wz.modules.report.entity.ElectricDailyEntity;
import com.wz.modules.report.entity.ElectricReportEntity;

@Mapper
public interface ElectricReportDao {

    public List<ElectricReportEntity> findElectricByDay(Map<String, Object> map);

    public int findElectricByDayTotal(Map<String, Object> map);

	public List<ElectricDailyEntity> doStatDailyElectricCnt(Map<String, Object> map);

	public int doStatDailyElectricCntTotal(Map<String, Object> map);
}
