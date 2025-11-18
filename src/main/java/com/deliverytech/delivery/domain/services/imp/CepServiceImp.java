package com.deliverytech.delivery.domain.services.imp;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.api.dto.CepRequest;
import com.deliverytech.delivery.api.dto.CepResponse;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.model.Cep;
import com.deliverytech.delivery.domain.model.Cidade;
import com.deliverytech.delivery.domain.repository.CepRepository;
import com.deliverytech.delivery.domain.repository.CidadeRepository;
import com.deliverytech.delivery.domain.services.CepService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CepServiceImp implements CepService {

    private final CepRepository cepRepository;
    private final CidadeRepository cidadeRepository;
    private final ModelMapper modelMapper;

    @Override
    public Cep buscarOuCriar(CepRequest dto) {
        String codigo = dto.codigo().trim().replaceAll("[^0-9]", "");

        Optional<Cep> seExiste = cepRepository.findByCodigo(codigo);

        if (seExiste.isPresent()) {
            return seExiste.get();
        }

        Cep cep = modelMapper.map(dto, Cep.class);
        if (cep == null) {
            throw new BusinessException("Erro ao mapear endereco a partir do DTO");
        }

        Cidade cidade = cidadeRepository.findById(Objects.requireNonNull(dto.cidadeId()))
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada para o id: " + dto.cidadeId()));

        cep.setCidade(cidade);

        return cepRepository.save(cep);
    }

    @Override
    public CepResponse criar(CepRequest dto) {

        if (dto.codigo() == null || dto.codigo().isBlank()) {
            throw new BusinessException("O código do CEP não pode ser vazio.");
        }
        if (dto.cidadeId() == null) {
            throw new BusinessException("O ID da cidade é obrigatório.");
        }

        Cep cep = buscarOuCriar(dto);
        cepRepository.save(Objects.requireNonNull(cep));

        return modelMapper.map(cep, CepResponse.class);
    }

    @Override
    public CepResponse alterar(Long id, CepRequest novoCep) {

        Cep cepExistente = cepRepository.findById(Objects.requireNonNull(id)).
                orElseThrow(() -> new EntityNotFoundException("Cep não encontrado"));

        if (novoCep.codigo() == null || novoCep.codigo().isBlank()) {
            throw new BusinessException("O código do CEP não pode ser vazio.");
        }
        if (novoCep.cidadeId() == null) {
            throw new BusinessException("O ID da cidade é obrigatório.");
        }

        Cidade cidade = cidadeRepository.findById(Objects.requireNonNull(novoCep.cidadeId()))
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada."));

        cepExistente.setCodigo(novoCep.codigo());
        cepExistente.setCidade(cidade);

        cepRepository.save(cepExistente);

        return CepResponse.of(cepExistente);
    }

    @Override
    public void deletar(Long cepId) {
        cepRepository.deleteById(Objects.requireNonNull(cepId, "Cep não encontrado para o id: " + cepId));
    }

    @Override
    public CepResponse buscarPorId(Long id) {
        Cep cep = cepRepository.findById(Objects.requireNonNull(id)).
                orElseThrow(() -> new EntityNotFoundException("Cep não encontrado"));
        return CepResponse.of(cep);
    }

    @Override
    public CepResponse buscarPorCodigo(String codigo) {
        Cep cep = cepRepository.findByCodigo(Objects.requireNonNull(codigo)).
                orElseThrow(() -> new EntityNotFoundException("Cep não encontrado"));
        return CepResponse.of(cep);
    }

    @Override
    public List<CepResponse> listarTodos() {
        List<Cep> ceps = cepRepository.findAll();
        return ceps.stream()
                .map(CepResponse::of)
                .toList();
    }

    @Override
    public List<CepResponse> listarPorCidadeNome(String nomeCidade) {
        List<Cep> ceps = cepRepository.findByCidadeNomeContainingIgnoreCase(nomeCidade);

        return ceps.stream()
                .map(CepResponse::of)
                .toList();
    }

}
