package com.wz.socket.cinterface;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.wz.modules.common.utils.SpringContextUtils;
import com.wz.properties.FrontProcessorProperties;
import com.wz.socket.cclient.SocketClient;
import com.wz.socket.handler.MsgHandler;
import com.wz.socket.model.TcpMessage;
import com.wz.socket.servlet.ApplicationContextUtil;
import com.wz.socket.utils.CommUtils;
import com.wz.socket.utils.TypeConvert;
import com.wz.socket.utils.ValidUtils;

public class ClientRequestCmdSend implements MsgHandler{
	//日志
	private static Logger  logger = Logger.getLogger(ClientRequestCmdSend.class);
	//socket客户端
	private  static SocketClient client;
	// 单例 --volatile 防止多线程环境，访问到未初始化完成的对象
	private volatile static ClientRequestCmdSend clientReuqestCmdSend;
	
	private ClientRequestCmdSend(){
		FrontProcessorProperties fp = SpringContextUtils.getBean(FrontProcessorProperties.class);
		client = SocketClient.getInstance(fp.getIp(), new Integer(fp.getPort()), this, 0, true);
		//client = SocketClient.getInstance("47.99.122.40", 65002, this, 0, true);
	}
	
	public static ClientRequestCmdSend getIntance(){
		if(clientReuqestCmdSend == null){
			synchronized (ClientRequestCmdSend.class) {
				if (clientReuqestCmdSend == null) {
					clientReuqestCmdSend = new ClientRequestCmdSend();
				}
			}

		}
		return clientReuqestCmdSend;
	}
	
	//返回消息回调方法
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
		
	}
	/**
	 * 控制开合闸
	 * @param gatewayId 网关ID
	 * @param terminalId 电箱ID
	 * @param switchId 空开地址
	 * @param switchStatus 开关状态 0表示分闸 1表示合闸
	 */
	public void sendControlSwichCmd(String gatewayId, String terminalId, String switchId, String switchStatus){
		byte[] datas = new byte[11];
		//起始帧
		datas[0] = 0x2E;
		//网关ID
		byte[] gwBytes = CommUtils.short2Byte(Short.parseShort(gatewayId));
		datas[1] = gwBytes[0];
		datas[2] = gwBytes[1];
		//终端ID
		byte[] dxBytes = CommUtils.short2Byte(Short.parseShort(terminalId));
		datas[3] = dxBytes[0];
		datas[4] = dxBytes[1];
		//版本号
		datas[5] = 0x01;
		//线路地址
		datas[6] = (byte)Integer.parseInt(switchId);
		//开关合分
		datas[7] = (byte)Integer.parseInt(switchStatus);
		//门锁开关
		datas[8] = 0x00;
		//奇偶校验
		datas[9] = 0x00;
		//停止位
		datas[10] = 0x2F;
		//验证奇偶校验
		if (!ValidUtils.checkEvenOdd(datas, 1)) {// 约定奇校验
			datas[9] = 0x01;
		}

		//读取参数
		client.send(datas);
	}
	/**
	 * 读取门限参数命令下发
	 * @param gatewayId 网关ID
	 * @param terminalId 电箱ID
	 * @param switchId 空开地址
	 * @param thresholdType 0--电压上限    1-电压下限    2-漏电流上限  3-功率上限 4-温度上限  5-电流门限
	 */
	public void sendReadThresholdParamsCmd(String gatewayId, String terminalId, String switchId, String thresholdType){
		byte[] datas = new byte[12];
		//起始帧
		datas[0] = 0x1E;
		//网关ID
		byte[] gwBytes = CommUtils.short2Byte(Short.parseShort(gatewayId));
		datas[1] = gwBytes[0];
		datas[2] = gwBytes[1];
		//终端ID
		byte[] dxBytes = CommUtils.short2Byte(Short.parseShort(terminalId));
		datas[3] = dxBytes[0];
		datas[4] = dxBytes[1];
		//版本号
		datas[5] = 0x01;
		//线路地址
		datas[6] = (byte)Integer.parseInt(switchId);
		//原485从机地址
		datas[7] = 0x01;
		//原485cmd功能码
		datas[8] = 0x04;
		//原485功能地址 
		//00--16位电压上限  01-16位电压下限 02-16位漏电流上限  03-16位功率上限 04-16位温度上限  05-16位电流门限
		//还要注意下，返回信息需要查看协议解析，注意每个写入的单位，比如电压写入单位1v，漏电流单位0.1mA,功率单位1W，电流单位0.01A
		datas[9] = (byte)Integer.parseInt(thresholdType);//16位电压上限
		//奇偶校验位
		datas[10] = 0x00;
		//停止位
		datas[11] = 0x1F;

		logger.info("发送的指令为（Hex)" + CommUtils.byteArrToHexStr(datas));
		System.out.println(CommUtils.byteArrToHexStr(datas));
		// 验证奇偶校验
		if (!ValidUtils.checkEvenOdd(datas, 1)) {// 约定奇校验
			datas[10] = 0x01;
		}

		//读取参数
		client.send(datas);
	}
	
	/**
	 * 设置门限参数命令下发
	 * @param gatewayId 网关ID
	 * @param terminalId 电箱ID
	 * @param switchId 空开地址(注意空开地址从1开始)
	 * @param thresholdType 0--电压上限    1-电压下限    2-漏电流上限  3-功率上限 4-温度上限  5-电流门限
	 * @param data 设置的数据
	 */
	public void sendWriteThresholdParamsCmd(String gatewayId, String terminalId, String switchId, String thresholdType, short data){
		byte[] datas = new byte[15];
		//起始帧
		datas[0] = 0x3E;
		//网关ID
		byte[] gwBytes = CommUtils.short2Byte(Short.parseShort(gatewayId));
		datas[1] = gwBytes[0];
		datas[2] = gwBytes[1];
		//终端ID
		byte[] dxBytes = CommUtils.short2Byte(Short.parseShort(terminalId));
		datas[3] = dxBytes[0];
		datas[4] = dxBytes[1];
		//版本号
		datas[5] = 0x01;
		//线路地址
		datas[6] = (byte)Integer.parseInt(switchId);
		//原485从机地址
		datas[7] = 0x01;
		//原485功能域
		datas[8] = 0x06;//配置参数命令标识
		//原485数据地址高位
		datas[9] = (byte)Integer.parseInt(thresholdType);//配置电压门限上限，可参照485协议附件cmd为06表
		//原485数据地址低位
		datas[10] = 0x00;//空开地址
			
		//处理数据,这里要注意下发的数据单位
		//电压1V 漏电电流0.1mA,功率1W，温度0.1度,电流0.01A
		byte[] tmpByts = TypeConvert.short2byte(data);
		//原485协议数据内容高位
		datas[11] = tmpByts[0];//0x07;
		//原485协议数据内容低位
		datas[12] = tmpByts[1];//(byte)0xD0;
		//奇偶校验位
		datas[13] = 0x00;
		//停止位
		datas[14] = 0x3F;
		System.out.println("发送的指令为（Hex)" + CommUtils.byteArrToHexStr(datas));
		//验证奇偶校验
		if (!ValidUtils.checkEvenOdd(datas, 1)) {// 约定奇校验
			datas[13] = 0x01;
		}
		//读取参数
		client.send(datas);
	}
	
	/**
	 * 设置网关参数
	 * @param gwAddress 网关地址
	 * @param gwNum 组网网关个数
	 * @param gwList 组网网关列表(从小到到)
	 * @param startAddr 组网起始节点地址
	 * @param endAddr 组网结束节点地址
	 * @param gwDelayTime 组网网关之间发送延时
	 * @param dxDelayTime 组网节点之间发送延时
	 * @param getServerTimeWx 获取服务器时间准许网络延时误差
	 * @param okFlag 配置有效标志位
	 * @param queueFlag 服务器发送命令至网关，网关插队或排队发送至节点标志位
	 * @param reportCycle 上报周期
	 * @param Gw2DxOverTime 网关发送节点命令回应超时时间
	 * @return
	 */
	public void setGwParams(String gwAddress, String gwNum, String gwList, String startAddr, String endAddr, String gwDelayTime,
			String dxDelayTime, String getServerTimeWx, String okFlag, String queueFlag, String reportCycle, String gw2DxOverTime) {
		//组网网关个数
		int num = Integer.parseInt(gwNum);
		byte[] datas = new byte[25+num*2];
		//起始帧
		datas[0] = 0x2C;
		//网关ID
		byte[] gwBytes = CommUtils.short2Byte(Short.parseShort(gwAddress));
		datas[1] = gwBytes[0];
		datas[2] = gwBytes[1];
		//终端id
		datas[3] = 0x00;
		datas[4] = 0x00;
		//协议
		datas[5] = 0x01;
		//组网网关个数
		//int num = Integer.parseInt(gwNum);
		datas[6] = (byte)num;
		//网关列表，从小到大
		int index = 7;
		String[] gwStrs = gwList.split(",");
		if(gwStrs != null && gwStrs.length > 0){
			for(int i=0; i<gwStrs.length; i++){
				byte[] temp = CommUtils.short2Byte(Short.parseShort(gwStrs[i]));
				datas[index++] = temp[0];
				datas[index++] = temp[1];
			}
		}
		//组网起始节点地址
		byte[] startAddrByts = CommUtils.short2Byte(Short.parseShort(startAddr));
		datas[index++] = startAddrByts[0];
		datas[index++] = startAddrByts[1];
		//组网结束节点地址
		byte[] endAddrByts = CommUtils.short2Byte(Short.parseShort(endAddr));
		datas[index++] = endAddrByts[0];
		datas[index++] = endAddrByts[1];
		//组网网关发送之间发送延时
		byte[] gwDelayTimeByts = CommUtils.short2Byte(Short.parseShort(gwDelayTime));
		datas[index++] = gwDelayTimeByts[0];
		datas[index++] = gwDelayTimeByts[1];
		//组网节点之间发送延时
		byte[] dxDelayTimeByts = CommUtils.short2Byte(Short.parseShort(dxDelayTime));
		datas[index++] = dxDelayTimeByts[0];
		datas[index++] = dxDelayTimeByts[1];
		//获取服务器时间准许网络延时误差
		byte[] getServerTimeWxByts = CommUtils.short2Byte(Short.parseShort(getServerTimeWx));
		datas[index++] = getServerTimeWxByts[0];
		datas[index++] = getServerTimeWxByts[1];
		//配置有效标志位
		datas[index++] = (byte)Integer.parseInt(okFlag);
		//服务器发送命令至网关,网关插队或排队发送至节点标志位
		datas[index++] = (byte)Integer.parseInt(queueFlag);
		//上报周期
		byte[] reportCycleByts = CommUtils.short2Byte(Short.parseShort(reportCycle));
		datas[index++] = reportCycleByts[0];
		datas[index++] = reportCycleByts[1];
		//网关发送给节点命令回应超时时间
		byte[] gw2DxOverTimeByts = CommUtils.short2Byte(Short.parseShort(gw2DxOverTime));
		datas[index++] = gw2DxOverTimeByts[0];
		datas[index++] = gw2DxOverTimeByts[1];		
		//奇偶校验位
		datas[index++] = 0x00;
		//停止位
		datas[index++] = 0x2D;
		System.out.println("发送的指令为（Hex)" + CommUtils.byteArrToHexStr(datas));
		//验证奇偶校验
		if (!ValidUtils.checkEvenOdd(datas, 1)) {// 约定奇校验
			datas[index-2] = 0x01;
		}
		//读取参数
		client.send(datas);

	}
	/**
	 * 读取网关参数
	 * @param gwAddress 网关地址
	 */
	public void getGwParams(String gwAddress) {
		byte[] datas = new byte[9];
		//起始帧
		datas[0] = 0x4C;
		//网关ID
		byte[] gwBytes = CommUtils.short2Byte(Short.parseShort(gwAddress));
		datas[1] = gwBytes[0];
		datas[2] = gwBytes[1];
		//终端id
		datas[3] = 0x00;
		datas[4] = 0x00;
		//协议
		datas[5] = 0x01;
		//数据域
		datas[6] = 0x00;
		//校验位
		datas[7] = 0x00;
		//停止位
		datas[8] = 0x4D;
		//验证奇偶校验
		if (!ValidUtils.checkEvenOdd(datas, 1)) {// 约定奇校验
			datas[7] = 0x01;
		}
		//读取参数
		client.send(datas);
	}

	public static void main(String[] args){
		//测试合分闸
		//ClientRequestCmdSend.getIntance().sendControlSwichCmd("0", "41", "0", "1");
		//其它测试
		//byte[] bs = TypeConvert.short2byte(2000);
		//System.out.println((bs[0]&0xFF)+","+(bs[1]&0xFF));
		//ClientRequestCmdSend.getIntance();
		//测试最大电流100A,注意底层电流单位为0.01A，所以这里要乘以100
		ClientRequestCmdSend.getIntance().sendWriteThresholdParamsCmd("0", "20", "0","3",(short)40000);
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//测试读取测试门限参数指令  读取功率门限
	//	ClientRequestCmdSend.getIntance().sendReadThresholdParamsCmd("0", "41", "1", "3");
		/*try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		//读取电压上限
		//ClientRequestCmdSend.getIntance().sendReadThresholdParamsCmd("0", "1", "0", "0");
	}
	
}
