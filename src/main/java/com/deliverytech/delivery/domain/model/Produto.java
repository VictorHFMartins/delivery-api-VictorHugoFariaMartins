package com.deliverytech.delivery.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.deliverytech.delivery.domain.enums.CategoriaProduto;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "restaurante")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Produto{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = true, length = 250)
    private String descricao;

    @NotNull(message = "A quantidade do produto é obrigatória")
    @Min(value = 0, message = "O estoque deve ser maior ou igual a 0")
    private Long quantidade;

    @NotNull(message = "O preço do produto é obrigatória")
    @DecimalMin(value = "0.0", message = "O preço deve ser maior ou igual a 0")
    private BigDecimal preco;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "categoria_produto")
    private CategoriaProduto categoriaProduto;

    @Column(nullable = false)
    private boolean disponibilidade;

    @Column(nullable = false, name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @PrePersist
    public void prePersist() {
        this.dataCadastro = LocalDateTime.now();
        if (quantidade < 0) {
            this.quantidade = 0L;
        }
        this.disponibilidade = (quantidade > 0);
    }
}
