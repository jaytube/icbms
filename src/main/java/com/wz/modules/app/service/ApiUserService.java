package com.wz.modules.app.service;


import com.wz.modules.app.entity.ApiUserEntity;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.service.UserService;

/**
 * 类UserService的功能描述:
 * 用户
 * @auther hxy
 * @date 2017-10-16 14:17:17
 */
public interface ApiUserService extends UserService {

	/**
	 * 用户登录
	 * @return          返回用户ID
	 */
	String login(UserEntity userEntity);

	/**
	 * 根据id获取用户相关信息
	 * @param id
	 * @return
	 */
	ApiUserEntity userInfo(String id);

	/**
	 * 修改密码
	 * @param oldUser
	 * @param newUser
	 * @return
	 */
	int updatePassword(UserEntity newUser, UserEntity oldUser);
	/**
	 * 用户登录 校验用户名和密码
	 * @param loginName
	 * @param password
	 * @return
	 */
	UserEntity checkUserAcct(String loginName, String password);

}
