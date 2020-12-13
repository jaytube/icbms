package com.wz.modules.sys.dao;


import org.apache.ibatis.annotations.Mapper;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.sys.entity.SysLogEntity;

/**
 * 类SysLogDao的功能描述:
 * 系统日志
 * @auther hxy
 * @date 2017-08-25 16:14:57
 */
@Mapper
public interface SysLogDao extends BaseDao<SysLogEntity> {
	
}
