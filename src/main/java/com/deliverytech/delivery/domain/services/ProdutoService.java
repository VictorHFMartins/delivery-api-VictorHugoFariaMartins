package com.deliverytech.delivery.domain.services;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery.api.dto.ProdutoRequest;
import com.deliverytech.delivery.api.dto.ProdutoResponse;
import com.deliverytech.delivery.domain.enums.CategoriaProduto;
import com.deliverytech.delivery.domain.model.Produto;

public interface ProdutoService {

    void deletar(Long ProdutoId);

    Produto buscarOuCriar(ProdutoRequest dto);

    ProdutoResponse criar(Long restauranteId, ProdutoRequest dto);

    ProdutoResponse atualizar(Long produtoId, ProdutoRequest dto);

    ProdutoResponse ativarInativar(Long produtoId);

    ProdutoResponse buscarPorId(Long produtoId);

    List<ProdutoResponse> buscarTodos();

    List<ProdutoResponse> buscarPorFiltro(String nome, Long Quantidade, BigDecimal preco, CategoriaProduto categoriaProduto);

    List<ProdutoResponse> buscarProdutosPorRestaurante(String retauranteNome, Long restauranteId);

}
