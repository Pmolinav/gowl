package com.pmolinav.auth.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Auth API",
                version = "1.0.0",
                description = "Auth endpoints to be called from App.",
                contact = @Contact(
                        name = "pmolinav",
                        url = "https://github.com/Pmolinav/"
                )
        )
)
public class SwaggerConfig {
}