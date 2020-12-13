package com.wz.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.sys.entity.NoticeUserEntity;

/**
 * 通知和用户关系表
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-08-31 15:58:58
 */
@Mapper
public interface NoticeUserDao extends BaseDao<NoticeUserEntity> {
    /**
     * 根据通知id和用户id 更新
     * @param noticeUserEntity
     * @return
     */
    int updateByNoticeIdUserId(NoticeUserEntity noticeUserEntity);
}
