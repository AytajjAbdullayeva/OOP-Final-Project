package entity;

import Logging.Logger;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
//import util.Logger;

public class Booking implements Serializable {
    private String bookingId;
    private String flightId;
    private List<Passenger> passengers;
    private LocalDateTime bookingTime;

    public Booking(String bookingId, String flightId, List<Passenger> passengers, LocalDateTime bookingTime) {
        Logger.DebugLog("Creating Booking: " + bookingId + ", Flight: " + flightId);
        this.bookingId = bookingId;
        this.flightId = flightId;
        this.passengers = passengers;
        this.bookingTime = bookingTime;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getFlightId() {
        return flightId;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId + " | Flight ID: " + flightId +
                " | Passengers: " + passengers + " | Time: " + bookingTime;
    }
}
