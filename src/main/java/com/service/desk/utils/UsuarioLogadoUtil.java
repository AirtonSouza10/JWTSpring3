package com.service.desk.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.service.desk.entidade.Usuario;

public class UsuarioLogadoUtil {
    private UsuarioLogadoUtil() {
    }

    public static Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof Usuario usuario) {
                return usuario;
            }
        }
        return null;
    }

    public static Long getIdUsuarioLogado() {
        Usuario usuario = getUsuarioLogado();
        return usuario != null ? usuario.getId() : null;
    }
}
