package com.deliverytech.delivery.api.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.api.dto.AuthRequest;
import com.deliverytech.delivery.api.dto.AuthResponse;
import com.deliverytech.delivery.api.dto.UsuarioRegisterRequest;
import com.deliverytech.delivery.api.exceptions.BusinessException;
import com.deliverytech.delivery.api.security.JwtUtil;
import com.deliverytech.delivery.api.security.UserPrincipal;
import com.deliverytech.delivery.domain.model.Administrador;
import com.deliverytech.delivery.domain.model.Cliente;
import com.deliverytech.delivery.domain.model.Restaurante;
import com.deliverytech.delivery.domain.model.Usuario;
import com.deliverytech.delivery.domain.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AuthResponse login(AuthRequest request) {

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.apelido(),
                            request.senha()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Apelido ou senha incorretos.");
        }

        Usuario usuario = usuarioRepository
                .findByApelido(request.apelido())
                .orElseThrow(() -> new RuntimeException("usuario não encontrado."));

        UserPrincipal principal = new UserPrincipal(usuario);
        String token = jwtUtil.generateToken(principal, usuario);

        return new AuthResponse(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getApelido(),
                usuario.getTipoUsuario().name(),
                usuario.getEmail());
    }

    public AuthResponse register(UsuarioRegisterRequest request) {

        if (usuarioRepository.existsByApelido(request.apelido())) {
            throw new BusinessException("Este apelido já está em uso.");
        }
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BusinessException("Este e-mail já está em uso.");
        }

        Usuario usuario = switch (request.tipoUsuario()) {
            case CLIENTE -> new Cliente();
            case RESTAURANTE -> new Restaurante();
            case ADMINISTRADOR -> new Administrador();
            default -> throw new BusinessException("Tipo de usuário inválido.");
        };

        usuario.setNome(request.nome());
        usuario.setApelido(request.apelido());
        usuario.setEmail(request.email());
        usuario.setTipoUsuario(request.tipoUsuario());
        usuario.setSenha(passwordEncoder.encode(request.senha()));

        usuarioRepository.save(usuario);

        UserPrincipal principal = new UserPrincipal(usuario);
        String token = jwtUtil.generateToken(principal, usuario);

        return new AuthResponse(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getApelido(),
                usuario.getTipoUsuario().name(),
                usuario.getEmail());
    }
}
