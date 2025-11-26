package com.deliverytech.delivery.domain.model;

import com.deliverytech.delivery.domain.enums.TipoUsuario;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "administradores")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Administrador extends Usuario {

    @PrePersist
    public void definirTipo() {
        if (tipoUsuario == null) {
            tipoUsuario = TipoUsuario.ADMINISTRADOR;
        }
    }

}
