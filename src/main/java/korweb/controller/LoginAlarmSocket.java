package korweb.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

@Component
public class LoginAlarmSocket extends TextWebSocketHandler{

    private static final List<WebSocketSession> loginlist = new Vector<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws  Exception{
        System.out.println("클라이언트 서버에 로그인 접속 성공!!");
        loginlist.add(session);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws  Exception{
        System.out.println("클라이언트와 접속이 끊겼다.");
        loginlist.remove(session);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message )throws Exception{
        System.out.println("로그인 알림 받음 : " + message.getPayload());
        for(int index = 0; index<=loginlist.size()-1;index++){
        WebSocketSession inclient = loginlist.get(index);
        inclient.sendMessage(message);
        }
    }
}
