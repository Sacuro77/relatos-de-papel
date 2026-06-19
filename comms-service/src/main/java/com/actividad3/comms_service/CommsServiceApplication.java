package com.actividad3.comms_service;

import com.actividad3.comms_service.config.MailProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(MailProperties.class)
@SpringBootApplication
public class CommsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommsServiceApplication.class, args);
    }
}
