package fin.dao;


import java.time.format.DateTimeParseException;
import fin.model.Flight;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import  java.time.format.DateTimeFormatter;

public class FlightDAO {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private List<Flight> flights = new ArrayList<>();
    private final String filePath = "flights.txt";

    public FlightDAO() {
        loadFlightsFromFile();
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights); // Return copy for immutability
    }

    public Flight getFlightById(String id) {
        return flights.stream()
                .filter(f -> f.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
        saveFlightsToFile();
    }

    public void saveFlightsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Flight f : flights) {
                writer.write(String.join(",",
                        f.getId(),
                        f.getDestination(),
                        f.getDepartureTime().toString(),
                        String.valueOf(f.getTotalSeats()),
                        String.valueOf(f.getAvailableSeats())
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing flight data: " + e.getMessage());
        }
    }

    private void loadFlightsFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    try {
                        String id = parts[0];
                        String dest = parts[1];
                        LocalDateTime dateTime = LocalDateTime.parse(parts[2], DATE_TIME_FORMATTER);
                        int totalSeats = Integer.parseInt(parts[3]);
                        int availableSeats = Integer.parseInt(parts[4]);

                        flights.add(new Flight(id, dest, dateTime, totalSeats, availableSeats));
                    } catch (DateTimeParseException e) {
                        System.err.println("Skipping invalid flight - bad date format: " + line);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid flight - bad number format: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading flight data: " + e.getMessage());
        }
    }
}