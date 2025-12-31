# Common Security Module

This module contains shared security components for authentication and authorization across all microservices.

## Purpose

The `common-security` module provides:
- JWT token generation and validation utilities
- Custom UserDetails implementation (UserPrincipal)
- JWT authentication filter base class
- Base security configuration with best practices

## Contents

### Security Components
- **JwtUtil**: Utility for JWT token operations (generation, validation, parsing)
- **UserPrincipal**: Custom UserDetails implementation for authenticated users
- **JwtAuthenticationFilter**: Base filter for JWT authentication
- **SecurityConfig**: Base security configuration class

## Usage

### 1. Add Dependency

```xml
<dependency>
    <groupId>com.library</groupId>
    <artifactId>common-security</artifactId>
</dependency>
```

### 2. Configure JWT Utility

```java
@Configuration
public class JwtConfiguration {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:3600000}") // 1 hour default
    private long jwtExpiration;
    
    @Value("${jwt.refresh-expiration:86400000}") // 24 hours default
    private long refreshExpiration;
    
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(jwtSecret, jwtExpiration, refreshExpiration);
    }
}
```

### 3. Implement JWT Authentication Filter

```java
@Component
public class ServiceJwtAuthenticationFilter extends JwtAuthenticationFilter {
    
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected JwtUtil getJwtUtil() {
        return jwtUtil;
    }
    
    @Override
    protected UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }
}
```

### 4. Extend Security Configuration

```java
@Configuration
@EnableWebSecurity
public class ServiceSecurityConfig extends SecurityConfig {
    
    private final ServiceJwtAuthenticationFilter jwtAuthFilter;
    
    @Override
    protected String[] getPublicEndpoints() {
        return new String[]{
            "/api/auth/**",
            "/actuator/health",
            "/swagger-ui/**",
            "/v3/api-docs/**"
        };
    }
    
    @Bean
    @Override
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http = configureHttpSecurity(http);
        
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(getPublicEndpoints()).permitAll()
                .anyRequest().authenticated()
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

### 5. Use UserPrincipal

```java
@Service
public class UserService {
    
    public UserPrincipal loadUser(User user) {
        return UserPrincipal.createDefault(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            List.of(user.getRole())
        );
    }
}
```

## Features

- **JWT Token Management**: Generate and validate JWT tokens
- **Secure Password Encoding**: BCrypt password encoder
- **Stateless Authentication**: Session-less JWT-based authentication
- **Role-Based Access Control**: Support for role-based authorization
- **Customizable**: Base classes can be extended for service-specific needs

## Configuration Properties

Add these properties to your `application.yml`:

```yaml
jwt:
  secret: ${JWT_SECRET:your-256-bit-secret-key-here-must-be-at-least-256-bits-long}
  expiration: 3600000  # 1 hour in milliseconds
  refresh-expiration: 86400000  # 24 hours in milliseconds
```

## Security Best Practices

1. **Never hardcode secrets**: Use environment variables for JWT secret
2. **Use strong secrets**: JWT secret should be at least 256 bits
3. **Set appropriate expiration**: Balance security and user experience
4. **Validate tokens thoroughly**: Always validate token signature and expiration
5. **Use HTTPS**: Always use HTTPS in production for token transmission

## Token Format

Authorization header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
