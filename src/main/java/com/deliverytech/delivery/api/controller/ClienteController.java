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

import com.deliverytech.delivery.api.dto.ClienteRequest;
import com.deliverytech.delivery.api.dto.ClienteResponse;
import com.deliverytech.delivery.domain.services.ClienteService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping()
    public ResponseEntity<ClienteResponse> cadastrar(@RequestBody @Valid ClienteRequest dto) {
        ClienteResponse cliente = clienteService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.id())
                .toUri();

        return ResponseEntity.created(location).body(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequest dto) {
        ClienteResponse cliente = clienteService.atualizar(id, dto);
        return ResponseEntity.ok(cliente);
    }

    @PatchMapping("/inativar/{id}")
    public ResponseEntity<ClienteResponse> ativarDesativar(@PathVariable Long id) {
        ClienteResponse cliente = clienteService.ativarDesativar(id);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        ClienteResponse cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ClienteResponse>> listarAtivos() {
        List<ClienteResponse> clientes = clienteService.listarPorStatusAtivo();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResponse>> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cep,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String telefone) {

        List<ClienteResponse> clientes = clienteService.buscarComFiltros(nome, email, cep, cidade, estado, telefone);
        return ResponseEntity.ok(clientes);
    }
}
