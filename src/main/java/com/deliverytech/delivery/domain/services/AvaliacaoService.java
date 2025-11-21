package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.AvaliacaoRequest;
import com.deliverytech.delivery.api.dto.AvaliacaoResponse;
import com.deliverytech.delivery.api.dto.RestauranteAvaliacoesResponse;

public interface AvaliacaoService {

    AvaliacaoResponse criar(Long restauranteId, AvaliacaoRequest request);

    AvaliacaoResponse editar(Long restauranteId, Long avaliacaoId, AvaliacaoRequest dto);

    void remover(Long avaliacaoId);

    AvaliacaoResponse responder(Long avaliacaoId, String resposta);

    List<AvaliacaoResponse> listarPorRestaurante(Long restauranteId);

    List<AvaliacaoResponse> listarPorCliente(Long clienteId);

    List<AvaliacaoResponse> listarPorData(Long restauranteId);

    List<AvaliacaoResponse> listarPorNota(Long restauranteId);

    RestauranteAvaliacoesResponse media(Long restauranteId);
}
