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

@Tag(name = "Cidades", description = "Endpoints para gerenciamento de cidades")
@RestController
@RequestMapping("/cidades")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CidadeController {

    private final CidadeService cidadeService;

    @Operation(
            summary = "Cadastrar cidade",
            description = "Cria uma nova cidade vinculada a um estado existente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cidade criada com sucesso",
                content = @Content(schema = @Schema(implementation = CidadeResponse.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PostMapping
    public ResponseEntity<CidadeResponse> cadastrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da cidade a ser cadastrada",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CidadeRequest.class))
            )
            @Valid @RequestBody CidadeRequest dto) {

        CidadeResponse cidade = cidadeService.criar(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cidade.id())
                .toUri();

        return ResponseEntity.created(location).body(cidade);
    }

    @Operation(
            summary = "Atualizar cidade",
            description = "Atualiza os dados de uma cidade existente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cidade atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cidade não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CidadeResponse> atualizar(
            @Parameter(description = "ID da cidade", example = "12")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados para atualizar a cidade",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CidadeRequest.class))
            )
            @Valid @RequestBody CidadeRequest dto) {

        CidadeResponse cidade = cidadeService.alterar(id, dto);
        return ResponseEntity.ok(cidade);
    }

    @Operation(
            summary = "Excluir cidade",
            description = "Remove uma cidade pelo seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cidade removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cidade não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da cidade", example = "5")
            @PathVariable Long id) {

        cidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar todas as cidades",
            description = "Retorna todas as cidades cadastradas."
    )
    @ApiResponse(responseCode = "200", description = "Lista gerada com sucesso")
    @GetMapping
    public ResponseEntity<List<CidadeResponse>> listarTodos() {
        return ResponseEntity.ok(cidadeService.listarTodos());
    }

    @Operation(
            summary = "Buscar cidade por ID",
            description = "Retorna os dados de uma cidade específica pelo seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cidade encontrada"),
        @ApiResponse(responseCode = "404", description = "Cidade não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CidadeResponse> buscarPorId(
            @Parameter(description = "ID da cidade", example = "3")
            @PathVariable Long id) {

        return ResponseEntity.ok(cidadeService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar cidades por nome",
            description = "Retorna todas as cidades cujo nome contenha parte do texto informado."
    )
    @ApiResponse(responseCode = "200", description = "Cidades filtradas com sucesso")
    @GetMapping("/nome")
    public ResponseEntity<List<CidadeResponse>> buscarPorNome(
            @Parameter(description = "Nome parcial ou completo da cidade", example = "São")
            @RequestParam String nome) {

        return ResponseEntity.ok(cidadeService.buscarPorNomeContendo(nome));
    }

    @Operation(
            summary = "Buscar cidades por UF",
            description = "Retorna todas as cidades pertencentes ao estado de uma UF específica."
    )
    @ApiResponse(responseCode = "200", description = "Cidades listadas com sucesso")
    @GetMapping("/estado")
    public ResponseEntity<List<CidadeResponse>> buscarPorEstadoUf(
            @Parameter(description = "UF do estado", example = "SP")
            @RequestParam String uf) {

        return ResponseEntity.ok(cidadeService.buscarCidadesPorEstadoUf(uf));
    }
}
