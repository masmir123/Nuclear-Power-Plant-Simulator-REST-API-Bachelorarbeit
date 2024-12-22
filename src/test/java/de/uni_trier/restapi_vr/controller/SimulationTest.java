package de.uni_trier.restapi_vr.controller;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import de.uni_trier.restapi_vr.simulator.NPPSystemInterface;
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


public class SimulationTest {

    private static Dispatcher dispatcher;
    private static SynchronousExecutionContext se;
    private static NPPSystemInterface nppSystemInterface;

    @BeforeAll
    public static void setUpTestSpace() {
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

    }

    @Test
    public void testGeneratorOutput() throws Exception {

    }

    @Test
    public void testReactorOutput() throws Exception {

    }

    @Test
    public void testPumpOutput() throws Exception {

    }

    @Test
    public void testValveOutput() throws Exception {

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

        String result = response.getContentAsString();

        System.out.println(result);
    }

}
