package com.deliverytech.delivery.api.dto;

import com.deliverytech.delivery.domain.enums.TipoLogradouro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoRequest(
    
        @NotBlank(message = "Nome do logradouro é obrigatório") 
        String logradouro,
        
        @NotNull(message = "Tipo do logradouro é obrigatório") 
        TipoLogradouro tipoLogradouro,
        
        @NotBlank(message = "Numero da residência é obrigatório") 
        String numero,

        String complemento,
        
        @NotBlank(message = "Nome do bairro é obrigatório") 
        String bairro,
        
        @NotBlank(message = "CEP é obrigatório") 
        String cepCodigo,
        
        @NotNull
        Long cidadeId,
        
        @NotNull(message = "Longitude é obrigatória") 
        double longitude,
        
        @NotNull(message = "Latitude é obrigatória") 
        double latitude) {

}
