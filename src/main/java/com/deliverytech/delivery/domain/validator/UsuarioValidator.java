package com.deliverytech.delivery.domain.validator;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.enums.TipoUsuario;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.model.Usuario;
import com.deliverytech.delivery.domain.repository.ClienteRepository;
import com.deliverytech.delivery.domain.repository.RestauranteRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {

    private final ClienteRepository clienteRepository;
    private final RestauranteRepository restauranteRepository;

    public Usuario validarUsuario(Long usuarioId) {
        Objects.requireNonNull(usuarioId, "O id de usuário nunca deve ser nulo.");

        Optional<Cliente> cliente = clienteRepository.findById(usuarioId);
        if (cliente.isPresent()) {
            return cliente.get();
        }
        Optional<Restaurante> restaurante = restauranteRepository.findById(usuarioId);
        if (restaurante.isPresent()) {
            return restaurante.get();
        }

        throw new EntityNotFoundException("Usuario não encontrado com id: " + usuarioId);
    }

    public Cliente validarCliente(Long id) {

        Usuario usuario = validarUsuario(id);

        if (usuario.getTipoUsuario() != TipoUsuario.CLIENTE) {
            throw new EntityNotFoundException(
                    "O id " + id + " não pertence a um Cliente."
            );
        }

        return clienteRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new EntityNotFoundException(
                "Cliente não encontrado para o id: " + id
        ));
    }

    public Restaurante validarRestaurante(Long id) {

        Usuario usuario = validarUsuario(id);

        if (usuario.getTipoUsuario() != TipoUsuario.RESTAURANTE) {
            throw new EntityNotFoundException(
                    "O id " + id + " não pertence a um Restaurante."
            );
        }

        return restauranteRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new EntityNotFoundException(
                "Restaurante não encontrado para o id: " + id
        ));
    }

}
