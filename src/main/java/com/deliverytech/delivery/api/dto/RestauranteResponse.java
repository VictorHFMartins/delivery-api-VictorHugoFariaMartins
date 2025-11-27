package com.deliverytech.delivery.api.dto;

import java.time.LocalTime;
import java.util.List;

import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.enums.EstadoRestaurante;
import com.deliverytech.delivery.domain.model.Restaurante;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados retornados de um restaurante cadastrado")
public record RestauranteResponse(
        @Schema(description = "ID do restaurante", example = "10")
        Long id,
        @Schema(description = "Nome do restaurante", example = "Hamburgueria do Zé")
        String nome,
        @Schema(description = "CNPJ formatado", example = "12.345.678/0001-55")
        String cnpj,
        @Schema(description = "Telefones do restaurante")
        List<String> telefones,
        @Schema(description = "E-mail de contato", example = "contato@hburgerze.com")
        String email,
        @Schema(description = "Status ativo/inativo", example = "Ativo")
        String status,
        @Schema(description = "Endereço formatado completo")
        String endereco,
        @Schema(description = "Categoria gastronômica", example = "BRASILEIRO")
        CategoriaRestaurante classe,
        @Schema(description = "Estado de abertura", example = "ABERTO")
        EstadoRestaurante estado,
        @Schema(description = "Horário de abertura", example = "11:00")
        LocalTime horarioAbertura,
        @Schema(description = "Horário de fechamento", example = "23:00")
        LocalTime horarioFechamento,
        @Schema(description = "Média geral das avaliações do restaurante", example = "4.7")
        Double nota
        ) {

    public static RestauranteResponse of(Restaurante r) {
        List<String> telefones = r.getTelefones().stream()
                .filter(t -> t.getUsuario() != null && t.getUsuario().getId().equals(r.getId()))
                .map(t -> "(" + t.getDdd() + ") " + t.getNumero())
                .toList();

        String cnpj = r.getCnpj();
        String cnpjFormatado;
        if (r.getCnpj() != null && r.getCnpj().length() == 14) {
            cnpjFormatado = cnpj.substring(0, 2)
                    + "." + cnpj.substring(2, 5)
                    + "." + cnpj.substring(5, 8)
                    + "/" + cnpj.substring(8, 12)
                    + "-" + cnpj.substring(12, 14);
        } else {
            cnpjFormatado = null;
        }

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
                cnpjFormatado,
                telefones,
                r.getEmail(),
                r.isStatus() ? "Ativo" : "Inativo",
                enderecoFormatado,
                r.getCategoria(),
                r.getEstado(),
                r.getHorarioAbertura(),
                r.getHorarioFechamento(),
                r.getNotaAvaliacao());
    }
}
