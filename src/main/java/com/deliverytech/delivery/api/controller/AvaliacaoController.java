package com.deliverytech.delivery.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.api.dto.AvaliacaoRequest;
import com.deliverytech.delivery.api.dto.AvaliacaoResponse;
import com.deliverytech.delivery.api.dto.RestauranteAvaliacoesResponse;
import com.deliverytech.delivery.api.dto.respostaAvaliacaoRequest;
import com.deliverytech.delivery.domain.services.AvaliacaoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurantes/{restauranteId}/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<AvaliacaoResponse> avaliar(
            @PathVariable Long restauranteId,
            @RequestBody @Valid AvaliacaoRequest dto) {

        AvaliacaoResponse avaliacao = avaliacaoService.criar(restauranteId, dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(avaliacao.id())
                .toUri();

        return ResponseEntity.created(location).body(avaliacao);
    }

    @PostMapping("/{avaliacaoId}/resposta")
    public ResponseEntity<AvaliacaoResponse> responder(
            @PathVariable Long avaliacaoId,
            @RequestBody @Valid respostaAvaliacaoRequest dto) {

        AvaliacaoResponse resposta = avaliacaoService.responder(avaliacaoId, dto.resposta());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resposta.id())
                .toUri();

        return ResponseEntity.created(location).body(resposta);
    }

    @PutMapping("/{avaliacaoId}")
    public ResponseEntity<AvaliacaoResponse> atualizar(
            @PathVariable Long restauranteId,
            @PathVariable Long avaliacaoId,
            @RequestBody @Valid AvaliacaoRequest dto) {

        AvaliacaoResponse avaliacao = avaliacaoService.editar(restauranteId, avaliacaoId, dto);
        return ResponseEntity.ok(avaliacao);
    }

    @DeleteMapping("/{avaliacaoId}")
    public ResponseEntity<Void> remover(@PathVariable Long avaliacaoId) {
        avaliacaoService.remover(avaliacaoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoResponse>> listar(@PathVariable Long restauranteId) {
        List<AvaliacaoResponse> avaliacoes = avaliacaoService.listarPorRestaurante(restauranteId);
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/ordenar/data")
    public ResponseEntity<List<AvaliacaoResponse>> ordenarPorData(@PathVariable Long restauranteId) {
        List<AvaliacaoResponse> avaliacoes = avaliacaoService.listarPorData(restauranteId);
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/ordenar/nota")
    public ResponseEntity<List<AvaliacaoResponse>> ordenarPorNota(@PathVariable Long restauranteId) {
        List<AvaliacaoResponse> avaliacoes = avaliacaoService.listarPorNota(restauranteId);
        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/media")
    public ResponseEntity<RestauranteAvaliacoesResponse> media(@PathVariable Long restauranteId) {
        RestauranteAvaliacoesResponse restaurante = avaliacaoService.media(restauranteId);
        return ResponseEntity.ok(restaurante);
    }
}
