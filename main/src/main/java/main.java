import DAO.UserDAO;
import controller.BookingController;
import controller.FlightController;
import DAO.BookingDao;
import DAO.FlightDAO;
import service.BookingService;
import service.FlightService;
import console.ConsoleApp;

public class main {
    public static void main(String[] args) {
        // Initialize the controllers (you may need to adjust these constructors based on your implementation)
        UserDAO.loadUserDatabase();
        FlightDAO dao = new FlightDAO();
        FlightService flightService = new FlightService(dao);
        FlightController flightController = new FlightController(flightService);
        BookingDao bookingDao = new BookingDao();
        BookingService bookingService = new BookingService(bookingDao, flightService);
        BookingController bookingController = new BookingController(bookingService);

        // Create the console application and run it
        ConsoleApp app = new ConsoleApp(flightController, bookingController);
        app.run();
    }
}
