package com.pmolinav.predictions.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Predictions BO API",
                version = "1.0.0",
                description = "Predictions Management from BackOffice.",
                contact = @Contact(
                        name = "pmolinav",
                        url = "https://github.com/Pmolinav/"
                )
        )
)
public class SwaggerConfig {
}