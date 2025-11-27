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

import com.deliverytech.delivery.api.dto.ClienteRankingResponse;
import com.deliverytech.delivery.api.dto.ClienteRequest;
import com.deliverytech.delivery.api.dto.ClienteResponse;
import com.deliverytech.delivery.domain.services.ClienteService;

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

@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes")
@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(
            summary = "Cadastrar cliente",
            description = "Cria um novo cliente, incluindo endereço e telefones."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                content = @Content(schema = @Schema(implementation = ClienteResponse.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados completos do cliente",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequest.class))
            )
            @Valid @RequestBody ClienteRequest dto) {

        ClienteResponse cliente = clienteService.criar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.id())
                .toUri();

        return ResponseEntity.created(location).body(cliente);
    }

    @Operation(
            summary = "Atualizar cliente",
            description = "Atualiza todos os dados do cliente, incluindo endereço e telefones."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(
            @Parameter(description = "ID do cliente", example = "10")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados do cliente",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteRequest.class))
            )
            @Valid @RequestBody ClienteRequest dto) {

        ClienteResponse cliente = clienteService.atualizar(id, dto);
        return ResponseEntity.ok(cliente);
    }

    @Operation(
            summary = "Ativar ou inativar cliente",
            description = "Alterna o status do cliente entre ATIVO e INATIVO."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ClienteResponse> ativarDesativar(
            @Parameter(description = "ID do cliente", example = "12")
            @PathVariable Long id) {

        ClienteResponse cliente = clienteService.ativarDesativar(id);
        return ResponseEntity.ok(cliente);
    }

    @Operation(
            summary = "Excluir cliente",
            description = "Remove o cliente definitivamente do sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cliente excluído"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID do cliente", example = "15")
            @PathVariable Long id) {

        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar cliente por ID",
            description = "Retorna um cliente específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(
            @Parameter(description = "ID do cliente", example = "7")
            @PathVariable Long id) {

        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar clientes ativos",
            description = "Retorna todos os clientes com status ATIVO."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/ativos")
    public ResponseEntity<List<ClienteResponse>> listarAtivos() {
        return ResponseEntity.ok(clienteService.listarPorStatusAtivo());
    }

    @Operation(
            summary = "Listar ranking de clientes por número de pedidos",
            description = "Retorna lista com clientes organizados por numeros de pedidos."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/pedidos/ranking")
    public List<ClienteRankingResponse> rankingClientes() {
        return clienteService.rankingClientes();
    }

    @Operation(
            summary = "Listar todos os clientes",
            description = "Retorna todos os clientes cadastrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @Operation(
            summary = "Buscar clientes por filtros",
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
    public ResponseEntity<List<ClienteResponse>> buscar(
            @Parameter(description = "Nome do cliente", example = "Ana")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Email do cliente", example = "ana@gmail.com")
            @RequestParam(required = false) String email,
            @Parameter(description = "CEP do cliente", example = "01001000")
            @RequestParam(required = false) String cep,
            @Parameter(description = "Cidade do cliente", example = "São Paulo")
            @RequestParam(required = false) String cidade,
            @Parameter(description = "Estado (UF)", example = "SP")
            @RequestParam(required = false) String estado,
            @Parameter(description = "Telefone do cliente", example = "11988887777")
            @RequestParam(required = false) String telefone) {

        return ResponseEntity.ok(
                clienteService.buscarComFiltros(nome, email, cep, cidade, estado, telefone)
        );
    }
}
