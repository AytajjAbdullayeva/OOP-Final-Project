package controller;

import logging.Logger;
import service.FlightService;
import entity.Flight;
import exception.FlightNotFoundException;

import java.time.LocalDate;
import java.util.List;

public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        Logger.DebugLog("FlightController initialized with FlightService");
        this.flightService = flightService;
    }

    public List<Flight> getUpcomingFlights() {
        Logger.DebugLog("Getting upcoming flights (next 24 hours)");
        List<Flight> flights = flightService.getFlightsInNext24Hours();
        Logger.DebugLog("Found " + flights.size() + " upcoming flights");
        return flights;
    }

    public Flight getFlightInfo(String id) {
        Logger.DebugLog("Getting flight info for ID: " + id);
        try {
            Flight flight = flightService.getFlightById(id);
            Logger.DebugLog("Successfully retrieved flight: " + flight.getId());
            return flight;
        } catch (FlightNotFoundException e) {
            Logger.DebugLog("Failed to get flight info: " + e.getMessage());
            throw e;
        }
    }

    public List<Flight> searchFlights(String destination, LocalDate date, int peopleCount) {
        Logger.DebugLog(String.format(
                "Searching flights: Destination=%s, Date=%s, People=%d",
                destination, date, peopleCount));

        if (peopleCount <= 0) {
            Logger.DebugLog("Invalid people count: " + peopleCount);
            throw new IllegalArgumentException("People count must be positive");
        }

        List<Flight> results = flightService.searchFlights(destination, date, peopleCount);
        Logger.DebugLog("Found " + results.size() + " matching flights");
        return results;
    }
}
