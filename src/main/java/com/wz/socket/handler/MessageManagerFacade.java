package com.wz.socket.handler;

import java.util.Map;

/**
 * @author Raymond
 * 
 */
public interface MessageManagerFacade {
	Map<Integer, MessageManagerLogicHandler> getFacadeMap();
}
