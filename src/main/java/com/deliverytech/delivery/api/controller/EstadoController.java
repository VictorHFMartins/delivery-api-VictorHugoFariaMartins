package com.deliverytech.delivery.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.api.dto.EstadoRequest;
import com.deliverytech.delivery.api.dto.EstadoResponse;
import com.deliverytech.delivery.domain.services.EstadoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/estados")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class EstadoController {

    private final EstadoService estadoService;

    @PostMapping
    public ResponseEntity<EstadoResponse> cadastrar(@RequestBody @Valid EstadoRequest dto) {
        EstadoResponse estado = estadoService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(estado.id())
                .toUri();
        return ResponseEntity.created(location).body(estado);
    }

    @PutMapping
    public ResponseEntity<EstadoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid EstadoRequest dto) {
        EstadoResponse estado = estadoService.alterar(id, dto);
        return ResponseEntity.ok(estado);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        estadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EstadoResponse>> listarTodos() {
        return ResponseEntity.ok(estadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponse> buscarPorId(@PathVariable Long id) {
        EstadoResponse estado = estadoService.buscarPorId(id);
        return ResponseEntity.ok(estado);
    }

    @GetMapping("/estado")
    public ResponseEntity<EstadoResponse> buscarPorUf(@RequestParam String uf) {
        EstadoResponse estado = estadoService.buscarPorUf(uf);
        return ResponseEntity.ok(estado);
    }
}
