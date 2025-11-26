package com.deliverytech.delivery.domain.services.imp;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.AdministradorRequest;
import com.deliverytech.delivery.api.dto.AdministradorResponse;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.model.Administrador;
import com.deliverytech.delivery.domain.model.Telefone;
import com.deliverytech.delivery.domain.repository.AdministradorRepository;
import com.deliverytech.delivery.domain.services.AdministradorService;
import com.deliverytech.delivery.domain.validator.EnderecoValidator;
import com.deliverytech.delivery.domain.validator.TelefoneValidator;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdministradorServiceImp implements AdministradorService {

    private final AdministradorRepository administradorRepository;

    private final UsuarioValidator usuarioValidator;
    private final TelefoneValidator telefoneValidator;
    private final EnderecoValidator enderecoValidator;

    @Override
    public AdministradorResponse criar(AdministradorRequest dto) {
        if (administradorRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado: " + dto.email());
        }

        var cep = enderecoValidator.validarOuCriarCep(dto.endereco().cepCodigo(), dto.endereco().cidadeId());
        var endereco = enderecoValidator.criarEndereco(dto.endereco(), cep);

        Administrador administrador = new Administrador();
        administrador.setNome(dto.nome());
        administrador.setEmail(dto.email());
        administrador.setEndereco(endereco);
        administrador.setStatus(true);

        var telefones = dto.telefones().stream()
                .map(t -> {
                    Telefone tel = new Telefone();
                    tel.setDdd(t.ddd());
                    tel.setNumero(telefoneValidator.formatarNumeroTelefone(t.numero()));
                    tel.setTipoTelefone(t.tipoTelefone());
                    tel.setUsuario(administrador);
                    tel.setAtivo(true);
                    return tel;
                })
                .collect(Collectors.toList());

        administrador.setTelefones(telefones);

        Administrador administradoresalvo = administradorRepository.save(administrador);
        return AdministradorResponse.of(administradoresalvo);
    }

    @Override
    public AdministradorResponse atualizar(Long id, AdministradorRequest dto) {
        Administrador administradorExistente = usuarioValidator.validarAdministrador(id);

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new BusinessException("O nome não pode ser vazio");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new BusinessException("O e-mail não pode ser vazio");
        }
        if (!administradorExistente.getEmail().equalsIgnoreCase(dto.email())
                && administradorRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        administradorExistente.setNome(dto.nome());
        administradorExistente.setEmail(dto.email());

        if (dto.telefones().isEmpty()) {
            throw new BusinessException("Deve existir ao menos um telefone");
        }

        administradorExistente.getTelefones().clear();

        List<Telefone> novosTelefones = dto.telefones().stream()
                .map(t -> {
                    Telefone telefone = new Telefone();
                    telefone.setDdd(t.ddd());
                    telefone.setNumero(telefoneValidator.formatarNumeroTelefone(t.numero()));
                    telefone.setAtivo(true);
                    telefone.setTipoTelefone(t.tipoTelefone());
                    telefone.setUsuario(administradorExistente);
                    return telefone;
                })
                .collect(Collectors.toList());

        administradorExistente.getTelefones().addAll(novosTelefones);

        if (dto.endereco() == null) {
            throw new BusinessException("Endereço é obrigatório");
        }

        var cep = enderecoValidator.validarOuCriarCep(dto.endereco().cepCodigo(), dto.endereco().cidadeId());
        var novoEndereco = enderecoValidator.criarEndereco(dto.endereco(), cep);

        administradorExistente.setEndereco(novoEndereco);

        administradorRepository.save(administradorExistente);

        return AdministradorResponse.of(administradorExistente);
    }

    @Override
    public AdministradorResponse ativarDesativar(Long administradorId) {
        Administrador administrador = usuarioValidator.validarAdministrador(administradorId);
        administrador.setStatus(!administrador.isStatus());

        return AdministradorResponse.of(administrador);
    }

    @Override
    public void excluir(Long administradorId) {
        Long id = Objects.requireNonNull(administradorId, "O id do administrador não pode ser nulo.");

        if (!administradorRepository.existsById(id)) {
            throw new EntityNotFoundException("Administrador não encontrado para o id: " + id);
        }

        administradorRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AdministradorResponse buscarPorId(long id) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado."));
        return AdministradorResponse.of(administrador);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdministradorResponse> listarPorStatusAtivo() {
        List<Administrador> administradores = administradorRepository.findByStatusTrue();

        return administradores.stream()
                .map(AdministradorResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdministradorResponse> listarTodos() {
        List<Administrador> administradores = administradorRepository.findAll();

        return administradores.stream()
                .map(AdministradorResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdministradorResponse> buscarComFiltros(String nome, String email, String cep, String cidade, String estado, String telefone) {

        List<Administrador> administradores = administradorRepository.findAll();

        if (nome != null && !nome.isBlank()) {
            administradores = administradores.stream()
                    .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }

        if (email != null && !email.isBlank()) {
            administradores = administradores.stream()
                    .filter(c -> c.getEmail().equalsIgnoreCase(email))
                    .toList();
        }

        if (cep != null && !cep.isBlank()) {
            administradores = administradores.stream()
                    .filter(c -> c.getEndereco().getCep().getCodigo().equals(cep))
                    .toList();
        }

        if (cidade != null && !cidade.isBlank()) {
            administradores = administradores.stream()
                    .filter(c -> c.getEndereco().getCep().getCidade().getNome().equalsIgnoreCase(cidade))
                    .toList();
        }

        if (estado != null && !estado.isBlank()) {
            administradores = administradores.stream()
                    .filter(c -> c.getEndereco().getCep().getCidade().getEstado().getUf().equalsIgnoreCase(estado))
                    .toList();
        }

        if (telefone != null && !telefone.isBlank()) {
            administradores = administradores.stream()
                    .filter(c -> c.getTelefones().stream()
                    .anyMatch(t -> t.getNumero().contains(telefone)))
                    .toList();
        }

        return administradores.stream()
                .map(AdministradorResponse::of)
                .toList();
    }
}
