package com.deliverytech.delivery.domain.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.api.security.UserPrincipal;
import com.deliverytech.delivery.domain.model.Usuario;
import com.deliverytech.delivery.domain.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String apelido) {
        Usuario usuario = usuarioRepository
                .findByApelido(apelido)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new UserPrincipal(usuario);
    }
}
