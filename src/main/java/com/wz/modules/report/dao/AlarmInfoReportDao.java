package com.wz.modules.report.dao;


import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.report.entity.AlarmInfoReportEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 空开设备警告日志表; InnoDB free: 237568 kB
 * 
 * @author hxy
 * @email rui.sun.java@gmail.com
 * @date 2018-08-23 20:27:10
 */
@Mapper
public interface AlarmInfoReportDao extends BaseDao<AlarmInfoReportEntity> {
	
}
