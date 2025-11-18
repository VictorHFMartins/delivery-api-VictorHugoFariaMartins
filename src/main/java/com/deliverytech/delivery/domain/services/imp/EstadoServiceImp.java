package com.deliverytech.delivery.domain.services.imp;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.EstadoRequest;
import com.deliverytech.delivery.api.dto.EstadoResponse;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.model.Estado;
import com.deliverytech.delivery.domain.repository.EstadoRepository;
import com.deliverytech.delivery.domain.services.EstadoService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class EstadoServiceImp implements EstadoService {

    private final EstadoRepository estadoRepository;
    private final ModelMapper modelMapper;

    @Override
    public Estado buscarOuCriar(EstadoRequest dto) {
        Optional<Estado> estado = estadoRepository.findByUfIgnoreCase(dto.uf());

        if (estado.isPresent()) {
            return estado.get();
        }

        Estado novoEstado = modelMapper.map(dto, Estado.class);
        if (novoEstado == null) {
            throw new BusinessException("Erro ao mapear endereco a partir do DTO");
        }

        return estadoRepository.save(novoEstado);
    }

    @Override
    public EstadoResponse criar(EstadoRequest dto) {

        if (dto.nome() == null || dto.nome().isEmpty()) {
            throw new BusinessException("Nome do estado não pode ser vazio.");
        }
        if (dto.uf() == null || dto.uf().isEmpty()) {
            throw new BusinessException("UF do estado não pode ser vazio.");
        }

        Estado estado = buscarOuCriar(dto);
        estadoRepository.save(Objects.requireNonNull(estado));

        return modelMapper.map(estado, EstadoResponse.class);
    }

    @Override
    public EstadoResponse alterar(Long estadoId, EstadoRequest estado) {

        Estado estadoExistente = estadoRepository.findById(Objects.requireNonNull(estadoId))
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado para o id: " + estadoId));

        if (estado.nome() == null || estado.nome().isEmpty()) {
            throw new BusinessException("Nome do estado não pode ser vazio.");
        }
        if (estado.uf() == null || estado.uf().isEmpty()) {
            throw new BusinessException("UF do estado não pode ser vazio.");
        }

        estadoExistente.setNome(estado.nome());
        estadoExistente.setUf(estado.uf());

        estadoRepository.save(estadoExistente);

        return EstadoResponse.of(estadoExistente);
    }

    @Override
    public void deletar(Long idEstado) {
        estadoRepository.deleteById(Objects.requireNonNull(idEstado, "Estado não encontrado para o id: " + idEstado));
    }

    @Override
    public EstadoResponse buscarPorId(Long id) {

        return EstadoResponse.of(estadoRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado para o id: " + id)));
    }

    @Override
    public EstadoResponse buscarPorUf(String uf) {

        return EstadoResponse.of(estadoRepository.findByUfIgnoreCase(uf)
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado para o UF: " + uf)));
    }

    @Override
    public List<EstadoResponse> listarTodos() {
        List<Estado> estados = estadoRepository.findAll();

        return estados.stream()
                .map(EstadoResponse::of)
                .toList();
    }

    @Override
    public List<EstadoResponse> listarPorNomeContendo(String nome) {

        List<Estado> estados = estadoRepository.findByNomeContainingIgnoreCase(nome);
        return estados.stream()
                .map(EstadoResponse::of)
                .toList();
    }

}
