package de.uni_trier.restapi_vr;

public class HelloApplication {

}

/* package com.example.restapi_vr;

import com.example.restapi_vr.simulator.NPPSystemInterface;
import com.example.restapi_vr.ui.NPPUI;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class HelloApplication extends Application {
    public static void main(String[] args) {
        try {

            Server server = new Server(8080);


            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");

            ResourceConfig resourceConfig = new ResourceConfig();
            resourceConfig.packages("com.example.restapi_vr");
            ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));
            context.addServlet(servletHolder, "/*");

            ServletHolder swaggerUiHolder = new ServletHolder("swagger-ui", new org.eclipse.jetty.servlet.DefaultServlet());
            swaggerUiHolder.setInitParameter("resourceBase", "META-INF/resources/webjars/swagger-ui/5.14.0/");
            swaggerUiHolder.setInitParameter("pathInfoOnly", "true");
            context.addServlet(swaggerUiHolder, "/swagger-ui/*");

            server.setHandler(context);

            Thread serverThread = new Thread(() -> {
                try {
                    server.start();
                    System.out.println("Server gestartet: http://localhost:8080/api/swagger-ui/index.html");
                    server.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            serverThread.start();


            NPPSystemInterface simulation = new NPPSystemInterface();
            Thread simulationThread = new Thread(simulation);
            simulationThread.start();
            NPPUI ui = new NPPUI(simulation);
            simulationThread.join();


            server.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // new NPPUI(new NPPSystemInterface());

} */