package com.wz.modules.activiti.dao;

import org.apache.ibatis.annotations.Mapper;

import com.wz.modules.activiti.entity.ExtendActNodeuserEntity;
import com.wz.modules.common.dao.BaseDao;

import java.util.List;

/**
 * 节点可选人
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-07-24 13:28:51
 */
@Mapper
public interface ExtendActNodeuserDao extends BaseDao<ExtendActNodeuserEntity> {

    /**
     * 根据节点获取节点审批人员信息
     * @param nodeId
     */
    List<ExtendActNodeuserEntity> getNodeUserByNodeId(String nodeId);

    /**
     * 根据节点Id删除
     * @param nodeId
     */
    void delByNodeId(String nodeId);
	
}
