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

@Tag(name = "CEPs", description = "Endpoints para gerenciamento de CEPs")
@RestController
@RequestMapping("/ceps")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class CepController {

    private final CepService cepService;

    @Operation(
            summary = "Cadastrar CEP",
            description = "Cria um novo CEP vinculado a uma cidade existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CEP criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CepResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação do payload")
    })
    @PostMapping
    public ResponseEntity<CepResponse> cadastrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do novo CEP",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CepRequest.class))
            )
            @RequestBody @Valid CepRequest dto) {

        CepResponse cep = cepService.criar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cep.id())
                .toUri();

        return ResponseEntity.created(location).body(cep);
    }

    @Operation(
            summary = "Atualizar CEP",
            description = "Atualiza os dados de um CEP já cadastrado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CEP atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "CEP não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CepResponse> atualizar(
            @Parameter(description = "ID do CEP que será atualizado", example = "15")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados do CEP",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CepRequest.class))
            )
            @RequestBody @Valid CepRequest dto) {

        CepResponse cep = cepService.alterar(id, dto);
        return ResponseEntity.ok(cep);
    }

    @Operation(
            summary = "Excluir CEP",
            description = "Remove definitivamente um CEP do sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "CEP removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "CEP não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do CEP", example = "22")
            @PathVariable Long id) {

        cepService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar todos os CEPs",
            description = "Retorna todos os CEPs cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<CepResponse>> listarTodos() {
        return ResponseEntity.ok(cepService.listarTodos());
    }

    @Operation(
            summary = "Buscar CEP por ID",
            description = "Retorna os dados de um CEP através do ID informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CEP encontrado"),
            @ApiResponse(responseCode = "404", description = "CEP não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CepResponse> buscarPorId(
            @Parameter(description = "ID do CEP", example = "7")
            @PathVariable Long id) {

        return ResponseEntity.ok(cepService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar CEP por código",
            description = "Retorna o CEP correspondente ao código informado (ex: 01001-000)."
    )
    @ApiResponse(responseCode = "200", description = "CEP encontrado")
    @GetMapping("/codigo")
    public ResponseEntity<CepResponse> buscarPorCodigo(
            @Parameter(description = "Código do CEP", example = "01001000")
            @RequestParam String num) {

        return ResponseEntity.ok(cepService.buscarPorCodigo(num));
    }

    @Operation(
            summary = "Listar CEPs por cidade",
            description = "Retorna todos os CEPs vinculados ao nome da cidade informada."
    )
    @ApiResponse(responseCode = "200", description = "CEPs listados com sucesso")
    @GetMapping("/cidade")
    public ResponseEntity<List<CepResponse>> buscarPorCidade(
            @Parameter(description = "Nome da cidade", example = "São Paulo")
            @RequestParam String nome) {

        return ResponseEntity.ok(cepService.listarPorCidadeNome(nome));
    }
}
