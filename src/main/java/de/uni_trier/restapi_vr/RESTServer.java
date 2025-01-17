package de.uni_trier.restapi_vr;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import de.uni_trier.restapi_vr.config.RESTApplication;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;
import de.uni_trier.restapi_vr.ui.NPPUI;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Rest Server, starts Jetty server and deploys RESTEasy ServletDispatcher
 */
public class RESTServer
{

    private static final String APIPATH = "/api";
    private static final String CONTEXTROOT = "/";
    private static Logger logger;


    public static void main( String[] args ) {
        logger = LoggerFactory.getLogger(RESTServer.class);
        for (String arg : args){
            if (arg.equalsIgnoreCase("--debug=true")) {
                LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                loggerContext.getLogger("ROOT").setLevel(Level.DEBUG);
                logger.debug("Debug mode enabled");
                //Start new logging task with interval of 5 seconds
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.scheduleAtFixedRate(() -> {
                    NPPSystemInterface.getInstance().logSystemState();
                }, 1000, 1000, TimeUnit.MILLISECONDS);
                new NPPUI();
                break;
            }
        }
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
            logger.error("Cannot start server: ", e);
        }
    }
}
