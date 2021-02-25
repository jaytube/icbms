package com.wz.modules.projectinfo.dao;

import java.util.List;

import com.wz.modules.projectinfo.entity.ProjectInfoPlainEntity;
import org.apache.ibatis.annotations.Mapper;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;

/**
 * 项目基础表; InnoDB free: 401408 kB
 * 
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:14:33
 */
@Mapper
public interface ProjectInfoDao extends BaseDao<ProjectInfoEntity> {
	
	public List<ProjectInfoEntity> queryListAll();

	public List<ProjectInfoEntity> queryProjectsByIds(String[] ids);

	public List<ProjectInfoPlainEntity> queryPlainProjectsByIds(String[] ids);

	ProjectInfoEntity queryById(String id);

}
