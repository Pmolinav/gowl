package com.pmolinav.leagues.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Leagues BO API",
                version = "1.0.0",
                description = "Leagues Management from BackOffice.",
                contact = @Contact(
                        name = "pmolinav",
                        url = "https://github.com/Pmolinav/"
                )
        )
)
public class SwaggerConfig {
}