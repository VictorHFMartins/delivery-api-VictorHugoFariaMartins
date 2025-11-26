package com.deliverytech.delivery.api.controller;

import java.math.BigDecimal;
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

import com.deliverytech.delivery.api.dto.ProdutoRequest;
import com.deliverytech.delivery.api.dto.ProdutoResponse;
import com.deliverytech.delivery.domain.enums.CategoriaProduto;
import com.deliverytech.delivery.domain.services.ProdutoService;

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

@Tag(name = "Produtos", description = "Endpoints de gerenciamento de produtos dos restaurantes")
@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Operation(
            summary = "Cadastrar produto",
            description = "Cria um produto vinculado a um restaurante"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                content = @Content(schema = @Schema(implementation = ProdutoResponse.class))),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PostMapping("/{restauranteId}")
    public ResponseEntity<ProdutoResponse> cadastrar(
            @Parameter(description = "ID do restaurante", example = "5")
            @PathVariable Long restauranteId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados para criação do produto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProdutoRequest.class))
            )
            @RequestBody @Valid ProdutoRequest dto) {

        ProdutoResponse produto = produtoService.criar(restauranteId, dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produto.id())
                .toUri();

        return ResponseEntity.created(location).body(produto);
    }

    @Operation(
            summary = "Atualizar produto",
            description = "Atualiza os dados de um produto existente"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PutMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @Parameter(description = "ID do produto", example = "12")
            @PathVariable Long produtoId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados do produto",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProdutoRequest.class))
            )
            @RequestBody @Valid ProdutoRequest dto) {

        ProdutoResponse produto = produtoService.atualizar(produtoId, dto);
        return ResponseEntity.ok(produto);
    }

    @Operation(
            summary = "Ativar / Inativar produto",
            description = "Alterna o status do produto entre ativo e inativo"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @PatchMapping("/{produtoId}/toggle-status")
    public ResponseEntity<ProdutoResponse> ativarDesativar(
            @Parameter(description = "ID do produto", example = "22")
            @PathVariable Long produtoId) {

        ProdutoResponse produto = produtoService.ativarInativar(produtoId);
        return ResponseEntity.ok(produto);
    }

    @Operation(
            summary = "Excluir produto",
            description = "Remove um produto do sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Produto removido"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do produto", example = "8")
            @PathVariable Long produtoId) {

        produtoService.deletar(produtoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar todos os produtos disponíveis",
            description = "Retorna todos os produtos disponíveis cadastrados"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista retornada com sucesso"
    )
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listarTodosDisponiveis() {
        return ResponseEntity.ok(produtoService.buscarTodosDisponiveis());
    }

    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna um produto pelo seu identificador"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping("/{produtoId}")
    public ResponseEntity<ProdutoResponse> buscarPorId(
            @Parameter(description = "ID do produto", example = "30")
            @PathVariable Long produtoId) {

        return ResponseEntity.ok(produtoService.buscarPorId(produtoId));
    }

    @Operation(
            summary = "Buscar produtos por filtros",
            description = "Filtra produtos por nome, quantidade, preço e categoria"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista filtrada retornada com sucesso"
    )
    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponse>> buscarPorFiltro(
            @Parameter(description = "Nome do produto", example = "Pizza")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Quantidade maior ou igual", example = "10")
            @RequestParam(required = false) Long quantidade,
            @Parameter(description = "Preço menor ou igual", example = "29.90")
            @RequestParam(required = false) BigDecimal preco,
            @Parameter(description = "Categoria do produto", example = "BEBIDAS")
            @RequestParam(required = false) CategoriaProduto categoria) {

        List<ProdutoResponse> produtos
                = produtoService.buscarPorFiltro(nome, quantidade, preco, categoria);

        return ResponseEntity.ok(produtos);
    }

    @Operation(
            summary = "Listar produtos de um restaurante",
            description = "Retorna todos os produtos cadastrados em um restaurante"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/restaurantes/{restauranteId}")
    public ResponseEntity<List<ProdutoResponse>> buscarPorRestaurante(
            @Parameter(description = "ID do restaurante", example = "15")
            @PathVariable Long restauranteId) {

        return ResponseEntity.ok(produtoService.buscarProdutosPorRestaurante(restauranteId));
    }
}
