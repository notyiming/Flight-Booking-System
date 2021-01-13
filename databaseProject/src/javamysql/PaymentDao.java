package javamysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PaymentDao {
    private BookingDao bookingDao = new BookingDao();
    public void generatePayment(Payment payment, ArrayList<Flight> flightArrayList, Customer customer) {
        try {
            Connection con = RunDB.getCon();

            String INSERT_PAYMENT_SQL = "INSERT INTO payment" +
                    "(creditCardNo, paymentDate) VALUES"+
                    "(?, ?);";
            PreparedStatement pstmt = con.prepareStatement(INSERT_PAYMENT_SQL);

            pstmt.setString(1, payment.getCreditCardNo());
            pstmt.setString(2, payment.getPaymentDate());

            pstmt.executeUpdate();
            System.out.println("Payment Successful");

            String QUERY_PAYMENTID_SQL = "Select paymentID From payment where creditCardNo = ? and paymentDate = ?";
            PreparedStatement pstmt2 = con.prepareStatement(QUERY_PAYMENTID_SQL);
            pstmt2.setString(1, payment.getCreditCardNo());
            pstmt2.setString(2, payment.getPaymentDate());

            ResultSet rs = pstmt2.executeQuery();
            rs.next();
            String paymentID = rs.getString(1);
            payment.setPaymentID(paymentID);
            bookingDao.generateBooking(payment, flightArrayList, customer);

        }catch (Exception io) {
            System.out.println("Payment Error, going back to main menu:\n"+io+"\n\n\n");
            Main.customerMenu();
        }
    }
}
