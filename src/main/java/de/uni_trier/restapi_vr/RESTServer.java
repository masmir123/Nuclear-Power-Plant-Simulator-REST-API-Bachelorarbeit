package de.uni_trier.restapi_vr;


import de.uni_trier.restapi_vr.config.RESTApplication;
import de.uni_trier.restapi_vr.controller.ControlController;
import de.uni_trier.restapi_vr.controller.SimulationController;
import de.uni_trier.restapi_vr.controller.SystemController;
import de.uni_trier.restapi_vr.service.ControlService;
import de.uni_trier.restapi_vr.service.SimulationService;
import de.uni_trier.restapi_vr.service.SystemService;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;
import de.uni_trier.restapi_vr.ui.NPPUI;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import java.util.Objects;


/**
 * Rest Server, starts Jetty server and deploys RESTEasy ServletDispatcher
 */
public class RESTServer
{

    private static final String APIPATH = "/api";
    private static final String CONTEXTROOT = "/";

    public static void main( String[] args ) {
        new NPPUI();
        startServer();
    }

    public static void startServer(){
        Server server = new Server(8080);

        //Basic Application context at "/"
        ServletContextHandler context = new ServletContextHandler(server, CONTEXTROOT);

        //RestEasy HttpServletDispatcher at "/api/*"
        ServletHolder restEasyServlet = new ServletHolder(new HttpServletDispatcher());
        restEasyServlet.setInitParameter("resteasy.servlet.mapping.prefix", APIPATH);
        restEasyServlet.setInitParameter("jakarta.ws.rs.Application", RESTApplication.class.getCanonicalName());
        context.addServlet(restEasyServlet, APIPATH + "/*");

        // DefaultServlet at "/"
        ServletHolder defaultServlet = new ServletHolder(new DefaultServlet());
        context.addServlet(defaultServlet, CONTEXTROOT);

        // Swagger UI at "/swagger-ui"
        context.setResourceBase(Objects.requireNonNull(RESTServer.class.getResource("/swagger-ui")).toExternalForm());
        context.setWelcomeFiles(new String[] {"index.html"});

        try {
            server.start();
            server.join();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
