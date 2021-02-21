package com.wz.front.controller.app;

import com.wz.front.dto.ProjectInfoDto;
import com.wz.front.dto.ProjectInfoPlainDto;
import com.wz.front.service.AppProjectInfoService;
import com.wz.modules.app.annotation.LoginRequired;
import com.wz.modules.common.controller.BaseController;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.projectinfo.service.ProjectInfoService;
import com.wz.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Cherry
 * @Date: 2020/12/19
 * @Desc: AppProjectController
 */
@CrossOrigin
@RestController
@RequestMapping("/app/projectinfo")
@Api(tags = "APP 项目列表")
public class AppProjectController extends BaseController {

    @Autowired
    private AppProjectInfoService appProjectInfoService;

    @GetMapping("/list")
    @LoginRequired
    @ApiOperation(value = "该账户下,该场馆所有可见的 设备总数,在线空开,离线空开")
    public CommonResponse list() {
        List<ProjectInfoPlainDto> projectList = appProjectInfoService.listPlainProjects();
        Map<String, Object> result = new HashMap<>();
        result.put("projectList", projectList);
        if(CollectionUtils.isNotEmpty(projectList)) {
            result.put("gymName", projectList.get(0).getGymName());
            result.put("gymId", projectList.get(0).getGymId());
        }
        return CommonResponse.success(result);
    }

    @GetMapping("/getByGymId/{gymId}")
    @LoginRequired
    @ApiOperation(value = "该账户下该场馆下面的项目")
    public CommonResponse listByGymId(@PathVariable("gymId") Integer gymId) {
        List<ProjectInfoPlainDto> projectList = appProjectInfoService.listProjectsByGymId(gymId);
        Map<String, Object> result = new HashMap<>();
        result.put("projectList", projectList);
        if(CollectionUtils.isNotEmpty(projectList)) {
            result.put("gymName", projectList.get(0).getGymName());
            result.put("gymId", projectList.get(0).getGymId());
        }
        return CommonResponse.success(result);
    }

}
