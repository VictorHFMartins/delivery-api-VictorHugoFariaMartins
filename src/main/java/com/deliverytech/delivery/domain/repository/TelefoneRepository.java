package com.deliverytech.delivery.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery.domain.enums.TipoTelefone;
import com.deliverytech.delivery.domain.enums.TipoUsuario;
import com.deliverytech.delivery.domain.model.Telefone;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

    // Busca por numeros ativos
    List<Telefone> findByAtivoTrue();

    // Lista Telefones por ddd
    List<Telefone> findByDdd(String ddd);

    // verifica telefone por número
    boolean existsByNumero(String numero);

    // Busca lista de telefones de usuário
    List<Telefone> findByUsuarioId(long id);

    // Busca telefone por número
    List<Telefone> findByNumeroContaining(String numero);

    // Lista telefones por tipo de usuário
    List<Telefone> findByTipoUsuario(TipoUsuario tipoUsuario);

    // Lista telefones por tipo telefone (CELULAR, FIXO, WHATSAPP)
    List<Telefone> findByTipoTelefone(TipoTelefone tipoTelefone);

    // Busca por telefone específico
    Optional<Telefone> findByDddAndNumero(String ddd, String Numero);

}
