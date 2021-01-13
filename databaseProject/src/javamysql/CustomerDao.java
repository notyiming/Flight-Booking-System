package javamysql;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CustomerDao {
    public void registerCustomer(Customer customer) {

        try {
            Connection con = RunDB.getCon();

            String INSERT_CUSTOMERS_SQL = "INSERT INTO customer" +
                    "(fname, lname, email, phoneNo, username) VALUES"+
                    "(?, ?, ?, ?, ?);";
            PreparedStatement pstmt = con.prepareStatement(INSERT_CUSTOMERS_SQL);
            pstmt.setString(1, customer.getFname());
            pstmt.setString(2, customer.getLname());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhoneNo());
            pstmt.setString(5, customer.getUsername());

            pstmt.executeUpdate();
            System.out.println("Your personal information is saved.\n\n\n");
            Main.customerMenu();

        }catch (Exception io) {
            System.out.println("Error, going back to main menu:\n"+io+"\n\n\n");
            Main.customerMenu();
        }
    }
}
