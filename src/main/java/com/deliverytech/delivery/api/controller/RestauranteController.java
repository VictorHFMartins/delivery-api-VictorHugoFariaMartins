package com.deliverytech.delivery.api.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery.api.dto.RestauranteRequest;
import com.deliverytech.delivery.api.dto.RestauranteResponse;
import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.services.RestauranteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Tag(name = "Restaurantes", description = "Endpoints relacionados ao gerenciamento de restaurantes")
@RestController
@RequestMapping("/restaurantes")
@CrossOrigin(origins = "*")
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class RestauranteController {

    private final RestauranteService restauranteService;

    @Operation(
            summary = "Cadastrar restaurante",
            description = "Cria um novo restaurante com endereço, telefones, horários e categoria.",
            requestBody = @RequestBody(
                    required = true,
                    description = "Dados necessários para cadastrar um restaurante.",
                    content = @Content(
                            schema = @Schema(implementation = RestauranteRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "nome": "Restaurante Sabor Brasileiro",
                                              "email": "contato@saborbr.com",
                                              "telefones": [
                                                {
                                                  "ddd": "11",
                                                  "numero": "945778899",
                                                  "tipoTelefone": "WHATSAPP"
                                                }
                                              ],
                                              "endereco": {
                                                "logradouro": "Rua das Palmeiras",
                                                "tipoLogradouro": "RUA",
                                                "numero": "120",
                                                "complemento": "Loja 02",
                                                "bairro": "Centro",
                                                "cepCodigo": "01001000",
                                                "cidadeId": 1,
                                                "longitude": -46.6333,
                                                "latitude": -23.5503
                                              },
                                              "cnpj": "11222333000144",
                                              "classe": "BRASILEIRO",
                                              "estado": "ABERTO",
                                              "horarioAbertura": "09:00",
                                              "horarioFechamento": "23:00",
                                              "taxaEntrega": 6.50
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "409", description = "Restaurante com esse CNPJ já existe")
    })
    @PostMapping
    public ResponseEntity<RestauranteResponse> cadastrar(
            @Valid @RequestBody RestauranteRequest dto) {

        RestauranteResponse restaurante = restauranteService.criar(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(restaurante.id())
                .toUri();

        return ResponseEntity.created(location).body(restaurante);
    }

    @Operation(
            summary = "Atualizar restaurante",
            description = "Atualiza todos os dados de um restaurante existente, incluindo endereço, categoria, horários e telefones."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "400", description = "Erro de validação")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponse> atualizar(
            @Parameter(description = "ID do restaurante a ser atualizado", example = "10")
            @PathVariable Long id,
            @Valid @RequestBody RestauranteRequest dto) {

        RestauranteResponse restaurante = restauranteService.alterar(id, dto);
        return ResponseEntity.ok(restaurante);
    }

    @Operation(
            summary = "Ativar ou inativar restaurante",
            description = "Alterna o status do restaurante entre ATIVO e INATIVO."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<RestauranteResponse> ativarDesativar(
            @Parameter(description = "ID do restaurante", example = "5")
            @PathVariable Long id) {

        RestauranteResponse restaurante = restauranteService.ativarInativar(id);
        return ResponseEntity.ok(restaurante);
    }

    @Operation(
            summary = "Excluir restaurante",
            description = "Remove um restaurante permanentemente do sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Restaurante removido"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do restaurante a ser deletado", example = "7")
            @PathVariable Long id) {

        restauranteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar restaurantes",
            description = "Retorna todos os restaurantes cadastrados no sistema."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<RestauranteResponse>> listarTodos() {
        return ResponseEntity.ok(restauranteService.listarTodos());
    }

    @Operation(
            summary = "Buscar restaurante por ID",
            description = "Retorna os dados de um restaurante pelo seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponse> buscarPorId(
            @Parameter(description = "ID do restaurante", example = "3")
            @PathVariable Long id) {

        return ResponseEntity.ok(restauranteService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar restaurante por CNPJ",
            description = "Pesquisa um restaurante pelo seu CNPJ (apenas números)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<RestauranteResponse> buscarPorCnpj(
            @Parameter(description = "CNPJ do restaurante", example = "11222333000144")
            @PathVariable String cnpj) {

        return ResponseEntity.ok(restauranteService.buscarPorCnpj(cnpj));
    }

    @Operation(
            summary = "Buscar restaurantes por filtros",
            description = """
                    Permite pesquisar restaurantes utilizando múltiplos filtros simultâneos:<br>
                    - email<br>
                    - número de telefone<br>
                    - taxa de entrega<br>
                    - nome<br>
                    - horário de abertura<br>
                    - horário de fechamento<br>
                    - categoria do restaurante
                    """
    )
    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    @GetMapping("/buscar")
    public ResponseEntity<List<RestauranteResponse>> buscarPorFiltro(
            @Parameter(description = "Email do restaurante", example = "contato@saborbr.com")
            @RequestParam(required = false) String email,
            @Parameter(description = "Número de telefone (apenas números)", example = "11988776655")
            @RequestParam(required = false) String numeroTelefone,
            @Parameter(description = "Taxa de entrega", example = "5.00")
            @RequestParam(required = false) BigDecimal taxaEntrega,
            @Parameter(description = "Nome do restaurante", example = "Sabor")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Horário mínimo de abertura", example = "09:00")
            @RequestParam(required = false) LocalTime horarioAbertura,
            @Parameter(description = "Horário máximo de fechamento", example = "23:00")
            @RequestParam(required = false) LocalTime horarioFechamento,
            @Parameter(description = "Categoria do restaurante", example = "BRASILEIRO")
            @RequestParam(required = false) CategoriaRestaurante categoriaRestaurante
    ) {

        List<RestauranteResponse> restaurantes = restauranteService.buscarPorFiltro(
                email,
                numeroTelefone,
                taxaEntrega,
                nome,
                horarioAbertura,
                horarioFechamento,
                categoriaRestaurante
        );

        return ResponseEntity.ok(restaurantes);
    }
}
