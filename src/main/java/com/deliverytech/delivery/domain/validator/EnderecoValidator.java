package com.deliverytech.delivery.domain.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.deliverytech.delivery.api.dto.EnderecoRequest;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.model.Cep;
import com.deliverytech.delivery.domain.model.Cidade;
import com.deliverytech.delivery.domain.model.Endereco;
import com.deliverytech.delivery.domain.repository.CepRepository;
import com.deliverytech.delivery.domain.repository.CidadeRepository;
import com.deliverytech.delivery.domain.repository.EnderecoRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EnderecoValidator {

    private final EnderecoRepository enderecoRepository;
    private final CidadeRepository cidadeRepository;
    private final CepRepository cepRepository;

    public Endereco validarEndereco(Long enderecoId) {

        return enderecoRepository.findById(Objects.requireNonNull(enderecoId))
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado com ID: " + enderecoId));
    }

    public Cep validarOuCriarCep(String codigoCep, Long cidadeId) {
        return cepRepository.findByCodigo(codigoCep)
                .orElseGet(() -> {
                    Cidade cidade = cidadeRepository.findById(Objects.requireNonNull(cidadeId))
                            .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada: " + cidadeId));

                    Cep novoCep = new Cep();
                    novoCep.setCodigo(codigoCep);
                    novoCep.setCidade(cidade);
                    return cepRepository.save(novoCep);
                });
    }

    public Endereco criarEndereco(EnderecoRequest dto, Cep cep) {
        Endereco e = new Endereco();
        e.setLogradouro(dto.logradouro());
        e.setTipoLogradouro(dto.tipoLogradouro());
        e.setNumero(dto.numero());
        e.setComplemento(dto.complemento());
        e.setBairro(dto.bairro());
        e.setCep(cep);
        return enderecoRepository.save(e);
    }
}
