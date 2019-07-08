package com.example.coolparking.service;

import com.example.coolparking.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/webSocket/{UUID}")
@Slf4j
public class WebSocket {
    private Session session;
    private String UUID="";
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();

    @OnOpen
    public void onOpen(Session session, @PathParam("UUID")String UUID){
        this.session = session;
        this.UUID = UUID;
        webSocketSet.add(this);
        log.info("有新连接，总数：{}", webSocketSet.size());
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        log.info("连接断开，总数：{}", webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message){
        log.info("收到客户端发送的消息：{}", message);
    }

    public void sendMessage(String message){
        for(WebSocket webSocket: webSocketSet){
            log.info("广播消息：{}", message);
            try{
                webSocket.session.getBasicRemote().sendText(message);
            }catch (Exception e){}
        }
    }

    public void sendInfo(String UUID, String message){
        for(WebSocket item: webSocketSet){
            if(item.UUID.equals(UUID)){
                try{
                    item.session.getBasicRemote().sendText(message);
                }catch (Exception e){}
            }
        }
    }
}
