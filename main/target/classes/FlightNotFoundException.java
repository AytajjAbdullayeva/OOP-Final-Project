package fin;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(String flightId) {
        super("Flight with ID " + flightId + " not found");
    }
}
