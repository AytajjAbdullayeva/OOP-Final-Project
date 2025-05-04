package DAO;

import entity.Booking;
import entity.Flight;
import entity.Passenger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookingDaoTest {

    private BookingDao bookingDao;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        bookingDao = new BookingDao();

        // Create test flight and passenger
        Flight flight = new Flight("TST123", "TestCity", LocalDateTime.now().plusDays(1), 100);
        Passenger passenger = new Passenger("Test", "User");

        // Create booking using correct constructor
        testBooking = new Booking(
                "BOOK123",                   // booking ID
                flight.getId(),              // flight ID (String)
                List.of(passenger),          // list of passengers
                LocalDateTime.now()          // booking time
        );

        bookingDao.addBooking(testBooking);
    }

    @AfterEach
    void tearDown() {
        bookingDao.removeBookingById("BOOK123");
    }

    @Test
    void testAddAndGetBookingById() {
        Optional<Booking> result = bookingDao.getBookingById("BOOK123");
        assertTrue(result.isPresent());
        assertEquals("BOOK123", result.get().getBookingId());
    }

    @Test
    void testRemoveBookingById() {
        bookingDao.removeBookingById("BOOK123");
        Optional<Booking> result = bookingDao.getBookingById("BOOK123");
        assertFalse(result.isPresent());
    }

    @Test
    void testGetBookingsByPassengerName() {
        List<Booking> result = bookingDao.getBookingsByPassengerName("Test User");
        assertFalse(result.isEmpty());
        assertEquals("BOOK123", result.get(0).getBookingId());
    }
}
