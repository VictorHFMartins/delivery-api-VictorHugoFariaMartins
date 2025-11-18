package com.deliverytech.delivery.api.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalTime;
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

import com.deliverytech.delivery.api.dto.RestauranteRequest;
import com.deliverytech.delivery.api.dto.RestauranteResponse;
import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.services.RestauranteService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/restaurantes")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class RestauranteController {

    private final RestauranteService restauranteService;

    @PostMapping
    public ResponseEntity<RestauranteResponse> cadastrar(@RequestBody @Valid RestauranteRequest dto) {
        RestauranteResponse restaurante = restauranteService.criar(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(restaurante.id())
                .toUri();

        return ResponseEntity.created(location).body(restaurante);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponse> atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteRequest dto) {
        RestauranteResponse restaurante = restauranteService.alterar(id, dto);
        return ResponseEntity.ok(restaurante);
    }

    @PatchMapping("/inativar/{id}")
    public ResponseEntity<RestauranteResponse> ativarDesativar(@PathVariable Long id) {
        RestauranteResponse restaurante = restauranteService.ativarInativar(id);
        return ResponseEntity.ok(restaurante);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        restauranteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RestauranteResponse>> listarTodos() {
        List<RestauranteResponse> restaurantes = restauranteService.listarTodos();
        return ResponseEntity.ok(restaurantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponse> buscarPorId(@PathVariable Long id) {
        RestauranteResponse restaurante = restauranteService.buscarPorId(id);
        return ResponseEntity.ok(restaurante);
    }

    @GetMapping("/cnpj")
    public ResponseEntity<RestauranteResponse> buscarPorCnpj(@RequestParam String cnpj) {
        RestauranteResponse restaurante = restauranteService.buscarPorCnpj(cnpj);
        return ResponseEntity.ok(restaurante);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RestauranteResponse>> buscarPorFiltro(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String numeroTelefone,
            @RequestParam(required = false) BigDecimal taxaEntrega,
            @RequestParam(required = false) String nomeRestaurante,
            @RequestParam(required = false) LocalTime horarioAbertura,
            @RequestParam(required = false) LocalTime horarioFechamento,
            @RequestParam(required = false) CategoriaRestaurante categoriaRestaurante
    ) {
        List<RestauranteResponse> restaurantes = restauranteService.buscarPorFiltro(email, numeroTelefone, taxaEntrega, nomeRestaurante, horarioAbertura, horarioFechamento, categoriaRestaurante);
        return ResponseEntity.ok(restaurantes);
    }
}
