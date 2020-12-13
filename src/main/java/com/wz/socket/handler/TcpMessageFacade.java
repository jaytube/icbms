package com.wz.socket.handler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.wz.socket.codec.MessageTcpDecoder;
import com.wz.socket.codec.MessageTcpEncoder;
import com.wz.socket.handler.biz.DataParamsResponseHandler;
import com.wz.socket.model.LoraTcpMessage;
import com.wz.socket.utils.TcpNetCmd;

@Component
public class TcpMessageFacade implements MessageManagerFacade {
    Logger logger = Logger.getLogger(TcpMessageFacade.class);

    private Map<Integer, MessageManagerLogicHandler> facadeMap = new HashMap<Integer, MessageManagerLogicHandler>();
    
    @Resource(type = DataParamsResponseHandler.class)
    private DataParamsResponseHandler dataParamsResponseHandler;



    @PostConstruct
    public void registry() {
        logger.info("====================TcpMessageFacade Registry=======================");
        //注册解码器及编码器
        MessageCodecRegister.addEncoder(LoraTcpMessage.class, new MessageTcpEncoder());
        MessageCodecRegister.addDecoder(LoraTcpMessage.class, new MessageTcpDecoder());
        // TCP协议解析组件
        this.facadeMap.put(TcpNetCmd.GET_DATA_FROM_SERVER_ID, dataParamsResponseHandler);//前置机响应服务器读取门限参数
    }

    @Override
    public Map<Integer, MessageManagerLogicHandler> getFacadeMap() {
        return facadeMap;
    }

}
