package com.deliverytech.delivery.api.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
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

import com.deliverytech.delivery.api.dto.RestauranteFreteResponse;
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
import lombok.RequiredArgsConstructor;

@Tag(name = "Restaurantes", description = "Gerenciamento de restaurantes do sistema DeliveryTech")
@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RestauranteController {

    private final RestauranteService restauranteService;

    @Operation(
            summary = "Cadastrar novo restaurante",
            description = "Cria um restaurante incluindo endereço, contatos, horários e categoria.",
            requestBody = @RequestBody(
                    description = "Objeto com os dados necessários para cadastro",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RestauranteRequest.class),
                            examples = @ExampleObject(value = """
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
                              "horarioFechamento": "23:00"
                            }
                            """)
                    )
            )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados"),
        @ApiResponse(responseCode = "409", description = "CNPJ já cadastrado no sistema")
    })
    @PostMapping
    public ResponseEntity<RestauranteResponse> cadastrar(
            @Valid @RequestBody RestauranteRequest dto
    ) {
        RestauranteResponse restaurante = restauranteService.criar(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(restaurante.id())
                .toUri();

        return ResponseEntity.created(uri).body(restaurante);
    }

    @Operation(
            summary = "Atualizar restaurante",
            description = "Atualiza informações completas de um restaurante."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado"),
        @ApiResponse(responseCode = "400", description = "Erro de validação"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponse> atualizar(
            @Parameter(description = "ID do restaurante", example = "10")
            @PathVariable Long id,
            @Valid @RequestBody RestauranteRequest dto
    ) {
        return ResponseEntity.ok(restauranteService.alterar(id, dto));
    }

    @Operation(
            summary = "Ativar ou inativar restaurante",
            description = "Alterna o status do restaurante entre ativo e inativo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status atualizado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<RestauranteResponse> ativarDesativar(
            @Parameter(description = "ID do restaurante", example = "5")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(restauranteService.ativarInativar(id));
    }

    @Operation(
            summary = "Excluir restaurante",
            description = "Remove um restaurante do sistema permanentemente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Restaurante removido"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do restaurante", example = "7")
            @PathVariable Long id
    ) {
        restauranteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Listar restaurantes",
            description = "Retorna todos os restaurantes cadastrados."
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada")
    @GetMapping
    public ResponseEntity<List<RestauranteResponse>> listarTodos() {
        return ResponseEntity.ok(restauranteService.listarTodos());
    }

    @Operation(
            summary = "Buscar por ID",
            description = "Retorna dados de um restaurante pelo ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponse> buscarPorId(
            @Parameter(description = "ID do restaurante", example = "3")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(restauranteService.buscarPorId(id));
    }

    @Operation(
            summary = "Buscar por CNPJ",
            description = "Pesquisa restaurante pelo CNPJ."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<RestauranteResponse> buscarPorCnpj(
            @Parameter(description = "CNPJ do restaurante", example = "11222333000144")
            @PathVariable String cnpj
    ) {
        return ResponseEntity.ok(restauranteService.buscarPorCnpj(cnpj));
    }

    @Operation(
            summary = "Total de vendas por restaurante",
            description = "Retorna o valor total vendido por um restaurante."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Valor retornado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    @GetMapping("/{restauranteId}/total")
    public ResponseEntity<BigDecimal> totalDeVendasPorRestaurante(
            @Parameter(description = "ID do restaurante", example = "4")
            @PathVariable Long restauranteId) {

        return ResponseEntity.ok(restauranteService.totalDeVendasPorRestaurante(restauranteId));
    }

    @Operation(
            summary = "Listar restaurantes por frete para cliente",
            description = "Permite filtrar os restaurante ordenanos por frete para o cliente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Valor retornado"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/cliente/{clienteId}/com-frete")
    public ResponseEntity<List<RestauranteFreteResponse>> listarPorFrete(
            @Schema(description = "ID do cliente", example = "8")
            @PathVariable Long clienteId) {
        List<RestauranteFreteResponse> lista = restauranteService.listarComFreteOrdenado(clienteId);
        return ResponseEntity.ok(lista);
    }

    @Operation(
            summary = "Pesquisar restaurantes por filtros",
            description = "Permite filtrar por nome, email, telefone, horários e categoria."
    )
    @ApiResponse(responseCode = "200", description = "Busca concluída")
    @GetMapping("/buscar")
    public ResponseEntity<List<RestauranteResponse>> buscarPorFiltro(
            @Parameter(description = "Email", example = "contato@sabor.com")
            @RequestParam(required = false) String email,
            @Parameter(description = "Telefone (apenas números)", example = "11988776655")
            @RequestParam(required = false) String numeroTelefone,
            @Parameter(description = "Nome do restaurante", example = "Sabor")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Horário mínimo de abertura", example = "09:00")
            @RequestParam(required = false) LocalTime horarioAbertura,
            @Parameter(description = "Horário máximo de fechamento", example = "23:00")
            @RequestParam(required = false) LocalTime horarioFechamento,
            @Parameter(description = "Categoria", example = "BRASILEIRO")
            @RequestParam(required = false) CategoriaRestaurante categoria
    ) {
        return ResponseEntity.ok(
                restauranteService.buscarPorFiltro(
                        email, numeroTelefone, nome,
                        horarioAbertura, horarioFechamento, categoria
                )
        );
    }
}
