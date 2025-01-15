package de.uni_trier.restapi_vr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import de.uni_trier.restapi_vr.config.RESTExceptionMapper;
import de.uni_trier.restapi_vr.simulator.DTO.Pump_DTO;
import de.uni_trier.restapi_vr.simulator.DTO.Valve_DTO;
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

import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControlTest {

    private static Dispatcher dispatcher;
    private static SynchronousExecutionContext se;
    private static NPPSystemInterface nppSystemInterface;
    /**
     * List of valid valve and pump ids
     */
    private static final String[] valves = {"SV1", "SV2", "WV1", "WV2"};
    private static final String[] pumps = {"WP1", "WP2", "CP"};

    @BeforeAll
    public static void setUpTestSpace() {
        dispatcher = MockDispatcherFactory.createDispatcher();
        POJOResourceFactory noDefaults = new POJOResourceFactory(ControlController.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);
        dispatcher.getProviderFactory().registerProvider(JacksonJsonProvider.class);
        dispatcher.getProviderFactory().registerProvider(RESTExceptionMapper.class);
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

    @Test public void testControlPumps() throws Exception{
        final String[] testPumps = {"WP1", "WP2", "CP", "WP3", "WP4"};
        final int[] testRPM = {100, 1000, 2000, 4000};

        for (String p : testPumps) {
            for (int r : testRPM) {
                MockHttpRequest request = MockHttpRequest.put("/control/pump/" + p + "?setRpm=" + r);
                request.accept(MediaType.APPLICATION_JSON);
                request.contentType(MediaType.APPLICATION_JSON);
                MockHttpResponse response = new MockHttpResponse();

                se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
                request.setAsynchronousContext(se);
                dispatcher.invoke(request, response);

                if (Arrays.asList(pumps).contains(p)) {
                    assertEquals(HttpServletResponse.SC_OK, response.getStatus());
                    Pump_DTO pump = new ObjectMapper().readValue(response.getContentAsString(), Pump_DTO.class);

                    assertEquals(p, pump.getName());
                    assertEquals(false, pump.isBlown());
                    assertEquals(r, pump.getSetRpm());
                } else {
                    assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
                }
            }
        }
    }

    @Test
    public void testControlValves() throws Exception {
        final String[] testValves = {"SV1", "SV2", "WV1", "WV2", "SV3", "WV1"};
        final boolean[] testStatus = {true, false};

        for (String v : testValves) {
            for (boolean status : testStatus){
                MockHttpRequest request = MockHttpRequest.put("/control/valve/" + v + "?activate=" + status);
                request.accept(MediaType.APPLICATION_JSON);
                request.contentType(MediaType.APPLICATION_JSON);
                MockHttpResponse response = new MockHttpResponse();

                se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
                request.setAsynchronousContext(se);
                dispatcher.invoke(request, response);


                if (Arrays.asList(valves).contains(v)) {
                    assertEquals(HttpServletResponse.SC_OK, response.getStatus());
                    Valve_DTO valve = new ObjectMapper().readValue(response.getContentAsString(), Valve_DTO.class);

                    assertEquals(v, valve.getName());
                    assertEquals(false, valve.isBlown());
                    assertEquals(status, valve.isStatus());
                } else {
                    assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
                }
            }
        }
    }
}
