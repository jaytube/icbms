package com.wz.modules.sys.dao;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.entity.UserProjectEntity;
import com.wz.modules.sys.entity.UserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserProjectDao extends BaseDao<UserProjectEntity> {

    /**
     * 根据用户ID，获取展会ID列表
     */
    List<String> queryProjectIdList(String userId);

    /**
     * 根据用户list批量删除用户角色中间表
     * @param users
     * @return
     */
    int deleteBatchByUserId(String[] users);

    /**
     * 根据角色list批量删除用户角色中间表
     * @param projectIds
     * @return
     */
    int deleteBatchByProjectId(String[] projectIds);

}
