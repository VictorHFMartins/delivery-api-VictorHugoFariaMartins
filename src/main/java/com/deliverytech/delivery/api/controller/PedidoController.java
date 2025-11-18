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
import com.deliverytech.delivery.domain.enums.StatusPedido;
import com.deliverytech.delivery.domain.services.PedidoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> cadastrar(@RequestBody @Valid PedidoRequest dto) {
        PedidoResponse pedido = pedidoService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedido.id())
                .toUri();

        return ResponseEntity.created(location).body(pedido);
    }

    @PutMapping("/{pedidoId}")
    public ResponseEntity<PedidoResponse> atualizar(@PathVariable Long pedidoId, @RequestBody @Valid PedidoRequest dto) {
        PedidoResponse pedido = pedidoService.atualizar(pedidoId, dto);
        return ResponseEntity.ok(pedido);
    }

    @PatchMapping("/{pedidoId}")
    public ResponseEntity<PedidoResponse> atualizarStatus(@PathVariable Long pedidoId, @RequestParam StatusPedido statusPedido) {
        PedidoResponse pedido = pedidoService.mudarStatusPedido(pedidoId, statusPedido);

        return ResponseEntity.ok(pedido);
    }

    @PatchMapping("/cancelar/{idPedido}")
    public ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable Long idPedido) {
        PedidoResponse pedido = pedidoService.cancelar(idPedido);

        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<Void> deletar(@PathVariable Long idPedido) {
        pedidoService.deletar(idPedido);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> buscarTodos() {
        List<PedidoResponse> pedidos = pedidoService.listarTodos();
        return ResponseEntity.ok(pedidos);

    }

    @GetMapping("/{pedidoId}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long pedidoId) {
        PedidoResponse pedido = pedidoService.buscarPorId(pedidoId);
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponse>> buscarPorCliente(@PathVariable Long clienteId) {
        List<PedidoResponse> pedidos = pedidoService.listarPorCliente(clienteId);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<PedidoResponse>> buscarPorRestaurante(@PathVariable Long restauranteId) {
        List<PedidoResponse> pedidos = pedidoService.listarPorRestaurante(restauranteId);
        return ResponseEntity.ok(pedidos);
    }
}
