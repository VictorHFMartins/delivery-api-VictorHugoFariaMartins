package com.deliverytech.delivery.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery.domain.enums.TipoUsuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    protected Long id;

    @Column(nullable = false, length = 100)
    protected String nome;

    @Column(nullable = false, unique = true)
    @Email
    protected String email;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Telefone> telefones;

    @ManyToOne
    @JoinColumn(name = "endereco_id", nullable = false)
    protected Endereco endereco;

    @Column(nullable = false)
    protected boolean status;

    @Column(name = "data_cadastro", nullable = false)
    protected LocalDateTime dataCadastro;

    @Column(nullable = true, name = "tipo_usuario")
    @Enumerated(EnumType.STRING)
    protected TipoUsuario tipoUsuario;

    @PrePersist
    protected void onCreate() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
        status = true;
    }
}
