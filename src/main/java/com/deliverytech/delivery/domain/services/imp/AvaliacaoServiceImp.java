package com.deliverytech.delivery.domain.services.imp;

import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.api.dto.AvaliacaoRequest;
import com.deliverytech.delivery.api.dto.AvaliacaoResponse;
import com.deliverytech.delivery.api.dto.RestauranteAvaliacoesResponse;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.model.Avaliacao;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.repository.AvaliacaoRepository;
import com.deliverytech.delivery.domain.repository.RestauranteRepository;
import com.deliverytech.delivery.domain.services.AvaliacaoService;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AvaliacaoServiceImp implements AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioValidator usuarioValidator;
    private final ModelMapper modelMapper;

    @Override
    public AvaliacaoResponse criar(Long restauranteId, AvaliacaoRequest req) {

        Restaurante restaurante = usuarioValidator.validarRestaurante(req.restauranteId());
        Cliente cliente = usuarioValidator.validarCliente(req.clienteId());

        if (avaliacaoRepository.existsByClienteIdAndRestauranteId(req.clienteId(), restauranteId)) {
            throw new BusinessException("Avaliação já existente para o cliente: " + req.clienteId()
                    + " e restaurante: " + restauranteId);
        }

        Avaliacao avaliacao = modelMapper.map(req, Avaliacao.class);

        avaliacao.setCliente(cliente);
        avaliacao.setRestaurante(restaurante);

        avaliacaoRepository.save(Objects.requireNonNull(avaliacao));

        Double novaMedia = avaliacaoRepository.calcularMediaPorRestaurante(restauranteId);
        restaurante.setNotaAvaliacao(novaMedia);

        return AvaliacaoResponse.of(avaliacao);
    }

    @Override
    public AvaliacaoResponse editar(Long restauranteId, Long avaliacaoId, AvaliacaoRequest dto) {
        Restaurante restaurante = usuarioValidator.validarRestaurante(restauranteId);

        Avaliacao avaliacao = avaliacaoRepository.findById(Objects.requireNonNull(avaliacaoId))
                .orElseThrow(() -> new BusinessException("avaliação " + avaliacaoId + " não encontrada para o restaurante: " + restauranteId));

        if (!avaliacao.getCliente().getId().equals(dto.clienteId())) {
            throw new BusinessException("Cliente não pode editar avaliação de outro cliente");
        }

        avaliacao.setNota(dto.nota());
        avaliacao.setComentario(dto.comentario());

        Double novaMedia = avaliacaoRepository.calcularMediaPorRestaurante(restauranteId);
        restaurante.setNotaAvaliacao(novaMedia);

        return AvaliacaoResponse.of(avaliacao);
    }

    @Override
    public void remover(Long avaliacaoId) {
        Avaliacao avaliacao = avaliacaoRepository.findById(Objects.requireNonNull(avaliacaoId))
                .orElseThrow(() -> new EntityNotFoundException("Avaliacao não encontrada para o id: " + avaliacaoId));
        Restaurante restaurante = usuarioValidator.validarRestaurante(avaliacao.getRestaurante().getId());

        avaliacaoRepository.deleteById(Objects.requireNonNull(avaliacaoId));

        Double novaMedia = avaliacaoRepository.calcularMediaPorRestaurante(restaurante.getId());
        restaurante.setNotaAvaliacao(novaMedia);

    }

    @Override
    public List<AvaliacaoResponse> listarPorRestaurante(Long restauranteId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByRestauranteId(restauranteId);
        return avaliacoes.stream()
                .map(AvaliacaoResponse::of)
                .toList();
    }

    @Override
    public AvaliacaoResponse responder(Long avaliacaoId, String resposta) {
        Avaliacao avaliacao = avaliacaoRepository.findById(Objects.requireNonNull(avaliacaoId))
                .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));

        avaliacao.setRespostaRestaurante(resposta);

        return AvaliacaoResponse.of(avaliacao);
    }

    @Override
    public List<AvaliacaoResponse> listarPorData(Long restauranteId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByRestauranteIdOrderByDataAvaliacaoDesc(restauranteId);

        return avaliacoes.stream()
                .map(AvaliacaoResponse::of)
                .toList();
    }

    @Override
    public List<AvaliacaoResponse> listarPorNota(Long restauranteId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByRestauranteIdOrderByNotaDesc(restauranteId);

        return avaliacoes.stream()
                .map(AvaliacaoResponse::of)
                .toList();
    }

    @Override
    public RestauranteAvaliacoesResponse media(Long restauranteId) {
        Restaurante restaurante = restauranteRepository.findById(Objects.requireNonNull(restauranteId))
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado para o id: " + restauranteId));

        Double novaMedia = avaliacaoRepository.calcularMediaPorRestaurante(restauranteId);
        restaurante.setNotaAvaliacao(novaMedia != null ? novaMedia : 0.0);

        restauranteRepository.save(restaurante);

        return RestauranteAvaliacoesResponse.of(restaurante);
    }

    @Override
    public List<AvaliacaoResponse> listarPorCliente(Long clienteId) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByClienteId(clienteId);
        return avaliacoes.stream()
                .map(AvaliacaoResponse::of)
                .toList();
    }
}
