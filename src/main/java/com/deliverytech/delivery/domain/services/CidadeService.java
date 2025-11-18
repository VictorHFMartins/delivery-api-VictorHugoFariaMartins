package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.CidadeRequest;
import com.deliverytech.delivery.api.dto.CidadeResponse;
import com.deliverytech.delivery.domain.model.Cidade;

public interface CidadeService {

    Cidade buscarOuCriar(CidadeRequest dto);

    CidadeResponse criar(CidadeRequest cidadeDto);

    CidadeResponse alterar(long id, CidadeRequest cidadeDto);

    CidadeResponse buscarPorId(Long cidadeId);

    List<CidadeResponse> listarTodos();

    List<CidadeResponse> buscarPorNomeContendo(String nomeCidade);

    List<CidadeResponse> buscarCidadesPorEstadoUf(String estadoUf);

    void deletar(Long id);

}
