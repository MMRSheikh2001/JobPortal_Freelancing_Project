package com.wordbridge.project.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService detailsService;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/**",
                                "/api/auth/login"

                        ).permitAll()


                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();


    }


    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(detailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;

    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        // CORS for SSLCommerz callbacks
        CorsConfiguration paymentCallbackConfig = new CorsConfiguration();
        paymentCallbackConfig.setAllowedOriginPatterns(List.of("*"));
        paymentCallbackConfig.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        paymentCallbackConfig.setAllowedHeaders(List.of("*"));
        paymentCallbackConfig.setAllowCredentials(false);

        // CORS for Angular
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://192.168.88.250:4200",
                "http://localhost:8090",
                "http://127.0.0.1:8090",
                "http://10.0.2.2:8090"
        ));
        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Cache-Control",
                "Content-Type"
        ));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        // SSLCommerz callback endpoints
        source.registerCorsConfiguration(
                "/api/payments/success",
                paymentCallbackConfig
        );

        source.registerCorsConfiguration(
                "/api/payments/fail",
                paymentCallbackConfig
        );

        source.registerCorsConfiguration(
                "/api/payments/cancel",
                paymentCallbackConfig
        );

        // Angular APIs
        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

}
