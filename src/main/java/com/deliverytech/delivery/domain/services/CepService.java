package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.CepRequest;
import com.deliverytech.delivery.api.dto.CepResponse;
import com.deliverytech.delivery.domain.model.Cep;

public interface CepService {

    Cep buscarOuCriar(CepRequest cepDto);

    CepResponse criar(CepRequest cepDto);

    CepResponse alterar(Long id, CepRequest cepDto);

    CepResponse buscarPorId(Long id);

    CepResponse buscarPorCodigo(String codigo);

    List<CepResponse> listarTodos();

    List<CepResponse> listarPorCidadeNome(String nomeCidade);

    void deletar(Long id);

}
