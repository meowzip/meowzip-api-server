package com.meowzip.apiserver.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Meowzip API")
                .version("v1")
                .description("길냥이집 앱 API");

        String authScheme = "Authorization";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(authScheme);

        Components components = new Components()
                .addSecuritySchemes(authScheme, new SecurityScheme()
                        .name(authScheme)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .components(components)
                .info(info)
                .addSecurityItem(securityRequirement);
    }
}
