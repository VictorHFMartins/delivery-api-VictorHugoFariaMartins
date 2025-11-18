package com.deliverytech.delivery.domain.services.imp;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.TelefoneRequest;
import com.deliverytech.delivery.api.dto.TelefoneResponse;
import com.deliverytech.delivery.api.dto.TelefoneUpdateRequest;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.enums.TipoTelefone;
import com.deliverytech.delivery.domain.enums.TipoUsuario;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.model.Telefone;
import com.deliverytech.delivery.domain.model.Usuario;
import com.deliverytech.delivery.domain.repository.ClienteRepository;
import com.deliverytech.delivery.domain.repository.RestauranteRepository;
import com.deliverytech.delivery.domain.repository.TelefoneRepository;
import com.deliverytech.delivery.domain.services.TelefoneService;
import com.deliverytech.delivery.domain.validator.TelefoneValidator;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class TelefoneServiceImp implements TelefoneService {

    private final ClienteRepository clienteRepository;
    private final TelefoneRepository telefoneRepository;
    private final RestauranteRepository restauranteRepository;

    private final UsuarioValidator usuarioValidator;
    private final TelefoneValidator telefoneValidator;

    private final ModelMapper modelMapper;

    @Override
    public Telefone buscarOuCriarTelefone(TelefoneRequest dto) {

        String numero = dto.numero().replaceAll("[^0-9]", "").trim();
        String ddd = dto.ddd().trim();

        Optional<Telefone> existente = telefoneRepository.findByDddAndNumero(ddd, numero);

        if (existente.isPresent()) {
            return existente.get();
        }

        Telefone telefone = modelMapper.map(dto, Telefone.class);
        if (telefone == null) {
            throw new BusinessException("Erro ao mapear Telefone a partir do DTO");
        }
        telefone.setAtivo(true);

        return telefoneRepository.save(telefone);
    }

    @Override
    public TelefoneResponse criar(Long usuarioId, TelefoneRequest dto) {
        Usuario usuario = usuarioValidator.validarUsuario(usuarioId);

        Telefone telefone = buscarOuCriarTelefone(dto);

        telefone.setUsuario(usuario);
        telefoneRepository.save(telefone);

        usuario.getTelefones().add(telefone);
        if (usuario instanceof Cliente c) {
            clienteRepository.save(c);
        }
        if (usuario instanceof Restaurante r) {
            restauranteRepository.save(r);
        }
        return TelefoneResponse.of(telefone);
    }

    @Override
    public TelefoneResponse atualizar(Long usuarioId, Long telefoneId, TelefoneUpdateRequest updateDto) {

        Usuario usuario = usuarioValidator.validarUsuario(usuarioId);
        Telefone telefoneExistente = telefoneValidator.validarTelefone(telefoneId);
        telefoneValidator.verificarTelefoneUsuario(usuario, telefoneExistente);

        telefoneExistente = modelMapper.map(updateDto, Telefone.class);

        telefoneRepository.save(Objects.requireNonNull(telefoneExistente));

        return modelMapper.map(telefoneExistente, TelefoneResponse.class);

    }

    @Override
    public void remover(Long usuarioId, Long telefoneId) {
        Usuario usuario = usuarioValidator.validarUsuario(usuarioId);
        Telefone telefone = telefoneValidator.validarTelefone(telefoneId);

        telefoneValidator.verificarTelefoneUsuario(usuario, telefone);

        usuario.getTelefones().removeIf(t -> t.getId().equals(telefoneId));

        telefoneRepository.delete(Objects.requireNonNull(telefone));

        if (usuario instanceof Cliente c) {
            clienteRepository.save(c);
        }
        if (usuario instanceof Restaurante r) {
            restauranteRepository.save(r);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TelefoneResponse buscarTelefonePorId(long telefoneId) {
        Telefone telefone = telefoneRepository.findById(telefoneId).
                orElseThrow(() -> new EntityNotFoundException("Telefone não encontrado"));

        return TelefoneResponse.of(telefone);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TelefoneResponse> listarNumerosAtivos() {
        List<Telefone> telefone = telefoneRepository.findByAtivoTrue();

        return telefone.stream().
                map(TelefoneResponse::of).
                toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TelefoneResponse> listarTodos() {
        List<Telefone> telefones = telefoneRepository.findAll();
        return telefones.stream().map(TelefoneResponse::of).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TelefoneResponse> buscarPorFiltro(String ddd, Long usuarioId, String numero, TipoUsuario tipoUsuario, TipoTelefone tipoTelefone) {
        List<Telefone> telefones = telefoneRepository.findAll();

        if (ddd != null && !ddd.isBlank()) {
            telefones = telefones.stream().filter(t -> t.getDdd().equalsIgnoreCase(ddd))
                    .toList();
        }
        if (usuarioId != null) {
            telefones = telefones.stream().filter(t -> t.getUsuario().getId().equals(usuarioId))
                    .toList();
        }
        if (numero != null && !numero.isBlank()) {
            telefones = telefones.stream().filter(t -> t.getNumero().equalsIgnoreCase(numero))
                    .toList();
        }
        if (tipoUsuario != null) {
            telefones = telefones.stream().filter(t -> t.getTipoUsuario().equals(tipoUsuario))
                    .toList();
        }
        if (tipoTelefone != null) {
            telefones = telefones.stream().filter(t -> t.getTipoTelefone().equals(tipoTelefone))
                    .toList();
        }

        return telefones.stream()
                .map(TelefoneResponse::of)
                .toList();
    }
}
