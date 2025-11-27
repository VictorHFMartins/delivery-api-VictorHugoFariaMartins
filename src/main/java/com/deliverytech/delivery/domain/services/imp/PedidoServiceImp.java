package com.deliverytech.delivery.domain.services.imp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.ItemPedidoRequest;
import com.deliverytech.delivery.api.dto.PedidoRequest;
import com.deliverytech.delivery.api.dto.PedidoResponse;
import com.deliverytech.delivery.api.dto.PedidoUpdateRequest;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.domain.enums.EstadoRestaurante;
import com.deliverytech.delivery.domain.enums.StatusPedido;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.ItemPedido;
import com.deliverytech.delivery.domain.model.Pedido;
import com.deliverytech.delivery.domain.model.Produto;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.repository.PedidoRepository;
import com.deliverytech.delivery.domain.repository.ProdutoRepository;
import com.deliverytech.delivery.domain.services.PedidoService;
import com.deliverytech.delivery.domain.utils.CalcularFrete;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PedidoServiceImp implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioValidator usuarioValidator;
    private final CalcularFrete frete;

    @Override
    public PedidoResponse criar(PedidoRequest dto) {

        Cliente cliente = usuarioValidator.validarCliente(dto.clienteId());
        Restaurante restaurante = usuarioValidator.validarRestaurante(dto.restauranteId());

        if (!cliente.isStatus()) {
            throw new BusinessException("Cliente encontra-se inativo no sistema.");
        }
        if (!restaurante.isStatus()) {
            throw new BusinessException("Restaurante encontra-se inativo no sistema.");
        }
        if (restaurante.getEstado() != EstadoRestaurante.ABERTO) {
            throw new BusinessException("Restaurante encontra-se fechado no sistema.");
        }
        if (dto.itens().isEmpty()) {
            throw new BusinessException("O pedido deve conter ao menos um item.");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setObservacoes(dto.observacoes());
        pedido.setStatusPedido(StatusPedido.PENDENTE);

        // Criar itens
        List<ItemPedido> itens = dto.itens()
                .stream()
                .map(i -> criarItemPedido(pedido, i))
                .toList();

        pedido.setItens(itens);

        calcularValores(pedido);

        aplicarDescontoSeHouver(pedido, dto.desconto());

        pedidoRepository.save(pedido);

        return PedidoResponse.of(pedido);
    }

    @Override
    public PedidoResponse atualizar(Long pedidoId, PedidoUpdateRequest dto) {

        Pedido pedido = pedidoRepository.findById(Objects.requireNonNull(pedidoId))
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado para o id: " + pedidoId));

        usuarioValidator.validarUsuario(pedido.getCliente().getId());
        usuarioValidator.validarUsuario(pedido.getRestaurante().getId());

        if (pedido.getStatusPedido() == StatusPedido.ENTREGUE
                || pedido.getStatusPedido() == StatusPedido.CANCELADO
                || pedido.getStatusPedido() == StatusPedido.DESPACHADO) {
            throw new BusinessException("Não é possível atualizar um pedido entregue, cancelado ou despachado.");
        }

        if (dto.itens().isEmpty()) {
            throw new BusinessException("O pedido deve conter ao menos um item.");
        }

        pedido.setObservacoes(dto.observacoes());
        pedido.getItens().clear();

        List<ItemPedido> novosItens = dto.itens()
                .stream()
                .map(i -> criarItemPedido(pedido, i))
                .toList();

        pedido.setItens(novosItens);

        calcularValores(pedido);

        pedidoRepository.save(pedido);

        return PedidoResponse.of(pedido);
    }

    @Override
    public PedidoResponse cancelar(Long pedidoId) {

        Pedido pedido = pedidoRepository.findById(Objects.requireNonNull(pedidoId))
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado para o id: " + pedidoId));

        if (pedido.getStatusPedido() == StatusPedido.CANCELADO) {
            throw new BusinessException("O pedido já foi cancelado anteriormente.");
        }

        validarTransicaoStatus(pedido.getStatusPedido(), StatusPedido.CANCELADO);

        pedido.setStatusPedido(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);

        return PedidoResponse.of(pedido);
    }

    @Override
    public void deletar(Long pedidoId) {
        pedidoRepository.deleteById(Objects.requireNonNull(pedidoId));
    }

    @Override
    public PedidoResponse buscarPorId(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(Objects.requireNonNull(pedidoId))
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado para o id: " + pedidoId));
        return PedidoResponse.of(pedido);
    }

    @Override
    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoResponse::of)
                .toList();
    }

    @Override
    public List<PedidoResponse> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId)
                .stream()
                .map(PedidoResponse::of)
                .toList();
    }

    @Override
    public List<PedidoResponse> listarPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteId(restauranteId)
                .stream()
                .map(PedidoResponse::of)
                .toList();
    }

    @Override
    public List<PedidoResponse> listarDezPrimeirosPorDataDePedido() {
        return pedidoRepository.findTop10ByOrderByDataPedidoDesc()
                .stream()
                .map(PedidoResponse::of)
                .toList();
    }

    @Override
    public List<PedidoResponse> listarPedidosEntreDatas(LocalDateTime inicio, LocalDateTime fim) {
        return pedidoRepository.findByDataPedidoBetween(inicio, fim)
                .stream()
                .map(PedidoResponse::of)
                .toList();
    }

    @Override
    public List<PedidoResponse> listarPedidosAcimaDe(BigDecimal valor) {
        return pedidoRepository.pedidosAcimaDe(valor)
                .stream()
                .map(PedidoResponse::of)
                .toList();
    }

    @Override
    public List<PedidoResponse> ListarPorPeríodoEStatus(LocalDateTime inicio, LocalDateTime fim, StatusPedido status) {
        return pedidoRepository.relatorioPorPeriodoEStatus(inicio, fim, status)
                .stream()
                .map(PedidoResponse::of)
                .toList();
    }

    @Override
    @Transactional
    public PedidoResponse mudarStatusPedido(Long pedidoId, StatusPedido novoStatus) {

        Pedido pedido = pedidoRepository.findById(Objects.requireNonNull(pedidoId))
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));

        validarTransicaoStatus(pedido.getStatusPedido(), novoStatus);

        pedido.setStatusPedido(novoStatus);
        pedidoRepository.save(pedido);

        return PedidoResponse.of(pedido);
    }

    @Override
    public BigDecimal calcularTotal(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(Objects.requireNonNull(pedidoId))
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado para o id: " + pedidoId));

        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return pedido.getItens().stream()
                .map(ItemPedido::getSubtotal)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

    }

    private void validarTransicaoStatus(StatusPedido atual, StatusPedido novo) {

        if (atual == StatusPedido.CANCELADO) {
            throw new BusinessException("Pedido cancelado não pode ser alterado.");
        }

        if (atual == StatusPedido.ENTREGUE) {
            throw new BusinessException("Pedido entregue não pode ser alterado.");
        }

        if (atual == StatusPedido.DESPACHADO && novo != StatusPedido.ENTREGUE) {
            throw new BusinessException("Após DESPACHADO só pode ir para ENTREGUE.");
        }
    }

    private ItemPedido criarItemPedido(Pedido pedido, ItemPedidoRequest itemDto) {

        Produto produto = produtoRepository.findById(Objects.requireNonNull(itemDto.produtoId()))
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        if (!produto.getRestaurante().getId().equals(pedido.getRestaurante().getId())) {
            throw new BusinessException("Produto não pertence ao restaurante.");
        }
        if (!produto.isDisponibilidade()) {
            throw new BusinessException("O produto não está disponível.");
        }
        if (produto.getQuantidade() < itemDto.quantidade()) {
            throw new BusinessException("Estoque insuficiente para o produto: " + produto.getNome());
        }

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(itemDto.quantidade());
        item.setPrecoUnitario(produto.getPreco());
        item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemDto.quantidade())));
        item.setPedido(pedido);

        produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
        if (produto.getQuantidade() == 0) {
            produto.setDisponibilidade(false);
        }

        return item;
    }

    public void aplicarDescontoSeHouver(Pedido pedido, Optional<BigDecimal> desconto) {

        BigDecimal valorDesconto = desconto.orElse(BigDecimal.ZERO);

        if (valorDesconto.compareTo(BigDecimal.ZERO) < 0) {
            valorDesconto = BigDecimal.ZERO;
        }

        BigDecimal finalComDesconto = pedido.getValorTotal().subtract(valorDesconto);

        pedido.setValorTotal(finalComDesconto.max(BigDecimal.ZERO));
    }

    public Pedido calcularValores(Pedido pedido) {

        // Total dos itens
        BigDecimal totalItens = pedido.getItens().stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Frete
        BigDecimal taxaEntrega = frete.calcularTaxa(
                pedido.getRestaurante().getEndereco().getLatitude(),
                pedido.getRestaurante().getEndereco().getLongitude(),
                pedido.getCliente().getEndereco().getLatitude(),
                pedido.getCliente().getEndereco().getLongitude()
        );

        pedido.setTaxaEntrega(taxaEntrega);
        pedido.setValorTotal(totalItens.add(taxaEntrega));

        return pedido;
    }

}
