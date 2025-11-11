package com.proyect.cineclub.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import jdk.jfr.Description;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Api - CineClub",
                description = "Servicio REST que permita gestionar películas, funciones y asientos; habilitar la reserva temporal (hold) de butacas con TTL y la adquisición final de tickets mediante una transición explícita de estados; y operar el sistema en contenedores.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Muriel Soiffer",
                        email = "soiffermuriel@gmail.com"
                )

        ),
        servers = {
                @Server(
                        description = "DEV SERVER",
                        url = "http://localhost:8080"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "BasicAuth"
                )
        }
)
@SecurityScheme(
        name = "BasicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        description = "Autenticación HTTP Basic (usuario/contraseña) requerida para acceder a los endpoints protegidos."
)
public class SwaggerConfig {

}
