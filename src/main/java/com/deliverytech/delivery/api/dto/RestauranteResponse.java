package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.enums.EstadoRestaurante;
import com.deliverytech.delivery.domain.model.Restaurante;

public record RestauranteResponse(
        Long id,
        String nome,
        String cnpj,
        List<String> telefones,
        String email,
        String status,
        String endereco,
        CategoriaRestaurante classe,
        EstadoRestaurante estado,
        LocalTime horarioAbertura,
        LocalTime horarioFechamento,
        BigDecimal taxaEntrega,
        Double nota
        ) {

    public static RestauranteResponse of(Restaurante r) {
        List<String> telefones = r.getTelefones().stream()
                .filter(t -> t.getUsuario() != null && t.getUsuario().getId().equals(r.getId()))
                .map(t -> "(" + t.getDdd() + ") " + t.getNumero())
                .toList();

        EnderecoResponse endereco = EnderecoResponse.of(r.getEndereco());
        String enderecoFormatado
                = endereco.tipoLogradouro() + ": "
                + endereco.logradouro() + ", "
                + endereco.numero() + ", "
                + endereco.bairro() + ", "
                + (endereco.complemento() != null ? endereco.complemento() : "")
                + "\nCEP: " + endereco.cep().codigo()
                + " - " + endereco.cep().cidade().nome()
                + "/" + endereco.cep().cidade().estado().uf();

        return new RestauranteResponse(
                r.getId(),
                r.getNome(),
                r.getCnpj(),
                telefones,
                r.getEmail(),
                r.isStatus() ? "Ativo" : "Inativo",
                enderecoFormatado,
                r.getCategoria(),
                r.getEstado(),
                r.getHorarioAbertura(),
                r.getHorarioFechamento(),
                r.getTaxaEntrega(),
                r.getNotaAvaliacao());
    }

}
