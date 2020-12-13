package com.wz.modules.quartz.dao;


import org.apache.ibatis.annotations.Mapper;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.quartz.entity.ScheduleJobEntity;

import java.util.Map;

/**
 * 类ScheduleJobDao的功能描述:
 * 定时任务
 * @auther hxy
 * @date 2017-08-25 16:14:04
 */
@Mapper
public interface ScheduleJobDao extends BaseDao<ScheduleJobEntity> {
	
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
