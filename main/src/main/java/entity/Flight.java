package entity;
import java.time.LocalDateTime;

public class Flight {
    private final String id;
    private final String destination;
    private final LocalDateTime departureTime;
    private final int totalSeats;
    private int availableSeats;

    public Flight(String id, String destination, LocalDateTime departureTime, int totalSeats) {
        this(id, destination, departureTime, totalSeats, totalSeats);
    }

    public Flight(String id, String destination, LocalDateTime departureTime, int totalSeats, int availableSeats) {
        this.id = id;
        this.destination = destination;
        this.departureTime = departureTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
    }

    // Getters
    public String getId() { return id; }
    public String getDestination() { return destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public int getTotalSeats() { return totalSeats; }
    public int getAvailableSeats() { return availableSeats; }

    // Setters
    public void setAvailableSeats(int availableSeats) {
        if (availableSeats < 0 || availableSeats > this.totalSeats) {
            throw new IllegalArgumentException("Invalid seat count");
        }
        this.availableSeats = availableSeats;
    }

    @Override
    public String toString() {
        return "Flight ID: " + id +
                ", Destination: " + destination +
                ", Departure: " + departureTime +
                ", Seats: " + availableSeats + "/" + totalSeats;
    }
}
