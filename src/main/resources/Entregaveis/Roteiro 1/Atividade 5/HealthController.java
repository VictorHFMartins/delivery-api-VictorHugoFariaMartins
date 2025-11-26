package com.deliverytech.delivery.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthController {

    @Value("${spring.application.name}")
    private String application;

    @Value("${spring.application.version}")
    private String version;

    @Value("${spring.application.group}")
    private String developer;

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "service", "Delevery API");
    }

    @GetMapping("/health/info")
    public appInfo info() {
        return new appInfo(
                application,
                version,
                developer, "12524133573",
                "Analise e Desenvolvimento de Sistemas",
                "Universidade Anhembi Morumbi (UAM)", "Vila Olímpia",
                System.getProperty("java.version"),
                org.springframework.boot.SpringBootVersion.getVersion());
    }

    public record appInfo(
            String application,
            String version,
            String developer,
            String ra,
            String curso,
            String Faculdade,
            String Campus,
            String javaVersion,
            String framework) {
    }

}
