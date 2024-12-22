package de.uni_trier.restapi_vr.config;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import de.uni_trier.restapi_vr.controller.ControlController;
import de.uni_trier.restapi_vr.controller.SimulationController;
import de.uni_trier.restapi_vr.controller.SystemController;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.servlet.ServletConfig;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Context;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Package: com.example.restapi_vr.config
 */
@ApplicationPath("/api")
public class RESTApplication extends Application {

    public RESTApplication(@Context ServletConfig servletConfig){
        super();

        OpenAPI oas = new OpenAPI();
        Info info = new Info()
                .title("NPP-Simulator_API")
                .version("1.0")
                .description("API for simulating and managing a Nuclear Power Plant system.");
        oas.info(info);

        SwaggerConfiguration swaggerConfig =
                new SwaggerConfiguration()
                        .openAPI(oas)
                        .prettyPrint(true)
                        .resourcePackages(
                                Stream.of("io.swagger.resources", "org.example.controller")
                                        .collect(Collectors.toSet())
                        );

        try {
            new JaxrsOpenApiContextBuilder()
                    .servletConfig(servletConfig)
                    .application(this)
                    .openApiConfiguration(swaggerConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        //Add Controllers
        resources.add(SystemController.class);
        resources.add(ControlController.class);
        resources.add(SimulationController.class);

        // Register OpenApiResource
        resources.add(OpenApiResource.class);

        // Add other resources
        resources.add(JacksonJsonProvider.class);

        return resources;
    }

}