package com.wz.socket.utils;

public class TcpNetCmd {
	/**
	 * 实时数据(业务数据&告警数据)
	 */
	public static final int REAL_TIME_DATA = 0x1A;

	/**
	 * 后台服务器下发设置线路参数数据
	 */
	public static final int SET_DATA_FROM_SERVER_ID = 0x3E;
	/**
	 * 后台服务器下发读取线路参数数据
	 */
	public static final int GET_DATA_FROM_SERVER_ID = 0x1E;
	/**
	 * 网关返回服务器下发设置参数响应消息
	 */
	public static final int RESPONSE_PARAMS_DATA_ID = 0x4A;
	/**
	 * 后台服务器下发设置线路合分闸
	 */
	public static final int SET_DATA_SWITCHONOFF_SERVER_ID = 0x2E;
	
//	/**
//	 * 请求
//	 */
//    public static final int REQ = 0x8f;
    /**
     * 应答
     */
    public static final int ACK = 0x0f;
    /**
     * 登录消息
     */
	public static final int LOGIN_ID = 0x00;
	/**
	 * 召测数据
	 */
	public static final int READ_DATA_ID = 0x04;
	/**
	 * 时间设置
	 */
	public static final int SET_TIME_ID = 0x03;
	/**
	 * 退出登录
	 */
	public static final int LOGOUT_ID = 0x01;
//	/**
//	 * 参数设置
//	 */
//	public static final int SET_PARAM_ID = 0x02;
}
