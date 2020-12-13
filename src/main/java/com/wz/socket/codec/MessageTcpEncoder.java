package com.wz.socket.codec;

import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.springframework.stereotype.Component;

import com.wz.socket.model.LoraTcpMessage;
import com.wz.socket.utils.Constant;
/**
 * 采集仪消息编码器
 * @author lenovo
 *
 */
@Component
public class MessageTcpEncoder implements MessageEncoder<LoraTcpMessage> {
	Logger logger = Logger.getLogger(MessageTcpEncoder.class);
	@Override
	public void encode(IoSession session, LoraTcpMessage message, ProtocolEncoderOutput out) throws Exception {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);  
		int cmd = message.getCmd();
		
		if(Constant.REQUEST_TYPE_CMD_HEART_TEST == cmd){
			logger.info("发送心跳包给BServer==============>网关["+message.getGatewayId()+"]");
			//校验下奇偶校验位
			byte[] data = new byte[]{(byte)0x2A, (byte)Integer.parseInt(message.getGatewayId()), 
					(byte)0x00,(byte)0x00, (byte)0x00, (byte)0x00,(byte)0x00,(byte)0x00 ,(byte)0x00, (byte)0x2B};
			//奇偶校验
			boolean resultFlag = checkEvenOdd(data, 1);
			if(!resultFlag){//奇偶校验未通过
				data[7] = (byte)0x01;
			}
			//帧起始符
			buf.put(data[0]);
			//网关ID
			buf.put(data[1]);
			//终端id，对于心跳测试，上位机只测试网关，不会测试终端，保留字节
			buf.put(data[2]);
			//协议
			buf.put(data[3]);
			//心跳包
			buf.put(data[4]);
			//门锁状态
			buf.put(data[5]);
			//风扇状态
			buf.put(data[6]);
			//校验位
			buf.put(data[7]);
			//停止位
			buf.put(data[8]);			
		} else if(Constant.REQUEST_TYPE_CMD_SET_PARMAS == cmd){//下发网关电箱线路设置参数
			logger.info("发送设置参数包给BServer==============>网关["+message.getGatewayId()+"]");
			byte[] data = new byte[15];
			//开始帧
			data[0] = (byte)0x3A;
			//网关id
			data[1] = (byte)Integer.parseInt(message.getGatewayId());
			//终端ID
			data[2] = (byte)Integer.parseInt(message.getTerminalId());
			//协议
			data[3] = (byte)Integer.parseInt(message.getVersion());
			
			int index = 4;
			//透传数据包
			for(int i=0; i<message.getData().length; i++){
				data[index++] = message.getData()[i];
			}
			//奇偶校验位
			data[index++] =0x00;
			//结束帧
			data[index++] = (byte)0x3B;
			
			//校验奇偶校验位
			boolean resultFlag = checkEvenOdd(data, 1);
			if(!resultFlag){//奇偶校验未通过
				data[index-2] = 0x01;
			}
			
		} else if(Constant.REQUEST_TYPE_CMD_GET_PARMAS == cmd){//下发网关电箱线路读取参数
			logger.info("发送读取参数包给BServer==============>网关["+message.getGatewayId()+"]");
			byte[] data = new byte[15];
			//开始帧
			data[0] = (byte)0x4A;
			//网关id
			data[1] = (byte)Integer.parseInt(message.getGatewayId());
			//终端ID
			data[2] = (byte)Integer.parseInt(message.getTerminalId());
			//协议
			data[3] = (byte)Integer.parseInt(message.getVersion());
			
			int index = 4;
			//透传数据包
			for(int i=0; i<message.getData().length; i++){
				data[index++] = message.getData()[i];
			}
			//奇偶校验位
			data[index++] =0x00;
			//结束帧
			data[index++] = (byte)0x4B;
			
			//校验奇偶校验位
			boolean resultFlag = checkEvenOdd(data, 1);
			if(!resultFlag){//奇偶校验未通过
				data[index-2] = 0x01;
			}
			
		} else if(Constant.RESPONSE_TYPE_CMD_SET_PARAMS == cmd){//给后台服务器响应消息读取参数
			
			byte[] data = new byte[15];
			//开始帧
			data[0] = (byte)0x1E;
			//网关id
			data[1] = (byte)Integer.parseInt(message.getGatewayId());
			//终端ID
			data[2] = (byte)Integer.parseInt(message.getTerminalId());
			//协议
			data[3] = (byte)Integer.parseInt(message.getVersion());
			
			int index = 4;
			//透传数据包
			for(int i=0; i<message.getData().length; i++){
				data[index++] = message.getData()[i];
			}
			//奇偶校验位
			data[index++] =0x00;
			//结束帧
			data[index++] = (byte)0x1F;
			
			//校验奇偶校验位
			boolean resultFlag = checkEvenOdd(data, 1);
			if(!resultFlag){//奇偶校验未通过
				data[index-2] = 0x01;
			}
		} else if(Constant.REQUEST_TYPE_CMD_SET_SWITCH_ONOFF == cmd){//控制网关电箱线路开关
			byte[] data = new byte[9];
			//开始帧
			data[0] = (byte)0x2A;
			//网关id
			data[1] = (byte)Integer.parseInt(message.getGatewayId());
			//终端ID
			data[2] = (byte)Integer.parseInt(message.getTerminalId());
			//协议
			data[3] = (byte)Integer.parseInt(message.getVersion());
			
			int index = 4;
			//透传数据包
			for(int i=0; i<message.getData().length; i++){
				data[index++] = message.getData()[i];
			}
			//奇偶校验位
			data[index++] =0x00;
			//结束帧
			data[index++] = (byte)0x2B;
			
			//校验奇偶校验位
			boolean resultFlag = checkEvenOdd(data, 1);
			if(!resultFlag){//奇偶校验未通过
				data[index-2] = 0x01;
			}
			
		}
		buf.flip();
		logger.info("BServer发送采集仪下行消息：" + buf.getHexDump());
		out.write(buf);
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
	
	/**
	 * 将byte[]转为各种进制的字符串
	 * @param bytes byte[]
	 * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return 转换后的字符串
	 */
	private String binary(byte[] datas, int radix){
		return new BigInteger(1, datas).toString(radix);// 这里的1代表正数
	}

}
