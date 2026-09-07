package com.bookstore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // =========================
    // PASSWORD ENCODER
    // =========================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =========================
    // SECURITY CONFIGURATION
    // =========================

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            // =========================
            // ALLOW ALL REQUESTS
            // =========================
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // =========================
            // CSRF DISABLED
            // =========================
            .csrf(csrf -> csrf.disable())

            // =========================
            // SPRING SECURITY LOGIN DISABLED
            // LoginController handles login
            // =========================
            .formLogin(form -> form.disable())

            // =========================
            // HTTP BASIC DISABLED
            // =========================
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}