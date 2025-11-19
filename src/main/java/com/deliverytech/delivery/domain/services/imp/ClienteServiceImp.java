package com.deliverytech.delivery.domain.services.imp;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.ClienteRequest;
import com.deliverytech.delivery.api.dto.ClienteResponse;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Telefone;
import com.deliverytech.delivery.domain.repository.ClienteRepository;
import com.deliverytech.delivery.domain.services.ClienteService;
import com.deliverytech.delivery.domain.validator.EnderecoValidator;
import com.deliverytech.delivery.domain.validator.TelefoneValidator;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteServiceImp implements ClienteService {

    private final ClienteRepository clienteRepository;

    private final UsuarioValidator usuarioValidator;
    private final TelefoneValidator telefoneValidator;
    private final EnderecoValidator enderecoValidator;

    @Override
    public ClienteResponse criar(ClienteRequest dto) {
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado: " + dto.email());
        }

        var cep = enderecoValidator.validarOuCriarCep(dto.endereco().cepCodigo(), dto.endereco().cidadeId());
        var endereco = enderecoValidator.criarEndereco(dto.endereco(), cep);

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setEndereco(endereco);
        cliente.setStatus(true);

        var telefones = dto.telefones().stream()
                .map(t -> {
                    Telefone tel = new Telefone();
                    tel.setDdd(t.ddd());
                    tel.setNumero(telefoneValidator.formatarNumeroTelefone(t.numero()));
                    tel.setTipoTelefone(t.tipoTelefone());
                    tel.setUsuario(cliente);
                    tel.setAtivo(true);
                    return tel;
                })
                .collect(Collectors.toList());

        cliente.setTelefones(telefones);

        Cliente clienteSalvo = clienteRepository.save(cliente);
        return ClienteResponse.of(clienteSalvo);
    }

    @Override
    public ClienteResponse atualizar(Long id, ClienteRequest dto) {
        Cliente clienteExistente = (Cliente) usuarioValidator.validarUsuario(id);

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new BusinessException("O nome não pode ser vazio");
        }
        if (dto.email() == null || dto.email().isBlank()) {
            throw new BusinessException("O e-mail não pode ser vazio");
        }
        if (!clienteExistente.getEmail().equalsIgnoreCase(dto.email())
                && clienteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        clienteExistente.setNome(dto.nome());
        clienteExistente.setEmail(dto.email());

        if (dto.telefones().isEmpty()) {
            throw new BusinessException("Deve existir ao menos um telefone");
        }

        clienteExistente.getTelefones().clear();

        List<Telefone> novosTelefones = dto.telefones().stream()
                .map(t -> {
                    Telefone telefone = new Telefone();
                    telefone.setDdd(t.ddd());
                    telefone.setNumero(telefoneValidator.formatarNumeroTelefone(t.numero()));
                    telefone.setAtivo(true);
                    telefone.setTipoTelefone(t.tipoTelefone());
                    telefone.setUsuario(clienteExistente);
                    return telefone;
                })
                .collect(Collectors.toList());

        clienteExistente.getTelefones().addAll(novosTelefones);

        if (dto.endereco() == null) {
            throw new BusinessException("Endereço é obrigatório");
        }

        var cep = enderecoValidator.validarOuCriarCep(dto.endereco().cepCodigo(), dto.endereco().cidadeId());
        var novoEndereco = enderecoValidator.criarEndereco(dto.endereco(), cep);

        clienteExistente.setEndereco(novoEndereco);

        clienteRepository.save(clienteExistente);

        return ClienteResponse.of(clienteExistente);
    }

    @Override
    public ClienteResponse ativarDesativar(Long clienteId) {
        Cliente cliente = (Cliente) usuarioValidator.validarUsuario(clienteId);
        cliente.setStatus(!cliente.isStatus());

        return ClienteResponse.of(cliente);
    }

    @Override
    public void excluir(Long idCliente) {
        Long id = Objects.requireNonNull(idCliente);

        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrado para o id: " + id);
        }

        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado."));
        return ClienteResponse.of(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listarPorStatusAtivo() {
        List<Cliente> clientes = clienteRepository.findByStatusTrue();

        return clientes.stream()
                .map(ClienteResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();

        return clientes.stream()
                .map(ClienteResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> buscarComFiltros(String nome, String email, String cep, String cidade, String estado, String telefone) {

        List<Cliente> clientes = clienteRepository.findAll();

        if (nome != null && !nome.isBlank()) {
            clientes = clientes.stream()
                    .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        }

        if (email != null && !email.isBlank()) {
            clientes = clientes.stream()
                    .filter(c -> c.getEmail().equalsIgnoreCase(email))
                    .toList();
        }

        if (cep != null && !cep.isBlank()) {
            clientes = clientes.stream()
                    .filter(c -> c.getEndereco().getCep().getCodigo().equals(cep))
                    .toList();
        }

        if (cidade != null && !cidade.isBlank()) {
            clientes = clientes.stream()
                    .filter(c -> c.getEndereco().getCep().getCidade().getNome().equalsIgnoreCase(cidade))
                    .toList();
        }

        if (estado != null && !estado.isBlank()) {
            clientes = clientes.stream()
                    .filter(c -> c.getEndereco().getCep().getCidade().getEstado().getUf().equalsIgnoreCase(estado))
                    .toList();
        }

        if (telefone != null && !telefone.isBlank()) {
            clientes = clientes.stream()
                    .filter(c -> c.getTelefones().stream()
                    .anyMatch(t -> t.getNumero().contains(telefone)))
                    .toList();
        }

        return clientes.stream()
                .map(ClienteResponse::of)
                .toList();
    }
}
