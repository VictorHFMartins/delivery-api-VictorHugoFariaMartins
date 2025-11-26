package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.AdministradorRequest;
import com.deliverytech.delivery.api.dto.AdministradorResponse;

public interface AdministradorService {

    void excluir(Long id);

    AdministradorResponse ativarDesativar(Long id);

    AdministradorResponse criar(AdministradorRequest dto);

    AdministradorResponse atualizar(Long id, AdministradorRequest dto);

    AdministradorResponse buscarPorId(long id);

    List<AdministradorResponse> listarPorStatusAtivo();

    List<AdministradorResponse> buscarComFiltros(String nome, String email, String cep, String cidade, String estado, String telefone);

    List<AdministradorResponse> listarTodos();

}
