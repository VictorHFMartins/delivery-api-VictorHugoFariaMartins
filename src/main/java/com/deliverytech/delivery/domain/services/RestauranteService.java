package com.deliverytech.delivery.domain.services;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import com.deliverytech.delivery.api.dto.RestauranteFreteResponse;
import com.deliverytech.delivery.api.dto.RestauranteRequest;
import com.deliverytech.delivery.api.dto.RestauranteResponse;
import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;

public interface RestauranteService {

    RestauranteResponse ativarInativar(Long restauranteId);

    void deletar(Long usuarioId);

    RestauranteResponse criar(RestauranteRequest restauranteDto);

    RestauranteResponse alterar(Long idRestaurante, RestauranteRequest restauranteDto);

    RestauranteResponse buscarPorId(Long idRestaurante);

    RestauranteResponse buscarPorCnpj(String cnpj);

    List<RestauranteResponse> listarTodos();

    List<RestauranteResponse> listarPorRankingTop5();

    BigDecimal totalDeVendasPorRestaurante(Long restauranteId);

    List<RestauranteResponse> buscarPorFiltro(
            String email,
            String numeroTelefone,
            String nome,
            LocalTime horarioAbertura,
            LocalTime horarioFechamento,
            CategoriaRestaurante categoriaRestaurante);

    List<RestauranteFreteResponse> listarComFreteOrdenado(Long clienteId);

}
