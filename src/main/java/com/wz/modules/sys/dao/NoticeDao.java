package com.wz.modules.sys.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.sys.entity.NoticeEntity;

import java.util.List;

/**
 * 通知
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-08-31 15:59:09
 */
@Mapper
public interface NoticeDao extends BaseDao<NoticeEntity> {

    /**
     * 我的通知列表
     * @param noticeEntity
     * @return
     */
    List<NoticeEntity> findMyNotice(NoticeEntity noticeEntity);
}
