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

import com.deliverytech.delivery.api.dto.EnderecoRequest;
import com.deliverytech.delivery.api.dto.EnderecoResponse;
import com.deliverytech.delivery.api.dto.EnderecoUpdateRequest;
import com.deliverytech.delivery.domain.enums.TipoLogradouro;
import com.deliverytech.delivery.domain.services.EnderecoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/enderecos")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<EnderecoResponse> cadastrar(@PathVariable Long usuarioId, @RequestBody @Valid EnderecoRequest dto) {
        EnderecoResponse endereco = enderecoService.criar(usuarioId, dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(endereco.id())
                .toUri();

        return ResponseEntity.created(location).body(endereco);
    }

    @PutMapping("/usuario/{usuarioId}/endereco/{enderecoId}")
    public ResponseEntity<EnderecoResponse> atualizar(
            @PathVariable Long usuarioId,
            @PathVariable Long enderecoId,
            @RequestBody @Valid EnderecoUpdateRequest enderecoUpdateDto) {

        EnderecoResponse endereco = enderecoService.atualizar(usuarioId, enderecoId, enderecoUpdateDto);

        return ResponseEntity.ok(endereco);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        enderecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EnderecoResponse>> listarTodos() {
        List<EnderecoResponse> enderecos = enderecoService.listarTodos();
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponse> buscarPorId(@PathVariable Long id) {
        EnderecoResponse endereco = enderecoService.buscarPorId(id);
        return ResponseEntity.ok(endereco);
    }

    @GetMapping("/logradouro")
    public ResponseEntity<EnderecoResponse> buscarPorNumeroELogradouro(@RequestParam String nome, @RequestParam String num) {
        EnderecoResponse endereco = enderecoService.buscarPorNumeroELogradouro(num, nome);
        return ResponseEntity.ok(endereco);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EnderecoResponse>> buscarPorFiltro(
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String cepCodigo,
            @RequestParam(required = false) String logradouro,
            @RequestParam(required = false) TipoLogradouro tipoLogradouro) {

        List<EnderecoResponse> enderecos = enderecoService.buscarPorFiltro(bairro, cepCodigo, logradouro, tipoLogradouro);
        return ResponseEntity.ok(enderecos);
    }
}
