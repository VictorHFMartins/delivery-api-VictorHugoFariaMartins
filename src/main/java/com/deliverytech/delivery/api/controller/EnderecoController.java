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
import com.deliverytech.delivery.domain.enums.TipoLogradouro;
import com.deliverytech.delivery.domain.services.EnderecoService;

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

@Tag(name = "Endereços", description = "Endpoints de gerenciamento de endereços de usuários")
@RestController
@RequestMapping("/enderecos")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class EnderecoController {

    private final EnderecoService enderecoService;

    @Operation(
            summary = "Cadastrar endereço",
            description = "Registra um endereço vinculado a um usuário (cliente ou restaurante)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso",
                content = @Content(schema = @Schema(implementation = EnderecoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PostMapping("/{usuarioId}")
    public ResponseEntity<EnderecoResponse> cadastrar(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long usuarioId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do endereço a ser cadastrado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EnderecoRequest.class))
            )
            @RequestBody @Valid EnderecoRequest dto) {

        EnderecoResponse endereco = enderecoService.criar(usuarioId, dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(endereco.id())
                .toUri();

        return ResponseEntity.created(location).body(endereco);
    }

    @Operation(
            summary = "Atualizar endereço",
            description = "Atualiza um endereço vinculado a um usuário."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário ou endereço não encontrado")
    })
    @PutMapping("/usuario/{usuarioId}/endereco/{enderecoId}")
    public ResponseEntity<EnderecoResponse> atualizar(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long usuarioId,
            @Parameter(description = "ID do endereço", example = "12")
            @PathVariable Long enderecoId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados do endereço",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EnderecoRequest.class))
            )
            @RequestBody @Valid EnderecoRequest dto) {

        EnderecoResponse endereco = enderecoService.atualizar(usuarioId, enderecoId, dto);
        return ResponseEntity.ok(endereco);
    }

    @Operation(
            summary = "Excluir endereço",
            description = "Remove um endereço pelo seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Endereço removido"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do endereço", example = "10")
            @PathVariable Long id) {

        enderecoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar todos os endereços",
            description = "Retorna todos os endereços cadastrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<EnderecoResponse>> listarTodos() {
        return ResponseEntity.ok(enderecoService.listarTodos());
    }

    @Operation(
            summary = "Buscar endereço por ID",
            description = "Retorna um endereço específico pelo seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Endereço encontrado"),
        @ApiResponse(responseCode = "404", description = "ID não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponse> buscarPorId(
            @Parameter(description = "ID do endereço", example = "8")
            @PathVariable Long id) {

        return ResponseEntity.ok(enderecoService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar endereço por logradouro e número",
            description = "Localiza um endereço combinando nome do logradouro + número."
    )
    @ApiResponse(responseCode = "200", description = "Endereço encontrado")
    @GetMapping("/logradouro")
    public ResponseEntity<EnderecoResponse> buscarPorNumeroELogradouro(
            @Parameter(description = "Nome do logradouro", example = "Rua das Flores")
            @RequestParam String logradouro,
            @Parameter(description = "Número da residência", example = "125")
            @RequestParam String numero) {

        EnderecoResponse endereco = enderecoService.buscarPorNumeroELogradouro(numero, logradouro);
        return ResponseEntity.ok(endereco);
    }

    @Operation(
            summary = "Buscar endereços por filtros",
            description = "Filtra endereços por bairro, CEP, logradouro ou tipo de logradouro."
    )
    @ApiResponse(responseCode = "200", description = "Endereços filtrados com sucesso")
    @GetMapping("/buscar")
    public ResponseEntity<List<EnderecoResponse>> buscarPorFiltro(
            @Parameter(description = "Nome do bairro", example = "Centro")
            @RequestParam(required = false) String bairro,
            @Parameter(description = "Código do CEP", example = "01311000")
            @RequestParam(required = false) String cepCodigo,
            @Parameter(description = "Nome do logradouro", example = "Avenida Paulista")
            @RequestParam(required = false) String logradouro,
            @Parameter(description = "Tipo do logradouro")
            @RequestParam(required = false) TipoLogradouro tipoLogradouro) {

        List<EnderecoResponse> enderecos
                = enderecoService.buscarPorFiltro(bairro, cepCodigo, logradouro, tipoLogradouro);

        return ResponseEntity.ok(enderecos);
    }
}
