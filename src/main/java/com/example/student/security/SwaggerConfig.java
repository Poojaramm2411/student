package com.example.student.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    // ─────────────────────────────────────────────
    //  OpenAPI Bean
    // ─────────────────────────────────────────────

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(serverList())
                .addSecurityItem(securityRequirement())
                .components(securityComponents());
    }

    // ─────────────────────────────────────────────
    //  API Info
    // ─────────────────────────────────────────────

    private Info apiInfo() {
        return new Info()
                .title("My Application REST API")
                .version("1.0.0")
                .description("""
                        ## API Documentation
                        
                        This API uses **JWT Bearer Token** authentication.
                        
                        ### How to Authenticate:
                        1. Call `/api/auth/login` with your credentials
                        2. Copy the `token` from the response
                        3. Click **Authorize** button above
                        4. Enter: `Bearer <your_token>`
                        5. Click **Authorize** and close
                        
                        Now all protected endpoints are accessible.
                        """)
                .contact(new Contact()
                        .name("Your Name")
                        .email("your@email.com")
                        .url("https://yourwebsite.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }

    // ─────────────────────────────────────────────
    //  Server List (Local + Production)
    // ─────────────────────────────────────────────

    private List<Server> serverList() {
        Server localServer = new Server()
                .url("http://localhost:8080")
                .description("Local Development Server");

        Server productionServer = new Server()
                .url(baseUrl)
                .description("Production Server");

        return List.of(localServer, productionServer);
    }

    // ─────────────────────────────────────────────
    //  Security Requirement (applies JWT globally)
    // ─────────────────────────────────────────────

    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement()
                .addList(SECURITY_SCHEME_NAME);
    }

    // ─────────────────────────────────────────────
    //  Security Components (JWT Bearer scheme)
    // ─────────────────────────────────────────────

    private Components securityComponents() {
        return new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter your JWT token below. Example: `Bearer eyJhbGci...`")
                );
    }
}