package com.wz.socket.handler.biz;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wz.modules.common.utils.JsonUtil;
import com.wz.modules.common.utils.RedisUtil;
import com.wz.socket.handler.MessageManagerLogicHandler;
import com.wz.socket.model.LoraTcpMessage;
import com.wz.socket.model.TcpMessage;
import com.wz.socket.utils.CommUtils;
import com.wz.socket.utils.Constant;
import com.wz.socket.utils.TcpNetCmd;

/**
 * 获取设置参数给网关返回消息处理
 * 
 * @author Raymond 2018年8月23日
 *
 */
@Component
public class DataParamsResponseHandler implements MessageManagerLogicHandler {
	Logger logger = Logger.getLogger(DataParamsResponseHandler.class);
	
	
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public void doExec(TcpMessage message, IoSession session) {
		LoraTcpMessage tcpMsg = (LoraTcpMessage) message;
		// 解析datas start
		byte[] datas = tcpMsg.getData();
		if(TcpNetCmd.GET_DATA_FROM_SERVER_ID == tcpMsg.getProtocolType()){//后台服务器下发读取线路参数响应数据
			if (datas != null && datas.length != 4) {// 如果返回的是实时数据
				logger.info("读取参数响应============>读取参数响应字节数不对!!!!");
			} else {
				logger.info("读取参数响应============>解析实时数据start!!!!");
				tcpMsg.setSwitchAddr(CommUtils.byteToInt(datas[0])+"");
				//功能地址 00--16位电压上限  01-16位电压下限 02-16位漏电流上限  03-16位功率上限 04-16位温度上限  05-16位电流门限
				int thresholdType = CommUtils.byteToInt(datas[1]);
				int val = CommUtils.byteArrayToInt(new byte[] { 0x00, 0x00, datas[2], datas[3] });
				Map<String, String> jsonMap = new HashMap<String, String>();
				//判断redis里是否已经存在这些值了
				String jsonStr = redisUtil.hget(Integer.parseInt(tcpMsg.getGatewayId()), Constant.TEMINAL_LINE_PARAMS_CONFIGURATION, tcpMsg.getGatewayId()+"_"+tcpMsg.getTerminalId()+"_"+tcpMsg.getSwitchAddr());
				if(StringUtils.isNotBlank(jsonStr)){
					//如果发现数据已经存在
					jsonMap = JsonUtil.getObjet(jsonStr, Map.class);
				}
				if(thresholdType == 0){
					//电压上限返回值
					jsonMap.put("voltage_upper", val+"");
				} else if(thresholdType == 1){
					//电压下限返回值
					jsonMap.put("voltage_lower", val+"");
				} else if(thresholdType == 2){
					//电流上限返回值
					jsonMap.put("electricity_upper", val+"");
				} else if(thresholdType == 3){
					//功率上限返回值
					jsonMap.put("power_upper", val+"");
				} else if(thresholdType == 4){
					//温度上限返回值
					jsonMap.put("temperature_upper", val+"");
				} else if(thresholdType == 5){
					//电流门限返回值
					jsonMap.put("electricity_limit", val+"");
				} 
				jsonMap.put("reportTime",  CommUtils.parseDate(System.currentTimeMillis()));
				redisUtil.hset(0, Constant.TEMINAL_LINE_PARAMS_CONFIGURATION, tcpMsg.getTerminalId()+"_"+tcpMsg.getSwitchAddr(), JsonUtil.getJsonByObj(jsonMap));
				
				logger.info("读取参数响应============>解析实时数据end!!!!");
			}
		}
		
		
	}

	public static void main(String[] args) {
		byte[] b = new byte[] { (byte) 0x00, (byte) 0xDD };
		System.out.print(CommUtils.getShort(b, 0));
	}

}
