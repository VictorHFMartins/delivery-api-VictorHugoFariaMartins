package com.deliverytech.delivery.domain.services.imp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.ItemPedidoRequest;
import com.deliverytech.delivery.api.dto.PedidoRequest;
import com.deliverytech.delivery.api.dto.PedidoResponse;
import com.deliverytech.delivery.api.dto.PedidoUpdateRequest;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.domain.enums.StatusPedido;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.ItemPedido;
import com.deliverytech.delivery.domain.model.Pedido;
import com.deliverytech.delivery.domain.model.Produto;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.repository.PedidoRepository;
import com.deliverytech.delivery.domain.repository.ProdutoRepository;
import com.deliverytech.delivery.domain.services.PedidoService;
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

    @Override
    public PedidoResponse criar(PedidoRequest dto) {
        Cliente cliente = (Cliente) usuarioValidator.validarUsuario(dto.clienteId());
        Restaurante restaurante = (Restaurante) usuarioValidator.validarUsuario(dto.restauranteId());

        if (dto.itens().isEmpty()) {
            throw new BusinessException("O pedido deve conter ao menos um item.");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setObservacoes(dto.observacoes());
        pedido.setStatusPedido(StatusPedido.PENDENTE);

        List<ItemPedido> itens = dto.itens().stream().map(i -> criarItemPedido(pedido, i)).toList();

        pedido.setItens(itens);
        pedido.calcularValorTotal();

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
            throw new BusinessException("Não é possível atualizar um pedido que já foi entregue, cancelado ou despachado.");
        }

        if (dto.itens().isEmpty()) {
            throw new BusinessException("O pedido deve conter ao menos um item.");
        }

        pedido.setObservacoes(dto.observacoes());
        pedido.getItens().clear();

        List<ItemPedido> novosItens = dto.itens().stream().map(i -> criarItemPedido(pedido, i)).toList();

        pedido.setItens(novosItens);
        pedido.calcularValorTotal();

        pedidoRepository.save(pedido);

        return PedidoResponse.of(pedido);
    }

    @Override
    public PedidoResponse cancelar(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(Objects.requireNonNull(pedidoId))
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado para o id: " + pedidoId));

        StatusPedido novoStatus = StatusPedido.CANCELADO;

        if (pedido.getStatusPedido() == novoStatus) {
            throw new BusinessException("O pedido já foi cancelado anteriormente.");
        }

        validarTransicaoStatus(pedido.getStatusPedido(), novoStatus);

        pedido.setStatusPedido(novoStatus);
        pedidoRepository.save(pedido);

        return PedidoResponse.of(pedido);
    }

    @Override
    public void deletar(Long pedidoId) {
        pedidoRepository.deleteById(Objects.requireNonNull(pedidoId, "Pedido não encontrado para o id: " + pedidoId));
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
                .stream().map(PedidoResponse::of).toList();
    }

    @Override
    public List<PedidoResponse> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId)
                .stream().map(PedidoResponse::of).toList();
    }

    @Override
    public List<PedidoResponse> listarPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestauranteId(restauranteId)
                .stream().map(PedidoResponse::of).toList();
    }

    @Override
    public BigDecimal calcularTotal(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(Objects.requireNonNull(pedidoId))
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado para o id: " + pedidoId));

        pedido.calcularValorTotal();
        pedidoRepository.save(pedido);

        return pedido.getValorTotal();
    }

    @Override
    @Transactional
    public PedidoResponse mudarStatusPedido(Long pedidoId, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(Objects.requireNonNull(pedidoId))
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado para o id: " + pedidoId));

        validarTransicaoStatus(pedido.getStatusPedido(), novoStatus);

        pedido.setStatusPedido(novoStatus);
        pedidoRepository.save(pedido);

        return PedidoResponse.of(pedido);
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
                .orElseThrow(() -> new EntityNotFoundException(
                "Produto não encontrado para o id: " + itemDto.produtoId()));

        if (!produto.getRestaurante().getId().equals(pedido.getRestaurante().getId())) {
            throw new BusinessException("O produto não pertence ao restaurante do pedido.");
        }

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setQuantidade(itemDto.quantidade());
        item.setPrecoUnitario(produto.getPreco());
        item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemDto.quantidade())));
        item.setPedido(pedido);
        return item;
    }

}
