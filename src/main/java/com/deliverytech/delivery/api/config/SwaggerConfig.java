package com.deliverytech.delivery.api.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "DeliveryTech API",
                version = "1.0.0",
                description = "API para gerenciamento completo de operações de delivery",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "Victor Hugo - DeliveryTech",
                        email = "dev@deliverytech.com",
                        url = "https://www.deliverytech.com"
                ),
                license = @io.swagger.v3.oas.annotations.info.License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Documentação detalhada",
                url = "https://deliverytech.com/docs"
        )
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development"),
                        new Server()
                                .url("https://api.deliverytech.com/v1")
                                .description("Production")
                ));
    }
}
