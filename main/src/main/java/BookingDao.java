//import model.Booking;
//import util.Logger;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class BookingDao {
    private List<Booking> bookings = new ArrayList<>();
    private final String FILE_PATH = "bookings.dat";

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
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            bookings = (List<Booking>) ois.readObject();
            Logger.DebugLog("Loaded " + bookings.size() + " bookings.");
        } catch (IOException | ClassNotFoundException e) {
            Logger.DebugLog("No existing bookings found or error reading file: " + e.getMessage());
            bookings = new ArrayList<>();
        }
    }

    private void saveBookingsToFile() {
        Logger.DebugLog("Saving bookings to file...");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(bookings);
            Logger.DebugLog("Bookings saved successfully.");
        } catch (IOException e) {
            Logger.DebugLog("Error saving bookings: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
