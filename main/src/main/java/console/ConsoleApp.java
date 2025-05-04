import Controller.BookingController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final FlightController flightController;
    private final BookingController bookingController;

    public ConsoleApp(FlightController flightController, BookingController bookingController) {
        this.flightController = flightController;
        this.bookingController = bookingController;
    }

    public void run() {
        while (true) {
            printAccountMenu();
            int choice = scanner.nextInt();
            String username;
            String password;
            switch (choice) {
                case 1:
                    System.out.println("Please enter your username: ");
                    username = scanner.next();
                    System.out.println("Please enter your password: ");
                    password = scanner.next();
                    if (UserController.Login(username, password)) {
                        System.out.println("Welcome " + username);
                        displayAppMenu(username);
                    }
                    break;
                case 2:
                    System.out.println("Enter your name:");
                    String name = scanner.next();
                    System.out.println("Enter your surname:");
                    String surname = scanner.next();
                    System.out.println("Enter your age:");
                    int age = Integer.parseInt(scanner.next());
                    System.out.println("Enter your gender(1 for Male, 0 for Female): ");
                    boolean gender = (Integer.parseInt(scanner.next()) == 0 ? false : true);
                    System.out.println("Enter your username: ");
                    username = scanner.next();
                    System.out.println("Enter your password: ");
                    password = scanner.next();
                    UserController.SignUp(name,surname,age,gender,username,password);
                    break;
                case 3:
                    return;
            }
        }
    }

    private void printAccountMenu() {
        System.out.println("\n==== Plane Ticket Booking System ====");
        System.out.println("1. LogIn");
        System.out.println("2. SignUp");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    private void displayAppMenu(String username) {
        while (true) {
            printMainMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        displayOnlineBoard();
                        break;
                    case 2:
                        handleFlightInfo();
                        break;
                    case 3:
                        handleFlightSearchAndBooking();
                        break;
                    case 4:
                        handleBookingCancellation();
                        break;
                    case 5:
                        handleMyFlights();
                        break;
                    case 6:
                        AccountSettings(username);
                        break;
                    case 7:
                        Logger.DebugLog("Logging out...");
                        UserController.Logout(username);
                        return;
                    default:
                        throw new InvalidMenuOptionException("Invalid menu option. Please try again.");
                }
            } catch (InvalidMenuOptionException | NumberFormatException e) {
                Logger.DebugLog(e.getMessage());
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n==== Plane Ticket Booking System ====");
        System.out.println("1. Online Board");
        System.out.println("2. Show Flight Info");
        System.out.println("3. Search and Book a Flight");
        System.out.println("4. Cancel Booking");
        System.out.println("5. My Flights");
        System.out.println("6. Account Settings");
        System.out.println("7. Logout");
        System.out.print("Choose an option: ");
    }

    private void AccountSettings(String username) {
        while (true) {
            printAccountSettings();
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.println("Enter your name:");
                    String name = scanner.next();
                    System.out.println("Enter your surname:");
                    String surname = scanner.next();
                    System.out.println("Enter your age:");
                    int age = Integer.parseInt(scanner.next());
                    System.out.println("Enter your gender(1 for Male, 0 for Female): ");
                    boolean gender = (Integer.parseInt(scanner.next()) == 0 ? false : true);
                    UserController.ChangeUserDetails(name,surname,age,gender,username);
                    break;
                case 2:
                    System.out.println("Enter your old password:");
                    String oldPassword = scanner.next();
                    System.out.println("Enter your new password:");
                    String newPassword = scanner.next();
                    UserController.ChangePassword(oldPassword,newPassword,username);
                    break;
                case 3:
                    System.out.println("Enter your new username:");
                    String newUsername = scanner.next();
                    UserController.ChangeUserName(username,newUsername);
                    break;
                case 4:
                    System.out.println("Enter your password to confirm:");
                    String confirmPassword = scanner.next();
                    if(UserController.DeleteAccount(username, confirmPassword))
                    {
                        UserController.Logout(username);
                    }
                    break;
                case 5:
                    return;
            }
        }
    }

    private void printAccountSettings()
    {
        System.out.println("\n==== Account Settings ====");
        System.out.println("1. Change User details");
        System.out.println("2. Change Password");
        System.out.println("3. Change Username");
        System.out.println("4. Delete Account");
        System.out.println("5. Exit");
        System.out.print("Choose an option: ");
    }

    private void displayOnlineBoard() {
        List<Flight> flights = flightController.getUpcomingFlights();
        flights.forEach(System.out::println);
    }

    private void handleFlightInfo() {
        System.out.print("Enter flight ID: ");
        String flightId = scanner.nextLine();
        try {
            Flight flight = flightController.getFlightInfo(flightId);
            System.out.println(flight);
        } catch (Exception e) {
            Logger.DebugLog("Flight not found.");
        }
    }

    private void handleFlightSearchAndBooking() {
        try {
            System.out.print("Enter destination: ");
            String destination = scanner.nextLine();
            System.out.print("Enter date (yyyy-MM-dd): ");
            LocalDate date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.print("Enter number of tickets: ");
            int tickets = Integer.parseInt(scanner.nextLine());

            List<Flight> availableFlights = flightController.searchFlights(destination, date, tickets);

            if (availableFlights.isEmpty()) {
                System.out.println("No flights found.");
                return;
            }

            for (int i = 0; i < availableFlights.size(); i++) {
                System.out.println((i + 1) + ". " + availableFlights.get(i));
            }
            System.out.print("Choose flight number (or 0 to cancel): ");
            int selected = Integer.parseInt(scanner.nextLine());
            if (selected == 0) return;

            Flight selectedFlight = availableFlights.get(selected - 1);
            List<Passenger> passengers = new ArrayList<>();

            for (int i = 0; i < tickets; i++) {
                System.out.print("Enter passenger " + (i + 1) + " firstname : ");
                String firstname = scanner.nextLine();
                System.out.print("Enter passenger " + (i + 1) + " lastname : ");
                String lastname = scanner.nextLine();
                passengers.add(new Passenger(firstname, lastname));
            }

            bookingController.handleBooking(selectedFlight.getId(), passengers);
        } catch (Exception e) {
            Logger.DebugLog("Invalid input or error occurred: " + e.getMessage());
        }
    }

    private void handleBookingCancellation() {
        System.out.print("Enter booking ID to cancel: ");
        String bookingId = scanner.nextLine();
        bookingController.handleCancellation(bookingId);
    }

    private void handleMyFlights() {
        System.out.print("Enter your full name: ");
        String fullName = scanner.nextLine();
        bookingController.showBookingsByPassenger(fullName);
    }
}

