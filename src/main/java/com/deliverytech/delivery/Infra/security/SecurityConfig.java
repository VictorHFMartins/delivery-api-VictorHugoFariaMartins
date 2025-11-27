package com.deliverytech.delivery.Infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.deliverytech.delivery.Infra.Jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/h2-console/**"
                        ).permitAll()

                        // ESPECÍFICO PRIMEIRO → avaliações precisam liberar CLIENTE
                        .requestMatchers("/restaurantes/*/avaliacoes/**")
                                .hasAnyRole("CLIENTE", "RESTAURANTE", "ADMINISTRADOR")

                        // CLIENTES
                        .requestMatchers("/clientes/**")
                                .hasAnyRole("CLIENTE", "ADMINISTRADOR")

                        // RESTAURANTES
                        .requestMatchers("/restaurantes/**")
                                .hasAnyRole("RESTAURANTE", "ADMINISTRADOR")

                        // PRODUTOS
                        .requestMatchers("/produtos/**")
                                .hasAnyRole("RESTAURANTE", "ADMINISTRADOR")

                        // PEDIDOS
                        .requestMatchers("/pedidos/cliente/**")
                                .hasAnyRole("CLIENTE", "ADMINISTRADOR")

                        .requestMatchers("/pedidos/restaurante/**")
                                .hasAnyRole("RESTAURANTE", "ADMINISTRADOR")

                        .requestMatchers("/pedidos/**")
                                .hasRole("ADMINISTRADOR")

                        // SOMENTE ADMIN
                        .requestMatchers(
                                "/enderecos/**",
                                "/telefones/**",
                                "/estados/**",
                                "/cidades/**",
                                "/ceps/**"
                        ).hasRole("ADMINISTRADOR")

                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
