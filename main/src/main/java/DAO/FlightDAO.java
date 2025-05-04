import java.time.format.DateTimeParseException;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import  java.time.format.DateTimeFormatter;
import exception.InvalidFlightDataException;

public class FlightDAO {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private List<Flight> flights = new ArrayList<>();
    private final String filePath = "src/flights.txt";

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
        Logger.DebugLog("Generating sample flights for next 30 days");

        flights.clear();
        List<String> destinations = List.of("London", "Paris", "Berlin", "Baku", "New York", "Rome", "Dubai", "Istanbul");
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 200; i++) {
            String id = "F" + (1000 + i);
            String dest = destinations.get(i % destinations.size());

            int plusDays = (int) (Math.random() * 30); 
            int plusHours = (int) (Math.random() * 24); 
            int plusMinutes = ((int) (Math.random() * 4)) * 15; 

            LocalDateTime departureTime = now.plusDays(plusDays).plusHours(plusHours).plusMinutes(plusMinutes);

            int totalSeats = 150 + (i % 50);
            flights.add(new Flight(id, dest, departureTime, totalSeats, totalSeats));
        }

        saveFlightsToFile();
        Logger.DebugLog("Generated 200 sample flights");
    }



    public List<Flight> getAllFlights() {
        Logger.DebugLog("Getting all flights (count: " + flights.size() + ")");
        return new ArrayList<>(flights); 
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

    public boolean deleteFlightById(String id) {
        Logger.DebugLog("Attempting to delete flight with ID: " + id);
        boolean removed = flights.removeIf(flight -> flight.getId().equalsIgnoreCase(id));

        if (removed) {
            Logger.DebugLog("Flight " + id + " removed from memory, saving to file...");
            saveFlightsToFile();
        } else {
            Logger.DebugLog("Flight " + id + " not found");
        }

        return removed;
    }


    private void loadFlightsFromFile() {
        Logger.DebugLog("Loading flights from file: " + filePath);
        flights.clear();
        File file = new File(filePath);
        boolean shouldGenerate = false;

        if (!file.exists() || file.length() == 0) {
            Logger.DebugLog("File empty or missing");
            shouldGenerate=true;

        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int loadedCount = 0;
            int skippedCount = 0;

            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length != 5) {
                        throw new InvalidFlightDataException("Malformed flight entry: " + line);
                    }

                    LocalDateTime dateTime = LocalDateTime.parse(parts[2]);

                    if (dateTime.isAfter(LocalDateTime.now()))
                    {
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

                } catch (InvalidFlightDataException | DateTimeParseException | NumberFormatException e) {
                    Logger.DebugLog("Skipping invalid flight data: " + line + " | Error: " + e.getMessage());
                    skippedCount++;
                }
            }

            Logger.DebugLog("Loaded " + loadedCount + " flights, skipped " + skippedCount + " entries");

            if (flights.stream().noneMatch(f -> f.getDepartureTime().isAfter(LocalDateTime.now()))) {
                Logger.DebugLog("No future flights found.");
                shouldGenerate=true;
            }
        } catch (IOException e) {
            Logger.DebugLog("Error reading flight data: " + e.getMessage());
            System.err.println("Error reading flight data: " + e.getMessage());
            shouldGenerate=true;
        }

        if (shouldGenerate){
            Logger.DebugLog("Generating sample flights");
            generateSampleFlights();
        }
    }


}
