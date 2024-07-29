package com.krososs.trains.websocket.security;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

@Component
public class SessionStore {

    @Getter
    private Map<String, WebSocketSession> sessions;

    public SessionStore(){
        this.sessions = new HashMap<>();
    }

    public void addToken(String accessToken){
        this.sessions.put(accessToken,null);
    }

    public void assignSession(String accessToken, WebSocketSession session){
        sessions.put(accessToken, session);
    }

    public boolean tokenIsPresent(String accessToken){
        return sessions.containsKey(accessToken);
    }

    public boolean sessionIsAssigned(String accessToken){
        return sessions.get(accessToken) != null;
    }

    public void printSession(){
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().getId());
        }
    }
}
