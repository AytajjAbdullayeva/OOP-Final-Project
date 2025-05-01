package fin.service;





import fin.dao.FlightDAO;
import fin.model.Flight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FlightService {
    private final FlightDAO flightDAO;

    public FlightService(FlightDAO flightDAO) {
        this.flightDAO = flightDAO;
    }

    public List<Flight> getFlightsInNext24Hours() {
        LocalDateTime now = LocalDateTime.now();
        return flightDAO.getAllFlights().stream()
                .filter(f -> f.getDepartureTime().isAfter(now) &&
                        f.getDepartureTime().isBefore(now.plusHours(24)))
                .collect(Collectors.toList());
    }

    public Flight getFlightById(String id) {
        return flightDAO.getFlightById(id);
    }

    public List<Flight> searchFlights(String destination, LocalDate date, int passengerCount) {
        return flightDAO.getAllFlights().stream()
                .filter(f -> f.getDestination().equalsIgnoreCase(destination))
                .filter(f -> f.getDepartureTime().toLocalDate().equals(date))
                .filter(f -> f.getAvailableSeats() >= passengerCount)
                .collect(Collectors.toList());
    }

    public boolean bookSeats(String flightId, int count) {
        Flight flight = flightDAO.getFlightById(flightId);
        if (flight != null && flight.getAvailableSeats() >= count) {
            flight.setAvailableSeats(flight.getAvailableSeats() - count);
            flightDAO.saveFlightsToFile(); // Update file after booking
            return true;
        }
        return false;
    }
}

