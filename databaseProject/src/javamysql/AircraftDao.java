package javamysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AircraftDao {
    public ArrayList<String> getAircraftArrayList() {
        try {
            Connection con = RunDB.getCon();

            String QUERY_AIRCRAFT_SQL = "Select * From aircraft";
            PreparedStatement pstmt = con.prepareStatement(QUERY_AIRCRAFT_SQL);

            ResultSet rs = pstmt.executeQuery();
            ArrayList<String> aircraftList = new ArrayList<>();
            while (rs.next()) {
                aircraftList.add(rs.getString(1) + "," + rs.getString(2));
            }
            return aircraftList;

        }catch (Exception io) {
            System.out.println("Error, going back to main menu:\n"+io);
            Main.customerMenu();
        }
        return null;
    }

    public void addAircraft(Aircraft aircraft) {
        try {
            Connection con = RunDB.getCon();

            String INSERT_AIRCRAFT_SQL = "INSERT INTO aircraft" +
                    "(aircraftModel, aircraftmanufacturer) VALUES"+
                    "(?, ?);";
            PreparedStatement pstmt = con.prepareStatement(INSERT_AIRCRAFT_SQL);
            pstmt.setString(1, aircraft.getAircraftModel());
            pstmt.setString(2, aircraft.getAircraftManufacturer());

            pstmt.executeUpdate();
            System.out.println("New aircraft is added!\n\n\n");
            Main.adminMenu();
        }catch (Exception io) {
            System.out.println("Error, going back to main menu:\n"+io+"\n\n\n");
            Main.selectRole();
        }
    }

    public String getAircraftManufacturer(String aircraftModel) {
        try {
            Connection con = RunDB.getCon();

            String QUERY_AIRCRAFT_SQL = "SELECT aircraftmanufacturer FROM aircraft where aircraftModel = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY_AIRCRAFT_SQL);
            pstmt.setString(1, aircraftModel);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getString(1);
        }catch (Exception io) {
            System.out.println("getAircraftManufacturer Error, going back to main menu:\n"+io+"\n\n\n");
            Main.customerMenu();
        }
        return null;
    }
}
