package com.wz.modules.app.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.wz.modules.app.entity.ApiUserEntity;

/**
 * 系统用户表
 * 
 * @author chenshun
 * @email rui.sun.java@gmail.com
 * @date 2017-05-03 09:41:38
 */
@Mapper
public interface ApiUserDao{
    /**
     * 根据id获取用户相关信息
     * @param id
     * @return
     */
    ApiUserEntity userInfo(String id);
}
