package com.deliverytech.delivery.api.dto;

import java.util.List;

import com.deliverytech.delivery.domain.model.Administrador;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AdministradorResponse", description = "Retorno detalhado dos dados de um administrador.")
public record AdministradorResponse(

        @Schema(description = "Identificador do administrador", example = "10")
        Long id,

        @Schema(description = "Nome completo do administrador", example = "João Silva")
        String nome,

        @Schema(description = "E-mail cadastrado do administrador", example = "joao@email.com")
        String email,

        @Schema(description = "Status do administrador", example = "Ativo")
        String status,

        @Schema(description = "Telefones associados ao administrador")
        List<String> telefones,

        @Schema(description = "Endereço completo do administrador")
        EnderecoResponse endereco
) {

    public static AdministradorResponse of(Administrador a) {

        List<String> telefones = a.getTelefones().stream()
                .filter(t -> t.getUsuario() != null && t.getUsuario().getId().equals(a.getId()))
                .map(t -> "(" + t.getDdd() + ") " + t.getNumero())
                .toList();

        return new AdministradorResponse(
                a.getId(),
                a.getNome(),
                a.getEmail(),
                a.isStatus() ? "Ativo" : "Inativo",
                telefones,
                a.getEndereco() != null ? EnderecoResponse.of(a.getEndereco()) : null
        );
    }
}
