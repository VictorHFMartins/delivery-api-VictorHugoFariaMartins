package com.deliverytech.delivery.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Níveis de avaliação do cliente")
public enum NotaAvaliacao {

    @Schema(description = "1 - Péssimo")
    PESSIMO,

    @Schema(description = "2 - Ruim")
    RUIM,

    @Schema(description = "3 - Regular")
    REGULAR,

    @Schema(description = "4 - Bom")
    BOM,

    @Schema(description = "5 - Ótimo")
    OTIMO
}
