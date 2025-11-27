package com.deliverytech.delivery.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.api.dto.FaturamentoCategoriaResponse;
import com.deliverytech.delivery.domain.enums.CategoriaProduto;
import com.deliverytech.delivery.domain.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNome(String nome);

    boolean existsByNome(String nome);

    List<Produto> findByRestauranteId(Long restauranteId);

    Optional<Produto> findByNomeAndRestauranteId(String nome, Long restauranteId);

    List<Produto> findByDisponibilidadeTrue();

    List<Produto> findByCategoriaProduto(CategoriaProduto categoria);

    List<Produto> findByPrecoLessThanEqual(BigDecimal preco);

    @Query("""
    SELECT new com.deliverytech.delivery.api.dto.FaturamentoCategoriaResponse(
        ip.produto.categoriaProduto,
        SUM(ip.precoUnitario * ip.quantidade)
    )
    FROM ItemPedido ip
    GROUP BY ip.produto.categoriaProduto
    ORDER BY SUM(ip.precoUnitario * ip.quantidade) DESC
""")
    List<FaturamentoCategoriaResponse> faturamentoPorCategoria();

}
