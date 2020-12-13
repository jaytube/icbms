package com.wz.socket.servlet;

import java.util.Map;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.wz.socket.handler.MessageManagerFacade;
import com.wz.socket.handler.MessageManagerLogicHandler;
import com.wz.socket.model.TcpMessage;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ApplicationContextUtil implements ApplicationContextAware {
	
	private static ApplicationContext context;
	private static Object lock=new Object();
	
	public static void registor() {
		synchronized (lock) {
			/*if (context == null) {
				context = new ClassPathXmlApplicationContext("yahe-manager-spring.xml");  
			}*/
		}
	}
	/**
	 * 获取远程控制消息处理类
	 * @return
	 * @throws JMSException 
	 */
	/*public static void CallControlMessageHandler(MapMessage message) throws JMSException{
		Map<String, ControlMessageHandler>  map = context.getBeansOfType(ControlMessageHandler.class);
		for(Map.Entry<String, ControlMessageHandler> entry : map.entrySet()){
			ControlMessageHandler control = entry.getValue();
			if(message != null && control.getProtocolType().equals(message.getString("protocolType"))){
				control.doExec(message);
			}
		}
	}
	*/
	/**
	 * 转发消息
	 * @param message
	 * @param session
	 */
	public  static void callIotRequestProvider(TcpMessage message, IoSession session){
		MessageManagerLogicHandler handler = null;
		System.out.println("=============" + context + "=========================");
		Map<String, MessageManagerFacade> map = context.getBeansOfType(MessageManagerFacade.class);
		System.out.println("=============" + map + "=========================");
		for(Map.Entry<String, MessageManagerFacade> entry : map.entrySet()){
			MessageManagerFacade facade = entry.getValue();
			if(facade.getFacadeMap() != null){
				handler = (MessageManagerLogicHandler) facade.getFacadeMap().get(message.getProtocolType());
				if(handler != null){
					 handler.doExec(message, session);
				}
			}
		}
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}
	/**
     * 根据bean的id来查找对象
     * @param id
     * @return
     */
    public static Object getBeanById(String id){
        return context.getBean(id);
    }

    /**
     * 根据bean的class来查找对象
     * @param c
     * @return
     */
    public static Object getBeanByClass(Class c){
        return context.getBean(c);
    }

    /**
     * 根据bean的class来查找所有的对象(包括子类)
     * @param c
     * @return
     */
    public static Map getBeansByClass(Class c){
        return context.getBeansOfType(c);
    }

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		context = arg0;
	}

}
