package com.wz.modules.kk.biz;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wz.modules.common.common.Constant;
import com.wz.modules.common.http.HttpUtil;
import com.wz.modules.common.utils.DateUtils;
import com.wz.modules.common.utils.MD5;
import com.wz.modules.common.utils.MapUtils;

import net.sf.json.JSONObject;

public class KkBizUtil {
	private final static Logger logger = LoggerFactory.getLogger(KkBizUtil.class);
	private final static String URL = "http://open.snd02.com/invoke/router.as";

	/**
	 * 电箱线路控制接口
	 * 
	 * @param deviceBoxMac
	 * @param switchAddStrs
	 * @param cmd
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String setCmdSwitch(String deviceBoxMac, String switchAddStrs, String cmd)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, String> paramsMap = getCommParamMap();
		paramsMap.put("projectCode", Constant.KK_PROJECT_CODE);
		paramsMap.put("method", "PUT_BOX_CONTROL");
		paramsMap.put("mac", deviceBoxMac);
		paramsMap.put("cmd", "OCSWITCH");
		paramsMap.put("value1", cmd);
		paramsMap.put("value2", switchAddStrs);
		String sign = getSignStr(paramsMap);
		paramsMap.put("sign", sign);
		String resultJson = HttpUtil.doPost(URL, paramsMap);
		System.out.println("=============>resultJson:" + resultJson);
		JSONObject dataObj = JSONObject.fromObject(resultJson);
		String result = dataObj.getString("code");
		logger.info("电箱线路控制返回结果================>result:" + result);
		return result;

	}

	/**
	 * 获取某电箱所有实时数据
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getBoxChannelsRealData(String deviceBoxMac)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, String> paramsMap = getCommParamMap();
		paramsMap.put("method", "GET_BOX_CHANNELS_REALTIME");
		paramsMap.put("projectCode", Constant.KK_PROJECT_CODE);
		paramsMap.put("mac", deviceBoxMac);
		String sign = getSignStr(paramsMap);
		paramsMap.put("sign", sign);
		String resultJson = HttpUtil.doPost(URL, paramsMap);
		logger.info("电箱线路返回实时数据================>resultJson:" + resultJson);
		return resultJson;
	}

	public static String getBoxDayPower(String deviceBoxMac)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, String> paramsMap = getCommParamMap();
		paramsMap.put("method", "GET_BOX_DAY_POWER");
		paramsMap.put("projectCode", Constant.KK_PROJECT_CODE);
		paramsMap.put("mac", deviceBoxMac);
		String nowDate = DateUtils.format(new Date());
		String preDate = DateUtils.getPreDay(nowDate, -1);
		paramsMap.put("year", preDate.split("-")[0]);
		paramsMap.put("month", preDate.split("-")[1]);
		paramsMap.put("day", preDate.split("-")[2]);
		String sign = getSignStr(paramsMap);
		paramsMap.put("sign", sign);
		String resultJson = HttpUtil.doPost(URL, paramsMap);
		logger.info("每日用电返回数据================>resultJson:" + resultJson);
		return resultJson;
	}

	/**
	 * @param deviceBoxMac
	 *            电箱mac
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param pageSize
	 *            每页条数
	 * @param page
	 *            查询第几页
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getAlarmsByTime(String deviceBoxMac, String startTime, String endTime, String pageSize,
			String page) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, String> paramsMap = getCommParamMap();
		paramsMap.put("method", "GET_BOX_ALARM");
		paramsMap.put("projectCode", Constant.KK_PROJECT_CODE);
		paramsMap.put("mac", deviceBoxMac);
		paramsMap.put("start", startTime);
		paramsMap.put("end", endTime);
		paramsMap.put("pageSize", pageSize + "");
		paramsMap.put("page", page + "");
		paramsMap.put("end", endTime);
		String sign = getSignStr(paramsMap);
		paramsMap.put("sign", sign);
		String resultJson = HttpUtil.doPost(URL, paramsMap);
		logger.info("告警查询返回的数据================>resultJson:" + resultJson);
		return resultJson;
	}

	/**
	 * 修改某个设备上传实时数据的频率设置接口
	 * 
	 * @param deviceBoxMac
	 * @param cmd
	 *            true：每2秒上传 false：每10分钟上传
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static String setUpdateDateTime(String deviceBoxMac, String cmd)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, String> paramsMap = getCommParamMap();
		// paramsMap.put("method", "GET_BOX_ALARM");
		String URL = "http://app.snd02.com:8080/app/ctl/box/rate.as";
		paramsMap.put("projectCode", Constant.KK_PROJECT_CODE);
		paramsMap.put("mac", deviceBoxMac);
		paramsMap.put("value", cmd);
		String sign = getSignStr(paramsMap);
		paramsMap.put("sign", sign);
		String resultJson = HttpUtil.doPost(URL, paramsMap);
		return resultJson;
	}

	private static Map<String, String> getCommParamMap() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr = sdf.format(new Date());
		String accessToken = KkLoginUtil.getAccessTokeFromKk(); // KkLoginUtil.getAccessTokenTmp();
		if (accessToken != null) {
			paramsMap.put("client_id", Constant.KK_CLIENT_ID);
			paramsMap.put("access_token", accessToken);
			paramsMap.put("timestamp", dateStr);
			return paramsMap;
		} else {
			return null;
		}
	}

	/**
	 * 获取签名
	 * 
	 * @param method
	 * @param accessToken
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	private static String getSignStr(Map<String, String> paramsMap)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		Map<String, String> resultMap = MapUtils.sortMapByKey(paramsMap);
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : resultMap.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
			sb.append(entry.getValue());
		}
		sb.append(Constant.KK_APP_SECRET);
		System.out.println("=============>old sign:" + sb.toString());
		String sign = MD5.MD5(sb.toString());
		return sign;
	}

	public static void main(String[] args) {
		try {
			// setCmdSwitch("187ED530FE80", "1,2,3", "close");
			/*
			 * String resultJson = KkBizUtil.getAlarmsByTime("187ED530FE80",
			 * "2018-01-01 12:00", "2018-04-01 12:00", "100", "1");
			 * System.out.println("====================>resultJson:"+resultJson)
			 * ;
			 */
			String resultJson = setUpdateDateTime("187ED530FE80", "true");
			System.out.println(resultJson);
			// getBoxChannelsRealData("187ED530FE80");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
