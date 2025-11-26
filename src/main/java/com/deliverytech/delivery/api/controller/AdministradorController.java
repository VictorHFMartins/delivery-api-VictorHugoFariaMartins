package com.deliverytech.delivery.api.controller;

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

import com.deliverytech.delivery.api.dto.AdministradorRequest;
import com.deliverytech.delivery.api.dto.AdministradorResponse;
import com.deliverytech.delivery.domain.services.AdministradorService;

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

@Tag(name = "administradores", description = "Endpoints para gerenciamento de administradores")
@RestController
@RequestMapping("/administradores")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class AdministradorController {

    private final AdministradorService administradorService;

    @Operation(
            summary = "Cadastrar administrador",
            description = "Cria um novo administrador, incluindo endereço e telefones."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "administrador criado com sucesso",
                    content = @Content(schema = @Schema(implementation = AdministradorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PostMapping
    public ResponseEntity<AdministradorResponse> cadastrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados completos do administrador",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AdministradorRequest.class))
            )
            @RequestBody @Valid AdministradorRequest dto) {

        AdministradorResponse administrador = administradorService.criar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(administrador.id())
                .toUri();

        return ResponseEntity.created(location).body(administrador);
    }

    @Operation(
            summary = "Atualizar administrador",
            description = "Atualiza todos os dados do administrador, incluindo endereço e telefones."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "administrador atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "administrador não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AdministradorResponse> atualizar(
            @Parameter(description = "ID do administrador", example = "10")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados do administrador",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AdministradorRequest.class))
            )
            @RequestBody @Valid AdministradorRequest dto) {

        AdministradorResponse administrador = administradorService.atualizar(id, dto);
        return ResponseEntity.ok(administrador);
    }

    @Operation(
            summary = "Ativar ou inativar administrador",
            description = "Alterna o status do administrador entre ATIVO e INATIVO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "administrador não encontrado")
    })
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<AdministradorResponse> ativarDesativar(
            @Parameter(description = "ID do administrador", example = "12")
            @PathVariable Long id) {

        AdministradorResponse administrador = administradorService.ativarDesativar(id);
        return ResponseEntity.ok(administrador);
    }

    @Operation(
            summary = "Excluir administrador",
            description = "Remove o administrador definitivamente do sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "administrador excluído"),
            @ApiResponse(responseCode = "404", description = "administrador não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID do administrador", example = "15")
            @PathVariable Long id) {

        administradorService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar administrador por ID",
            description = "Retorna um administrador específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "administrador encontrado"),
            @ApiResponse(responseCode = "404", description = "administrador não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AdministradorResponse> buscarPorId(
            @Parameter(description = "ID do administrador", example = "7")
            @PathVariable Long id) {

        return ResponseEntity.ok(administradorService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar administradores ativos",
            description = "Retorna todos os administradores com status ATIVO."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/ativos")
    public ResponseEntity<List<AdministradorResponse>> listarAtivos() {
        return ResponseEntity.ok(administradorService.listarPorStatusAtivo());
    }

    @Operation(
            summary = "Listar todos os administradores",
            description = "Retorna todos os administradores cadastrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<AdministradorResponse>> listarTodos() {
        return ResponseEntity.ok(administradorService.listarTodos());
    }

    @Operation(
            summary = "Buscar administradores por filtros",
            description = """
                    Busca avançada utilizando filtros opcionais:
                    - Nome
                    - Email
                    - CEP
                    - Cidade
                    - Estado
                    - Telefone
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros de filtro inválidos")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<AdministradorResponse>> buscar(
            @Parameter(description = "Nome do administrador", example = "Ana")
            @RequestParam(required = false) String nome,

            @Parameter(description = "Email do administrador", example = "ana@gmail.com")
            @RequestParam(required = false) String email,

            @Parameter(description = "CEP do administrador", example = "01001000")
            @RequestParam(required = false) String cep,

            @Parameter(description = "Cidade do administrador", example = "São Paulo")
            @RequestParam(required = false) String cidade,

            @Parameter(description = "Estado (UF)", example = "SP")
            @RequestParam(required = false) String estado,

            @Parameter(description = "Telefone do administrador", example = "11988887777")
            @RequestParam(required = false) String telefone) {

        return ResponseEntity.ok(
                administradorService.buscarComFiltros(nome, email, cep, cidade, estado, telefone)
        );
    }
}
