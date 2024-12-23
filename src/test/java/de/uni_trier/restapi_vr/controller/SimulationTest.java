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

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;


public class SimulationTest {

    private static Logger logger;
    private static Dispatcher dispatcher;
    private static SynchronousExecutionContext se;
    private static NPPSystemInterface nppSystemInterface;

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
        request.accept(MediaType.WILDCARD);
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();

        se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(se);
        dispatcher.invoke(request, response);

        Condenser_DTO condenser = new ObjectMapper().readValue(response.getContentAsString(), Condenser_DTO.class);

        assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        assertEquals(0, condenser.getPressure());
        assertEquals(4000, condenser.getWaterLevel());
        assertTrue(condenser.isOperational());
    }

    @Test
    public void testGeneratorOutput() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/simulation/generator");
        request.accept(MediaType.WILDCARD);
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();

        se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(se);
        dispatcher.invoke(request, response);

        Generator_DTO generator = new ObjectMapper().readValue(response.getContentAsString(), Generator_DTO.class);

        assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        assertEquals(0, generator.getPower());
        assertFalse(generator.isBlown());
    }

    @Test
    public void testReactorOutput() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/simulation/reactor");
        request.accept(MediaType.WILDCARD);
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();

        se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(se);
        dispatcher.invoke(request, response);

        Reactor_DTO reactor = new ObjectMapper().readValue(response.getContentAsString(), Reactor_DTO.class);

        assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        assertEquals(0, reactor.getPressure());
        assertEquals(2000, reactor.getWaterLevel());
        assertTrue(reactor.isOperational());
        assertTrue(reactor.isIntact());
    }


    @Test
    public void testPumpOutput() throws Exception {
        //TODO: Test CP + Test invalid query param input
        for (int i = 1; i <= 2; i++) {
            MockHttpRequest request = MockHttpRequest.get("/simulation/pump/" + i);
            request.accept(MediaType.WILDCARD);
            request.contentType(MediaType.APPLICATION_JSON);
            MockHttpResponse response = new MockHttpResponse();

            se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
            request.setAsynchronousContext(se);
            dispatcher.invoke(request, response);

            Pump_DTO pump = new ObjectMapper().readValue(response.getContentAsString(), Pump_DTO.class);

            assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            assertEquals("WP" + i, pump.getName());
            assertEquals(0, pump.getRpm());
            assertEquals(0, pump.getSetRpm());
            assertEquals(2000, pump.getMaxRpm());
            assertFalse(pump.isBlown());
            assertTrue(pump.isOperational());
        }
    }

    @Test
    public void testValveOutput() throws Exception {
        //TODO Test WV + test invalid query param input
        for (int i = 1; i <= 2; i++) {
            MockHttpRequest request = MockHttpRequest.get("/simulation/valve/" + i);
            request.accept(MediaType.WILDCARD);
            request.contentType(MediaType.APPLICATION_JSON);
            MockHttpResponse response = new MockHttpResponse();

            se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
            request.setAsynchronousContext(se);
            dispatcher.invoke(request, response);

            Valve_DTO valve = new ObjectMapper().readValue(response.getContentAsString(), Valve_DTO.class);

            assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
            assertEquals(valve.getName(), "SV" + i);
            assertFalse(valve.isStatus());
            assertFalse(valve.isBlown());

        }
    }

    @Test
    public void testHealthOutput() throws Exception {
        MockHttpRequest request = MockHttpRequest.get("/simulation/health");
        request.accept(MediaType.WILDCARD);
        request.contentType(MediaType.APPLICATION_JSON);
        MockHttpResponse response = new MockHttpResponse();

        se = new SynchronousExecutionContext((SynchronousDispatcher) dispatcher, request, response);
        request.setAsynchronousContext(se);
        dispatcher.invoke(request, response);

        ObjectMapper objectMapper = new ObjectMapper();
        List<Components_DTO> results = objectMapper.readValue(response.getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Components_DTO.class));

        assertEquals(HttpResponseCodes.SC_OK, response.getStatus());
        assertEquals(results.size(), nppSystemInterface.getComponents().size());
        for (Components_DTO result : results) {
            logger.info(result.getName() + " " + result.isBroken());
            assertNotNull(result.getName());
            assertNotEquals( 0, result.getName().length());
            assertFalse(result.isBroken());
        }
    }
}
