package com.wz.front.controller.app;

import com.wz.front.dto.ProjectInfoDto;
import com.wz.front.service.ClientProjectInfoService;
import com.wz.front.service.CurrentUser;
import com.wz.modules.app.annotation.LoginRequired;
import com.wz.modules.common.controller.BaseController;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.common.utils.DateUtils;
import com.wz.modules.common.utils.ShiroUtils;
import com.wz.modules.projectinfo.entity.ProjectInfoEntity;
import com.wz.modules.projectinfo.service.ProjectInfoService;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    private UserService userService;

    @Autowired
    private ProjectInfoService projectInfoService;

    @Autowired
    private ClientProjectInfoService clientProjectInfoService;

    @GetMapping("/list")
    @LoginRequired
    @ApiOperation(value = "该账户下,该场馆所有可见的 设备总数,在线空开,离线空开")
    public CommonResponse list() {
        List<ProjectInfoDto> projectList = clientProjectInfoService.listProjects();
        Map<String, Object> result = new HashMap<>();
        result.put("projectList", projectList);
        result.put("gymName", "世博展览馆");
        return CommonResponse.success(result);
    }

}
