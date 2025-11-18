package com.deliverytech.delivery.api.controller;

import java.math.BigDecimal;
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

import com.deliverytech.delivery.api.dto.ProdutoRequest;
import com.deliverytech.delivery.api.dto.ProdutoResponse;
import com.deliverytech.delivery.domain.enums.CategoriaProduto;
import com.deliverytech.delivery.domain.services.ProdutoService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping("/{restauranteId}")
    public ResponseEntity<ProdutoResponse> cadastrar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoRequest dto) {
        ProdutoResponse produto = produtoService.criar(restauranteId, dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produto.id())
                .toUri();
        return ResponseEntity.created(location).body(produto);
    }

    @PutMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long produtoId, @RequestBody @Valid ProdutoRequest dto) {
        ProdutoResponse produto = produtoService.atualizar(produtoId, dto);
        return ResponseEntity.ok(produto);
    }

    @PatchMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponse> ativarInativar(@PathVariable Long produtoId) {
        ProdutoResponse produto = produtoService.ativarInativar(produtoId);
        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Void> deletar(@PathVariable Long produtoId) {
        produtoService.deletar(produtoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> buscarTodos() {
        List<ProdutoResponse> produtos = produtoService.buscarTodos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarTodos(@PathVariable Long id) {
        ProdutoResponse produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponse>> buscarTodos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) BigDecimal preco,
            @RequestParam(required = false) CategoriaProduto categoriaProduto
    ) {
        List<ProdutoResponse> produtos = produtoService.buscarPorFiltro(nome, id, preco, categoriaProduto);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/restaurante")
    public ResponseEntity<List<ProdutoResponse>> buscarPorRestaurante(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Long id
    ) {
        List<ProdutoResponse> produtos = produtoService.buscarProdutosPorRestaurante(nome, id);
        return ResponseEntity.ok(produtos);
    }
}
