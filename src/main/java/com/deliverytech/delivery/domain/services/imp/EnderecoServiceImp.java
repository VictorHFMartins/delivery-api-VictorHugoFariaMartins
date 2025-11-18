package com.deliverytech.delivery.domain.services.imp;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.CepRequest;
import com.deliverytech.delivery.api.dto.EnderecoRequest;
import com.deliverytech.delivery.api.dto.EnderecoResponse;
import com.deliverytech.delivery.api.dto.EnderecoUpdateRequest;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.domain.enums.TipoLogradouro;
import com.deliverytech.delivery.domain.model.Cep;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Endereco;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.model.Usuario;
import com.deliverytech.delivery.domain.repository.ClienteRepository;
import com.deliverytech.delivery.domain.repository.EnderecoRepository;
import com.deliverytech.delivery.domain.repository.RestauranteRepository;
import com.deliverytech.delivery.domain.services.CepService;
import com.deliverytech.delivery.domain.services.EnderecoService;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class EnderecoServiceImp implements EnderecoService {

    private final ClienteRepository clienteRepository;
    private final EnderecoRepository enderecoRepository;
    private final RestauranteRepository restauranteRepository;

    private final CepService cepService;

    private final UsuarioValidator usuarioValidator;

    private final ModelMapper modelMapper;

    @Override
    public Endereco buscarOuCriarEndereco(EnderecoRequest dto) {
        String numero = dto.numero();
        String logradouroNome = dto.logradouro();

        Optional<Endereco> seExiste = enderecoRepository.findByNumeroAndLogradouroIgnoreCase(numero, logradouroNome);

        if (seExiste.isPresent()) {
            return seExiste.get();
        }

        Endereco endereco = modelMapper.map(dto, Endereco.class);
        if (endereco == null) {
            throw new BusinessException("Erro ao mapear endereco a partir do DTO");
        }

        CepRequest cepDto = new CepRequest(dto.cepCodigo(), dto.cidadeId());

        Cep cep = cepService.buscarOuCriar(cepDto);
        endereco.setCep(cep);

        return enderecoRepository.save(endereco);
    }

    @Override
    public EnderecoResponse criar(Long usuarioId, EnderecoRequest dto) {
        Usuario usuario = usuarioValidator.validarUsuario(usuarioId);

        Endereco endereco = buscarOuCriarEndereco(dto);

        usuario.setEndereco(endereco);
        if (usuario instanceof Cliente cliente) {
            clienteRepository.save(cliente);
        }
        if (usuario instanceof Restaurante restaurante) {
            restauranteRepository.save(restaurante);
        }

        return EnderecoResponse.of(endereco);
    }

    @Override
    public EnderecoResponse atualizar(Long usuarioId, Long enderecoId, EnderecoUpdateRequest dto) {
        Usuario usuario = usuarioValidator.validarUsuario(usuarioId);

        Endereco endereco = enderecoRepository.findById(Objects.requireNonNull(enderecoId))
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado para o id: " + enderecoId));

        endereco.setLogradouro(dto.logradouro());
        endereco.setTipoLogradouro(dto.tipoLogradouro());
        endereco.setNumero(dto.numero());
        endereco.setComplemento(dto.complemento());
        endereco.setBairro(dto.bairro());

        CepRequest cepDto = new CepRequest(dto.cepCodigo(), dto.cidadeId());
        Cep cep = cepService.buscarOuCriar(cepDto);
        endereco.setCep(cep);

        endereco.setLatitude(dto.latitude());
        endereco.setLongitude(dto.longitude());

        enderecoRepository.save(endereco);
        usuario.setEndereco(endereco);

        if (usuario instanceof Cliente cliente) {
            clienteRepository.save(cliente);
        }
        if (usuario instanceof Restaurante restaurante) {
            restauranteRepository.save(restaurante);
        }
        return EnderecoResponse.of(endereco);
    }

    @Override
    public void deletar(Long enderecoId) {
        enderecoRepository.deleteById(Objects.requireNonNull(enderecoId, "Endereço não encontrado para o id: " + enderecoId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoResponse> listarTodos() {
        List<Endereco> enderecos = enderecoRepository.findAll();
        return enderecos.stream()
                .map(EnderecoResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EnderecoResponse buscarPorId(Long idEndereco) {
        return EnderecoResponse.of(enderecoRepository.findById(Objects.requireNonNull(idEndereco))
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado para o id: " + idEndereco)));
    }

    @Override
    @Transactional(readOnly = true)
    public EnderecoResponse buscarPorNumeroELogradouro(String numero, String logradouroNome) {
        return EnderecoResponse.of(enderecoRepository.findByNumeroAndLogradouroIgnoreCase(numero, logradouroNome)
                .orElseThrow(() -> new EntityNotFoundException("Endereco não encontrado para os parametros: " + numero + " e " + logradouroNome)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnderecoResponse> buscarPorFiltro(String bairro, String cepCodigo, String logradouro,
            TipoLogradouro tipoLogradouro) {
        List<Endereco> enderecos = enderecoRepository.findAll();

        if (bairro != null && !bairro.isBlank()) {
            enderecos = enderecos.stream().filter(e -> e.getBairro().toLowerCase().contains(bairro.toLowerCase()))
                    .toList();
        }
        if (cepCodigo != null && !cepCodigo.isBlank()) {
            enderecos = enderecos.stream().filter(e -> e.getCep().getCodigo().equalsIgnoreCase(cepCodigo))
                    .toList();
        }
        if (logradouro != null && !logradouro.isBlank()) {
            enderecos = enderecos.stream().filter(e -> e.getLogradouro().toLowerCase().contains(logradouro.toLowerCase()))
                    .toList();
        }
        if (tipoLogradouro != null) {
            enderecos = enderecos.stream().filter(e -> e.getTipoLogradouro().equals(tipoLogradouro))
                    .toList();
        }

        return enderecos.stream()
                .map(EnderecoResponse::of)
                .toList();
    }

}
