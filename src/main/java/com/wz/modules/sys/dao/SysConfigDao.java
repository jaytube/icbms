package com.wz.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.oss.entity.SysConfigEntity;

/**
 * 类SysConfigDao的功能描述:
 * 系统配置信息
 * @auther hxy
 * @date 2017-08-25 16:14:20
 */
@Mapper
public interface SysConfigDao extends BaseDao<SysConfigEntity> {
	
	/**
	 * 根据key，查询value
	 */
	String queryByKey(String paramKey);
	
	/**
	 * 根据key，更新value
	 */
	int updateValueByKey(@Param("key") String key, @Param("value") String value);
	
}
