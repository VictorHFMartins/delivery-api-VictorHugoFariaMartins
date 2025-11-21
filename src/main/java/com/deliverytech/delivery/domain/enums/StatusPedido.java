package com.deliverytech.delivery.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status atual do pedido no fluxo de entrega")
public enum StatusPedido {

    @Schema(description = "Pedido foi criado e aguarda confirmação")
    PENDENTE,

    @Schema(description = "Restaurante confirmou o pedido")
    CONFIRMADO,

    @Schema(description = "Pedido está sendo preparado")
    PREPARO,

    @Schema(description = "Pedido saiu para entrega")
    DESPACHADO,

    @Schema(description = "Pedido foi entregue ao cliente")
    ENTREGUE,

    @Schema(description = "Pedido foi cancelado")
    CANCELADO
}
