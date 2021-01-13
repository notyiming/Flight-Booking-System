package javamysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AirlineDao {
    public ArrayList<String> getAirlineArrayList() {
        try {
            Connection con = RunDB.getCon();

            String QUERY_AIRLINE_SQL = "Select * From airline";
            PreparedStatement pstmt = con.prepareStatement(QUERY_AIRLINE_SQL);

            ResultSet rs = pstmt.executeQuery();
            ArrayList<String> airlineCodeList = new ArrayList<>();
            while (rs.next()) {
                airlineCodeList.add(rs.getString(1) + "," + rs.getString(2));
            }
            return airlineCodeList;

        }catch (Exception io) {
            System.out.println("Error, going back to main menu:\n"+io);
            Main.customerMenu();
        }
        return null;
    }

    public void addAirline(Airline airline) {
        try {
            Connection con = RunDB.getCon();

            String INSERT_AIRLINE_SQL = "INSERT INTO airline" +
                    "(airlineCode, airlineName) VALUES"+
                    "(?, ?);";
            PreparedStatement pstmt = con.prepareStatement(INSERT_AIRLINE_SQL);
            pstmt.setString(1, airline.getAirlineCode());
            pstmt.setString(2, airline.getAirlineName());

            pstmt.executeUpdate();
            System.out.println("New airline is added!\n\n\n");
            Main.adminMenu();
        }catch (Exception io) {
            System.out.println("Error, going back to main menu:\n"+io+"\n\n\n");
            Main.selectRole();
        }
    }

    public String getAirlineName(String airlineCode) {
        try {
            Connection con = RunDB.getCon();

            String QUERY_AIRLINE_SQL = "SELECT airlineName FROM airline where airlineCode = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY_AIRLINE_SQL);
            pstmt.setString(1, airlineCode);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getString(1);
        }catch (Exception io) {
            System.out.println("getAirlineName Error, going back to main menu:\n"+io+"\n\n\n");
            Main.customerMenu();
        }
        return null;
    }
}
