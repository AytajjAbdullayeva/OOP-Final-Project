import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FlighDAOTest {
    private static FlightDAO flightDAO;

    @BeforeEach
    public void setUp() {
        flightDAO = new FlightDAO();
    }

    @Test
    public void TestgetAllFlights()
    {
        List<Flight> flights = flightDAO.getAllFlights();
        assertNotEquals(flights.size(), 0);
        for(Flight flight : flights) assertNotEquals(flight,null);
    }

    @Test
    public void TestgetFlightById()
    {
        List<Flight> flights = flightDAO.getAllFlights();
        assertNotEquals(flights.size(), 0);
        for(int i=0; i<flights.size(); i++)
        {
            assertEquals(flights.get(i), flightDAO.getFlightById(flights.get(i).getId()));
        }
    }

    @Test
    public void TestaddFlight()
    {
        LocalDateTime testTime = LocalDateTime.now();
        Flight tflight = new Flight("TestId","TestDest",testTime,100,90);
        flightDAO.addFlight(tflight);
        List<Flight> flights = flightDAO.getAllFlights();
        assertNotEquals(flights.size(), 0);
        assertEquals(tflight, flightDAO.getFlightById(tflight.getId()));
        flightDAO.deleteFlightById(tflight.getId());
    }
}
