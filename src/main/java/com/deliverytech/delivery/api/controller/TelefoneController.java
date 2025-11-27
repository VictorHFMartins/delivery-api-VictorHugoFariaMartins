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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Telefones", description = "Endpoints relacionados ao gerenciamento de telefones dos usuários")
@RestController
@RequestMapping("/telefones")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class TelefoneController {

    private final TelefoneService telefoneService;

    @Operation(
        summary = "Cadastrar telefone",
        description = "Cria e associa um telefone a um usuário (Cliente, Restaurante ou Administrador).",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para criação de telefone",
            required = true,
            content = @Content(
                schema = @Schema(implementation = TelefoneRequest.class),
                examples = @ExampleObject("""
                        {
                          "ddd": "11",
                          "numero": "987654321",
                          "tipoTelefone": "CELULAR"
                        }
                        """)
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Telefone criado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PostMapping("/{usuarioId}")
    public ResponseEntity<TelefoneResponse> cadastrar(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long usuarioId,

            @Valid
            @org.springframework.web.bind.annotation.RequestBody
            TelefoneRequest dto) {

        TelefoneResponse telefone = telefoneService.criar(usuarioId, dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(telefone.id())
                .toUri();

        return ResponseEntity.created(location).body(telefone);
    }


    @Operation(
        summary = "Atualizar telefone",
        description = "Modifica as informações de um telefone existente.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados para atualização do telefone",
            required = true,
            content = @Content(
                schema = @Schema(implementation = TelefoneUpdateRequest.class),
                examples = @ExampleObject("""
                        {
                          "ddd": "11",
                          "numero": "999887766",
                          "tipoTelefone": "WHATSAPP"
                        }
                        """)
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Telefone atualizado"),
        @ApiResponse(responseCode = "404", description = "Telefone ou usuário não encontrado")
    })
    @PutMapping("/usuario/{usuarioId}/telefone/{telefoneId}")
    public ResponseEntity<TelefoneResponse> alterar(
            @Parameter(description = "ID do usuário", example = "2")
            @PathVariable Long usuarioId,

            @Parameter(description = "ID do telefone", example = "10")
            @PathVariable Long telefoneId,

            @Valid
            @org.springframework.web.bind.annotation.RequestBody
            TelefoneUpdateRequest dto) {

        return ResponseEntity.ok(telefoneService.atualizar(usuarioId, telefoneId, dto));
    }


    @Operation(
        summary = "Remover telefone",
        description = "Exclui permanentemente um telefone de um usuário."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Telefone removido"),
        @ApiResponse(responseCode = "404", description = "Telefone não encontrado")
    })
    @DeleteMapping("/usuario/{usuarioId}/telefone/{telefoneId}")
    public ResponseEntity<Void> remover(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long usuarioId,

            @Parameter(description = "ID do telefone", example = "15")
            @PathVariable Long telefoneId) {

        telefoneService.remover(usuarioId, telefoneId);
        return ResponseEntity.noContent().build();
    }


    @Operation(
        summary = "Listar todos os telefones",
        description = "Retorna todos os telefones cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    @GetMapping
    public ResponseEntity<List<TelefoneResponse>> listarTodos() {
        return ResponseEntity.ok(telefoneService.listarTodos());
    }


    @Operation(
        summary = "Buscar telefones usando filtros avançados",
        description = """
                Filtra telefones pelos campos:
                - DDD  
                - Usuário  
                - Número  
                - Tipo de Usuário (CLIENTE, RESTAURANTE, ADMINISTRADOR)  
                - Tipo de Telefone (FIXO, CELULAR, WHATSAPP)
                """
    )
    @ApiResponse(responseCode = "200", description = "Resultado filtrado")
    @GetMapping("/buscar")
    public ResponseEntity<List<TelefoneResponse>> buscarPorFiltro(
            @Parameter(description = "DDD", example = "11")
            @RequestParam(required = false) String ddd,

            @Parameter(description = "ID do usuário", example = "3")
            @RequestParam(required = false) Long usuarioId,

            @Parameter(description = "Número do telefone", example = "999887766")
            @RequestParam(required = false) String numero,

            @Parameter(description = "Tipo de usuário", example = "CLIENTE")
            @RequestParam(required = false) TipoUsuario tipoUsuario,

            @Parameter(description = "Tipo de telefone", example = "CELULAR")
            @RequestParam(required = false) TipoTelefone tipoTelefone) {

        return ResponseEntity.ok(
                telefoneService.buscarPorFiltro(ddd, usuarioId, numero, tipoUsuario, tipoTelefone)
        );
    }
}
