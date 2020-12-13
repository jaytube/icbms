package com.wz.modules.quartz.dao;


import org.apache.ibatis.annotations.Mapper;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.quartz.entity.ScheduleJobLogEntity;

/**
 * 类ScheduleJobLogDao的功能描述:
 * 定时任务日志
 * @auther hxy
 * @date 2017-08-25 16:13:41
 */
@Mapper
public interface ScheduleJobLogDao extends BaseDao<ScheduleJobLogEntity> {
	
}
