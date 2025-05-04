package Controller;//import model.Passenger;
//import model.Booking;
//import service.BookingService;
//import util.Logger;

import java.util.List;

public class BookingController {
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void handleBooking(String flightId, List<Passenger> passengers) {
        Logger.DebugLog("Handling new booking request for flight: " + flightId);
        try {
            Booking booking = bookingService.createBooking(flightId, passengers);
            Logger.DebugLog("Booking successful! " + booking);
        } catch (IllegalArgumentException e) {
            Logger.DebugLog("Booking failed: " + e.getMessage());
        }
    }

    public void handleCancellation(String bookingId) {
        Logger.DebugLog("Handling cancellation for booking ID: " + bookingId);
        boolean success = bookingService.cancelBooking(bookingId);
        if (success) {
            Logger.DebugLog("Booking cancelled successfully.");
        } else {
            Logger.DebugLog("Booking ID not found.");
        }
    }

    public void showBookingsByPassenger(String fullName) {
        Logger.DebugLog("Showing bookings for passenger: " + fullName);
        List<Booking> bookings = bookingService.getBookingsByPassengerName(fullName);
        if (bookings.isEmpty()) {
            Logger.DebugLog("No bookings found for: " + fullName);
        } else {
            bookings.forEach(b -> Logger.DebugLog(b.toString()));
        }
    }
}
