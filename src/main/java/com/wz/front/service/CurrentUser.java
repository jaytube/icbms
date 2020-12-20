package com.wz.front.service;

import com.wz.front.util.DeviceType;
import com.wz.front.util.DeviceUtils;
import com.wz.modules.app.utils.JwtUtils;
import com.wz.modules.common.utils.ShiroUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Cherry
 * @Date: 2020/12/20
 * @Desc: CurrentUser
 */
@Component
public class CurrentUser {

    @Autowired
    private JwtUtils jwtUtils;

    public String getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        DeviceType deviceType = DeviceUtils.checkDeviceType(request);
        if (deviceType == DeviceType.MOBILE_APP) {
            String token = request.getHeader(jwtUtils.getHeader());
            Claims claims = jwtUtils.getClaimByToken(token);
            return claims.getSubject();
        } else {
            return ShiroUtils.getUserId();
        }
    }
}
