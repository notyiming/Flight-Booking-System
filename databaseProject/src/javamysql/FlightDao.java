package javamysql;

import java.sql.*;
import java.util.ArrayList;

public class FlightDao {
    public ArrayList<String> displayDepartureLocations() {
        try {
            Connection con = RunDB.getCon();

            String QUERY_DEPARTURE_LOCATION_SQL = "Select departure From flight";
            PreparedStatement pstmt = con.prepareStatement(QUERY_DEPARTURE_LOCATION_SQL);

            ResultSet rs = pstmt.executeQuery();
            ArrayList<String> departureList = new ArrayList<>();
            while (rs.next()) {
                departureList.add(rs.getString(1));
            }

            ArrayList<String> departureListNoDuplicates = new ArrayList<>();
            for(String d: departureList) {
                if (!departureListNoDuplicates.contains(d)){
                    departureListNoDuplicates.add(d);
                }
            }
            return departureListNoDuplicates;

        }catch (Exception io) {
            System.out.println("Error, going back to main menu:\n"+io);
            Main.customerMenu();
        }
        return null;
    }

    public ArrayList<String> displayDestinationLocations(String destination) {
        try {
            Connection con = RunDB.getCon();

            String QUERY_DESTINATION_LOCATION_SQL = "Select destination From flight where departure = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY_DESTINATION_LOCATION_SQL);
            pstmt.setString(1, destination);

            ResultSet rs = pstmt.executeQuery();
            ArrayList<String> destinationList = new ArrayList<>();
            while (rs.next()) {
                destinationList.add(rs.getString(1));
            }

            ArrayList<String> destinationListNoDuplicates = new ArrayList<>();
            for(String d: destinationList) {
                if (!destinationListNoDuplicates.contains(d)){
                    destinationListNoDuplicates.add(d);
                }
            }
            return destinationListNoDuplicates;

        }catch (Exception io) {
            System.out.println("Error, going back to main menu:\n"+io);
            Main.customerMenu();
        }
        return null;
    }

    public ArrayList<String> displayDateTime(String departure, String destination) {
        try {
            Connection con = RunDB.getCon();

            String QUERY_FLIGHTDATETIME_PRICE_SQL = "Select flightNo, departureDateTime, arrivalDateTime, Price From flight where departure = ? and destination = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY_FLIGHTDATETIME_PRICE_SQL);
            pstmt.setString(1, departure);
            pstmt.setString(2, destination);

            ResultSet rs = pstmt.executeQuery();
            ArrayList<String> flightNoList = new ArrayList<>();

            int count = 1;
            while (rs.next()) {
                System.out.println(count + ". Flight No: " + rs.getString(1) + "\nDeparture: " + rs.getString(2) +
                        "\nArrival: " + rs.getString(3) + "\nPrice: " + rs.getString(4));
                System.out.println("===============================");
                flightNoList.add(rs.getString(1));
                count++;
            }
            return flightNoList;

        }catch (Exception io) {
            System.out.println("Error, going back to main menu:\n"+io);
            Main.customerMenu();
        }
        return null;
    }

    public Flight getFlightDetails(String flightNo) {
        try {
            Connection con = RunDB.getCon();

            String QUERY_FLIGHT_SQL = "SELECT * FROM flight where flightNo = ?";
            PreparedStatement pstmt = con.prepareStatement(QUERY_FLIGHT_SQL);
            pstmt.setString(1, flightNo);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            Flight flight = new Flight();
            flight.setFlightNo(rs.getString(1));
            flight.setDeparture(rs.getString(2));
            flight.setDestination(rs.getString(3));
            flight.setDepartureDateTime(Timestamp.valueOf(rs.getString(4)));
            flight.setArrivalDateTime(Timestamp.valueOf(rs.getString(5)));
            flight.setPrice(Integer.parseInt(rs.getString(6)));
            flight.setAirlineCode(rs.getString(7));
            flight.setAircraftModel(rs.getString(8));
            return flight;

        }catch (Exception io) {
            System.out.println("getFlightDetails Error, going back to main menu:\n"+io+"\n\n\n");
            Main.customerMenu();
        }
        return null;
    }

    public void addFlight(Flight flight){
        try {
            Connection con = RunDB.getCon();

            String INSERT_FLIGHT_SQL = "INSERT INTO flight" +
                    "(flightNo, departure, destination, departureDateTime, arrivalDateTime, price, airlineCode, aircraftModel) VALUES"+
                    "(?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement pstmt = con.prepareStatement(INSERT_FLIGHT_SQL);
            pstmt.setString(1, flight.getFlightNo());
            pstmt.setString(2, flight.getDeparture());
            pstmt.setString(3, flight.getDestination());
            pstmt.setTimestamp(4, flight.getDepartureDateTime());
            pstmt.setTimestamp(5, flight.getArrivalDateTime());
            pstmt.setInt(6, flight.getPrice());
            pstmt.setString(7, flight.getAirlineCode());
            pstmt.setString(8, flight.getAircraftModel());

            pstmt.executeUpdate();
            System.out.println("New flight is added!\n\n\n");
            Main.adminMenu();
        }catch (Exception io) {
            System.out.println("Error, going back to main menu:\n"+io+"\n\n\n");
            Main.selectRole();
        }
    }
}
