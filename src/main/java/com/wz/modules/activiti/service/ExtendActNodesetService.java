package com.wz.modules.activiti.service;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.wz.modules.activiti.entity.ExtendActNodesetEntity;

/**
 * 流程节点配置
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-07-24 13:28:51
 */
public interface ExtendActNodesetService {
	
	ExtendActNodesetEntity queryObject(String id);
	
	List<ExtendActNodesetEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void delete(String id);
	
	void deleteBatch(String[] ids);

	/**
	 * 保存节点业务信息、审批范围设置、节点条件设置
	 * @param extendActNodesetEntity
	 */
	ExtendActNodesetEntity saveNode(ExtendActNodesetEntity extendActNodesetEntity) throws IOException;

	/**
	 * 根据nodeId查询节点信息
	 * @param nodeId
	 * @return
	 */
	ExtendActNodesetEntity queryByNodeId(String nodeId);

	/**
	 * 根据nodeId和模型id查询节点信息
	 * @param nodeId
	 * @param modelId
	 * @return
	 */
	ExtendActNodesetEntity queryByNodeIdModelId(String nodeId, String modelId);

}
