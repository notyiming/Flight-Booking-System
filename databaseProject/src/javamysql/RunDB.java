package javamysql;

import java.sql.*;

/**
 *
 * @Yi Ming Tan
 */
public class RunDB {
    private static Connection con;
    public static Connection getCon() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/r00183099flightBookingSystem?user=root&password=mysql");
        }catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
        return con;
    }
}
