package com.wz.modules.sys.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.sys.entity.CodeEntity;

import java.util.List;

/**
 * 系统数据字典
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-07-14 13:42:42
 */
@Mapper
public interface CodeDao extends BaseDao<CodeEntity> {
    /**
     * 查询所有的字典及子字典(用做字典缓存)
     * @return
     */
    List<CodeEntity> queryAllCode();

    /**
     * 查询所有的字典及子字典(用做字典缓存)
     * @return
     */
    List<CodeEntity> queryChildsByMark(String mark);

    /**
     * 根据字典标识查询
     * @param mark
     * @return
     */
    CodeEntity queryByMark(String mark);


}
