package javamysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BoardingTicketDao {
    private AircraftDao aircraftDao = new AircraftDao();
    private AirlineDao airlineDao = new AirlineDao();
    private FlightDao flightDao = new FlightDao();

    public void printTicket(Booking booking, ArrayList<Flight> flightArrayList, Customer customer) {
        try {
            Connection con = RunDB.getCon();

            for(Flight flight:flightArrayList) {
                String INSERT_BOARDINGTICKET_SQL = "INSERT INTO boardingticket" +
                        "(seat, bookingID, flightNo) VALUES"+
                        "(?, ?, ?);";
                PreparedStatement pstmt = con.prepareStatement(INSERT_BOARDINGTICKET_SQL);
                int randomNum = ThreadLocalRandom.current().nextInt(1, 25);
                Random r = new Random();
                String alphabet = "ABCDEFGHIJK";
                String number = "" + randomNum;
                String seatNo = number + alphabet.charAt(r.nextInt(alphabet.length()));
                pstmt.setString(1, seatNo);
                pstmt.setString(2, booking.getBookingID());
                pstmt.setString(3, flight.getFlightNo());

                pstmt.executeUpdate();
            }
            System.out.println("Ticket(s) Generated Successfully, it has also been sent to " + customer.getEmail());
            viewTickets(booking, customer);

        }catch (Exception io) {
            System.out.println("printTicket Error, going back to main menu:\n"+io);
            Main.customerMenu();
        }
    }

    public void viewTickets(Booking booking, Customer customer) {
        try {
            Connection con = RunDB.getCon();

            String QUERY_TICKET_SQL = "SELECT * FROM (booking inner join boardingticket on booking.bookingID = boardingticket.bookingID) inner join flight on boardingticket.flightNo = flight.flightNo where customerID = ? and boardingticket.bookingID = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY_TICKET_SQL);
            pstmt.setString(1, customer.getCustomerID());
            pstmt.setString(2, booking.getBookingID());

            ResultSet rs = pstmt.executeQuery();

            System.out.println("====================================");
            System.out.println("Booking Summary");
            System.out.println("====================================");


            while (rs.next()) {
                String fullName = customer.getFname() + " " + customer.getLname();
                Flight flightDetails = flightDao.getFlightDetails(rs.getString(7));
                assert flightDetails != null;
                String airlineName = airlineDao.getAirlineName(flightDetails.getAirlineCode());
                String aircraftManufacturer = aircraftDao.getAircraftManufacturer(flightDetails.getAircraftModel());
                String seat = rs.getString(5);
                String ticketID = rs.getString(4);
                String bookingID = rs.getString(1);

                System.out.println("Customer: " + fullName);
                System.out.println("Flight No: " + flightDetails.getFlightNo());
                System.out.println("Departure: " + flightDetails.getDeparture() + " " + flightDetails.getDepartureDateTime());
                System.out.println("Destination: " + flightDetails.getDestination() + " " + flightDetails.getArrivalDateTime());
                System.out.println("Aircraft: " + aircraftManufacturer + " " + flightDetails.getAircraftModel());
                System.out.println("Operated by: " + airlineName);
                System.out.println("Seat: " + seat);
                System.out.println("Ticket ID: " + ticketID);
                System.out.println("Booking ID: " + bookingID);
                System.out.println("====================================");
            }

            System.out.println("\n\n\n");
            Main.customerLoggedInMenu(customer);

        }catch (Exception io) {
            System.out.println("viewTicket Error, going back to main menu:\n"+io);
            Main.customerMenu();
        }
    }

    public void viewTickets(Customer customer) {
        try {
            Connection con = RunDB.getCon();

            String QUERY_TICKET_SQL = "SELECT * FROM (booking inner join boardingticket on booking.bookingID = boardingticket.bookingID) inner join flight on boardingticket.flightNo = flight.flightNo where customerID = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY_TICKET_SQL);
            pstmt.setString(1, customer.getCustomerID());

            ResultSet rs = pstmt.executeQuery();

            System.out.println("====================================");
            System.out.println("Flight Boarding Tickets");
            System.out.println("====================================");
            while(rs.next()){
                String fullName = customer.getFname() + " " + customer.getLname();
                Flight flightDetails = flightDao.getFlightDetails(rs.getString(7));
                assert flightDetails != null;
                String airlineName = airlineDao.getAirlineName(flightDetails.getAirlineCode());
                String aircraftManufacturer = aircraftDao.getAircraftManufacturer(flightDetails.getAircraftModel());
                String seat = rs.getString(5);
                String ticketID = rs.getString(4);
                String bookingID = rs.getString(1);

                System.out.println("Customer: " + fullName);
                System.out.println("Flight No: " + flightDetails.getFlightNo());
                System.out.println("Departure: " + flightDetails.getDeparture() + " " + flightDetails.getDepartureDateTime());
                System.out.println("Destination: " + flightDetails.getDestination() + " " + flightDetails.getArrivalDateTime());
                System.out.println("Aircraft: " + aircraftManufacturer + " " + flightDetails.getAircraftModel());
                System.out.println("Operated by: " + airlineName);
                System.out.println("Seat: " + seat);
                System.out.println("Ticket ID: " + ticketID);
                System.out.println("Booking ID: " + bookingID);
                System.out.println("====================================");
            }

            System.out.println("\n\n\n");
            Main.customerLoggedInMenu(customer);

        }catch (Exception io) {
            System.out.println("viewTicket Error, going back to main menu:\n"+io);
            Main.customerMenu();
        }
    }

    public void cancelFlightTicket(Customer customer, String ticketID) {
        try {
            Connection con = RunDB.getCon();

            String DELETE_TICKET_SQL = "DELETE boardingticket FROM (boardingticket inner join booking on boardingticket.bookingID = booking.bookingID) where customerID = ? and ticketID = ?";
            PreparedStatement pstmt = con.prepareStatement(DELETE_TICKET_SQL);
            pstmt.setString(1, customer.getCustomerID());
            pstmt.setString(2, ticketID);
            pstmt.executeUpdate();
            System.out.println("Flight is cancelled.\n\n\n");
            Main.customerLoggedInMenu(customer);


        }catch (Exception io) {
            System.out.println("error"+io);
        };
    }
}
