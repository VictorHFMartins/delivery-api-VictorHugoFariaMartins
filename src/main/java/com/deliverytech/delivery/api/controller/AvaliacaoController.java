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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Avaliações", description = "Endpoints para avaliações de restaurantes")
@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurantes/{restauranteId}/avaliacoes")
@SecurityRequirement(name = "bearer-key")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @Operation(
            summary = "Registrar avaliação",
            description = "Cria uma nova avaliação para o restaurante informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Avaliação registrada com sucesso",
                    content = @Content(schema = @Schema(implementation = AvaliacaoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PostMapping
    public ResponseEntity<AvaliacaoResponse> avaliar(
            @Parameter(description = "ID do restaurante avaliado", example = "12")
            @PathVariable Long restauranteId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da avaliação",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AvaliacaoRequest.class))
            )
            @RequestBody @Valid AvaliacaoRequest dto) {

        AvaliacaoResponse avaliacao = avaliacaoService.criar(restauranteId, dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(avaliacao.id())
                .toUri();

        return ResponseEntity.created(location).body(avaliacao);
    }

    @Operation(
            summary = "Responder avaliação",
            description = "Permite ao restaurante responder uma avaliação existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Resposta registrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    @PostMapping("/{avaliacaoId}/resposta")
    public ResponseEntity<AvaliacaoResponse> responder(
            @Parameter(description = "ID da avaliação", example = "44")
            @PathVariable Long avaliacaoId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Conteúdo da resposta",
                    required = true,
                    content = @Content(schema = @Schema(implementation = respostaAvaliacaoRequest.class))
            )
            @RequestBody @Valid respostaAvaliacaoRequest dto) {

        AvaliacaoResponse resposta = avaliacaoService.responder(avaliacaoId, dto.resposta());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resposta.id())
                .toUri();

        return ResponseEntity.created(location).body(resposta);
    }

    @Operation(
            summary = "Atualizar avaliação",
            description = "Edita uma avaliação existente vinculada ao restaurante."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada"),
            @ApiResponse(responseCode = "404", description = "Avaliação ou restaurante não encontrado")
    })
    @PutMapping("/{avaliacaoId}")
    public ResponseEntity<AvaliacaoResponse> atualizar(
            @Parameter(description = "ID do restaurante", example = "12")
            @PathVariable Long restauranteId,

            @Parameter(description = "ID da avaliação", example = "33")
            @PathVariable Long avaliacaoId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados da avaliação",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AvaliacaoRequest.class))
            )
            @RequestBody @Valid AvaliacaoRequest dto) {

        AvaliacaoResponse avaliacao = avaliacaoService.editar(restauranteId, avaliacaoId, dto);
        return ResponseEntity.ok(avaliacao);
    }

    @Operation(
            summary = "Excluir avaliação",
            description = "Remove permanentemente uma avaliação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Avaliação removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada")
    })
    @DeleteMapping("/{avaliacaoId}")
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID da avaliação", example = "50")
            @PathVariable Long avaliacaoId) {

        avaliacaoService.remover(avaliacaoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar avaliações do restaurante",
            description = "Retorna todas as avaliações registradas para o restaurante."
    )
    @ApiResponse(responseCode = "200", description = "Avaliações retornadas")
    @GetMapping
    public ResponseEntity<List<AvaliacaoResponse>> listar(
            @Parameter(description = "ID do restaurante", example = "12")
            @PathVariable Long restauranteId) {

        return ResponseEntity.ok(avaliacaoService.listarPorRestaurante(restauranteId));
    }

    @Operation(
            summary = "Ordenar avaliações por data",
            description = "Retorna as avaliações ordenadas da mais recente para a mais antiga."
    )
    @ApiResponse(responseCode = "200", description = "Avaliações ordenadas por data")
    @GetMapping("/ordenar/data")
    public ResponseEntity<List<AvaliacaoResponse>> ordenarPorData(
            @Parameter(description = "ID do restaurante", example = "12")
            @PathVariable Long restauranteId) {

        return ResponseEntity.ok(avaliacaoService.listarPorData(restauranteId));
    }

    @Operation(
            summary = "Ordenar avaliações por nota",
            description = "Retorna as avaliações ordenadas pela nota (da maior para a menor)."
    )
    @ApiResponse(responseCode = "200", description = "Avaliações ordenadas por nota")
    @GetMapping("/ordenar/nota")
    public ResponseEntity<List<AvaliacaoResponse>> ordenarPorNota(
            @Parameter(description = "ID do restaurante", example = "12")
            @PathVariable Long restauranteId) {

        return ResponseEntity.ok(avaliacaoService.listarPorNota(restauranteId));
    }

    @Operation(
            summary = "Obter média geral do restaurante",
            description = "Retorna a média das notas e a quantidade total de avaliações."
    )
    @ApiResponse(responseCode = "200", description = "Média retornada")
    @GetMapping("/media")
    public ResponseEntity<RestauranteAvaliacoesResponse> media(
            @Parameter(description = "ID do restaurante", example = "12")
            @PathVariable Long restauranteId) {

        return ResponseEntity.ok(avaliacaoService.media(restauranteId));
    }
}
