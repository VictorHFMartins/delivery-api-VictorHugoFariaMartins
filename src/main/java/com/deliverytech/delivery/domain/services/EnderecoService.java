package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.EnderecoRequest;
import com.deliverytech.delivery.api.dto.EnderecoResponse;
import com.deliverytech.delivery.api.dto.EnderecoUpdateRequest;
import com.deliverytech.delivery.domain.enums.TipoLogradouro;
import com.deliverytech.delivery.domain.model.Endereco;

public interface EnderecoService {

    Endereco buscarOuCriarEndereco(EnderecoRequest enderecoDto);

    EnderecoResponse criar(Long usuarioId, EnderecoRequest enderecoDto);

    EnderecoResponse atualizar(Long usuarioId, Long enderecoId, EnderecoUpdateRequest enderecoUpdateDto);

    EnderecoResponse buscarPorId(Long idEndereco);

    List<EnderecoResponse> listarTodos();

    List<EnderecoResponse> buscarPorFiltro(String bairro, String cepCodigo, String logradouro, TipoLogradouro tipoLogradouro);

    EnderecoResponse buscarPorNumeroELogradouro(String numeroEndereco, String logradouroNome);

    void deletar(Long id);

}
