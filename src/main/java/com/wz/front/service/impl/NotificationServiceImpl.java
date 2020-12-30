package com.wz.front.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.wz.config.EmailNotificationConfig;
import com.wz.config.SmsNotificationConfig;
import com.wz.front.service.NotificationService;
import com.wz.modules.common.utils.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Cherry
 * @Date: 2020/12/30
 * @Desc: NotificationServiceImpl
 */
@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailNotificationConfig emailNotificationConfig;

    @Autowired
    private SmsNotificationConfig smsNotificationConfig;

    @Override
    public CommonResponse sendEmail(String toAddress, String subject, String text) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(emailNotificationConfig.getUsername());
        simpleMailMessage.setTo(toAddress);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);

        try {
            javaMailSender.send(simpleMailMessage);
            log.info("邮件已发送。");
            return CommonResponse.success("邮件已发送。");
        } catch (Exception e) {
            log.error("邮件发送失败。" + e.getMessage());
            return CommonResponse.error("邮件发送失败。");
        }
    }

    @Override
    public CommonResponse sendSms(String toPhoneNum, String text) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsNotificationConfig.getAccessKeyId(), smsNotificationConfig.getAccessKeyId());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", toPhoneNum);
        request.putQueryParameter("SignName", "123");
        request.putQueryParameter("TemplateCode", smsNotificationConfig.getTemplateCode());
        Map<String, Object> param = new HashMap<>();
        param.put("code", text);
        request.putQueryParameter("TemplateParam", JSON.toJSONString(param));
        try {
            com.aliyuncs.CommonResponse commonResponse = client.getCommonResponse(request);

            log.error("短信发送成功！：");
            commonResponse.getData();
            return CommonResponse.success("短息发送成功！");
        } catch (ServerException e) {
            log.error("短信发送失败：", e);
            return CommonResponse.error("短息发送失败！");
        } catch (ClientException e) {
            log.error("短信发送失败：", e);
            return CommonResponse.error("短息发送失败！");
        }
    }
}
