package fin.dao;


import fin.Logger;
import java.time.format.DateTimeParseException;
import fin.model.Flight;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import  java.time.format.DateTimeFormatter;

public class FlightDAO {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private List<Flight> flights = new ArrayList<>();
    private final String filePath = "flights.txt";

    public FlightDAO() {
        Logger.DebugLog("Initializing FlightDAO");
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            Logger.DebugLog("Flight data file not found or empty, generating sample flights");
            generateSampleFlights();
        } else {
            Logger.DebugLog("Loading flights from existing file");
            loadFlightsFromFile();
        }
        Logger.DebugLog("FlightDAO initialized with " + flights.size() + " flights");
    }

    private void generateSampleFlights() {
        Logger.DebugLog("Generating 50 sample flights");
        flights.clear();
        List<String> destinations = List.of("London", "Paris", "Berlin", "Baku","New York");
        LocalDateTime now = LocalDateTime.now(); // Cari vaxtı əsas götür

        for (int i = 0; i < 50; i++) {
            String id = "FL" + (1000 + i);
            String dest = destinations.get(i % destinations.size());
            // İndiki vaxtdan 1-24 saat ərzində uçuşlar yarat
            LocalDateTime date = now.plusHours(1 + (i % 24))
                    .plusMinutes((i % 10) * 15);
            int seats = 150 + (i % 50);
            flights.add(new Flight(id, dest, date, seats, seats));
        }
        saveFlightsToFile();
        Logger.DebugLog("Generated 50 sample flights");
    }

    public List<Flight> getAllFlights() {
        Logger.DebugLog("Getting all flights (count: " + flights.size() + ")");
        return new ArrayList<>(flights); // Return copy for immutability
    }

    public Flight getFlightById(String id) {
        Logger.DebugLog("Looking up flight by ID: " + id);
        return flights.stream()
                .filter(f -> f.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public void addFlight(Flight flight) {
        Logger.DebugLog("Adding new flight: " + flight.getId());
        flights.add(flight);
        saveFlightsToFile();
    }

    public void saveFlightsToFile() {
        Logger.DebugLog("Saving flights to file: " + filePath);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Flight f : flights) {
                writer.write(String.join(",",
                        f.getId(),
                        f.getDestination(),
                        f.getDepartureTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        String.valueOf(f.getTotalSeats()),
                        String.valueOf(f.getAvailableSeats())
                ));
                writer.newLine();
            }
            Logger.DebugLog("Successfully saved " + flights.size() + " flights to file");
        } catch (IOException e) {
            Logger.DebugLog("Error writing flight data: " + e.getMessage());
            System.err.println("Error writing flight data: " + e.getMessage());
        }
    }

    private void loadFlightsFromFile() {
        Logger.DebugLog("Loading flights from file: " + filePath);
        flights.clear();
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            Logger.DebugLog("File empty or missing, generating sample flights");
            generateSampleFlights();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int loadedCount = 0;
            int skippedCount = 0;

            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length == 5) {
                        LocalDateTime dateTime = LocalDateTime.parse(parts[2]);

                        if (dateTime.isAfter(LocalDateTime.now().minusDays(1))) {
                            flights.add(new Flight(
                                    parts[0],
                                    parts[1],
                                    dateTime,
                                    Integer.parseInt(parts[3]),
                                    Integer.parseInt(parts[4])
                            ));
                            loadedCount++;
                        } else {
                            skippedCount++;
                        }
                    }
                } catch (Exception e) {
                    Logger.DebugLog("Skipping invalid flight data: " + line + " | Error: " + e.getMessage());
                    skippedCount++;
                }
            }

            Logger.DebugLog("Loaded " + loadedCount + " flights, skipped " + skippedCount + " entries");

            if (flights.stream().noneMatch(f -> f.getDepartureTime().isAfter(LocalDateTime.now()))) {
                Logger.DebugLog("No future flights found, generating samples");
                generateSampleFlights();
            }
        } catch (IOException e) {
            Logger.DebugLog("Error reading flight data: " + e.getMessage());
            System.err.println("Error reading flight data: " + e.getMessage());
            generateSampleFlights();
        }
    }
}
