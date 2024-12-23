package de.uni_trier.restapi_vr.client;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DemoClientTest {

    @BeforeAll
    public static void setUpTestSpace() {
        //TODO: Consider using real client.
    }

    /**
     * Demo Client: Test Protocol 1 - "Reaktor Starten"
     * This Protocol consists of the following steps:
     * - Open V2
     * - Set CP to 1600 U/min
     * - Open WV1
     * - Start removing control Rods until reactor water level at 2100 mm
     * - Open FV1
     * - Close WP1
     * - Increase WP1 AND control rods to keep reactor waterlevel at 2100 mm until generator 700 MW
     * - Insert control rods.
     *
     *  Sub-steps will be tested by asserting the correct status of the components.
     */
    @Test
    public void testProtocol_1() {

    }

}