package com.example.restapi_vr.simulator;

import jakarta.servlet.ServletContextHandler;
import jakarta.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class JettyServer {

    public static void main(String[] args) throws Exception {

        Server server = new Server(8080);


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/api");


        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "org.simulator");


        ServletHolder swaggerUIServlet = context.addServlet(org.webjars.servlet.WebjarsServlet.class, "/swagger-ui/*");
        swaggerUIServlet.setInitParameter("org.webjars.servlet.WebjarsServlet.use-caching", "false");


        server.setHandler(context);


        server.start();
        System.out.println("Server läuft unter http://localhost:8080/api");
        System.out.println("Swagger UI verfügbar unter http://localhost:8080/api/swagger-ui/index.html");
        server.join();
    }

}