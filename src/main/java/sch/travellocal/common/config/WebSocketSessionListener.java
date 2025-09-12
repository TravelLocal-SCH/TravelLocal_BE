package sch.travellocal.common.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketSessionListener {

    private final Set<String> connectedSessions = Collections.synchronizedSet(new HashSet<>());

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        connectedSessions.add(sessionId);
        System.out.println("New WebSocket connection: " + sessionId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        connectedSessions.remove(sessionId);
        System.out.println("WebSocket disconnected: " + sessionId);
    }

    public int getConnectedSessionCount() {
        return connectedSessions.size();
    }
}
