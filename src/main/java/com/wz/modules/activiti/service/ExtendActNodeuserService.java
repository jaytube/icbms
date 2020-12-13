package com.wz.modules.activiti.service;

import java.util.List;
import java.util.Map;

import com.wz.modules.activiti.entity.ExtendActNodeuserEntity;

/**
 * 节点可选人
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-07-24 13:28:51
 */
public interface ExtendActNodeuserService {
	
	ExtendActNodeuserEntity queryObject(String id);
	
	List<ExtendActNodeuserEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ExtendActNodeuserEntity extendActNodeuser);
	
	void update(ExtendActNodeuserEntity extendActNodeuser);
	
	void delete(String id);
	
	void deleteBatch(String[] ids);

	/**
	 * 根据节点获取节点审批人员信息
	 * @param nodeId
	 */
	 List<ExtendActNodeuserEntity> getNodeUserByNodeId(String nodeId);

}
