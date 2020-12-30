package com.wz.front.service;

import com.wz.modules.common.utils.CommonResponse;

/**
 * @Author: Cherry
 * @Date: 2020/12/30
 * @Desc: NotificationService
 */
public interface NotificationService {

    CommonResponse sendEmail(String toAddress, String subject, String text);

    CommonResponse sendSms(String toPhoneNum, String text);
}
