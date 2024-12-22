package de.uni_trier.restapi_vr.simulator;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class Swagger_Config extends Application {

    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8080/api").description("Local server"))
                .info(new Info()
                        .title("NPP-Simulator_API")
                        .version("1.0")
                        .description("API for simulating and managing a Nuclear Power Plant system.")
                        );
    }

}