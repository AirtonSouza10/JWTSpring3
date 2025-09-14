package com.service.desk.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.service.desk.entidade.Usuario;

@Service
public class TokenService {

    @Value("${api.security.token.secret}") 
    private String secret;

    public String generateToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // Adiciona ROLE_ para o Spring Security
            String roles = usuario.getRoles().stream()
                                  .map(role -> "ROLE_" + role.getNome().toUpperCase())
                                  .collect(Collectors.joining(","));

            return JWT.create()
                      .withIssuer("auth-api")
                      .withSubject(usuario.getLogin())
                      .withClaim("roles", roles)
                      .withExpiresAt(getExpirationDate())
                      .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                      .withIssuer("auth-api")
                      .build()
                      .verify(token)
                      .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public List<String> getRolesFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        var decodedJWT = JWT.require(algorithm)
                            .withIssuer("auth-api")
                            .build()
                            .verify(token);

        String rolesString = decodedJWT.getClaim("roles").asString(); // "ROLE_ADMIN,ROLE_USER"
        return Arrays.stream(rolesString.split(","))
                     .toList();
    }
}
