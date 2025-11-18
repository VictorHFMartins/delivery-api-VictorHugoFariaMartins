package com.deliverytech.delivery.domain.model;

import java.util.List;

import com.deliverytech.delivery.domain.enums.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Usuario {

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Pedido> pedidos;

    @PrePersist
    public void definirTipo() {
        if (tipoUsuario == null) {
            tipoUsuario = TipoUsuario.CLIENTE;
        }
    }
}
