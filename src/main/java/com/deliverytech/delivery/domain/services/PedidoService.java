package com.deliverytech.delivery.domain.services;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery.api.dto.PedidoRequest;
import com.deliverytech.delivery.api.dto.PedidoResponse;
import com.deliverytech.delivery.domain.enums.StatusPedido;

public interface PedidoService {

    PedidoResponse criar(PedidoRequest dto);

    PedidoResponse atualizar(Long pedidoId, PedidoRequest dto);

    PedidoResponse cancelar(Long pedidoId);

    void deletar(Long id);

    PedidoResponse buscarPorId(Long pedidoId);

    List<PedidoResponse> listarTodos();

    List<PedidoResponse> listarPorCliente(Long clienteId);

    List<PedidoResponse> listarPorRestaurante(Long restauranteId);

    BigDecimal calcularTotal(Long pedidoId);

    PedidoResponse mudarStatusPedido(Long pedidoId, StatusPedido statusPedido);

}
