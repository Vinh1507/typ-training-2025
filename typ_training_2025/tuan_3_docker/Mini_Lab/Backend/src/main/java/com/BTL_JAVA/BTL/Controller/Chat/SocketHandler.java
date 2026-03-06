package com.BTL_JAVA.BTL.Controller.Chat;

import com.BTL_JAVA.BTL.DTO.Request.Auth.IntrospectRequest;
import com.BTL_JAVA.BTL.Entity.Chat.WebSocketSession;
import com.BTL_JAVA.BTL.Service.AuthenticationService;
import com.BTL_JAVA.BTL.Service.Chat.WebSocketSessionService;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.nimbusds.jose.JOSEException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class SocketHandler {
    SocketIOServer server;
    AuthenticationService authenticationService;
    WebSocketSessionService  webSocketSessionService;

    @OnConnect
    public void clientConnected(SocketIOClient client) throws ParseException, JOSEException {
        //Get token from request param
        String token=client.getHandshakeData().getSingleUrlParam("token");

        //Verify token
        var introspectResponse=authenticationService.introspect(IntrospectRequest
                .builder()
                .token(token)
                .build());


        //If Token is invalid disconnected

        if(introspectResponse.isValid()){
            log.info("Client connected: {}",client.getSessionId());
            //Persist webSocketSession
            WebSocketSession webSocketSession= WebSocketSession.builder()
                    .socketSessionId(client.getSessionId().toString())
                    .userId(introspectResponse.getUserId())
                    .createdAt(Instant.now())
                    .build();
            webSocketSession=webSocketSessionService.createWebSocketSession(webSocketSession);

            log.info("WebSocketSession created: {}",webSocketSession.getId());
        }
        else {
            log.error("Authenticated failed: {} {}",client.getSessionId(), token);
            client.disconnect();
        }

        log.info("Client connected: {},{}", client.getSessionId(),token);
    }

    @OnDisconnect
    public void clientDisconnected(SocketIOClient client) {
        log.info("Client disconnected: {}", client.getSessionId());
        webSocketSessionService.deleteSession(client.getSessionId().toString());
    }

    @PostConstruct
    public void startServer() {
        server.start();
        server.addListeners(this);
        log.info("Server started");
    }

    @PreDestroy
    public void stopServer() {
        server.stop();
        log.info("Server stopped");
    }

    @OnEvent("join_room")
    public void onJoinRoom(SocketIOClient client, String roomName) {
        client.joinRoom(roomName);
        log.info("Client {} joined room {}", client.getSessionId(), roomName);
    }
}
