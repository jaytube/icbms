package com.wz.modules.projectinfo.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.wz.modules.common.utils.RedisUtil;
import com.wz.modules.common.utils.Utils;
import com.wz.modules.deviceinfo.service.DeviceBoxInfoService;
import com.wz.modules.projectinfo.dao.ProjectInfoDao;
import com.wz.modules.projectinfo.dao.ProjectRoleDao;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;
import com.wz.modules.projectinfo.entity.ProjectInfoPlainEntity;
import com.wz.modules.projectinfo.service.LocationInfoService;
import com.wz.modules.projectinfo.service.ProjectInfoService;
import com.wz.modules.sys.dao.UserDao;
import com.wz.modules.sys.entity.UserEntity;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("projectInfoService")
public class ProjectInfoServiceImpl implements ProjectInfoService {
    @Autowired
    private ProjectInfoDao projectInfoDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProjectRoleDao projectRoleDao;

    @Autowired
    private LocationInfoService locationInfoService;

    @Autowired
    private DeviceBoxInfoService deviceBoxInfoService;

    @Override
    public ProjectInfoEntity queryObject(String id) {
        ProjectInfoEntity projectInfoEntity = projectInfoDao.queryObject(id);
        return projectInfoEntity;
    }

    @Override
    public List<ProjectInfoEntity> queryProjects(String[] ids) {
        return projectInfoDao.queryProjectsByIds(ids);
    }

    @Override
    public List<ProjectInfoPlainEntity> queryPlainProjects(String[] ids) {
        return projectInfoDao.queryPlainProjectsByIds(ids);
    }

    @Override
    public List<ProjectInfoEntity> queryList(Map<String, Object> map) {
        return projectInfoDao.queryList(map);
    }

    @Override
    public List<ProjectInfoEntity> queryListByBean(ProjectInfoEntity entity) {
        return projectInfoDao.queryListByBean(entity);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return projectInfoDao.queryTotal(map);
    }

    @Override
    public int save(ProjectInfoEntity projectInfo) {
        projectInfo.setId(Utils.uuid());
        projectInfo.setCreateTime(new Date());
        int num = projectInfoDao.save(projectInfo);
        if (!StringUtils.isEmpty(projectInfo.getGatewayAddress())) {
            String[] strs = projectInfo.getGatewayAddress().split(",");
            for (String str : strs) {
                redisUtil.hset(0, "GATEWAY_CONFIG", str, projectInfo.getId());
            }
        }

        this.doProcessProjectRole(projectInfo);
        return num;
    }

    public void doProcessProjectRole(ProjectInfoEntity projectInfo) {
        projectRoleDao.delete(projectInfo.getId());

        if (!StringUtils.isEmpty(projectInfo.getId()) && null != projectInfo.getRoleIdList()
                && projectInfo.getRoleIdList().size() > 0) {
            // 保存项目与角色关系
            Map<String, Object> map = new HashMap<>();
            map.put("projectId", projectInfo.getId());
            map.put("roleIdList", projectInfo.getRoleIdList());
            projectRoleDao.save(map);
        }
    }

    @Override
    public int update(ProjectInfoEntity projectInfo) {
        ProjectInfoEntity originalProject = projectInfoDao.queryObject(projectInfo.getId());
        String originalGatewayAddress = originalProject.getGatewayAddress();
        if (!StringUtils.isEmpty(originalGatewayAddress)) {
            for (String str : originalGatewayAddress.split(",")) {
                redisUtil.hdel(0, "GATEWAY_CONFIG", str);
            }
        }

        int num = projectInfoDao.update(projectInfo);
        if (!StringUtils.isEmpty(projectInfo.getGatewayAddress())) {

            String[] strs = projectInfo.getGatewayAddress().split(",");
            for (String str : strs) {
                redisUtil.hset(0, "GATEWAY_CONFIG", str, projectInfo.getId());
            }
        }

        this.doProcessProjectRole(projectInfo);

        return num;
    }

    @Override
    public int delete(String id) {
        return projectInfoDao.delete(id);
    }

    @Override
    @Transactional
    public int deleteBatch(String[] ids) {
        // 移除用户关联项目
        for (String projectId : ids) {
            List<UserEntity> userList = this.userDao.queryUserProjectRel(projectId);
            for (UserEntity user : userList) {
                String tmpProjectIds = user.getProjectIds();
                String adjustProjectIds = this.removeProjectStrs(tmpProjectIds, projectId);
                user.setProjectIds(adjustProjectIds);
                this.userDao.update(user);
            }
            // 移除项目关联电箱位置
            this.locationInfoService.delProjectLocationRel(projectId);

            // 移除项目下的位置
            this.locationInfoService.delProjectLocation(projectId);

            // 移除电箱
            this.deviceBoxInfoService.deleteProjectDeviceBox(projectId);
        }
        return projectInfoDao.deleteBatch(ids);
    }

    @Override
    public List<ProjectInfoEntity> queryListAll() {
        return projectInfoDao.queryListAll();
    }

    @Override
    public List<String> queryRoleIdList(String projectId) {
        return projectRoleDao.queryRoleIdList(projectId);
    }

    private String removeProjectStrs(String tmpStr, String str) {
        List<String> tmpLists = new ArrayList<String>();
        String[] strs = tmpStr.split(",");

        for (String s : strs) {
            if (!str.equals(s)) {
                tmpLists.add(s);
            }
        }

        if (tmpLists.size() == 0) {
            return null;
        } else {
            return StringUtil.join(tmpLists.toArray(), ",");
        }
    }

}
