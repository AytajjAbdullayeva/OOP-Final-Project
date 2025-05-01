import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class main {
    public static void main(String[] args) {
        // Initialize core components
        FlightDAO flightDAO = new FlightDAO();
        FlightService flightService = new FlightService(flightDAO);
        FlightController flightController = new FlightController(flightService);

        BookingDao bookingDao = new BookingDao();
        BookingService bookingService = new BookingService(bookingDao, flightService);
        BookingController bookingController = new BookingController(bookingService);

        // --- Sample Passengers ---
        Passenger p1 = new Passenger("John", "Doe");
        Passenger p2 = new Passenger("Jane", "Smith");
        Passenger p3 = new Passenger("Alice", "Johnson");
        Passenger p4 = new Passenger("Bob", "Brown");

        List<Passenger> group1 = Arrays.asList(p1, p2);
        List<Passenger> group2 = List.of(p3);
        List<Passenger> group3 = Arrays.asList(p1, p3, p4);

        // --- View Flights in Next 24 Hours ---
        List<Flight> next24Hours = flightController.getUpcomingFlights();
        if (next24Hours.isEmpty()) {
            System.out.println("No flights in next 24 hours.");
            return;
        }

        Flight selectedFlight = next24Hours.get(0);
        String selectedFlightId = selectedFlight.getId();
        System.out.println("Selected flight for booking: " + selectedFlight);

        // --- Make Bookings ---
        bookingController.handleBooking(selectedFlightId, group1);
        bookingController.handleBooking(selectedFlightId, group2);
        bookingController.handleBooking(selectedFlightId, group3);

        // --- Search Bookings ---
        bookingController.showBookingsByPassenger("John Doe");
        bookingController.showBookingsByPassenger("Alice Johnson");
        bookingController.showBookingsByPassenger("Unknown Person");

        // --- Cancel a Booking ---
        // Grab a bookingId from bookingDao to cancel
        String toCancelId = bookingDao.getBookingsByPassengerName("John Doe")
                .stream()
                .findFirst()
                .map(b -> b.getBookingId())
                .orElse(null);
        if (toCancelId != null) {
            bookingController.handleCancellation(toCancelId);
        }

        // --- Search Flights ---
        List<Flight> flightsToParis = flightController.searchFlights("Paris", LocalDate.now().plusDays(3), 2);
        System.out.println("Flights to Paris in 3 days: " + flightsToParis.size());

        // --- Add a Custom Flight ---
        Flight customFlight = new Flight("FX123", "Mars", LocalDate.now().plusDays(10).atTime(15, 30), 200);
        flightDAO.addFlight(customFlight);
        System.out.println("Custom flight added: " + customFlight);

        // --- Display All Bookings ---
        System.out.println("All current bookings:");
        for (Booking booking : bookingDao.getBookingsByPassengerName("Alice Johnson")) {
            System.out.println(booking);
        }
    }
}
