package com.wz.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.sys.entity.OrganEntity;
import com.wz.modules.sys.entity.UserWindowDto;

import java.util.List;

/**
 * 组织表
 * 
 * @author admin
 * @email rui.sun.java@gmail.com
 * @date 2017-07-14 13:42:42
 */
@Mapper
public interface OrganDao extends BaseDao<OrganEntity> {
    /**
     * 根据实体条件查询
     * @return
     */
    List<UserWindowDto> queryPageByDto(UserWindowDto userWindowDto);
}
