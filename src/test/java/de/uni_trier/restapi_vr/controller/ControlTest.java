package de.uni_trier.restapi_vr.controller;

import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.core.SynchronousExecutionContext;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.jboss.resteasy.spi.Dispatcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControlTest {

    private static Dispatcher dispatcher;
    private static SynchronousExecutionContext se;
    private static NPPSystemInterface nppSystemInterface;

    @BeforeAll
    public static void setUpTestSpace() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        POJOResourceFactory noDefaults = new POJOResourceFactory(ControlController.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);
        nppSystemInterface = NPPSystemInterface.getInstance();
    }

    @BeforeEach
    public void setUpSimulation() {
        nppSystemInterface.restartSimulation();
    }

    /*
     * Test ros endpoint for several values [-10,110], increment 10
     */
    @Test
    public void testControlRods() throws Exception {
        for (int i = -10; i <= 110; i+=10) {
            MockHttpRequest request = MockHttpRequest.put("/control/rods?setRod=" + i);
            request.accept(MediaType.APPLICATION_JSON);
            request.contentType(MediaType.APPLICATION_JSON);
            MockHttpResponse response = new MockHttpResponse();

            se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
            request.setAsynchronousContext(se);
            dispatcher.invoke(request, response);

            if (i < 0 || i > 100)
                assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
            else
                assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        }
    }

    @Test public void testControlPumps() {

    }

    @Test
    public void testControlValves() {

    }
}
