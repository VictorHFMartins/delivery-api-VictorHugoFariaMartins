package com.deliverytech.delivery.domain.repository;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.enums.EstadoRestaurante;
import com.deliverytech.delivery.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    // Buscar por nome
    Optional<Restaurante> findByNomeContainingIgnoreCase(String nome);

    // Busca por cnpj
    Optional<Restaurante> findByCnpj(String cnpj);

    // Buscar restaurantes ativos
    List<Restaurante> findByStatusTrue();

    // Busca por Estado do restaurante: ABERTO, FECHADO, MANUTENCAO
    List<Restaurante> findByEstado(EstadoRestaurante estado);

    // Buscar por categoria
    List<Restaurante> findByCategoria(CategoriaRestaurante categoria);

    // Busca por horário de abertura
    List<Restaurante> findByHorarioAbertura(LocalTime horaAbertura);

    // Busca por horário de fechamento
    List<Restaurante> findByHorarioFechamento(LocalTime horaFechamento);

    // busca clientes por numero;
    List<Restaurante> findByTelefonesNumeroContaining(String numero);

    // Top 5 restaurantes por nome (ordem alfabética)
    List<Restaurante> findTop5ByOrderByNotaAvaliacaoDescNomeAsc();

    boolean existsByEmail(String email);

    @Query("""
        SELECT SUM(p.valorTotal)
        FROM Pedido p
        WHERE p.restaurante.id = :restauranteId
    """)
    BigDecimal totalVendasPorRestaurante(@Param("restauranteId") Long restauranteId);

}
