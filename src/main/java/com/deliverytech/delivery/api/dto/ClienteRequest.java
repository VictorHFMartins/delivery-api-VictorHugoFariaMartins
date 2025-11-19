package com.deliverytech.delivery.api.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteRequest(

    @NotBlank(message = "O nome é obrigatório")
    String nome,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "E-mail inválido")
    String email,

    @Valid
    @NotNull(message = "Os telefones são obrigatórios")
    List<TelefoneRequest> telefones,

    @Valid
    @NotNull(message = "O endereço é obrigatório")
    EnderecoRequest endereco
) {}
