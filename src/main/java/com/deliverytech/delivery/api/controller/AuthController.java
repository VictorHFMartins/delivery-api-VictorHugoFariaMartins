package com.deliverytech.delivery.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery.api.dto.AuthRequest;
import com.deliverytech.delivery.api.dto.AuthResponse;
import com.deliverytech.delivery.api.dto.UsuarioRegisterRequest;
import com.deliverytech.delivery.domain.services.imp.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Autenticação", description = "Endpoints de login e registro de usuários. (Rotas públicas)")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Realiza login e retorna o token JWT",
            description = """
                Autentica um usuário utilizando apelido e senha.
                Caso as credenciais estejam corretas, retorna:
                - Token JWT
                - ID do usuário
                - Nome
                - Apelido
                - Tipo de usuário
                - Email
                """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Credenciais de autenticação",
                    content = @Content(schema = @Schema(implementation = AuthRequest.class))
            ),
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Autenticado com sucesso",
                        content = @Content(schema = @Schema(implementation = AuthResponse.class))
                ),
                @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                @ApiResponse(responseCode = "401", description = "Credenciais incorretas"),
                @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
                @ApiResponse(responseCode = "500", description = "Erro interno da API")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody AuthRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            summary = "Registra um novo usuário",
            description = """
                Cria um novo usuário dos tipos:
                - CLIENTE
                - RESTAURANTE
                - ADMINISTRADOR

                Após criar, retorna automaticamente:
                - Token JWT
                - Dados completos do usuário
                """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados completos para criação do usuário",
                    content = @Content(schema = @Schema(implementation = UsuarioRegisterRequest.class))
            ),
            responses = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Usuário criado com sucesso",
                        content = @Content(schema = @Schema(implementation = AuthResponse.class))
                ),
                @ApiResponse(responseCode = "400", description = "Erro de validação nos campos"),
                @ApiResponse(responseCode = "409", description = "Apelido ou email já estão em uso"),
                @ApiResponse(responseCode = "500", description = "Erro interno da API")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody UsuarioRegisterRequest request) {

        return ResponseEntity.status(201).body(authService.register(request));
    }
}
