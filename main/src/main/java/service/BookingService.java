//import dao.BookingDao;
//import model.Booking;
//import model.Passenger;
//import util.Logger;
package Service;
import DAO.BookingDao;
import Logging.Logger;
import entity.Booking;
import entity.Passenger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class BookingService {
    private BookingDao bookingDao;
    private FlightService flightService; // Assume FlightService exists and injected

    public BookingService(BookingDao bookingDao, FlightService flightService) {
        this.bookingDao = bookingDao;
        this.flightService = flightService;
    }

    public Booking createBooking(String flightId, List<Passenger> passengers) {
        Logger.DebugLog("Attempting to create booking for flight " + flightId);
        if (!flightService.hasAvailableSeats(flightId, passengers.size())) {
            Logger.DebugLog("Not enough available seats on flight " + flightId);
            throw new IllegalArgumentException("Not enough available seats for this flight.");
        }
        String bookingId = UUID.randomUUID().toString();
        Logger.DebugLog("Generated booking ID: " + bookingId);
        Booking booking = new Booking(bookingId, flightId, passengers, LocalDateTime.now());
        flightService.increaseAvailableSeats(flightId, passengers.size());
        bookingDao.addBooking(booking);
        Logger.DebugLog("Booking created and saved: " + booking);
        return booking;
    }

    public boolean cancelBooking(String bookingId) {
        Logger.DebugLog("Attempting to cancel booking: " + bookingId);
        return bookingDao.getBookingById(bookingId)
                .map(booking -> {
                    flightService.decreaseAvailableSeats(booking.getFlightId(), booking.getPassengers().size());
                    bookingDao.removeBookingById(bookingId);
                    Logger.DebugLog("Booking cancelled: " + bookingId);
                    return true;
                })
                .orElseGet(() -> {
                    Logger.DebugLog("Booking not found: " + bookingId);
                    return false;
                });
    }

    public List<Booking> getBookingsByPassengerName(String fullName) {
        Logger.DebugLog("Fetching bookings for passenger: " + fullName);
        return bookingDao.getBookingsByPassengerName(fullName);
    }
}
