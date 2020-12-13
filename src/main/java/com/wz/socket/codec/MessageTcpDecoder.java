package com.wz.socket.codec;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.springframework.stereotype.Component;

import com.wz.socket.model.LoraTcpMessage;
import com.wz.socket.utils.CommUtils;
import com.wz.socket.utils.MessageUtil;
import com.wz.socket.utils.ValidUtils;
@Component
public class MessageTcpDecoder implements MessageDecoder{
	Logger logger = Logger.getLogger(MessageTcpDecoder.class) ;
	@Override
	public MessageDecoderResult decodable(IoSession session, IoBuffer buf) {
		int remain = buf.remaining();
		if(remain <= 1){
			return  MessageDecoderResult.NOT_OK;
		}
		buf.mark();
		//结束标志是否也包含在内？
		byte[] data = new byte[remain];
		buf.get(data);
		//进行奇偶校验,约定和协议用的是奇校验还是偶校验
		boolean checkEvenOddVal = ValidUtils.checkEvenOdd(data, 1);
		if(!checkEvenOddVal){
			return MessageDecoderResult.NOT_OK;
		}		
		buf.reset();
		return MessageDecoderResult.OK;
	}

	@Override
	public MessageDecoderResult decode(IoSession session, IoBuffer buf, ProtocolDecoderOutput out) throws Exception {
		LoraTcpMessage message = new LoraTcpMessage();
		logger.info("收到前置机消息" + buf.getHexDump());
		// System.out.println("收到采集仪消息" + buf.getHexDump());
		buf.mark();
		byte startByte = buf.get();
		//帧起始符 
		if(startByte == 0x1E){//服务器后台下发读取参数命令
			message.setProtocolType(CommUtils.byteToInt(startByte));	
			logger.info("============>收前置机下发给后台响应读取门限参数命令!");
		} else {//否则是无效数据
			return null;
		}

		//网关ID-2字节
		byte gatewayByte = buf.get();
		byte gatewayByte2 = buf.get();
		String gateway = CommUtils.getShort(new byte[]{gatewayByte, gatewayByte2}, 0)+"";
		message.setGatewayId(gateway);
		//终端ID-2字节
		byte terminalByte = buf.get();
		byte terminalByte2 = buf.get();
		String terminal = CommUtils.getShort(new byte[]{terminalByte, terminalByte2}, 0)+"";
		message.setTerminalId(terminal);		
		//协议版本号-1字节
		byte versionByte = buf.get();
		message.setVersion(CommUtils.byteToInt(versionByte)+"");
	
		//剩余字节数
		int remain = buf.remaining();
		if(remain<=1){
			return null;
		}
		//解析数据域  去掉最后的校验位和停止位
		byte[] datas = MessageUtil.getBytes(buf, remain-2);
		message.setData(datas);
				
		//校验位
		buf.get();
		//停止位
		byte endByte = buf.get();
		if(startByte == 0x1E){//前置机响应返回读取参数（门限参数）
			if(endByte != 0x1F){
				logger.info("============>前置机响应读取参数停止位不是1F!!!!");
				return null;
			}	
		}  else{
			logger.info("============>无效停止位!!!!");
			return null;
		}
		out.write(message);
		logger.info("============>返回数据解析成功!!!!");
		return MessageDecoderResult.OK;
	}

	@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
		
	}


}
