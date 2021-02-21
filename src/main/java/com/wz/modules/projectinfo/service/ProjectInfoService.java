package com.wz.modules.projectinfo.service;

import java.util.List;
import java.util.Map;

import com.wz.modules.projectinfo.entity.ProjectInfoEntity;
import com.wz.modules.projectinfo.entity.ProjectInfoPlainEntity;

/**
 * 项目基础表; InnoDB free: 401408 kB
 * 
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:14:33
 */
public interface ProjectInfoService {

	ProjectInfoEntity queryObject(String id);

	List<ProjectInfoEntity> queryProjects(String[] ids);

	List<ProjectInfoEntity> queryList(Map<String, Object> map);

	List<ProjectInfoEntity> queryListByBean(ProjectInfoEntity entity);

	int queryTotal(Map<String, Object> map);

	int save(ProjectInfoEntity projectInfo);

	int update(ProjectInfoEntity projectInfo);

	int delete(String id);

	int deleteBatch(String[] ids);

	List<ProjectInfoEntity> queryListAll();

	List<String> queryRoleIdList(String projectId);

	List<ProjectInfoPlainEntity> queryPlainProjects(String[] ids);

}
