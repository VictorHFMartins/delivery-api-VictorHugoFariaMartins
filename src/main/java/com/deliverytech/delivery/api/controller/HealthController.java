package com.deliverytech.delivery.api.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Health", description = "Endpoints de monitoramento e informações da API")
@RestController
@RequestMapping("/health")
public class HealthController {

    @Value("${spring.application.name}")
    private String application;

    @Value("${spring.application.version}")
    private String version;

    @Value("${spring.application.group}")
    private String developer;

    @Operation(
            summary = "Status da API",
            description = "Retorna o estado atual da API e o timestamp da verificação."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Status da aplicação retornado",
            content = @Content(
                    mediaType = "application/json"
            )
    )
    @GetMapping
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "timestamp", LocalDateTime.now().toString(),
                "service", "Delivery API"
        );
    }

    @Operation(
            summary = "Informações da aplicação",
            description = "Retorna metadados da aplicação, incluindo versão, desenvolvedor, curso e informações de runtime."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Informações retornadas com sucesso",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AppInfo.class)
            )
    )
    @GetMapping("/info")
    public AppInfo info() {
        return new AppInfo(
                application,
                version,
                developer,
                "12524133573",
                "Análise e Desenvolvimento de Sistemas",
                "Universidade Anhembi Morumbi (UAM)",
                "Vila Olímpia",
                System.getProperty("java.version"),
                org.springframework.boot.SpringBootVersion.getVersion()
        );
    }

    @Schema(
            name = "AppInfo",
            description = "Informações detalhadas da aplicação"
    )
    public record AppInfo(
            @Schema(description = "Nome da aplicação", example = "Delivery API") String application,
            @Schema(description = "Versão atual", example = "1.0.0") String version,
            @Schema(description = "Responsável/desenvolvedor", example = "Victor Hugo Martins") String developer,
            @Schema(description = "Registro acadêmico", example = "12524133573") String ra,
            @Schema(description = "Curso relacionado", example = "Análise e Desenvolvimento de Sistemas") String curso,
            @Schema(description = "Instituição de ensino", example = "Universidade Anhembi Morumbi") String faculdade,
            @Schema(description = "Campus", example = "Vila Olímpia") String campus,
            @Schema(description = "Versão do Java utilizada") String javaVersion,
            @Schema(description = "Versão do Spring Boot utilizada") String framework
    ) {}
}
