package com.wz.front.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static com.wz.front.util.DeviceType.MOBILE_APP;
import static com.wz.front.util.DeviceType.WEB_BROWSER;

/**
 * @Author: Cherry
 * @Date: 2020/12/20
 * @Desc: DeviceUtils
 */
@Component
public class DeviceUtils {

    public static DeviceType checkDeviceType(HttpServletRequest request) {
        String device = request.getHeader("DEVICE-TYPE");
        if (WEB_BROWSER.toString().equalsIgnoreCase(device)) {
            return WEB_BROWSER;
        } else if (MOBILE_APP.toString().equalsIgnoreCase(device)) {
            return MOBILE_APP;
        } else {
            return WEB_BROWSER;
        }
    }

}
