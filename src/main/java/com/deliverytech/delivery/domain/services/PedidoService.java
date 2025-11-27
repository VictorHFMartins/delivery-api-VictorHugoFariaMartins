package com.deliverytech.delivery.domain.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery.api.dto.PedidoRequest;
import com.deliverytech.delivery.api.dto.PedidoResponse;
import com.deliverytech.delivery.api.dto.PedidoUpdateRequest;
import com.deliverytech.delivery.domain.enums.StatusPedido;

public interface PedidoService {

    PedidoResponse criar(PedidoRequest dto);

    PedidoResponse atualizar(Long pedidoId, PedidoUpdateRequest dto);

    PedidoResponse cancelar(Long pedidoId);

    void deletar(Long id);

    PedidoResponse buscarPorId(Long pedidoId);

    PedidoResponse mudarStatusPedido(Long pedidoId, StatusPedido statusPedido);

    List<PedidoResponse> listarTodos();

    List<PedidoResponse> listarPorCliente(Long clienteId);

    List<PedidoResponse> listarPorRestaurante(Long restauranteId);

    List<PedidoResponse> listarDezPrimeirosPorDataDePedido();

    List<PedidoResponse> listarPedidosAcimaDe(BigDecimal valor);

    List<PedidoResponse> ListarPorPeríodoEStatus(LocalDateTime inicio, LocalDateTime fim, StatusPedido status);

    List<PedidoResponse> listarPedidosEntreDatas(LocalDateTime inicio, LocalDateTime fim);

    BigDecimal calcularTotal(Long pedidoId);

}
