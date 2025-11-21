package com.deliverytech.delivery.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.api.dto.PedidoRequest;
import com.deliverytech.delivery.api.dto.PedidoResponse;
import com.deliverytech.delivery.api.dto.PedidoUpdateRequest;
import com.deliverytech.delivery.domain.enums.StatusPedido;
import com.deliverytech.delivery.domain.services.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Pedidos", description = "Endpoints de gerenciamento de pedidos")
@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(
            summary = "Criar pedido",
            description = "Cria um novo pedido associado a um cliente e restaurante."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Cliente ou restaurante não encontrado")
    })
    @PostMapping
    public ResponseEntity<PedidoResponse> cadastrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados completos do pedido",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PedidoRequest.class))
            )
            @RequestBody @Valid PedidoRequest dto) {

        PedidoResponse pedido = pedidoService.criar(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedido.id())
                .toUri();

        return ResponseEntity.created(location).body(pedido);
    }

    @Operation(
            summary = "Atualizar pedido",
            description = "Atualiza todos os dados do pedido, incluindo itens e observações."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PutMapping("/{pedidoId}")
    public ResponseEntity<PedidoResponse> atualizar(
            @Parameter(description = "ID do pedido", example = "10")
            @PathVariable Long pedidoId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados do pedido",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PedidoUpdateRequest.class))
            )
            @RequestBody @Valid PedidoUpdateRequest dto) {

        PedidoResponse pedido = pedidoService.atualizar(pedidoId, dto);
        return ResponseEntity.ok(pedido);
    }

    @Operation(
            summary = "Alterar status do pedido",
            description = "Atualiza apenas o status do pedido."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PatchMapping("/{pedidoId}/toggle-status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @Parameter(description = "ID do pedido", example = "7")
            @PathVariable Long pedidoId,

            @Parameter(description = "Novo status", example = "CONFIRMADO")
            @RequestParam StatusPedido statusPedido) {

        PedidoResponse pedido = pedidoService.mudarStatusPedido(pedidoId, statusPedido);
        return ResponseEntity.ok(pedido);
    }

    @Operation(
            summary = "Cancelar pedido",
            description = "Altera o status do pedido para CANCELADO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @PatchMapping("/cancelar/{pedidoId}")
    public ResponseEntity<PedidoResponse> cancelarPedido(
            @Parameter(description = "ID do pedido", example = "11")
            @PathVariable Long pedidoId) {

        PedidoResponse pedido = pedidoService.cancelar(pedidoId);
        return ResponseEntity.ok(pedido);
    }

    @Operation(
            summary = "Deletar pedido",
            description = "Exclui um pedido definitivamente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido removido"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @DeleteMapping("/{pedidoId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do pedido", example = "14")
            @PathVariable Long pedidoId) {

        pedidoService.deletar(pedidoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar pedidos",
            description = "Retorna todos os pedidos cadastrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @Operation(
            summary = "Buscar pedido por ID",
            description = "Retorna um pedido específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    @GetMapping("/{pedidoId}")
    public ResponseEntity<PedidoResponse> buscarPorId(
            @Parameter(description = "ID do pedido", example = "5")
            @PathVariable Long pedidoId) {

        return ResponseEntity.ok(pedidoService.buscarPorId(pedidoId));
    }

    @Operation(
            summary = "Listar pedidos por cliente",
            description = "Retorna todos os pedidos de um cliente específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponse>> listarPorCliente(
            @Parameter(description = "ID do cliente", example = "3")
            @PathVariable Long clienteId) {

        return ResponseEntity.ok(pedidoService.listarPorCliente(clienteId));
    }

    @Operation(
            summary = "Listar pedidos por restaurante",
            description = "Retorna todos os pedidos destinados ao restaurante informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<PedidoResponse>> listarPorRestaurante(
            @Parameter(description = "ID do restaurante", example = "4")
            @PathVariable Long restauranteId) {

        return ResponseEntity.ok(pedidoService.listarPorRestaurante(restauranteId));
    }
}
