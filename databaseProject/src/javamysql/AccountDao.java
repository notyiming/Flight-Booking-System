package javamysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDao {
    public void verifyAccount(Account account) {
        try {
            Connection con = RunDB.getCon();

            String QUERY_ACCOUNT_SQL = "Select * From account where username = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY_ACCOUNT_SQL);
            pstmt.setString(1, account.getUsername());


            String QUERY_CUSTOMERID_SQL = "Select * From customer where username = ?";
            PreparedStatement pstmt2 = con.prepareStatement(QUERY_CUSTOMERID_SQL);
            pstmt2.setString(1, account.getUsername());

            ResultSet rs = pstmt.executeQuery();
            ResultSet rs2 = pstmt2.executeQuery();
            if (rs.next()) {
                rs2.next();
                Customer customer = new Customer();
                customer.setCustomerID(rs2.getString(1));
                customer.setFname(rs2.getString(2));
                customer.setLname(rs2.getString(3));
                customer.setEmail(rs2.getString(4));
                customer.setPhoneNo(rs2.getString(5));
                customer.setUsername(rs2.getString(6));
                if (account.getPassword().equals(rs.getString(2))) {
                    System.out.println("Logged In!\n\n\n");
                    Main.customerLoggedInMenu(customer);
                }
                else {
                    System.out.println("Incorrect Password!\n\n\n");
                    Main.customerMenu();
                }
            } else {
                System.out.println("Invalid User!\n\n\n");
                Main.customerMenu();
            }

        }catch (Exception io) {
            System.out.println("Error, going back to main menu:\n"+io+"\n\n\n");
            Main.customerMenu();
        }
    }

    public void registerAccount(Account account) {
        try {
            Connection con = RunDB.getCon();

            String INSERT_ACCOUNT_SQL = "INSERT INTO account" +
                    "(username, password) VALUES"+
                    "(?, ?);";
            PreparedStatement pstmt = con.prepareStatement(INSERT_ACCOUNT_SQL);
            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());

            pstmt.executeUpdate();
            System.out.println("New account is created\n\n\n");
        }catch (Exception io) {
            System.out.println("Error, going back to main menu:\n"+io+"\n\n\n");
            Main.customerMenu();
        }
    }

    public void changePassword(String newPassword, Customer customer) {
        try {
            Connection con = RunDB.getCon();

            String UPDATE_PASSWORD_SQL = " Update account set password = ? where username =?";
            PreparedStatement pstmt = con.prepareStatement(UPDATE_PASSWORD_SQL);
            pstmt.setString(1, newPassword);
            pstmt.setString(2, customer.getUsername());

            pstmt.executeUpdate();
            System.out.println("Your new password is set.\n\n\n");
            Main.customerMenu();
        }catch (Exception io) {
            System.out.println("changePassword Error, going back to main menu:\n"+io+"\n\n\n");
            Main.customerMenu();
        }
    }
}
