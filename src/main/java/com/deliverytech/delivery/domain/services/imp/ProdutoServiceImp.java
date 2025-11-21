package com.deliverytech.delivery.domain.services.imp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.ProdutoRequest;
import com.deliverytech.delivery.api.dto.ProdutoResponse;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.domain.enums.CategoriaProduto;
import com.deliverytech.delivery.domain.model.Produto;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.repository.ProdutoRepository;
import com.deliverytech.delivery.domain.services.ProdutoService;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ProdutoServiceImp implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final UsuarioValidator usuarioValidator;
    private final ModelMapper modelMapper;

    private Produto buscarOuCriar(Long restauranteId, ProdutoRequest dto) {

        String nome = dto.nome().trim();

        Optional<Produto> existenteOpt
                = produtoRepository.findByNomeAndRestauranteId(nome, restauranteId);

        if (existenteOpt.isPresent()) {
            Produto existente = existenteOpt.get();
            existente.setDisponibilidade(existente.getQuantidade() > 0);
            return existente;
        }

        Produto novo = modelMapper.map(dto, Produto.class);

        if (novo == null) {
            throw new BusinessException("Erro ao mapear produto a partir do DTO");
        }

        novo.setDisponibilidade(dto.quantidade() > 0);

        return novo;
    }

    @Override
    public ProdutoResponse criar(Long restauranteId, ProdutoRequest dto) {

        Restaurante restaurante = usuarioValidator.validarRestaurante(restauranteId);

        Produto produto = buscarOuCriar(restauranteId, dto);

        produto.setRestaurante(restaurante);
        restaurante.getProdutos().add(produto);

        Produto salvo = produtoRepository.save(produto);

        return ProdutoResponse.of(salvo);
    }

    @Override
    public ProdutoResponse atualizar(Long produtoId, ProdutoRequest dto) {

        Produto produto = produtoRepository.findById(Objects.requireNonNull(produtoId))
                .orElseThrow(() -> new EntityNotFoundException(
                "Produto não encontrado para o id: " + produtoId));

        if (!produto.getRestaurante().getId().equals(dto.restauranteId())) {
            throw new BusinessException("Produto não pertence ao restaurante informado.");
        }

        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setCategoriaProduto(dto.categoriaProduto());
        produto.setPreco(dto.preco());
        produto.setQuantidade(dto.quantidade());

        produto.setDisponibilidade(dto.quantidade() > 0);

        produtoRepository.save(produto);

        return ProdutoResponse.of(produto);
    }

    @Override
    public ProdutoResponse ativarInativar(Long produtoId) {

        Produto produto = produtoRepository.findById(Objects.requireNonNull(produtoId))
                .orElseThrow(() -> new EntityNotFoundException(
                "Produto não encontrado para o id: " + produtoId));

        produto.setDisponibilidade(!produto.isDisponibilidade());
        produtoRepository.save(produto);

        return ProdutoResponse.of(produto);
    }

    @Override
    public void deletar(Long produtoId) {
        produtoRepository.deleteById(
                Objects.requireNonNull(produtoId, "Produto não encontrado para o id: " + produtoId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarTodos() {
        return produtoRepository.findAll().stream()
                .map(ProdutoResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long produtoId) {

        Produto produto = produtoRepository.findById(Objects.requireNonNull(produtoId))
                .orElseThrow(() -> new EntityNotFoundException(
                "Produto não encontrado para o id: " + produtoId));

        return ProdutoResponse.of(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarPorFiltro(
            String nome,
            Long quantidade,
            BigDecimal preco,
            CategoriaProduto categoria) {

        List<Produto> produtos = produtoRepository.findAll();

        if (nome != null && !nome.isBlank()) {
            produtos = produtos.stream()
                    .filter(p -> p.getNome().toLowerCase()
                    .contains(nome.toLowerCase()))
                    .toList();
        }

        if (quantidade != null) {
            produtos = produtos.stream()
                    .filter(p -> Objects.equals(p.getQuantidade(), quantidade))
                    .toList();
        }

        if (preco != null) {
            produtos = produtos.stream()
                    .filter(p -> p.getPreco().compareTo(preco) == 0)
                    .toList();
        }

        if (categoria != null) {
            produtos = produtos.stream()
                    .filter(p -> p.getCategoriaProduto().equals(categoria))
                    .toList();
        }

        return produtos.stream()
                .map(ProdutoResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarProdutosPorRestaurante(Long restauranteId) {

        List<Produto> produtos = produtoRepository.findByRestauranteId(restauranteId);

        return produtos.stream()
                .map(ProdutoResponse::of)
                .toList();
    }

}
