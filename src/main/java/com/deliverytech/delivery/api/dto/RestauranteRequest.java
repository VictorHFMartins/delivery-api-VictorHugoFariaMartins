package com.deliverytech.delivery.api.dto;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.enums.EstadoRestaurante;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestauranteRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "E-mail inválido")
        String email,
        
        @Valid
        @NotNull(message = "Os telefones são obrigatórios")
        List<TelefoneRequest> telefones,
        
        @Valid
        @NotNull(message = "O endereço é obrigatório")
        EnderecoRequest endereco,
        
        @NotBlank(message = "CNPJ é obrigatório")
        String cnpj,
        
        @NotNull(message = "A classe do restaurante é obrigatória")
        CategoriaRestaurante classe,
        
        @NotNull(message = "O estado do restaurante é obrigatória")
        EstadoRestaurante estado,
        
        @NotNull(message = "A hora de abertura do restaurante é obrigatória")
        LocalTime horarioAbertura,
        
        @NotNull(message = "A hora de fechamento do restaurante é obrigatória")
        LocalTime horarioFechamento,
        
        @NotNull(message = "A taxa de entrega do restaurante é obrigatória")
        BigDecimal taxaEntrega
        ) {

}
