package com.wz.modules.app.controller;


import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import com.wz.modules.app.annotation.CurrentUser;
import com.wz.modules.app.annotation.LoginRequired;
import com.wz.modules.common.utils.Result;
import com.wz.modules.sys.entity.UserEntity;

/**
 * 类ApiTestController的功能描述:
 * APP测试接口
 * @auther hxy
 * @date 2017-10-16 14:16:18
 */
@CrossOrigin
@RestController
@RequestMapping("/app")
@Api(tags = "APP测试接口")
public class ApiTestController {

    /**
     * 获取用户信息
     */
    @LoginRequired
    @GetMapping("userInfo")
    public Result userInfo(@CurrentUser UserEntity user){
        return Result.ok().put("user", user);
    }

    /**
     * 获取用户ID
     */
    @LoginRequired
    @GetMapping("userId")
    public Result userInfo(@RequestAttribute("userId") String userId){
        return Result.ok().put("userId", userId);
    }

    /**
     * 忽略Token验证测试
     */
    @GetMapping("notToken")
    public Result notToken(){
        return Result.ok().put("msg", "无需token也能访问。。。");
    }

}
