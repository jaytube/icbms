package com.wz.socket.handler;

import org.apache.mina.core.session.IoSession;

import com.wz.socket.model.TcpMessage;


/**
 * @author Raymond
 * 
 */
public interface MessageManagerLogicHandler {

	void doExec(TcpMessage message, IoSession session);
}
