package com.wz.modules.kk.biz;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wz.modules.common.common.Constant;
import com.wz.modules.common.http.HttpUtil;
import com.wz.modules.common.utils.MD5;
import com.wz.modules.common.utils.RedisUtil;
import com.wz.modules.common.utils.SpringContextUtils;

import net.sf.json.JSONObject;

public class KkLoginUtil {
	private final static Logger logger = LoggerFactory.getLogger(KkLoginUtil.class);
 
	private static RedisUtil redisUtil = (RedisUtil) SpringContextUtils.getBean("redisUtil");
	private final static int ACCESSTOKE_EXPIRE_TIME = 2000;
	private final static int REFRESHTOKE_EXPIRE_TIME = 3000;
	
	
	/**
	 * 获取code
	 */
	private static  String getCode(){
		Map<String, String> params = new HashMap<String, String>();
		String url = "http://open.snd02.com/oauth/authverify2.as";
		params.put("response_type", "code");
		params.put("client_id", Constant.KK_CLIENT_ID);
		params.put("redirect_uri", Constant.KK_REDIRECT_URL);
		params.put("uname", Constant.KK_USNER_NAME);
		params.put("passwd", Constant.KK_PASSWORD);
		String codeJson = HttpUtil.doPost(url, params);
		JSONObject codeJsonObj = JSONObject.fromObject(codeJson);
		String code = codeJsonObj.getString("code");
		return code;
	}
	
	/**
	 * 获取临时码 access token 默认3600s时效
	 * MD5(app_key + grant_type + redirect_uri + code + app_secret)
	 */
	private static  Map<String, String> getAccessToken(String code){
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> params = new HashMap<String, String>();
		String url="http://open.snd02.com/oauth/token.as";
		params.put("client_id", Constant.KK_CLIENT_ID);
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.KK_CLIENT_ID);
		sb.append("authorization_code");
		sb.append( Constant.KK_REDIRECT_URL);
		sb.append(code);
		sb.append(Constant.KK_APP_SECRET);
		params.put("client_secret", MD5.MD5Encode(sb.toString()));
		params.put("grant_type", "authorization_code");
		params.put("redirect_uri", Constant.KK_REDIRECT_URL);
		params.put("code", code);
		String accessTokenJson = HttpUtil.doPost(url, params);		
		JSONObject accessTokeJsonObj = JSONObject.fromObject(accessTokenJson);
		String dataJson = accessTokeJsonObj.getString("data");
		JSONObject dataObj = JSONObject.fromObject(dataJson);		
		String accessToken = dataObj.getString("accessToken");		
		String refreshToken = dataObj.getString("refreshToken");
		resultMap.put("accessToken", accessToken);
		resultMap.put("refreshToken", refreshToken);
		try {
			redisUtil.setString("accessToken", accessToken, ACCESSTOKE_EXPIRE_TIME);
			redisUtil.setString("refreshToken", refreshToken, REFRESHTOKE_EXPIRE_TIME);
		} catch (Exception e) {					
			e.printStackTrace();
		}
				
	
		return resultMap;
	}
	
	/**
	 * 刷新临时码 access token 默认3600s时效
	 * MD5(app_key + grant_type + redirect_uri + refresh_token+ app_secret)
	 */
	private static Map<String, String> refreshAccessToken(String refreshToken){
		Map<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> params = new HashMap<String, String>();
		String url="http://open.snd02.com/oauth/refresh.as";
		params.put("client_id", Constant.KK_CLIENT_ID);
		
		StringBuffer sb = new StringBuffer();
		sb.append(Constant.KK_CLIENT_ID);
		sb.append("refresh_token");
		sb.append( Constant.KK_REDIRECT_URL);
		sb.append(refreshToken);
		sb.append(Constant.KK_APP_SECRET);
		
		params.put("client_secret", MD5.MD5Encode(sb.toString()));
		params.put("grant_type", "refresh_token");
		params.put("redirect_uri", Constant.KK_REDIRECT_URL);
		params.put("refresh_token", refreshToken);
		String refreshTokenJson = HttpUtil.doPost(url, params);
		
		JSONObject refreshTokenJsonObj = JSONObject.fromObject(refreshTokenJson);
		String dataJson = refreshTokenJsonObj.getString("data");
		logger.info("===================>返回data："+dataJson);
		JSONObject dataObj = JSONObject.fromObject(dataJson);		
		String accessToken = dataObj.getString("accessToken");		
		String refreshTokenNew = dataObj.getString("refreshToken");
		resultMap.put("accessToken", accessToken);
		resultMap.put("refreshToken", refreshTokenNew);
		
		try {
			redisUtil.setString("accessToken", accessToken, ACCESSTOKE_EXPIRE_TIME);
			redisUtil.setString("refreshToken", refreshTokenNew, REFRESHTOKE_EXPIRE_TIME);
		} catch (Exception e) {					
			e.printStackTrace();
		}
		
		return resultMap;
	}
	/**
	 * 从接口获取accessToken
	 * @return
	 */
	public static String getAccessTokeFromKk(){
		
		try {
			String accessTokenCache = redisUtil.getString("accessToken");
			String refreshTokenCache = redisUtil.getString("refreshToken");
			if(StringUtils.isNotBlank(accessTokenCache)){
				logger.info("空开接口通过Redis缓存返回验证码==============>accessToken:"+accessTokenCache);
				return accessTokenCache;
			} else {
				if(StringUtils.isNotBlank(refreshTokenCache)) {//刷新accesstoken
					Map<String, String> resultMap = refreshAccessToken(refreshTokenCache);
					if(resultMap != null && resultMap.size() > 0){	
						logger.info("空开接口通过刷新接口返回验证码==============>accessToken:"+resultMap.get("accessToken"));
						return resultMap.get("accessToken");
					} else {
						return null;
					}
				} else {//第一次获取
					String code = getCode();		
					Map<String, String> resultMap = getAccessToken(code);
					if(resultMap != null && resultMap.size() > 0){							
						logger.info("空开接口第一次返回验证码==============>accessToken:"+resultMap.get("accessToken"));
						return resultMap.get("accessToken");
					} else {
						return null;
					}
				}
				
			}
		} catch (Exception e) {		
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 临时测试使用
	 * @return
	 */
	public static String getAccessTokenTmp(){
		String code = getCode();		
		Map<String, String> resultMap = getAccessToken(code);
		if(resultMap != null && resultMap.size() > 0){							
			logger.info("空开接口第一次返回验证码==============>accessToken:"+resultMap.get("accessToken"));
			return resultMap.get("accessToken");
		} else {
			return null;
		}
	}
	
	
	public static void main(String[] args){
		/*String code = getCode();		
		System.out.println("======>code:"+code);
		Map<String, String> resultMap = getAccessToken(code);		
		System.out.println("======>accessToken:"+resultMap.get("accessToken")+", refreshToken:"+resultMap.get("refreshToken"));*/
		Map<String, String> rsMap = refreshAccessToken("a8f4eb37eb640b2d90cd3828996b954d");
		System.out.println("======>accessToken:"+rsMap.get("accessToken")+", refreshToken:"+rsMap.get("refreshToken"));
	}
}
