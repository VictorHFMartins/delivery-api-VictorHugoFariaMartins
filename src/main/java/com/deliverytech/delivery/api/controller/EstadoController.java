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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Estados", description = "Endpoints para gerenciamento de estados")
@RestController
@RequestMapping("/estados")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class EstadoController {

    private final EstadoService estadoService;

    @Operation(
            summary = "Cadastrar Estado",
            description = "Cria um novo estado com nome e UF."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Estado criado com sucesso",
                    content = @Content(schema = @Schema(implementation = EstadoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PostMapping
    public ResponseEntity<EstadoResponse> cadastrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do estado a ser cadastrado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EstadoRequest.class))
            )
            @RequestBody @Valid EstadoRequest dto) {

        EstadoResponse estado = estadoService.criar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(estado.id())
                .toUri();

        return ResponseEntity.created(location).body(estado);
    }

    @Operation(
            summary = "Atualizar Estado",
            description = "Atualiza os dados de um estado existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EstadoResponse> atualizar(
            @Parameter(description = "ID do estado", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados do estado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EstadoRequest.class))
            )
            @RequestBody @Valid EstadoRequest dto) {

        EstadoResponse estado = estadoService.alterar(id, dto);
        return ResponseEntity.ok(estado);
    }

    @Operation(
            summary = "Excluir Estado",
            description = "Remove um estado existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do estado", example = "1")
            @PathVariable Long id) {

        estadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar Estados",
            description = "Retorna todos os estados cadastrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<EstadoResponse>> listarTodos() {
        return ResponseEntity.ok(estadoService.listarTodos());
    }

    @Operation(
            summary = "Buscar Estado por ID",
            description = "Retorna os dados de um estado pelo seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado encontrado"),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstadoResponse> buscarPorId(
            @Parameter(description = "ID do estado", example = "2")
            @PathVariable Long id) {

        return ResponseEntity.ok(estadoService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar Estado por UF",
            description = "Busca um estado pelo seu código UF (ex: SP, RJ, MG)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado encontrado"),
            @ApiResponse(responseCode = "404", description = "UF não encontrada")
    })
    @GetMapping("/buscar")
    public ResponseEntity<EstadoResponse> buscarPorUf(
            @Parameter(description = "UF do estado", example = "SP")
            @RequestParam String uf) {

        return ResponseEntity.ok(estadoService.buscarPorUf(uf));
    }
}
