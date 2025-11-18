package com.deliverytech.delivery.domain.services.imp;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.CidadeRequest;
import com.deliverytech.delivery.api.dto.CidadeResponse;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.model.Cidade;
import com.deliverytech.delivery.domain.model.Estado;
import com.deliverytech.delivery.domain.repository.CidadeRepository;
import com.deliverytech.delivery.domain.repository.EstadoRepository;
import com.deliverytech.delivery.domain.services.CidadeService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CidadeServiceImp implements CidadeService {

    private final CidadeRepository cidadeRepository;
    private final EstadoRepository estadoRepository;
    private final ModelMapper modelMapper;

    @Override
    public Cidade buscarOuCriar(CidadeRequest dto) {
        Optional<Cidade> cidade = cidadeRepository.findByNomeIgnoreCase(dto.nome());

        if (cidade.isPresent()) {
            return cidade.get();
        }

        Cidade novaCidade = modelMapper.map(dto, Cidade.class);
        if (novaCidade == null) {
            throw new BusinessException("Erro ao mapear endereco a partir do DTO");
        }
        Estado estado = estadoRepository.findById(Objects.requireNonNull(dto.estadoId()))
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado para o id: " + dto.estadoId()));

        novaCidade.setEstado(estado);

        return cidadeRepository.save(novaCidade);
    }

    @Override
    public CidadeResponse criar(CidadeRequest dto) {

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new BusinessException("O código do CEP não pode ser vazio.");
        }
        if (dto.estadoId() == null) {
            throw new BusinessException("O ID do estado é obrigatório.");
        }

        Cidade cidade = buscarOuCriar(dto);
        cidadeRepository.save(Objects.requireNonNull(cidade));

        return modelMapper.map(cidade, CidadeResponse.class);
    }

    @Override
    public CidadeResponse alterar(long cidadeId, CidadeRequest cidade) {
        Cidade cidadeExistente = cidadeRepository.findById(cidadeId)
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada" + cidadeId));

        Estado estado = estadoRepository.findById(Objects.requireNonNull(cidade.estadoId()))
                .orElseThrow(() -> new EntityNotFoundException("Estado não encontrado" + cidade.estadoId()));

        cidadeExistente.setNome(cidade.nome());
        cidadeExistente.setEstado(estado);

        cidadeRepository.save(cidadeExistente);

        return CidadeResponse.of(cidadeExistente);
    }

    @Override
    public void deletar(Long cidadeId) {
        cidadeRepository.deleteById(Objects.requireNonNull(cidadeId, "Cidade não encontrada para o id: " + cidadeId));
    }

    @Override
    @Transactional(readOnly=true)
    public CidadeResponse buscarPorId(Long id) {

        return CidadeResponse.of(cidadeRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new BusinessException("Cidade não encontrada para o id: " + id)));
    }

    @Override
    @Transactional(readOnly=true)
    public List<CidadeResponse> listarTodos() {
        List<Cidade> cidades = cidadeRepository.findAll();
        return cidades.stream()
                .map(CidadeResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly=true)
    public List<CidadeResponse> buscarPorNomeContendo(String nome) {

        List<Cidade> cidades = cidadeRepository.findByNomeContainingIgnoreCase(nome);
        return cidades.stream()
                .map(CidadeResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly=true)
    public List<CidadeResponse> buscarCidadesPorEstadoUf(String estadoUf) {

        List<Cidade> cidades = cidadeRepository.findByEstadoUfIgnoreCase(estadoUf);
        return cidades.stream()
                .map(CidadeResponse::of)
                .toList();
    }
}
