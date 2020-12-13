package com.wz.modules.demo.service;


import java.util.List;
import java.util.Map;

import com.wz.modules.common.page.Page;
import com.wz.modules.demo.entity.LeaveEntity;

/**
 * 请假流程测试
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-07-31 13:33:23
 */
public interface LeaveService {
	
	LeaveEntity queryObject(String id);
	
	List<LeaveEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(LeaveEntity leave);
	
	void update(LeaveEntity leave);
	
	int delete(String id);
	
	void deleteBatch(String[] ids);

	Page<LeaveEntity> findPage(LeaveEntity leaveEntity, int pageNum);
}
