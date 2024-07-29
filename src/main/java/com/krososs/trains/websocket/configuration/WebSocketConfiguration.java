package com.krososs.trains.websocket.configuration;

import com.krososs.trains.rest.security.JWTService;
import com.krososs.trains.websocket.security.SessionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.http.server.*;
import com.krososs.trains.websocket.handlers.ServerHandler;


import java.util.Map;

@EnableWebSocket
@Configuration
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private ServerHandler serverHandler;

    @Autowired
    private SessionStore sessionStore;

    @Autowired
    private JWTService jwtService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(serverHandler, "/app/*")
                .setAllowedOrigins("*")
                .addInterceptors(serverInterceptor());
    }

    @Bean
    public HandshakeInterceptor serverInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

                String accessToken = request.getHeaders().get("Authorization")
                        .toString()
                        .replace("[", "")
                        .replace("]", "")
                        .substring(7);

                if (!sessionStore.tokenIsPresent(accessToken)) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return false;
                }

                if(sessionStore.sessionIsAssigned(accessToken)){
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return false;
                }

                if (jwtService.tokenExpired(accessToken)){
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return false;
                }

                attributes.put("access_token", accessToken);
                return true;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
            }
        };
    }
}
