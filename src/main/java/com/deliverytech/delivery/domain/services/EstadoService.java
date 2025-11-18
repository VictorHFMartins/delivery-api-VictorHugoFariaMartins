package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.EstadoRequest;
import com.deliverytech.delivery.api.dto.EstadoResponse;
import com.deliverytech.delivery.domain.model.Estado;

public interface EstadoService {

    Estado buscarOuCriar(EstadoRequest dto);

    EstadoResponse criar(EstadoRequest estadoDto);

    EstadoResponse alterar(Long idEstado, EstadoRequest estadoDto);

    EstadoResponse buscarPorId(Long id);

    EstadoResponse buscarPorUf(String ufEstado);

    List<EstadoResponse> listarPorNomeContendo(String nomeEstado);

    void deletar(Long id);

    List<EstadoResponse> listarTodos();

}
