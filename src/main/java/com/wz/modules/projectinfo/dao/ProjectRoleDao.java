package com.wz.modules.projectinfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.projectinfo.entity.ProjectRoleEntity;

/**
 * 项目角色关系表
 * 
 * @author hxy
 * @email rui.sun.java@gmail.com
 * @date 2019-10-15 22:59:42
 */
@Mapper
public interface ProjectRoleDao extends BaseDao<ProjectRoleEntity> {

	/**
	 * 根据用户ID，获取角色ID列表
	 */
	List<String> queryRoleIdList(String projectId);

}
