package com.citpl.student.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final CorsConfig corsConfig;

    // ─────────────────────────────────────────────
    //  Public URLs — No JWT needed
    // ─────────────────────────────────────────────

    private static final String[] PUBLIC_URLS = {

        // Auth endpoints
        "/api/auth/**",

        // Swagger URLs  ← Allowed without token
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-resources",
        "/swagger-resources/**",
        "/webjars/**"
    };

    //  Security Filter Chain

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ── Disable CSRF (REST API, not browser forms) ──
            .csrf(AbstractHttpConfigurer::disable)

            // ── Apply CORS config ──
            .cors(cors -> cors.configurationSource(
                    corsConfig.corsConfigurationSource()))

            // ── URL Authorization Rules ──
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(PUBLIC_URLS).permitAll()  // ← Public routes
                    .anyRequest().authenticated()              // ← All others need JWT
            )

            // ── Stateless Session (JWT based) ──
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // ── Set Custom AuthenticationProvider ──
            .authenticationProvider(authenticationProvider())

            // ── Add JWT Filter before Spring's default filter ──
            .addFilterBefore(jwtAuthFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //  Authentication Provider

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);  // ← Your custom service
        provider.setPasswordEncoder(passwordEncoder());       // ← BCrypt encoder
        return provider;
    }

    //  Authentication Manager

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //  Password Encoder (BCrypt)
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}