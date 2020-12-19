package com.wz.front.controller.app;


import com.wz.modules.app.service.ApiUserService;
import com.wz.modules.app.utils.JwtUtils;
import com.wz.modules.common.utils.Result;
import com.wz.modules.sys.entity.UserEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 类ApiLoginController的功能描述:
 * APP登录授权
 *
 * @auther hxy
 * @date 2017-10-16 14:15:39
 */
@CrossOrigin
@Controller
@Api(tags = "APP登录")
@RequestMapping("/app")
public class AppLoginController {
    @Autowired
    private ApiUserService userApiService;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 登录
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(UserEntity userEntity) {
        //用户登录
        String userId = userApiService.login(userEntity);

        //生成token
        String token = jwtUtils.generateToken(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());

        return Result.ok(map);
    }

}
