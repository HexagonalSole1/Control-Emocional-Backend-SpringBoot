package com.example.APISkeleton.security;

import com.example.APISkeleton.security.exceptions.ExceptionAccessDeniedHandlerImpl;
import com.example.APISkeleton.security.exceptions.ExceptionAuthenticationEntryPointImpl;
import com.example.APISkeleton.security.filters.JWTVerifierFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JWTVerifierFilter jwtVerifierFilter;
    private final ExceptionAccessDeniedHandlerImpl accessDeniedHandler;
    private final ExceptionAuthenticationEntryPointImpl exceptionAuthenticationEntryPoint;

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    public SecurityConfig(JWTVerifierFilter jwtVerifierFilter, ExceptionAccessDeniedHandlerImpl accessDeniedHandler, ExceptionAuthenticationEntryPointImpl exceptionAuthenticationEntryPoint) {
        this.jwtVerifierFilter = jwtVerifierFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.exceptionAuthenticationEntryPoint = exceptionAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> corsConfiguration()))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtVerifierFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> {
                    exception.authenticationEntryPoint(exceptionAuthenticationEntryPoint);
                    exception.accessDeniedHandler(accessDeniedHandler);
                })
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "users").permitAll()
                                .requestMatchers(HttpMethod.POST, "auth/authenticate").permitAll()
                                .requestMatchers(HttpMethod.POST, "auth/refresh-token").permitAll()
                                .requestMatchers("/roles/**").hasRole("ADMIN")
                                .requestMatchers("jobs/**").permitAll()
                                .requestMatchers("job-applications/**").permitAll()
                                .requestMatchers("notifications/**").permitAll()
                                .requestMatchers("notificationsLog/**").permitAll()
                                // Nuevos endpoints para el diario emocional
                                .requestMatchers("/emotions/**").authenticated()
                                .requestMatchers("/emotion-records/**").authenticated()
                                .requestMatchers("/notes/**").authenticated()
                                .anyRequest().authenticated()
                );

        return http.build();
    }

    private CorsConfiguration corsConfiguration() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "HEAD", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization"));
        configuration.setExposedHeaders(List.of("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Authorization"));
        return configuration;
    }
}