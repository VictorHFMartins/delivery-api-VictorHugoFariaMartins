package com.deliverytech.delivery.domain.model;

import com.deliverytech.delivery.domain.enums.TipoTelefone;
import com.deliverytech.delivery.domain.enums.TipoUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "telefones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Telefone {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "ddd", nullable = false, length = 3)
    private String ddd;

    @Column(name = "numero", nullable = false, length = 15)
    private String numero;

    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = true)
    private TipoUsuario tipoUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_telefone", nullable = true)
    private TipoTelefone tipoTelefone;

    @PrePersist
    public void prePersist() {

        if (ativo == false) {
            ativo = true;
        }
        if (tipoUsuario != null) {
            this.tipoUsuario = usuario.getTipoUsuario();
        }
    }

}
