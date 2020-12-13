package com.wz.socket.handler;

import org.apache.mina.core.session.IoSession;

import com.wz.socket.model.TcpMessage;

/**
 * 接收消息处理接口
 * @author Raymond
 *
 */
public interface MsgHandler {
	public void onMsg(TcpMessage message, IoSession session);
	public void onSessionClosed(IoSession session);
	/**
	 * 握手成功
	 * @param session
	 * @param handShakeMsgID
	 */
	public void onHandShakeCompleted(IoSession session, int handShakeMsgID);
}
