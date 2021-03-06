package com.wz.modules.projectinfo.dao;

import com.wz.modules.common.dao.BaseDao;
import com.wz.modules.projectinfo.entity.LocationInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 位置基础表; InnoDB free: 401408 kB
 *
 * @author Raymond
 * @email rui.sun.java@gmail.com
 * @date 2018-03-13 15:14:33
 */
@Mapper
public interface LocationInfoDao extends BaseDao<LocationInfoEntity> {

    List<LocationInfoEntity> findLocInfoByPId(String projectId);

    List<LocationInfoEntity> findProjectLocationRel(String projectId);

    /**
     * 根据父位置Id查询位置
     *
     * @param parenId
     * @return
     */
    List<LocationInfoEntity> queryListParentId(String parenId);

    void deleteLinkByLocId(String locId);

    void deleteBoxLocLinkBatch(String[] ids);

    String findLocIdByLocName(Map<String, String> map);

    int delProjectLocationRel(@Param("projectId") String projectId);

    int delProjectLocation(@Param("projectId") String projectId);

    List<LocationInfoEntity> fetchAll();

    int updateTypeBatch(List<LocationInfoEntity> locationInfoEntities);

    /**
     * 获取项目下展会节点
     * @return
     */
    List<LocationInfoEntity> findProjectXhibitionNodes(String projectId);

}