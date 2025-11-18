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
import com.deliverytech.delivery.domain.repository.RestauranteRepository;
import com.deliverytech.delivery.domain.services.ProdutoService;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class ProdutoServiceImp implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final RestauranteRepository restauranteRepository;

    private final UsuarioValidator usuarioValidator;

    private final ModelMapper modelMapper;

    @Override
    public Produto buscarOuCriar(ProdutoRequest dto) {
        String nome = dto.nome().trim();

        Optional<Produto> existente = produtoRepository.findByNome(nome);

        if (existente.isPresent()) {
            return existente.get();
        }

        Produto produto = modelMapper.map(dto, Produto.class);
        if (produto == null) {
            throw new BusinessException("Erro ao mapear produto a partir do DTO");
        }

        if (produto.getQuantidade() > 0) {
            produto.setDisponibilidade(true);
        } else {
            produto.setDisponibilidade(false);
        }

        return produtoRepository.save(produto);

    }

    @Override
    public ProdutoResponse criar(Long restauranteId, ProdutoRequest dto) {

        Restaurante restaurante = (Restaurante) usuarioValidator.validarUsuario(restauranteId);

        Produto produto = buscarOuCriar(dto);

        produto.setRestaurante(restaurante);
        produtoRepository.save(produto);

        restaurante.getProdutos().add(produto);
        restauranteRepository.save(restaurante);

        return ProdutoResponse.of(produto);

    }

    @Override
    public ProdutoResponse atualizar(Long produtoId, ProdutoRequest dto) {

        Produto produto = produtoRepository.findById(Objects.requireNonNull(produtoId))
                .orElseThrow(() -> new EntityNotFoundException("produto não encontrado para o id: " + produtoId));

        produto.setNome(dto.nome());
        produto.setQuantidade(dto.quantidade());
        produto.setPreco(dto.preco());
        produto.setDescricao(dto.descricao());
        produto.setCategoriaProduto(dto.categoriaProduto());

        produtoRepository.save(Objects.requireNonNull(produto));

        return modelMapper.map(produto, ProdutoResponse.class);

    }

    @Override
    public ProdutoResponse ativarInativar(Long produtoId) {
        Produto produto = produtoRepository.findById(Objects.requireNonNull(produtoId))
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado para o id: " + produtoId));

        produto.setDisponibilidade(!produto.isDisponibilidade());
        produtoRepository.save(produto);
        return ProdutoResponse.of(produto);
    }

    @Override
    public void deletar(Long produtoId) {
        produtoRepository.deleteById(Objects.requireNonNull(produtoId, "Produto não encontrado para o id: " + produtoId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarTodos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream()
                .map(ProdutoResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long produtoId) {
        Produto produto = produtoRepository.findById(Objects.requireNonNull(produtoId))
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado para o id: " + produtoId));
        return ProdutoResponse.of(produto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarPorFiltro(
            String nome,
            Long quantidade,
            BigDecimal preco,
            CategoriaProduto categoriaProduto) {

        List<Produto> produtos = produtoRepository.findAll();

        if (nome != null && !nome.isBlank()) {
            produtos = produtos.stream()
                    .filter(p -> p.getNome()
                    .toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }
        if (quantidade != null) {
            produtos = produtos.stream()
                    .filter(p -> p.getQuantidade()
                    .equals(quantidade))
                    .toList();
        }
        if (preco != null) {
            produtos = produtos.stream()
                    .filter(p -> p.getPreco()
                    .equals(preco))
                    .toList();
        }
        if (categoriaProduto != null) {
            produtos = produtos.stream()
                    .filter(p -> p.getCategoriaProduto()
                    .equals(categoriaProduto))
                    .toList();
        }

        return produtos.stream()
                .map(ProdutoResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdutoResponse> buscarProdutosPorRestaurante(
            String restauranteNome,
            Long restauranteId) {

        List<Produto> produtos = produtoRepository.findAll();

        if (restauranteNome != null && !restauranteNome.isBlank()) {
            produtos = produtos.stream()
                    .filter(p -> p.getRestaurante().getNome()
                    .toLowerCase().contains(restauranteNome.toLowerCase()))
                    .toList();
            return produtos.stream()
                    .map(ProdutoResponse::of)
                    .toList();
        }

        if (restauranteId != null) {
            produtos = produtos.stream()
                    .filter(p -> p.getRestaurante().getId()
                    .equals(restauranteId))
                    .toList();

            return produtos.stream()
                    .map(ProdutoResponse::of)
                    .toList();
        }

        return produtos.stream()
                .map(ProdutoResponse::of)
                .toList();
    }

}
