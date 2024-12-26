package de.uni_trier.restapi_vr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import de.uni_trier.restapi_vr.simulator.DTO.*;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.core.SynchronousDispatcher;
import org.jboss.resteasy.core.SynchronousExecutionContext;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.POJOResourceFactory;
import org.jboss.resteasy.spi.Dispatcher;
import org.jboss.resteasy.spi.HttpResponseCodes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


public class SimulationTest {

    private static Logger logger;
    private static Dispatcher dispatcher;
    private static SynchronousExecutionContext se;
    private static NPPSystemInterface nppSystemInterface;
    /**
     * List of valid valve and pump ids
     */
    private static final String[] VALVES = {"SV1", "SV2", "WV1", "WV2"};
    private static final String[] PUMPS = {"WP1", "WP2", "CP"};

    @BeforeAll
    public static void setUpTestSpace() {
        logger = Logger.getLogger(SimulationController.class.getName());
        dispatcher = MockDispatcherFactory.createDispatcher();
        POJOResourceFactory noDefaults = new POJOResourceFactory(SimulationController.class);
        dispatcher.getRegistry().addResourceFactory(noDefaults);
        dispatcher.getProviderFactory().registerProvider(JacksonJsonProvider.class);
        nppSystemInterface = NPPSystemInterface.getInstance();
    }

    @BeforeEach
    public void setUpSimulation() {
        nppSystemInterface.restartSimulation();

    }

    @Test
    public void testCondenserOutput() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/simulation/condenser");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();

        se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(se);
        dispatcher.invoke(request, response);

        assertEquals(HttpResponseCodes.SC_OK, response.getStatus());

        Condenser_DTO condenser = new ObjectMapper().readValue(response.getContentAsString(), Condenser_DTO.class);

        assertEquals(0, condenser.getPressure(), "Parameter: pressure");
        assertEquals(4000, condenser.getWaterLevel(), "Parameter: waterLevel");
        assertTrue(condenser.isOperational(), "Parameter: operational");
    }

    @Test
    public void testGeneratorOutput() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/simulation/generator");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();

        se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(se);
        dispatcher.invoke(request, response);

        assertEquals(HttpResponseCodes.SC_OK, response.getStatus());

        Generator_DTO generator = new ObjectMapper().readValue(response.getContentAsString(), Generator_DTO.class);

        assertEquals(0, generator.getPower(), "Parameter: power");
        assertFalse(generator.isBlown(), "Parameter: blown");
    }

    @Test
    public void testReactorOutput() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/simulation/reactor");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();

        se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(se);
        dispatcher.invoke(request, response);

        assertEquals(HttpResponseCodes.SC_OK, response.getStatus());

        Reactor_DTO reactor = new ObjectMapper().readValue(response.getContentAsString(), Reactor_DTO.class);

        assertEquals(0, reactor.getPressure(), "Parameter: pressure");
        assertEquals(2000, reactor.getWaterLevel(), "Parameter: waterLevel");
        assertFalse(reactor.isOperational(), "Parameter: operational");
        assertTrue(reactor.isIntact(),"Parameter: intact");
    }

    @Test
    public void testPumpOutput() throws Exception {
        final String[] testPumps = {"WP1", "WP2", "CP", "WP3", "WP4"};

        for (String p : testPumps) {
            MockHttpRequest request = MockHttpRequest.get("/simulation/pump/" + p);
            request.accept(MediaType.APPLICATION_JSON);
            request.contentType(MediaType.APPLICATION_JSON);
            MockHttpResponse response = new MockHttpResponse();

            se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
            request.setAsynchronousContext(se);
            dispatcher.invoke(request, response);

            if (Arrays.asList(PUMPS).contains(p)) {
                assertEquals(HttpResponseCodes.SC_OK, response.getStatus());

                Pump_DTO pump = new ObjectMapper().readValue(response.getContentAsString(), Pump_DTO.class);

                assertEquals(pump.getName(), p);
                assertEquals(0, pump.getRpm(), "Parameter: rpm");
                assertEquals(0, pump.getSetRpm(), "Parameter: setRpm");
                assertEquals(2000, pump.getMaxRpm(), "Parameter: maxRpm");
                assertFalse(pump.isBlown());
            } else {
                assertEquals(HttpResponseCodes.SC_BAD_REQUEST, response.getStatus(), "Invalid request or query parameter");
            }
        }
    }

    @Test
    public void testValveOutput() throws Exception {
        final String[] testValves = {"SV1", "SV2", "WV1", "WV2", "SV3", "WV1"};

        for (String v : testValves) {
            MockHttpRequest request = MockHttpRequest.get("/simulation/valve/" + v);
            request.accept(MediaType.APPLICATION_JSON);
            request.contentType(MediaType.APPLICATION_JSON);
            MockHttpResponse response = new MockHttpResponse();

            se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
            request.setAsynchronousContext(se);
            dispatcher.invoke(request, response);

            if (Arrays.asList(VALVES).contains(v)) {
                assertEquals(HttpResponseCodes.SC_OK, response.getStatus());

                Valve_DTO valve = new ObjectMapper().readValue(response.getContentAsString(), Valve_DTO.class);

                assertEquals(valve.getName(), v, "Parameter: name");
                assertFalse(valve.isStatus(), "Parameter: status");
                assertFalse(valve.isBlown(), "Parameter: blown");
            } else {
                assertEquals(HttpResponseCodes.SC_BAD_REQUEST, response.getStatus());
            }
        }
    }

    @Test
    public void testHealthOutput() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/simulation/health");
        request.accept(MediaType.APPLICATION_JSON);
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();

        se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(se);
        dispatcher.invoke(request, response);

        assertEquals(HttpResponseCodes.SC_OK, response.getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        List<Components_DTO> results = objectMapper.readValue(response.getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Components_DTO.class));

        assertEquals(results.size(), nppSystemInterface.getComponents().size(), "Number of components");
        for (Components_DTO result : results) {
            logger.info(result.getName() + " " + result.isBroken());
            assertNotNull(result.getName(), "Parameter: name");
            assertNotEquals( 0, result.getName().length(), "Parameter: name");
            assertFalse(result.isBroken(),  "Parameter: broken");
        }
    }
}
