package javamysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BookingDao {
    private BoardingTicketDao boardingTicketDao = new BoardingTicketDao();
    public void generateBooking(Payment payment, ArrayList<Flight> flightArrayList, Customer customer) {
        try {
            Connection con = RunDB.getCon();

            String INSERT_BOOKING_SQL = "INSERT INTO booking" +
                    "(paymentID, customerID) VALUES"+
                    "(?, ?);";
            PreparedStatement pstmt = con.prepareStatement(INSERT_BOOKING_SQL);

            pstmt.setString(1, payment.getPaymentID());
            pstmt.setString(2, customer.getCustomerID());

            pstmt.executeUpdate();
            System.out.println("Booking Successful");


            String QUERY_BOOKINGID_SQL = "Select * From booking where paymentID = ?";
            PreparedStatement pstmt2 = con.prepareStatement(QUERY_BOOKINGID_SQL);
            pstmt2.setString(1, payment.getPaymentID());

            ResultSet rs = pstmt2.executeQuery();
            rs.next();
            String bookingID = rs.getString(1);
            String paymentID = rs.getString(2);
            String customerID = rs.getString(3);

            Booking booking = new Booking();
            booking.setBookingID(bookingID);
            booking.setPaymentID(paymentID);
            booking.setCustomerID(customerID);

            boardingTicketDao.printTicket(booking, flightArrayList, customer);

        }catch (Exception io) {
            System.out.println("Booking Error, going back to main menu:\n"+io+"\n\n\n");
            Main.customerMenu();
        }
    }
}
