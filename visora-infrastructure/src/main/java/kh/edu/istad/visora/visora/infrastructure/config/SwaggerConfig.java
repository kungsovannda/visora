package kh.edu.istad.visora.visora.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        final String customAuthScheme = "Access Token";
        var openApi = new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(customAuthScheme))
                .components(
                        new Components()
                                .addSecuritySchemes(customAuthScheme,
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .bearerFormat("JWT")
                                                .scheme("bearer")
                                                .in(SecurityScheme.In.HEADER)
                                                .type(SecurityScheme.Type.HTTP)
                                )
                );
        openApi.setServers(List.of(
                new Server().url("https://visora-api.gital.me"),
                new Server().url("http://localhost:8080")
        ));
        return openApi;
    }
}
