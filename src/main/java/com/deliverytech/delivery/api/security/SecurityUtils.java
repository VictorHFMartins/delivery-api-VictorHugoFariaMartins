package com.deliverytech.delivery.api.security;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.deliverytech.delivery.domain.model.Usuario;

public class SecurityUtils {

    /** Retorna o UserPrincipal da sessão */
    public static UserPrincipal getCurrentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
            return principal;
        }

        throw new AccessDeniedException("Usuário não autenticado");
    }

    /** Retorna o usuario autenticado */
    public static Usuario getCurrentUser() {
        return getCurrentPrincipal().getUsuario();
    }

    /** ID do usuário */
    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    /** Email do usuário */
    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }

    /** TipoUsuario (ROLE) */
    public static String getCurrentUserTipoUsuario() {
        return getCurrentUser().getTipoUsuario().name();
    }

    /** Verifica Tipo de Usuario */
    public static boolean hasTipoUsuario(String tipoUsuario) {
        return getCurrentUser().getTipoUsuario().name().equalsIgnoreCase(tipoUsuario);
    }

    public static boolean isCliente() {
        return hasTipoUsuario("CLIENTE");
    }

    public static boolean isAdministrador() {
        return hasTipoUsuario("ADMINISTRADOR");
    }

    public static boolean isAtendente() {
        return hasTipoUsuario("RESTAURANTE");
    }
}
