package controller;

import controller.*;
import org.junit.jupiter.api.Test;
import service.*;
import DAO.*;
import entity.Booking;
import entity.Flight;
import entity.Passenger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BookingControllerTest {
    private BookingController bookingController;
    private BookingService bookingService;
    private BookingDao bookingDao;
    private FlightService flightService;
    private FlightDAO flightDao;

    private Booking booking;

    @BeforeEach
    public void setUp() {
        bookingDao = new BookingDao();
        flightDao = new FlightDAO();


        flightService = new FlightService(flightDao);
        bookingService = new BookingService(bookingDao,flightService);
        bookingController = new BookingController(bookingService);

        LocalDateTime startTime = LocalDateTime.of(2021,1,1,1,1);
        Flight ex_flight = new Flight("TestFID","TestDestination",startTime,100,90);

        Passenger p1 = new Passenger("Test1name","Test1surname");
        Passenger p2 = new Passenger("Test2name","Test2surname");
        Passenger p3 = new Passenger("Test3name","Test3surname");
        ArrayList<Passenger> passengers = new ArrayList<Passenger>();
        passengers.add(p1);
        passengers.add(p2);
        passengers.add(p3);

        flightDao.addFlight(ex_flight);
        bookingController.handleBooking(ex_flight.getId(), passengers);
    }

    @Test
    public void TesthandleCancellation() {
        List<String> ids = bookingController.showBookingIdbyPassenger("Test1name Test1surname");
        String temp1 = ids.get(0);
        bookingController.handleCancellation(ids.get(0));

        ids.clear();
        ids=bookingController.showBookingIdbyPassenger("Test1name Test1surname");
        assertEquals(ids.size(),1);
    }
}
