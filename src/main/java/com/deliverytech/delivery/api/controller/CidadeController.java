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

import com.deliverytech.delivery.api.dto.CidadeRequest;
import com.deliverytech.delivery.api.dto.CidadeResponse;
import com.deliverytech.delivery.domain.services.CidadeService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/cidades")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class CidadeController {

    private final CidadeService cidadeService;

    @PostMapping
    public ResponseEntity<CidadeResponse> cadastrar(@RequestBody @Valid CidadeRequest dto) {
        CidadeResponse cidade = cidadeService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cidade.id())
                .toUri();

        return ResponseEntity.created(location).body(cidade);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CidadeResponse> atualizar(@PathVariable Long id, @RequestBody @Valid CidadeRequest dto) {
        CidadeResponse cidade = cidadeService.alterar(id, dto);
        return ResponseEntity.ok(cidade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CidadeResponse>> listarTodos() {
        List<CidadeResponse> cidades = cidadeService.listarTodos();
        return ResponseEntity.ok(cidades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CidadeResponse> buscarPorId(@PathVariable Long id) {
        CidadeResponse cidade = cidadeService.buscarPorId(id);
        return ResponseEntity.ok(cidade);
    }

    @GetMapping("/nome")
    public ResponseEntity<List<CidadeResponse>> buscarPorNome(@RequestParam String nome) {
        List<CidadeResponse> cidades = cidadeService.buscarPorNomeContendo(nome);
        return ResponseEntity.ok(cidades);
    }

    @GetMapping("/estado")
    public ResponseEntity<List<CidadeResponse>> buscarPorEstadoUf(@RequestParam String uf) {
        List<CidadeResponse> cidades = cidadeService.buscarCidadesPorEstadoUf(uf);
        return ResponseEntity.ok(cidades);
    }
}
