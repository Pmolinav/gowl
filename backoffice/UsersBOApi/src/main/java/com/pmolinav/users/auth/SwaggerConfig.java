package com.pmolinav.users.auth;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Users BO API",
                version = "1.0.0",
                description = "Users Management from BackOffice.",
                contact = @Contact(
                        name = "pmolinav",
                        url = "https://github.com/Pmolinav/"
                )
        )
)
public class SwaggerConfig {
}