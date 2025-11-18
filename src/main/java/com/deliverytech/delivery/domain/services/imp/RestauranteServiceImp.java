package com.deliverytech.delivery.domain.services.imp;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.RestauranteRequest;
import com.deliverytech.delivery.api.dto.RestauranteResponse;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.enums.CategoriaRestaurante;
import com.deliverytech.delivery.domain.model.Endereco;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.model.Telefone;
import com.deliverytech.delivery.domain.repository.RestauranteRepository;
import com.deliverytech.delivery.domain.repository.TelefoneRepository;
import com.deliverytech.delivery.domain.services.RestauranteService;
import com.deliverytech.delivery.domain.validator.EnderecoValidator;
import com.deliverytech.delivery.domain.validator.UsuarioValidator;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class RestauranteServiceImp implements RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final TelefoneRepository telefoneRepository;

    private final UsuarioValidator usuarioValidator;
    private final EnderecoValidator enderecoValidator;

    private final ModelMapper modelMapper;

    @Override
    public RestauranteResponse criar(RestauranteRequest dto) {
        if (restauranteRepository.existsByEmail(dto.email())) {
            throw new BusinessException("E-mail já cadastrado" + dto.email());
        }

        Restaurante restaurante = modelMapper.map(dto, Restaurante.class);
        if (restaurante == null) {
            throw new BusinessException("Erro ao mapear restaurante a partir do DTO");
        }

        Restaurante retauranteSalvo = restauranteRepository.save(restaurante);

        RestauranteResponse response = modelMapper.map(retauranteSalvo, RestauranteResponse.class);
        if (response == null) {
            throw new BusinessException("Erro ao mapear Restaurante para RestauranteResponse");
        }

        return response;
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
        if (dto.telefoneIds() == null) {
            throw new BusinessException("Deve existir ao menos um telefone");
        }
        if (dto.enderecoId() == null) {
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

        if (!dto.telefoneIds().isEmpty()) {

            restauranteExistente.getTelefones().clear();
            List<Telefone> novosTelefones = dto.telefoneIds().stream()
                    .filter(idTel -> idTel != null)
                    .map(idTel -> telefoneRepository.findById(Objects.requireNonNull(idTel))
                    .orElseThrow(() -> new EntityNotFoundException("Telefone não encontrado: " + idTel)))
                    .peek(t -> t.setUsuario(restauranteExistente))
                    .collect(Collectors.toList());

            restauranteExistente.getTelefones().addAll(novosTelefones);
        }
        Endereco endereco = enderecoValidator.validarEndereco(dto.enderecoId());

        restauranteExistente.setEndereco(endereco);
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
            String nomeRestaurante,
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
        if (nomeRestaurante != null && !nomeRestaurante.isBlank()) {
            restaurantes = restaurantes.stream()
                    .filter(r -> r.getNome().equalsIgnoreCase(nomeRestaurante))
                    .toList();
        } else {
            System.out.println("nomeRestaurante não encontrado para nome: " + nomeRestaurante);
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
