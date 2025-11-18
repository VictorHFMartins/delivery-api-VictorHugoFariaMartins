package com.deliverytech.delivery.domain.services.imp;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.ClienteRequest;
import com.deliverytech.delivery.api.dto.ClienteResponse;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Endereco;
import com.deliverytech.delivery.domain.model.Telefone;
import com.deliverytech.delivery.domain.repository.ClienteRepository;
import com.deliverytech.delivery.domain.repository.TelefoneRepository;
import com.deliverytech.delivery.domain.services.ClienteService;
import com.deliverytech.delivery.domain.validator.EnderecoValidator;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteServiceImp implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final TelefoneRepository telefoneRepository;

    private final UsuarioValidator usuarioValidator;
    private final EnderecoValidator enderecoValidator;

    private final ModelMapper modelMapper;

    @Override
    public ClienteResponse criar(ClienteRequest dto) {
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado" + dto.email());
        }

        Cliente cliente = modelMapper.map(dto, Cliente.class);
        if (cliente == null) {
            throw new BusinessException("Erro ao mapear Cliente a partir do DTO");
        }

        Cliente clienteSalvo = clienteRepository.save(cliente);

        ClienteResponse response = modelMapper.map(clienteSalvo, ClienteResponse.class);
        if (response == null) {
            throw new BusinessException("Erro ao mapear Cliente para ClienteResponse");
        }

        return response;
    }

    @Override
    public ClienteResponse atualizar(Long id, ClienteRequest dto) {
        Cliente clienteExistente = (Cliente) usuarioValidator.validarUsuario(id);

        if (dto.email() == null || dto.email().isEmpty()) {
            throw new BusinessException("E-mail não pode ser vazio");
        }
        if (!clienteExistente.getEmail().equals(dto.email())
                && clienteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (dto.nome() == null || dto.nome().isEmpty()) {
            throw new BusinessException("Nome não pode ser vazio");
        }
        if (dto.telefoneIds() == null) {
            throw new BusinessException("Deve existir ao menos um telefone");
        }
        if (dto.enderecoId() == null) {
            throw new BusinessException("EndereçoId não pode ser vazio");
        }

        clienteExistente.setNome(dto.nome());
        clienteExistente.setEmail(dto.email());

        if (!dto.telefoneIds().isEmpty()) {

            clienteExistente.getTelefones().clear();
            List<Telefone> novosTelefones = dto.telefoneIds().stream()
                    .filter(idTel -> idTel != null)
                    .map(idTel -> telefoneRepository.findById(Objects.requireNonNull(idTel))
                    .orElseThrow(() -> new EntityNotFoundException("Telefone não encontrado: " + idTel)))
                    .peek(t -> t.setUsuario(clienteExistente))
                    .collect(Collectors.toList());

            clienteExistente.getTelefones().addAll(novosTelefones);
        }
        Endereco endereco = enderecoValidator.validarEndereco(dto.enderecoId());

        clienteExistente.setEndereco(endereco);
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
    @Transactional(readOnly=true)
    public ClienteResponse buscarPorId(long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Id não encontrado."));
        return ClienteResponse.of(cliente);
    }

    @Override
    @Transactional(readOnly=true)
    public List<ClienteResponse> listarPorStatusAtivo() {
        List<Cliente> clientes = clienteRepository.findByStatusTrue();

        return clientes.stream()
                .map(ClienteResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly=true)
    public List<ClienteResponse> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();

        return clientes.stream()
                .map(ClienteResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly=true)
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
