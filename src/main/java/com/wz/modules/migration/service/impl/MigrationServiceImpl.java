package com.wz.modules.migration.service.impl;

import com.wz.modules.migration.service.MigrationService;
import com.wz.modules.migration.util.LocationInfoUtil;
import com.wz.modules.projectinfo.dao.LocationInfoDao;
import com.wz.modules.projectinfo.entity.LocationInfoEntity;
import com.wz.modules.sys.dao.UserDao;
import com.wz.modules.sys.dao.UserProjectDao;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.entity.UserProjectEntity;
import com.wz.modules.sys.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class MigrationServiceImpl implements MigrationService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserProjectDao userProjectDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private LocationInfoDao locationInfoDao;

    @Override
    public Boolean migrateUserProjects() {
        Map<String, Object> map = new HashMap<>();
        List<UserEntity> users = userService.queryList(map);

        if (!CollectionUtils.isEmpty(users)) {
            String[] userIds = users.stream().map(UserEntity::getId).collect(Collectors.toList()).toArray(new String[]{});
            for (UserEntity user : users) {
                String projectIds = user.getProjectIds();
                if (StringUtils.isNotBlank(projectIds)) {
                    List<UserProjectEntity> userProjectEntities = Arrays.stream(projectIds.split(","))
                            .map(String::trim)
                            .map(t -> {
                                UserProjectEntity userPro = new UserProjectEntity();
                                userPro.setUserId(user.getId());
                                userPro.setProjectId(t);
                                return userPro;
                            }).collect(Collectors.toList());
                    userProjectDao.saveBatch(userProjectEntities);
                }
            }
            userDao.updateBatchProjectIds(userIds);
        }

        return true;
    }

    @Override
    public Boolean migrateLocationInfos() {
        List<LocationInfoEntity> locationInfoEntities = locationInfoDao.fetchAll();
        buildTree(locationInfoEntities);
        return true;
    }

    private void buildTree(List<LocationInfoEntity> locationInfoEntities) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(locationInfoEntities)) {
            return;
        }

        locationInfoEntities.forEach(locationInfoEntity -> {
            LocationInfoUtil.parseLocationNodeType(locationInfoEntity);
        });
        locationInfoDao.updateTypeBatch(locationInfoEntities);
    }
}
