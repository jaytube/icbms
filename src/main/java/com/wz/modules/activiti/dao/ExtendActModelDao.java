package com.wz.modules.activiti.dao;

import org.apache.ibatis.annotations.Mapper;

import com.wz.modules.activiti.entity.ExtendActModelEntity;
import com.wz.modules.common.dao.BaseDao;

/**
 * 流程模板扩展表
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-07-14 11:02:01
 */
@Mapper
public interface ExtendActModelDao extends BaseDao<ExtendActModelEntity> {

    /**
     * 根据模型id获取流程模型和业务相关信息
      * @param modelId
     * @return
     */
    ExtendActModelEntity getModelAndBusInfo(String modelId);

	
}
