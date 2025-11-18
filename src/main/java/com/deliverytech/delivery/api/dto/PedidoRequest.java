package com.deliverytech.delivery.api.dto;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotNull;

public record PedidoRequest(
        @NotNull(message = "Id do cliente é obrigatório.")
        Long clienteId,
       
        @NotNull(message = "Id do restaurante é obrigatório.")
        Long restauranteId,
       
        @NotNull(message = "Deve haver ao menos um item no pedido.")
        List<ItemPedidoRequest> itens,
        
        @Length(max=250, message="Limite máximo de 250 caracteres.")
        String observacoes
        ) {

}
