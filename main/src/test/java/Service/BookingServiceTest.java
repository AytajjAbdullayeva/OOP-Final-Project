package Service;

import DAO.BookingDao;
import DAO.FlightDAO;
import entity.Booking;
import entity.Flight;
import entity.Passenger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingServiceTest {

    private BookingService bookingService;
    private BookingDao bookingDao;
    private FlightService flightService;
    private FlightDAO flightDao;

    private Booking booking;

    @BeforeEach
    public void setUp() {
        bookingDao = new BookingDao();
        flightDao = new FlightDAO();


        flightService = new FlightService(flightDao);
        bookingService = new BookingService(bookingDao,flightService);

        LocalDateTime startTime = LocalDateTime.of(2021,1,1,1,1);
        Flight ex_flight = new Flight("TestFID","TestDestination",startTime,100,90);

        Passenger p1 = new Passenger("Test1name","Test1surname");
        Passenger p2 = new Passenger("Test2name","Test2surname");
        Passenger p3 = new Passenger("Test3name","Test3surname");
        ArrayList<Passenger> passengers = new ArrayList<Passenger>();
        passengers.add(p1);
        passengers.add(p2);
        passengers.add(p3);

        flightDao.addFlight(ex_flight);
        booking = bookingService.createBooking(ex_flight.getId(), passengers);
    }

    @AfterEach
    public void tearDown() {
        bookingService.cancelBooking(booking.getBookingId());
    }

    @Test
    public void testgetBookingsByPassengerName()
    {
        List<Booking> p1bookings = bookingService.getBookingsByPassengerName("Test1name Test1surname");
        List<Booking> p2bookings = bookingService.getBookingsByPassengerName("Test2name Test2surname");
        List<Booking> p3bookings = bookingService.getBookingsByPassengerName("Test3name Test3surname");

        assertEquals(p1bookings.size(),1);
        assertEquals(p2bookings.size(),1);
        assertEquals(p3bookings.size(),1);
        assertEquals(p1bookings.get(0).getBookingId(),booking.getBookingId());
        assertEquals(p2bookings.get(0).getBookingId(),booking.getBookingId());
        assertEquals(p3bookings.get(0).getBookingId(),booking.getBookingId());
    }
}
