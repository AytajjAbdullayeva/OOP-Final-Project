package fin.service;




import fin.Logger;
import fin.dao.FlightDAO;
import fin.model.Flight;
import fin.FlightNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class FlightService {
    private final FlightDAO flightDAO;

    public FlightService(FlightDAO flightDAO) {
        Logger.DebugLog("FlightService initialized with FlightDAO");
        this.flightDAO = flightDAO;
    }



    public List<Flight> getFlightsInNext24Hours() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusHours(24);


        List<Flight> allFlights = flightDAO.getAllFlights();
        Logger.DebugLog("Total flights in system: " + allFlights.size());

        List<Flight> result = allFlights.stream()
                .filter(f -> f.getDepartureTime().isAfter(now))
                .filter(f -> f.getDepartureTime().isBefore(end))
                .peek(f -> Logger.DebugLog("Included flight: " + f.getId() +
                        " | Time: " + f.getDepartureTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) +
                        " | Seats: " + f.getAvailableSeats() + "/" + f.getTotalSeats()))
                .collect(Collectors.toList());


        Logger.DebugLog("Found " + result.size() + " flights in next 24 hours");
        return result;
    }


    public Flight getFlightById(String id) {
        Logger.DebugLog("Attempting to get flight by ID: " + id);
        Flight flight = flightDAO.getFlightById(id);
        if (flight == null) {
            Logger.DebugLog("Flight not found with ID: " + id);
            throw new FlightNotFoundException(id);
        }
        Logger.DebugLog("Successfully retrieved flight: " + flight.getId());
        return flight;
    }


    public List<Flight> searchFlights(String destination, LocalDate date, int passengerCount) {
        Logger.DebugLog(String.format(
                "Searching flights: Destination=%s, Date=%s, Passengers=%d",
                destination, date, passengerCount));

        List<Flight> results = flightDAO.getAllFlights().stream()
                .filter(f -> f.getDestination().equalsIgnoreCase(destination))
                .filter(f -> f.getDepartureTime().toLocalDate().equals(date))
                .filter(f -> f.getAvailableSeats() >= passengerCount)
                .collect(Collectors.toList());

        Logger.DebugLog("Found " + results.size() + " matching flights");
        return results;
    }

    public boolean decreaseAvailableSeats(String flightId, int seats) {
        Logger.DebugLog(String.format(
                "Attempting to decrease seats: FlightID=%s, Seats=%d",
                flightId, seats));
        Flight flight = flightDAO.getFlightById(flightId);
        if (flight == null || flight.getAvailableSeats() < seats) {
            Logger.DebugLog("Failed to decrease seats - " +
                    (flight == null ? "Flight not found" : "Not enough available seats"));
            return false;
        }
        flight.setAvailableSeats(flight.getAvailableSeats() - seats);
        flightDAO.saveFlightsToFile();// Fayla yazma
        Logger.DebugLog(String.format(
                "Successfully decreased seats. New availability: %d/%d",
                flight.getAvailableSeats(), flight.getTotalSeats()));
        return true;
    }

    // Rollback üçün
    public void increaseAvailableSeats(String flightId, int seats) {
        Logger.DebugLog(String.format(
                "Increasing seats: FlightID=%s, Seats=%d",
                flightId, seats));
        Flight flight = flightDAO.getFlightById(flightId);
        if (flight != null) {
            flight.setAvailableSeats(flight.getAvailableSeats() + seats);
            flightDAO.saveFlightsToFile();
            Logger.DebugLog(String.format(
                    "Successfully increased seats. New availability: %d/%d",
                    flight.getAvailableSeats(), flight.getTotalSeats()));
        } else {
            Logger.DebugLog("Flight not found - could not increase seats");
        }
    }

    public boolean hasAvailableSeats(String flightId, int requestedSeats) {
        Logger.DebugLog(String.format(
                "Checking available seats: FlightID=%s, RequestedSeats=%d",
                flightId, requestedSeats));
        Flight flight = flightDAO.getFlightById(flightId);
        if (flight == null) {
            Logger.DebugLog("Flight not found");
            return false;
        }
        boolean result = flight.getAvailableSeats() >= requestedSeats;
        Logger.DebugLog(String.format(
                "Available seats check result: %s (%d available)",
                result ? "OK" : "NOT ENOUGH", flight.getAvailableSeats()));
        return result;
    }

}

