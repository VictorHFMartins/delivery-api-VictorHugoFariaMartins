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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.api.dto.TelefoneRequest;
import com.deliverytech.delivery.api.dto.TelefoneResponse;
import com.deliverytech.delivery.api.dto.TelefoneUpdateRequest;
import com.deliverytech.delivery.domain.enums.TipoTelefone;
import com.deliverytech.delivery.domain.enums.TipoUsuario;
import com.deliverytech.delivery.domain.services.TelefoneService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Telefones", description = "Endpoints relacionados ao gerenciamento de telefones dos usuários")
@RestController
@RequestMapping("/telefones")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class TelefoneController {

    private final TelefoneService telefoneService;

    @Operation(
            summary = "Cadastrar telefone",
            description = "Cria e associa um telefone a um usuário (Cliente ou Restaurante).",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação de telefone",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TelefoneRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "ddd": "11",
                                              "numero": "987654321",
                                              "tipoTelefone": "CELULAR"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Telefone cadastrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados")
    })
    @PostMapping("/{usuarioId}")
    public ResponseEntity<TelefoneResponse> cadastrar(
            @Parameter(description = "ID do usuário ao qual o telefone será vinculado", example = "1")
            @PathVariable Long usuarioId,
            @Valid @RequestBody TelefoneRequest dto) {

        TelefoneResponse telefone = telefoneService.criar(usuarioId, dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(telefone.id())
                .toUri();

        return ResponseEntity.created(location).body(telefone);
    }

    @Operation(
            summary = "Atualizar telefone",
            description = "Atualiza os dados de um telefone cadastrado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos que podem ser alterados do telefone",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TelefoneUpdateRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "ddd": "11",
                                              "numero": "966554433",
                                              "tipoTelefone": "WHATSAPP"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Telefone atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Telefone ou usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PutMapping("/usuario/{usuarioId}/telefone/{telefoneId}")
    public ResponseEntity<TelefoneResponse> alterar(
            @Parameter(description = "ID do usuário dono do telefone", example = "2")
            @PathVariable Long usuarioId,
            @Parameter(description = "ID do telefone a ser atualizado", example = "10")
            @PathVariable Long telefoneId,
            @Valid @RequestBody TelefoneUpdateRequest telefoneUpdateDto) {

        TelefoneResponse telefone = telefoneService.atualizar(usuarioId, telefoneId, telefoneUpdateDto);
        return ResponseEntity.ok(telefone);
    }

    @Operation(
            summary = "Remover telefone",
            description = "Remove um telefone pertencente a um usuário."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Telefone removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Telefone não encontrado")
    })
    @DeleteMapping("/usuario/{usuarioId}/telefone/{telefoneId}")
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID do usuário dono do telefone", example = "5")
            @PathVariable Long usuarioId,
            @Parameter(description = "ID do telefone a ser deletado", example = "17")
            @PathVariable Long telefoneId) {

        telefoneService.remover(usuarioId, telefoneId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar telefones",
            description = "Retorna todos os telefones cadastrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<TelefoneResponse>> listarTodos() {
        return ResponseEntity.ok(telefoneService.listarTodos());
    }

    @Operation(
            summary = "Buscar telefones por filtros",
            description = """
                    Permite filtrar telefones pelos campos:
                    - ddd
                    - usuarioId
                    - numero
                    - tipoUsuario (CLIENTE / RESTAURANTE)
                    - tipoTelefone (FIXO / CELULAR / WHATSAPP)
                    """
    )
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @GetMapping("/buscar")
    public ResponseEntity<List<TelefoneResponse>> buscarPorFiltro(
            @Parameter(description = "DDD do telefone", example = "11")
            @RequestParam(required = false) String ddd,
            @Parameter(description = "ID do usuário dono do telefone", example = "3")
            @RequestParam(required = false) Long usuarioId,
            @Parameter(description = "Número do telefone (somente números)", example = "999887766")
            @RequestParam(required = false) String numero,
            @Parameter(description = "Tipo do usuário dono do telefone", example = "CLIENTE")
            @RequestParam(required = false) TipoUsuario tipoUsuario,
            @Parameter(description = "Tipo de telefone", example = "WHATSAPP")
            @RequestParam(required = false) TipoTelefone tipoTelefone
    ) {

        return ResponseEntity.ok(
                telefoneService.buscarPorFiltro(ddd, usuarioId, numero, tipoUsuario, tipoTelefone)
        );
    }
}
