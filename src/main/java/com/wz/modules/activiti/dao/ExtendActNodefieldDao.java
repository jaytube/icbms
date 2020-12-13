package com.wz.modules.activiti.dao;

import org.apache.ibatis.annotations.Mapper;

import com.wz.modules.activiti.entity.ExtendActNodefieldEntity;
import com.wz.modules.common.dao.BaseDao;

import java.util.List;

/**
 * 流程节点对应的条件
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-07-24 13:28:51
 */
@Mapper
public interface ExtendActNodefieldDao extends BaseDao<ExtendActNodefieldEntity> {

    /**
     * 根据节点id删除
     * @param nodeId
     */
    void delByNodeId(String nodeId);

    /**
     * 根据节点集合查询
     * @param nodes
     * @return
     */
    List<ExtendActNodefieldEntity> queryByNodes(List<String> nodes);
}
