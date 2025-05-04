import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlighServiceTest {

    private static FlightDAO flightDao;
    private static FlightService flightService;
    private static Flight tflight;

    @BeforeEach
    public void setUp() {
        flightDao = new FlightDAO();
        flightService= new FlightService(flightDao);
        LocalDateTime today = LocalDateTime.now();
        today = today.plusHours(12);
        tflight = new Flight("TestId","TestDest",today,100,90);
        flightDao.addFlight(tflight);
    }
    @AfterEach
    public void tearDown() {
        flightDao.deleteFlightById(tflight.getId());
    }

    @Test
    public void TestgetFlightsInNext24Hours()
    {
        List<Flight> flights = flightService.getFlightsInNext24Hours();
        int index = flights.indexOf(tflight);
        assertNotEquals(index, -1);
    }

    @Test
    public void TestgetFlightById()
    {
        Flight tflight2 = flightService.getFlightById(tflight.getId());
        assertEquals(tflight, tflight2);
    }

    @Test
    public void TestsearchFlights()
    {
        List<Flight> flights = flightService.searchFlights("TestDest",tflight.getDepartureTime().toLocalDate(),3);
        assertEquals(flights.size(), 1);
        assertEquals(flights.get(0), tflight);
    }

    @Test
    public void TestdecreaseAvailableSeats()
    {
        int available=tflight.getAvailableSeats();
        flightService.decreaseAvailableSeats(tflight.getId(),5);
        assertEquals(available-5,tflight.getAvailableSeats());
    }

    @Test
    public void TestincreaseAvailableSeats()
    {
        int available=tflight.getAvailableSeats();
        flightService.increaseAvailableSeats(tflight.getId(),5);
        assertEquals(available+5,tflight.getAvailableSeats());
    }

    @Test
    public void TesthasAvailableSeats()
    {
        int available=tflight.getAvailableSeats(),total=tflight.getTotalSeats();
        flightService.decreaseAvailableSeats(tflight.getId(),available);
        assertFalse(flightService.hasAvailableSeats(tflight.getId(),1));
    }
}
