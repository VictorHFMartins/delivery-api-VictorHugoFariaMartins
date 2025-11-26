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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Autenticação", description = "Endpoints de login e criação de usuários")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Realiza login e retorna token JWT", description = "Autentica um usuário e retorna o token JWT + dados básicos.", responses = {
            @ApiResponse(responseCode = "200", description = "Autenticado com sucesso", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Credenciais incorretas"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Validated @RequestBody AuthRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Registra um novo usuária", description = "Cria um usuário e retorna automaticamente o token JWT.", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário criado", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "409", description = "Apelido ou e-mail já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Validated @RequestBody UsuarioRegisterRequest request) {

        return ResponseEntity.status(201).body(authService.register(request));
    }
}
