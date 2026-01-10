package com.library.bookservice.infrastructure.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Swagger/OpenAPI Configuration
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bookServiceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8081");
        localServer.setDescription("Local Development Server");

        Contact contact = new Contact();
        contact.setName("Library Management Team");
        contact.setEmail("support@library.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Book Service API")
                .version("1.0.0")
                .description("RESTful API for Book Management in Library Management System.  " +
                        "Provides endpoints for creating, reading, updating, and deleting books, " +
                        "as well as managing book inventory and reservations.")
                .contact(contact)
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}