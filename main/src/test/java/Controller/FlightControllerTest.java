package Controller;

import DAO.FlightDAO;
import Service.FlightService;
import entity.Flight;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FlightControllerTest {

    private FlightDAO flightDAO;
    private FlightService flightService;
    private FlightController flightController;
    private Flight tflight;

    @BeforeEach
    public void setUp() {
        flightDAO = new FlightDAO();
        flightService = new FlightService(flightDAO);
        flightController = new FlightController(flightService);

        LocalDateTime today = LocalDateTime.now().plusHours(12);
        tflight = new Flight("TestId","TestDest",today,100,90);
        flightDAO.addFlight(tflight);
    }

    @AfterEach
    public void tearDown() {
        flightDAO.deleteFlightById(tflight.getId());
    }

    @Test
    public void TestgetUpcomingFlights()
    {
        List<Flight> flights = flightController.getUpcomingFlights();

        assertNotEquals(flights.size(),0);
    }

    @Test
    public void TestgetFlightInfo()
    {
        Flight flight = flightController.getFlightInfo(tflight.getId());
        assertEquals(flight,tflight);
    }

    @Test
    public void TestsearchFlights()
    {
        List<Flight> flights=flightController.searchFlights("TestDest",tflight.getDepartureTime().toLocalDate(),5);
        assertEquals(flights.size(),1);
        assertEquals(flights.get(0),tflight);
    }

}
