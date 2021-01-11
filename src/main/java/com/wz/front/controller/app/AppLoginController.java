package com.wz.front.controller.app;


import com.wz.front.service.NotificationService;
import com.wz.modules.app.service.ApiUserService;
import com.wz.modules.app.utils.JwtUtils;
import com.wz.modules.common.utils.CommonResponse;
import com.wz.modules.common.utils.Result;
import com.wz.modules.sys.entity.UserEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AppLoginController {

    @Autowired
    private ApiUserService userApiService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private NotificationService notificationService;

    /**
     * 登录
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public Result login(UserEntity userEntity) {
        //用户登录
        String userId = userApiService.login(userEntity);
        log.info("/app/login start==>" + userEntity.getLoginName());

        //生成token
        String token = jwtUtils.generateToken(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        log.info("/app/login success==>" + userEntity.getLoginName());
        return Result.ok(map);
    }


    @RequestMapping(value = "/forget", method = RequestMethod.GET)
    @ApiOperation(tags = "忘记密码", value = "忘记密码")
    @ResponseBody
    public Result forget(String phoneNumber, String userName) {
        String password = userApiService.resetPassword(userName, phoneNumber);
        if (password == null) {
            return Result.error("用户名和手机号不匹配，密码重置失败！");
        }
        UserEntity userEntity = userApiService.queryByLoginName(userName);
        String text = "您的临时密码为：【" + password + "】，您正在进行密码重置操作，登录后请及时修改密码，如非本人操作，请忽略本短信！";
        CommonResponse email = notificationService.sendEmail(userEntity.getEmail(), "密码重置（请勿回复）", text);
        CommonResponse sms = notificationService.sendSms(userEntity.getPhone(), password);
        Map<String, Object> result = new HashMap<>();
        result.put("密码重置状态：", "密码重置成功! + 【进测试用，后面会删除】临时密码【" + password + "】");
        result.put("邮件通知：", email);
        result.put("短信通知：", sms);
        return Result.ok(result);
    }

}
