package com.wz.modules.sys.service;


import java.util.List;
import java.util.Map;

import com.wz.modules.common.page.Page;
import com.wz.modules.sys.entity.RoleEntity;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.entity.UserWindowDto;

/**
 * 系统用户表
 * 
 * @author chenshun
 * @email rui.sun.java@gmail.com
 * @date 2017-05-03 09:41:38
 */
public interface UserService {
	
	UserEntity queryObject(String id);

	List<UserEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);
	
	void save(UserEntity user);
	
	void update(UserEntity user);
	
	void delete(String id);
	
	void deleteBatch(String[] ids);

	/**
	 * 根据登陆用户查询有效的用户
	 * @param logName
	 * @return
	 */
	UserEntity queryByLoginName(String logName);

	/**
	 * 用户对应的机构id,数据权限控制
	 * @param userId
	 * @return key:roleId 角色id value:角色所对应的机构集合
	 */
	Map<String,List<String>> queryBapidByUserId(String userId);

	/**
	 * 用户对应的机构id,数据权限控制
	 * @param userId
	 */
	List<String> queryBapidByUserIdArray(String userId);


	/**
	 * 用户对应的部门id,数据权限控制
	 * @param userId
	 * @return key:roleId 角色id value:角色所对应的部门集合
	 */
	Map<String,List<String>> queryBaidByUserId(String userId);

	/**
	 * 用户对应的部门id,数据权限控制
	 * @param userId
	 */
	List<String> queryBaidByUserIdArray(String userId);

	/**
	 * 根据条件分页查询
	 * @param userEntity
	 * @param pageNum
	 * @return
	 */
	Page<UserWindowDto> findPage(UserWindowDto userEntity, int pageNum);

	/**
	 * 修改密码
	 * @param user
	 * @return
	 */
	int updatePassword(UserEntity user);

	/**
	 * 批量更新角色状态
	 * @param status 状态(0正常 -1禁用)
	 * @return
	 */
	int updateBatchStatus(String[] ids, String status);

	/**
	 * 重置密码
	 * @param ids
	 * @return
	 */
	int resetPassWord(String[] ids);


	List<UserEntity> queryObjectByRole();
	
	
	
	List<UserEntity> queryUserProjectRel(String projectId);
	
	
	List<RoleEntity> queryRoleUser();
}
