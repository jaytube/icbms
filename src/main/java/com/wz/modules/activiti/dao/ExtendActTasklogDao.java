package com.wz.modules.activiti.dao;

import org.apache.ibatis.annotations.Mapper;

import com.wz.modules.activiti.entity.ExtendActTasklogEntity;
import com.wz.modules.common.dao.BaseDao;

/**
 * 
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-08-04 11:46:48
 */
@Mapper
public interface ExtendActTasklogDao extends BaseDao<ExtendActTasklogEntity> {
    /**
     * 根据任务id 更改日志
     * @param extendActTasklogEntity
     * @return
     */
    int updateByTaskId(ExtendActTasklogEntity extendActTasklogEntity);

    /**
     * 转办任务时更新任务日志，有可能会存在多次转办，就会产生多条任务日志，所有这里用 taskId+appAction为空 作唯一
     * @param extendActTasklogEntity
     * @return
     */
    int updateByTaskIdOpinion(ExtendActTasklogEntity extendActTasklogEntity);
}
