package com.deliverytech.delivery.domain.model;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.enums.EstadoRestaurante;
import com.deliverytech.delivery.domain.enums.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "restaurantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Restaurante extends Usuario {

    @Column(length = 18, nullable = false)
    private String cnpj;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoriaRestaurante categoria;

    @Column(nullable = true)
    private LocalTime horarioAbertura;

    @Column(nullable = true)
    private LocalTime horarioFechamento;

    @Column(name = "taxa_entrega", nullable = false)
    private BigDecimal taxaEntrega;

    @AssertTrue(message = "Horário de abertura deve ser antes do fechamento")
    public boolean isHorarioValido() {
        return horarioAbertura == null || horarioFechamento == null
                || horarioAbertura.isBefore(horarioFechamento);
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRestaurante estado;

    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Produto> produtos;

    @PrePersist
    public void definirEstado() {
        if (this.estado == null) {
            if (LocalTime.now().isAfter(horarioAbertura)) {
                this.estado = EstadoRestaurante.ABERTO;
            }

            if (LocalTime.now().isBefore(horarioFechamento)) {
                this.estado = EstadoRestaurante.FECHADO;
            }
        }
        if (tipoUsuario != null) {
            tipoUsuario = TipoUsuario.RESTAURANTE;
        }
    }

}
