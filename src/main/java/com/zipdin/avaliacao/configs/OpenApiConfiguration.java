package com.zipdin.avaliacao.configs;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Releases API",
        version = "1.0",
        description = "API para gerenciamento de releases de sistemas",
        contact = @Contact(
            name = "Darieldon Medeiros",
            email = "darieldonbm99@outlook.com",
            url = "https://github.com/DarieldonMedeiros"
        )
    ),
    security = {
        @SecurityRequirement(name = "bearerAuth")
    }
)

@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {

}
