package com.wz.modules.projectinfo.service;

import java.util.List;
import java.util.Map;

import com.wz.modules.projectinfo.entity.LocationInfoEntity;

/**
 * 位置基础表; InnoDB free: 401408 kB
 * 
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:14:33
 */
public interface LocationInfoService {

	LocationInfoEntity queryObject(String id);

	List<LocationInfoEntity> queryList(Map<String, Object> map);

	List<LocationInfoEntity> queryListByBean(LocationInfoEntity entity);

	int queryTotal(Map<String, Object> map);

	String save(LocationInfoEntity locationInfo);

	int update(LocationInfoEntity locationInfo);

	int delete(String id);

	int deleteLocation(String id);

	int deleteBatch(String[] ids);

	List<LocationInfoEntity> findLocInfoByPId(String projectId);

	List<LocationInfoEntity> findProjectLocationRel(String projectId);

	List<LocationInfoEntity> queryListParentId(String parenId);

	String findLocIdByLocName(Map<String, String> map);

	int delProjectLocationRel(String projectId);

	int delProjectLocation(String projectId);

	void saveLocationsForNewProject(String projectId, int gymId, String projectName);

}