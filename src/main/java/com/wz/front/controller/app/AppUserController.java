package com.wz.front.controller.app;

import com.wz.modules.app.annotation.LoginRequired;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.sys.entity.UserEntity;
import com.wz.modules.sys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/app/user")
@Api(tags = "APP 获取用户信息")
public class AppUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/info/{loginName}")
    @LoginRequired
    @ApiOperation(value = "获取场馆列表")
    public CommonResponse<UserEntity> getUserInfo(@PathVariable("loginName") String loginName) {
        UserEntity userEntity = userService.queryByLoginName(loginName);
        return CommonResponse.success(userEntity);
    }
}
