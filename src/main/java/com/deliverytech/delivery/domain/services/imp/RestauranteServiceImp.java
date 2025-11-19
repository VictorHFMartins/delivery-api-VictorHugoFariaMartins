package com.deliverytech.delivery.domain.services.imp;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.RestauranteRequest;
import com.deliverytech.delivery.api.dto.RestauranteResponse;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.model.Telefone;
import com.deliverytech.delivery.domain.repository.RestauranteRepository;
import com.deliverytech.delivery.domain.services.RestauranteService;
import com.deliverytech.delivery.domain.validator.EnderecoValidator;
import com.deliverytech.delivery.domain.validator.TelefoneValidator;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class RestauranteServiceImp implements RestauranteService {

    private final RestauranteRepository restauranteRepository;

    private final UsuarioValidator usuarioValidator;
    private final EnderecoValidator enderecoValidator;
    private final TelefoneValidator telefoneValidator;

    @Override
    public RestauranteResponse criar(RestauranteRequest dto) {

        if (restauranteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado" + dto.email());
        }
        var cep = enderecoValidator.validarOuCriarCep(dto.endereco().cepCodigo(), dto.endereco().cidadeId());
        var endereco = enderecoValidator.criarEndereco(dto.endereco(), cep);

        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.nome());
        restaurante.setCnpj(dto.cnpj());
        restaurante.setEmail(dto.email());
        restaurante.setEndereco(endereco);
        restaurante.setCategoria(dto.classe());
        restaurante.setEstado(dto.estado());
        restaurante.setHorarioAbertura(dto.horarioAbertura());
        restaurante.setHorarioFechamento(dto.horarioFechamento());
        restaurante.setTaxaEntrega(dto.taxaEntrega());

        var telefones = dto.telefones().stream()
                .map(t -> {
                    Telefone tel = new Telefone();
                    tel.setDdd(t.ddd());
                    tel.setNumero(telefoneValidator.formatarNumeroTelefone(t.numero()));
                    tel.setTipoTelefone(t.tipoTelefone());
                    tel.setUsuario(restaurante);
                    tel.setAtivo(true);
                    return tel;
                })
                .collect(Collectors.toList());

        restaurante.setTelefones(telefones);

        Restaurante restauranteSalvo = restauranteRepository.save(restaurante);
        return RestauranteResponse.of(restauranteSalvo);
    }

    @Override
    public RestauranteResponse alterar(Long id, RestauranteRequest dto) {
        Restaurante restauranteExistente = (Restaurante) usuarioValidator.validarUsuario(id);

        if (dto.email() == null || dto.email().isEmpty()) {
            throw new BusinessException("E-mail não pode ser vazio");
        }
        if (!restauranteExistente.getEmail().equals(dto.email())
                && restauranteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        if (dto.nome() == null || dto.nome().isEmpty()) {
            throw new BusinessException("Nome não pode ser vazio");
        }
        if (dto.telefones().isEmpty()) {
            throw new BusinessException("Deve existir ao menos um telefone");
        }
        if (dto.endereco() == null) {
            throw new BusinessException("EndereçoId não pode ser vazio");
        }
        if (dto.cnpj() == null) {
            throw new BusinessException("Cnpj não pode ser vazio");
        }
        if (dto.classe() == null) {
            throw new BusinessException("Categoria é obrigatória");
        }
        if (dto.estado() == null) {
            throw new BusinessException("Estado do Restaurante não pode ser vazio");
        }
        if (dto.horarioAbertura() == null) {
            throw new BusinessException("Horario de abertura não pode ser vazio");
        }
        if (dto.horarioFechamento() == null) {
            throw new BusinessException("Horario de fechamento não pode ser vazio");
        }
        if (dto.taxaEntrega() == null) {
            throw new BusinessException("taxaEntrega é obrigatória");
        }

        restauranteExistente.setNome(dto.nome());
        restauranteExistente.setEmail(dto.email());

        if (dto.telefones().isEmpty()) {
            throw new BusinessException("Deve existir ao menos um telefone");
        }

        restauranteExistente.getTelefones().clear();

        List<Telefone> novosTelefones = dto.telefones().stream()
                .map(t -> {
                    Telefone telefone = new Telefone();
                    telefone.setDdd(t.ddd());
                    telefone.setNumero(telefoneValidator.formatarNumeroTelefone(t.numero()));
                    telefone.setAtivo(true);
                    telefone.setTipoTelefone(t.tipoTelefone());
                    telefone.setUsuario(restauranteExistente);
                    return telefone;
                })
                .collect(Collectors.toList());

        restauranteExistente.getTelefones().addAll(novosTelefones);

        if (dto.endereco() == null) {
            throw new BusinessException("Endereço é obrigatório");
        }

        var cep = enderecoValidator.validarOuCriarCep(dto.endereco().cepCodigo(), dto.endereco().cidadeId());
        var novoEndereco = enderecoValidator.criarEndereco(dto.endereco(), cep);

        restauranteExistente.setEndereco(novoEndereco);
        restauranteExistente.setCnpj(dto.cnpj());
        restauranteExistente.setCategoria(dto.classe());
        restauranteExistente.setEstado(dto.estado());
        restauranteExistente.setHorarioAbertura(dto.horarioAbertura());
        restauranteExistente.setHorarioFechamento(dto.horarioFechamento());
        restauranteExistente.setTaxaEntrega(dto.taxaEntrega());

        restauranteRepository.save(restauranteExistente);

        return RestauranteResponse.of(restauranteExistente);
    }

    @Override
    public RestauranteResponse ativarInativar(Long restauranteId) {
        Restaurante restaurante = (Restaurante) usuarioValidator.validarUsuario(restauranteId);
        restaurante.setStatus(!restaurante.isStatus());

        return RestauranteResponse.of(restaurante);
    }

    @Override
    public void deletar(Long restauranteId) {
        restauranteRepository.deleteById(Objects.requireNonNull(restauranteId, "Restaurante não encontrado para o id: " + restauranteId));
    }

    @Override
    @Transactional(readOnly = true)
    public RestauranteResponse buscarPorId(Long restauranteId) {
        Restaurante restaurante = (Restaurante) usuarioValidator.validarUsuario(restauranteId);
        return RestauranteResponse.of(restaurante);
    }

    @Override
    @Transactional(readOnly = true)
    public RestauranteResponse buscarPorCnpj(String cnpj) {
        Restaurante restaurante = restauranteRepository.findByCnpjIgnoreCase(cnpj)
                .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado para o cnpj: " + cnpj));
        return RestauranteResponse.of(restaurante);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponse> listarTodos() {
        List<Restaurante> restaurantes = restauranteRepository.findAll();
        return restaurantes.stream()
                .map(RestauranteResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponse> listarPorRankingTop5() {
        List<Restaurante> restaurantes = restauranteRepository.findTop5ByOrderByNomeAsc();
        return restaurantes.stream()
                .map(RestauranteResponse::of)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestauranteResponse> buscarPorFiltro(
            String email,
            String numeroTelefone,
            BigDecimal taxaEntrega,
            String nome,
            LocalTime horarioAbertura,
            LocalTime horarioFechamento,
            CategoriaRestaurante categoriaRestaurante) {

        List<Restaurante> restaurantes = restauranteRepository.findAll();

        if (email != null && !email.isBlank()) {
            String emailNormalizado = email.trim().toLowerCase();
            restaurantes = restaurantes.stream()
                    .filter(r -> r.getEmail() != null
                    && r.getEmail().trim().toLowerCase().equals(emailNormalizado))
                    .toList();
        } else {
            System.out.println("email não encontrado para endereco: " + email);
        }
        if (numeroTelefone != null && !numeroTelefone.isBlank()) {
            restaurantes = restaurantes.stream()
                    .filter(r -> r.getTelefones().stream()
                    .anyMatch(t -> t.getNumero().contains(numeroTelefone)))
                    .toList();
        } else {
            System.out.println("numeroTelefone não encontrado para numero: " + numeroTelefone);
        }
        if (taxaEntrega != null) {
            restaurantes = restaurantes.stream()
                    .filter(r -> r.getTaxaEntrega().equals(taxaEntrega))
                    .toList();
        } else {
            System.out.println("taxaEntrega não encontrado para taxa: " + taxaEntrega);
        }
        if (nome != null && !nome.isBlank()) {
            restaurantes = restaurantes.stream()
                    .filter(r -> r.getNome().contains(nome))
                    .toList();
        } else {
            System.out.println("nomeRestaurante não encontrado para nome: " + nome);
        }
        if (horarioAbertura != null) {
            restaurantes = restaurantes.stream()
                    .filter(r -> r.getHorarioAbertura().equals(horarioAbertura))
                    .toList();
        } else {
            System.out.println("horarioAbertura não encontrado para horario: " + horarioAbertura);
        }

        if (horarioFechamento != null) {
            restaurantes = restaurantes.stream()
                    .filter(r -> r.getHorarioFechamento().equals(horarioFechamento))
                    .toList();
        } else {
            System.out.println("horarioFechamento não encontrado para horario: " + horarioFechamento);
        }
        if (categoriaRestaurante != null) {
            restaurantes = restaurantes.stream()
                    .filter(r -> r.getCategoria().equals(categoriaRestaurante))
                    .toList();
        } else {
            System.out.println("categoriaRestaurante não encontrado para categoria: " + categoriaRestaurante);
        }

        return restaurantes.stream()
                .map(RestauranteResponse::of)
                .toList();
    }

}
