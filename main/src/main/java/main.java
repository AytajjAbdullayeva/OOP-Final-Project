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
        
        UserDAO.loadUserDatabase();
        FlightDAO dao = new FlightDAO();
        FlightService flightService = new FlightService(dao);
        FlightController flightController = new FlightController(flightService);
        BookingDao bookingDao = new BookingDao();
        BookingService bookingService = new BookingService(bookingDao, flightService);
        BookingController bookingController = new BookingController(bookingService);

        ConsoleApp app = new ConsoleApp(flightController, bookingController);
        app.run();
    }
}
