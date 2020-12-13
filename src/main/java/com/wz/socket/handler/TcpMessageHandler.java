package com.wz.socket.handler;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import com.wz.socket.model.TcpMessage;

@Component
public class TcpMessageHandler implements MessageManagerLogicHandler {

	Logger logger = Logger.getLogger(TcpMessageHandler.class);


	@Resource(type = TcpMessageFacade.class)
	private TcpMessageFacade facade;
	
	public void doExec(TcpMessage message, IoSession session) {
		handleMsgPack(message, session); 
	}
	
	public void handleMsgPack(TcpMessage message, IoSession session){
		if(facade.getFacadeMap() != null){
			MessageManagerLogicHandler handler = (MessageManagerLogicHandler) facade.getFacadeMap().get(message.getCmd());
			if(handler != null){
				handler.doExec(message, session);
			}
		}
	}
}
