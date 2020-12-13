package com.wz.modules.common.jiguang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;


/**
 * 激光推送
 */
public class JiguangPush {
    private static final Logger log = LoggerFactory.getLogger(JiguangPush.class);
    private static String masterSecret = "e1b24d4628d88d7bcf7db281";
    private static String appKey = "21158531fc9eb944775ca416";
    /**
     * 极光推送
     */
    public static void jiguangPush(String alias, String msg, String projectInfo){
    	
    		log.info("对别名" + alias + "的用户推送信息");
            PushResult result = push(String.valueOf(alias), msg, projectInfo);
            if(result != null && result.isResultOK()){
                log.info("针对别名" + alias + "的信息推送成功！");
            }else{
                log.info("针对别名" + alias + "的信息推送失败！");
         }  
    }
    
    /**
     * 生成极光推送对象PushPayload（采用java SDK）
     * @param alias
     * @param alert
     * @return PushPayload
     */
    public static PushPayload buildPushObject_android_ios_alias_alert(String alias,String alert, String projectInfo){
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .addExtra("type", "1")
                                .addExtra("project", projectInfo)
                                .setAlert(alert)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .addExtra("type", "1")
                                .addExtra("project", projectInfo)
                                .setAlert(alert)                                
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(false)//true-推送生产环境 false-推送开发环境（测试使用参数）
                        .setTimeToLive(90)//消息在JPush服务器的失效时间（测试使用参数）
                        .build())
                .build();
    }
    /**
     * 极光推送方法(采用java SDK)
     * @param alias
     * @param alert
     * @return PushResult
     */
    public static PushResult push(String alias,String alert, String projectInfo){
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        PushPayload payload = buildPushObject_android_ios_alias_alert(alias, alert, projectInfo);
        try {
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
            return null;
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            log.info("Msg ID: " + e.getMsgId());
            return null;
        }    
    }    
    public static void main(String[] args){
    	JiguangPush.jiguangPush("icbms", "A101电箱总路发生断电告警！", "{id:86db1ac705a54157bd8a01e7a7d27511,projectName:2018年上海大型会展}");
    }
}