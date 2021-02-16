package com.wz.socket.utils;

public class Constant {

	public static final int SHUANGRUI_CAIJIYI_TCP = 0x00001;
	//Redis 实时数据key
	public static final String REAL_DATA = "REAL_DATA";
	//Redis 实时数据存储历史表最新时间
	public static final String REAL_HIS_DATA_STORE_UP_TO_DATE = "REAL_HIS_DATA_STORE_UP_TO_DATE";
	//Redis 实时告警key
	public static final String ALARM_DATA = "ALARM_DATA";
	//Redis 网关状态key
	public static final String GATEWARY_STATUS = "GATEWARY_STATUS";
	//Redis 后台服务器Client状态key
	public static final String C_CLIENT_STATUS = "C_CLIENT_STATUS";
	//Redis 所有电箱线路参数
	public static final String TEMINAL_LINE_PARAMS_CONFIGURATION = "LINE_PARAMS_CONFIGURATION";
	//实时数据类型
	public static final char REAL_DATA_TYPE = '0';
	//实时告警类型
	public static final char REAL_ALARM_TYPE = '1';
	//控制电箱返回消息类型 心跳包下发
	public static final int REQUEST_TYPE_CMD_HEART_TEST = 1;
	//设置空开线路参数下发
	public static final int REQUEST_TYPE_CMD_SET_PARMAS = 2;
	//返回设置参数响应给服务器端
	public static final int RESPONSE_TYPE_CMD_SET_PARAMS = 3;
	//设置空开合分闸
	public static final int REQUEST_TYPE_CMD_SET_SWITCH_ONOFF = 4;
	//读取参数
	public static final int REQUEST_TYPE_CMD_GET_PARMAS = 5;
	//BSession开始标识
	public static final String BSESSION_START = "BClient_Session_";
	//CSession开始标识
	public static final String CSESSION_START = "CClient_Session_";
	//标识成功状态
	public static final String SUCCESS_STATUS = "0";
	//标识失败状态
	public static final String FAIL_STATUS = "1";

	public static final String TERMINAL_STATUS= "TERMINAL_STATUS";
	
}
