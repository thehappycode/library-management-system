package com.library.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Base Security Configuration
 * Services should extend this class and customize as needed
 * 
 * Provides common security configurations:
 * - Password encoder (BCrypt)
 * - Stateless session management
 * - CORS configuration
 * - CSRF protection
 */
@EnableMethodSecurity
public abstract class SecurityConfig {
    
    /**
     * Password encoder bean
     * Uses BCrypt hashing algorithm
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Base security filter chain configuration
     * Services can override this method to customize security rules
     * 
     * Default configuration:
     * - Stateless session management
     * - CSRF disabled (for REST APIs)
     * - CORS enabled
     * 
     * Override this method in service-specific config and customize as needed
     */
    protected HttpSecurity configureHttpSecurity(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .sessionManagement(session -> 
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
    }
    
    /**
     * Get public endpoints that don't require authentication
     * Services should override this to define their public endpoints
     */
    protected abstract String[] getPublicEndpoints();
    
    /**
     * Get admin-only endpoints
     * Services can override this to define admin-restricted endpoints
     */
    protected String[] getAdminEndpoints() {
        return new String[]{"/api/admin/**"};
    }
    
    /**
     * Default security filter chain
     * Override this in service implementation for custom security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http = configureHttpSecurity(http);
        
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(getPublicEndpoints()).permitAll()
                .requestMatchers(getAdminEndpoints()).hasRole("ADMIN")
                .anyRequest().authenticated()
        );
        
        return http.build();
    }
}
