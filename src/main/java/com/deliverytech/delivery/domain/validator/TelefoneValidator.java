package com.deliverytech.delivery.domain.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery.domain.model.Telefone;
import com.deliverytech.delivery.domain.model.Usuario;
import com.deliverytech.delivery.domain.repository.TelefoneRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TelefoneValidator {

    private final TelefoneRepository telefoneRepository;

    public Telefone validarTelefone(Long telefoneId) {

        return telefoneRepository.findById(Objects.requireNonNull(telefoneId)).
                orElseThrow(() -> new EntityNotFoundException("Telefone não encontrado com o id:" + telefoneId));
    }

    public void verificarTelefoneUsuario(Usuario usuario, Telefone telefone) {

        if (!telefone.getUsuario().getId().equals(usuario.getId())) {
            throw new BusinessException("Telefone não pertence a cliente especificado.");
        }
    }

    public String formatarNumeroTelefone(String numero) {
        numero = numero.replaceAll("\\D", "");

        return switch (numero.length()) {
            case 9 -> numero.substring(0, 5) + "-" + numero.substring(5);
            case 8 -> numero.substring(0, 4) + "-" + numero.substring(4);
            default -> numero;
        };
    }
}
