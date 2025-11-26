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

    /** Cargo (ROLE) */
    public static String getCurrentUserCargo() {
        return getCurrentUser().getTipoUsuario().name();
    }

    /** Verifica cargo */
    public static boolean hasCargo(String cargo) {
        return getCurrentUser().getTipoUsuario().name().equalsIgnoreCase(cargo);
    }

    public static boolean isCliente() {
        return hasCargo("CLIENTE");
    }

    public static boolean isAdministrador() {
        return hasCargo("ADMINISTRADOR");
    }

    public static boolean isAtendente() {
        return hasCargo("RESTAURANTE");
    }
}
