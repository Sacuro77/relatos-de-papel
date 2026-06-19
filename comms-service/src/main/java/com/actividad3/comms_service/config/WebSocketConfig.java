package com.actividad3.comms_service.config;

import com.actividad3.comms_service.websocket.SupportChatEndpoint;
import org.apache.tomcat.websocket.server.WsSci;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class WebSocketConfig {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    @Bean
    public TomcatContextCustomizer supportChatWebSocketEndpoint() {
        return context -> {
            context.addServletContainerInitializer(new WsSci(), Set.of(SupportChatEndpoint.class));
            log.info("Endpoint WebSocket registrado path=/ws/support");
        };
    }
}
