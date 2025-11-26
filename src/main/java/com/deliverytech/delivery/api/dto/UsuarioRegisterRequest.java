package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.TipoUsuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para criação de um novo usuario")
public record UsuarioRegisterRequest(

    @Schema(description = "Nome completo", example = "Victor Hugo Martins")
    @NotBlank(message = "Nome é obrigatório")
    String nome,

    @Schema(description = "Apelido único de acesso", example = "victor")
    @NotBlank(message = "Apelido é obrigatório")
    String apelido,

    @Schema(description = "E-mail do usuario", example = "victor@mtech.com")
    @Email(message = "E-mail inválido")
    String email,

    @Schema(description = "Senha de acesso", example = "123456")
    @NotBlank(message = "Senha é obrigatória")
    String senha,

    @Schema(description = "Tipo de usuario", example = "RESTAURANTE")
    @NotNull(message = "Tipo de usuario é obrigatório")
    TipoUsuario tipoUsuario
) {}
