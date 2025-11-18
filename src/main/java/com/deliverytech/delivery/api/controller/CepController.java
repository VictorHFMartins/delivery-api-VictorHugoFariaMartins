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

import com.deliverytech.delivery.api.dto.CepRequest;
import com.deliverytech.delivery.api.dto.CepResponse;
import com.deliverytech.delivery.domain.services.CepService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/ceps")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class CepController {

    private final CepService cepService;

    @PostMapping
    public ResponseEntity<CepResponse> cadastrar(@RequestBody @Valid CepRequest dto) {
        CepResponse cep = cepService.criar(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cep.id())
                .toUri();

        return ResponseEntity.created(location).body(cep);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CepResponse> atualizar(@PathVariable Long id, @RequestBody @Valid CepRequest dto) {
        CepResponse cep = cepService.alterar(id, dto);
        return ResponseEntity.ok(cep);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cepService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CepResponse>> listarTodos() {
        List<CepResponse> ceps = cepService.listarTodos();
        return ResponseEntity.ok(ceps);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CepResponse> buscarPorId(@PathVariable Long id) {
        CepResponse cep = cepService.buscarPorId(id);
        return ResponseEntity.ok(cep);
    }

    @GetMapping("/codigo")
    public ResponseEntity<CepResponse> buscarPorCodigo(@RequestParam String num) {
        CepResponse cep = cepService.buscarPorCodigo(num);
        return ResponseEntity.ok(cep);
    }

    @GetMapping("/cidade")
    public ResponseEntity<List<CepResponse>> buscarPorCidade(@RequestParam String nome) {
        List<CepResponse> ceps = cepService.listarPorCidadeNome(nome);
        return ResponseEntity.ok(ceps);
    }
}
