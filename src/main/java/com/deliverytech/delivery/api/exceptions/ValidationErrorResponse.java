package com.deliverytech.delivery.api.exceptions;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ValidationErrorResponse",
        description = "Modelo padrão de retorno para erros da API")
public class ValidationErrorResponse {

    @Schema(description = "Código HTTP do erro", example = "400")
    private int status;

    @Schema(description = "Tipo do erro", example = "Dados inválidos")
    private String error;

    @Schema(description = "Mensagem detalhada ou mapa de erros de validação",
            example = "{nome: 'O nome é obrigatório'}")
    private String message;

    @Schema(description = "Momento em que o erro ocorreu")
    private LocalDateTime timestamp;

    public ValidationErrorResponse(int status, String error, String message, LocalDateTime timestamp) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
