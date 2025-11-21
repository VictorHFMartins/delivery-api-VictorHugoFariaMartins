package com.deliverytech.delivery.api.dto;

import java.util.List;

import com.deliverytech.delivery.domain.model.Cliente;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ClienteResponse", description = "Retorno detalhado dos dados de um cliente.")
public record ClienteResponse(

        @Schema(description = "Identificador do cliente", example = "10")
        Long id,

        @Schema(description = "Nome completo do cliente", example = "João Silva")
        String nome,

        @Schema(description = "E-mail cadastrado do cliente", example = "joao@email.com")
        String email,

        @Schema(description = "Status do cliente", example = "Ativo")
        String status,

        @Schema(description = "Telefones associados ao cliente")
        List<String> telefones,

        @Schema(description = "Endereço completo do cliente")
        EnderecoResponse endereco
) {

    public static ClienteResponse of(Cliente c) {

        List<String> telefones = c.getTelefones().stream()
                .filter(t -> t.getUsuario() != null && t.getUsuario().getId().equals(c.getId()))
                .map(t -> "(" + t.getDdd() + ") " + t.getNumero())
                .toList();

        return new ClienteResponse(
                c.getId(),
                c.getNome(),
                c.getEmail(),
                c.isStatus() ? "Ativo" : "Inativo",
                telefones,
                c.getEndereco() != null ? EnderecoResponse.of(c.getEndereco()) : null
        );
    }
}
