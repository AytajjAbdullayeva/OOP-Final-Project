//import model.Booking;
//import util.Logger;
package DAO;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import entity.Booking;
import entity.Passenger;
import logging.Logger;


public class BookingDao {
    private List<Booking> bookings = new ArrayList<>();
    private final String FILE_PATH = "Databases/bookings.txt";

    public BookingDao() {
        Logger.DebugLog("Initializing BookingDao...");
        loadBookingsFromFile();
    }

    public void addBooking(Booking booking) {
        Logger.DebugLog("Adding booking: " + booking.getBookingId());
        bookings.add(booking);
        saveBookingsToFile();
    }

    public void removeBookingById(String bookingId) {
        Logger.DebugLog("Removing booking: " + bookingId);
        bookings.removeIf(b -> b.getBookingId().equals(bookingId));
        saveBookingsToFile();
    }

    public Optional<Booking> getBookingById(String bookingId) {
        Logger.DebugLog("Retrieving booking by ID: " + bookingId);
        return bookings.stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst();
    }

    public List<Booking> getBookingsByPassengerName(String fullName) {
        Logger.DebugLog("Retrieving bookings for passenger: " + fullName);
        return bookings.stream()
                .filter(b -> b.getPassengers().stream()
                        .anyMatch(p -> p.getFullName().equalsIgnoreCase(fullName)))
                .collect(Collectors.toList());
    }

    private void loadBookingsFromFile() {
        Logger.DebugLog("Loading bookings from file...");
        bookings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Booking booking = parseBookingFromString(line);
                if (booking != null) {
                    bookings.add(booking);
                }
            }
            Logger.DebugLog("Loaded " + bookings.size() + " bookings.");
        } catch (IOException e) {
            Logger.DebugLog("No existing bookings found or error reading file: " + e.getMessage());
        }
    }

    private Booking parseBookingFromString(String line) {
        try {
            String[] parts = line.split(",", 4); // limit to 4 parts
            String bookingID = parts[0];
            String flightID = parts[1];
            String passengerStr = parts[2];
            String bookingTimeStr = parts[3];

            List<Passenger> passengers = new ArrayList<>();
            for (String p : passengerStr.split("\\|")) {
                String[] nameParts = p.split(" ");
                if (nameParts.length == 2) {
                    passengers.add(new Passenger(nameParts[0], nameParts[1]));
                }
            }

            LocalDateTime bookingTime = LocalDateTime.parse(bookingTimeStr);

            return new Booking(bookingID, flightID, passengers, bookingTime);
        } catch (Exception e) {
            Logger.DebugLog("Error parsing booking line: " + e.getMessage());
            return null;
        }
    }

    private void saveBookingsToFile() {
        Logger.DebugLog("Saving bookings to file...");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Booking booking : bookings) {
                writer.write(booking.toString());
                writer.newLine();
            }
            Logger.DebugLog("Bookings saved successfully.");
        } catch (IOException e) {
            Logger.DebugLog("Error saving bookings: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
