package com.wz.modules.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.demo.entity.LeaveEntity;

/**
 * 请假流程测试
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-07-31 13:33:23
 */
@Mapper
public interface LeaveDao extends BaseDao<LeaveEntity> {
	
}
