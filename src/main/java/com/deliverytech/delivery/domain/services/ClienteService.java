package com.deliverytech.delivery.domain.services;

import java.util.List;

import com.deliverytech.delivery.api.dto.ClienteRankingResponse;
import com.deliverytech.delivery.api.dto.ClienteRequest;
import com.deliverytech.delivery.api.dto.ClienteResponse;

public interface ClienteService {

    void excluir(Long id);

    ClienteResponse ativarDesativar(Long clienteId);

    ClienteResponse criar(ClienteRequest clienteDto);

    ClienteResponse atualizar(Long id, ClienteRequest clienteDto);

    ClienteResponse buscarPorId(long idCliente);

    List<ClienteResponse> listarPorStatusAtivo();

    List<ClienteResponse> buscarComFiltros(String nome, String email, String cep, String cidade, String estado, String telefone);

    List<ClienteResponse> listarTodos();

    List<ClienteRankingResponse> rankingClientes();

}
