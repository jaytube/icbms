package com.wz.modules.common.validator;

import org.apache.commons.lang.StringUtils;

import com.wz.modules.common.common.RRException;

/**
 * 类Assert的功能描述:
 * 数据校验
 * @auther hxy
 * @date 2017-08-25 16:18:34
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RRException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RRException(message);
        }
    }
}
