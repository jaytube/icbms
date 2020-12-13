package com.wz.socket.test;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.wz.socket.cclient.SocketClient;
import com.wz.socket.handler.MsgHandler;
import com.wz.socket.model.TcpMessage;
import com.wz.socket.servlet.ApplicationContextUtil;

public class ClientTest implements MsgHandler{
	
	Logger logger = Logger.getLogger(ClientTest.class);
	
	private  static SocketClient client;
	
	public SocketClient getClient() {
		return client;
	}

	public ClientTest(){
		//String addr, int port, MsgHandler msgHandler, int handShakeMsgID
		client = SocketClient.getInstance("121.42.13.143", 65001, this, 0, true);		
	}

	@Override
	public void onMsg(TcpMessage message, IoSession session) {
		logger.info("================>cmd:"+message.getProtocolType());
		ApplicationContextUtil.callIotRequestProvider(message, session);
	}

	@Override
	public void onSessionClosed(IoSession session) {
		
		
	}

	@Override
	public void onHandShakeCompleted(IoSession session, int handShakeMsgID) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		ClientTest clientTest = new ClientTest();
		
/******************************控制开关合闸示例start**************************************/
		/*byte[] datas = new byte[9];
		//起始帧
		datas[0] = 0x2E;
		//网关ID
		datas[1] = 0x00;
		//终端ID
		datas[2] = 0x00;
		//版本号
		datas[3] = 0x01;
		//心跳包
		datas[4] = 0x00;
		//开关合分
		datas[5] = 0x00;
		//门锁开关
		datas[6] = 0x00;
		//奇偶校验
		datas[7] = 0x00;
		//停止位
		datas[8] = 0x2F;
		//验证奇偶校验
		if(!clientTest.checkEvenOdd(datas, 1)){//约定奇校验
			datas[7] = 0x01;
		}*/
/******************************控制开关合闸示例end**************************************/
		
/******************************读取参数start*****************************************/
		byte[] datas = new byte[10];
		//起始帧
		datas[0] = 0x1E;
		//网关ID
		datas[1] = 0x00;
		//终端ID
		datas[2] = 0x00;
		//版本号
		datas[3] = 0x01;
		//空开地址
		datas[4] = 0x00;
		//原485从机地址
		datas[5] = 0x01;
		//原485cmd功能码
		datas[6] = 0x04;
		//原485功能地址 
		//00--16位电压上限  01-16位电压下限 02-16位漏电流上限  03-16位功率上限 04-16位温度上限  05-16位电流门限
		//还要注意下，返回信息需要查看协议解析，注意每个写入的单位，比如电压写入单位1v，漏电流单位0.1mA,功率单位1W，电流单位0.01A
		datas[7] = 0x00;//16位电压上限
		//奇偶校验位
		datas[8] = 0x00;
		//停止位
		datas[9] = 0x1F;
		//验证奇偶校验
		if(!clientTest.checkEvenOdd(datas, 1)){//约定奇校验
			datas[8] = 0x01;
		}
/******************************读取参数end*****************************************/
		
/******************************设置参数start*****************************************/
		/*byte[] datas = new byte[13];
		//起始帧
		datas[0] = 0x3E;
		//网关ID
		datas[1] = 0x00;
		//终端ID
		datas[2] = 0x00;
		//版本号
		datas[3] = 0x01;
		//空开地址
		datas[4] = 0x00;
		//原485从机地址
		datas[5] = 0x01;
		//原485功能域
		datas[6] = 0x06;//配置参数命令标识
		//原485数据地址高位
		datas[7] = 0x00;//配置电压门限上限，可参照485协议附件cmd为06表
		//原485数据地址低位
		datas[8] = 0x00;//空开地址
		//原485协议数据内容高位
		datas[9] = 0x07;
		//原485协议数据内容低位
		datas[10] = (byte)0xD0;
		//奇偶校验位
		datas[11] = 0x00;
		//停止位
		datas[12] = 0x3F;
		//验证奇偶校验
		if(!clientTest.checkEvenOdd(datas, 1)){//约定奇校验
			datas[11] = 0x01;
		}*/
		
/******************************设置参数end*****************************************/
		
		//读取参数
		clientTest.getClient().send(datas);
	}
	
	/**
	 * 将byte[]转为各种进制的字符串
	 * @param bytes byte[]
	 * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return 转换后的字符串
	 */
	private String binary(byte[] datas, int radix){
		return new BigInteger(1, datas).toString(radix);// 这里的1代表正数
	}
	
	/**
	 * 奇偶校验
	 * val 1为奇校验  0为偶校验
	 * @return
	 */
	private boolean checkEvenOdd(byte[] datas, int val){
		//先获取字节二进制的1的个数
		String byteStr = binary(datas, 2);
		logger.info("==============>奇偶校验原始字节："+byteStr);
		if(StringUtils.isNotBlank(byteStr)){
			String[] oddCnts = byteStr.split("1");
			if(val == (oddCnts.length-1)%2){
				logger.info("==============>奇偶校验通过!");
				return true;
			}
		}
		logger.info("==============>奇偶校验不通过!");
		return false;
	}

}
