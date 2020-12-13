package com.wz.modules.activiti.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.wz.modules.activiti.entity.ExtendActNodesetEntity;
import com.wz.modules.common.dao.BaseDao;

/**
 * 流程节点配置
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-07-24 13:28:51
 */
@Mapper
public interface ExtendActNodesetDao extends BaseDao<ExtendActNodesetEntity> {
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
    ExtendActNodesetEntity queryByNodeIdModelId(@Param("nodeId") String nodeId,@Param("modelId") String modelId);

}
