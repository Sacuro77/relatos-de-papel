package com.actividad3.comms_service.websocket;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ServerEndpoint("/ws/support")
public class SupportChatEndpoint {

    private static final Logger log = LoggerFactory.getLogger(SupportChatEndpoint.class);

    @OnOpen
    public void onOpen(Session session) {
        log.info("WebSocket soporte conexion abierta sessionId={}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("WebSocket soporte mensaje recibido sessionId={} payload={}", session.getId(), message);

        String response = "Hola, soy el asistente de Relatos de Papel. Recibi tu mensaje: " + message;
        session.getBasicRemote().sendText(response);

        log.info("WebSocket soporte respuesta enviada sessionId={} payload={}", session.getId(), response);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.info("WebSocket soporte conexion cerrada sessionId={} reason={}", session.getId(), closeReason);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        String sessionId = session == null ? "unknown" : session.getId();
        log.warn("WebSocket soporte error sessionId={}", sessionId, error);
    }
}
