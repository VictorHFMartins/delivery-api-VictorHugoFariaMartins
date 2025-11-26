package com.deliverytech.delivery.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de usuários cadastrados no sistema")
public enum TipoUsuario {
    CLIENTE,
    RESTAURANTE,
    ADMINISTRADOR 
}
