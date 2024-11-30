package com.example.restapi_vr.base;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class HelloApplication extends Application {
    public static void main(String[] args) {
        try {

            Server server = new Server(8080);


            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);


            ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/api/*");
            jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.example.restapi_vr.base");


            ServletHolder swaggerUIServlet = context.addServlet(org.webjars.servlet.WebjarsServlet.class, "/swagger-ui/*");


            server.start();
            System.out.println("Server gestartet: http://localhost:8080/api/swagger-ui/index.html");


            NPPSystemInterface simulation = new NPPSystemInterface();
            Thread simulationThread = new Thread(simulation);
            simulationThread.start();
            NPPUI Interface = new NPPUI(simulation)


            server.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // new NPPUI(new NPPSystemInterface());

}