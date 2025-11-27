package com.deliverytech.delivery.domain.services;

import java.math.BigDecimal;
import java.util.List;

import com.deliverytech.delivery.api.dto.FaturamentoCategoriaResponse;
import com.deliverytech.delivery.api.dto.ItemPedidoResponse;
import com.deliverytech.delivery.api.dto.ProdutoRequest;
import com.deliverytech.delivery.api.dto.ProdutoResponse;
import com.deliverytech.delivery.domain.enums.CategoriaProduto;

public interface ProdutoService {

    void deletar(Long ProdutoId);

    ProdutoResponse criar(Long restauranteId, ProdutoRequest dto);

    ProdutoResponse atualizar(Long produtoId, ProdutoRequest dto);

    ProdutoResponse ativarInativar(Long produtoId);

    ProdutoResponse buscarPorId(Long produtoId);

    List<ProdutoResponse> buscarTodosDisponiveis();

    List<ProdutoResponse> buscarPorFiltro(String nome, Long quantidade, BigDecimal preco, CategoriaProduto categoriaProduto);

    List<ProdutoResponse> buscarProdutosPorRestaurante(Long restauranteId);
    
    List<ItemPedidoResponse> listarMaisVendidos();

    List<FaturamentoCategoriaResponse> faturamentoPorCategoria();

}
