package com.creppyfm.server.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import jdk.jfr.Description;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Foli Creppy",
                        email = "creppyfm@gmail.com",
                        url = "https://github.com/creppyfm/DeleGate"
                ),
                description = "OpenApi documentation for DeleGate API endpoints.",
                title = "OpenApi specification - DeleGate",
                version = "0.1.0"
        ),
        servers = {
                @Server(
                        description = "Local Development Environment",
                        /*
                        * Modify to deployed server url
                        * */
                        url = "https://delegate.herokuapp.com/"
                )
        }
)
public class OpenApiConfig {
}
