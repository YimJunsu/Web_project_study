package korweb.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Vector;

@Component
public class LoginAlarmSocket extends TextWebSocketHandler{

    private static final List<WebSocketSession> loginlist = new Vector<>();

}
