package javamysql;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static CustomerDao customerDao = new CustomerDao();
    private static AccountDao accountDao = new AccountDao();
    private static BoardingTicketDao boardingTicketDao = new BoardingTicketDao();
    private static FlightDao flightDao = new FlightDao();
    private static PaymentDao paymentDao = new PaymentDao();
    private static AirlineDao airlineDao = new AirlineDao();
    private static AircraftDao aircraftDao = new AircraftDao();

    public static void enterPaymentDetails(ArrayList<Flight> flightArrayList, Customer customer){
        System.out.println("Enter payment details");
        System.out.println("=====================");
        System.out.println("Credit Card No: ");
        Scanner input = new Scanner(System.in);
        String creditCardNo = input.next();

        Payment payment = new Payment();
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        payment.setCreditCardNo(creditCardNo);
        payment.setPaymentDate(timestamp);


        paymentDao.generatePayment(payment, flightArrayList, customer);
    }

    public static void addAirline(){
        System.out.println("Adding new Airline");
        System.out.println("==================");
        System.out.println("Airline Code:");
        Scanner inputAirlineCode = new Scanner(System.in);
        String airlineCode = inputAirlineCode.next();
        System.out.println("Airline Name:");
        Scanner inputAirlineName = new Scanner(System.in);
        String airlineName = inputAirlineName.nextLine();

        Airline airline = new Airline();
        airline.setAirlineCode(airlineCode);
        airline.setAirlineName(airlineName);

        airlineDao.addAirline(airline);
    }

    public static void addAircraft(){
        System.out.println("Adding new Aircraft");
        System.out.println("==================");
        System.out.println("Aircraft Model:");
        Scanner input = new Scanner(System.in);
        String aircraftModel = input.next();
        System.out.println("Aircraft Manufacturer:");
        String aircraftManufacturer = input.next();

        Aircraft aircraft = new Aircraft();
        aircraft.setAircraftModel(aircraftModel);
        aircraft.setAircraftManufacturer(aircraftManufacturer);

        aircraftDao.addAircraft(aircraft);
    }

    public static void addFlight(){
        System.out.println("Adding new Flight");
        System.out.println("==================");
        System.out.println("Flight Number:");
        Scanner input = new Scanner(System.in);
        String flightNo = input.next();
        System.out.println("Departure Location: ");
        String departure = input.next().toUpperCase();
        System.out.println("Destination Location: ");
        String destination = input.next().toUpperCase();

        System.out.println("Departure Date: (yyyy-MM-dd)");
        String departureDate = input.next();
        System.out.println("Departure Time: (HH:mm:ss)");
        String departureTime = input.next();
        String departureDateTime = departureDate + " " + departureTime;
        Timestamp departureTimeStamp = Timestamp.valueOf(departureDateTime);

        System.out.println("Arrival Date: (yyyy-MM-dd)");
        String arrivalDate = input.next();
        System.out.println("Arrival Time: (HH:mm:ss)");
        String arrivalTime = input.next();
        String arrivalDateTime = arrivalDate + " " + arrivalTime;
        Timestamp arrivalTimeStamp = Timestamp.valueOf(arrivalDateTime);

        System.out.println("Price: ");
        int price = input.nextInt();
        System.out.println("Select Airline: ");
        ArrayList<String> airlinesList = airlineDao.getAirlineArrayList();
        for(String airline:airlinesList){
            System.out.println(airlinesList.indexOf(airline)+1 + ". " + airline);
        }
        int airlineChoice = input.nextInt();
        String airlineCode = airlinesList.get(airlineChoice - 1).split(",")[0];

        System.out.println("Select Aircraft");
        ArrayList<String> aircraftList = aircraftDao.getAircraftArrayList();
        for(String aircraft:aircraftList){
            System.out.println(aircraftList.indexOf(aircraft)+1 + ". " + aircraft);
        }
        int aircraftChoice = input.nextInt();
        String aircraftModel = aircraftList.get(aircraftChoice - 1).split(",")[0];

        Flight flight = new Flight();
        flight.setFlightNo(flightNo);
        flight.setDeparture(departure);
        flight.setDestination(destination);
        flight.setDepartureDateTime(departureTimeStamp);
        flight.setArrivalDateTime(arrivalTimeStamp);
        flight.setPrice(price);
        flight.setAirlineCode(airlineCode);
        flight.setAircraftModel(aircraftModel);

        flightDao.addFlight(flight);
    }

    public static void viewFlights(Customer customer, ArrayList<Flight> flightArrayList){
        System.out.println("Choose departure location:");
        ArrayList<String> departureList = flightDao.displayDepartureLocations();
        if (departureList.size() != 0) {
            for (String d : departureList) {
                System.out.println(departureList.indexOf(d) + 1 + ". " + d);
            }
            Scanner input = new Scanner(System.in);
            int departureOption = input.nextInt();
            String departure = departureList.get(departureOption - 1);

            System.out.println("Choose destination location:");
            ArrayList<String> destinationList = flightDao.displayDestinationLocations(departure);
            for (String d : destinationList) {
                System.out.println(destinationList.indexOf(d) + 1 + ". " + d);
            }
            input = new Scanner(System.in);
            int destinationOption = input.nextInt();
            String destination = destinationList.get(destinationOption - 1);

            System.out.println("Select flight:");
            System.out.println("===============================");
            System.out.println(departure + "\tto\t" + destination);
            System.out.println("===============================");
            ArrayList<String> flightNoList = flightDao.displayDateTime(departure, destination);
            input = new Scanner(System.in);
            int flightDetailOption = input.nextInt();
            String flightNo = flightNoList.get(flightDetailOption - 1);

            Flight flight = flightDao.getFlightDetails(flightNo);
            flightArrayList.add(flight);
            bookingMenu(flightArrayList, customer);
        } else {
            System.out.println("There are no flights recorded yet.\n\n\n");
            customerMenu();
        }
    }

    public static void changePassword(Customer customer){
        System.out.println("===============================");
        System.out.println("Change Password");
        System.out.println("===============================");
        System.out.println("Enter your new password: ");
        Scanner input = new Scanner(System.in);
        String newPassword = input.next();
        accountDao.changePassword(newPassword, customer);

    }

    public static void bookingMenu(ArrayList<Flight> flightArrayList, Customer customer){
        System.out.println("Book another flight?\n1.Yes\n2.Go to checkout");
        Scanner input = new Scanner(System.in);
        int userInput = input.nextInt();
        switch (userInput){
            case 1:
                viewFlights(customer, flightArrayList);
                break;
            case 2:
                enterPaymentDetails(flightArrayList, customer);
                break;
        }
    }

    public static void cancelFlightTicket(Customer customer){
        System.out.println("Cancel Flight");
        System.out.println("======================");
        System.out.println("Enter Ticket ID:");
        Scanner input = new Scanner(System.in);
        String ticketID = input.next();
        boardingTicketDao.cancelFlightTicket(customer, ticketID);

    }

    public static void login(){
        System.out.println("===============================");
        System.out.println("Log In");
        System.out.println("===============================");
        System.out.println("Username: ");
        Scanner input = new Scanner(System.in);
        String username = input.next();

        System.out.println("Password: ");
        input = new Scanner(System.in);
        String password = input.next();

        Account account = new Account(); //a new account
        account.setUsername(username);
        account.setPassword(password);


        accountDao.verifyAccount(account);
    }

    public static void register(){
        System.out.println("Registration");
        System.out.println("===========================");
        System.out.println("First name: ");
        Scanner input = new Scanner(System.in);
        String fname = input.next();

        System.out.println("Last name: ");
        input = new Scanner(System.in);
        String lname = input.next();

        System.out.println("Email: ");
        input = new Scanner(System.in);
        String email = input.next();

        System.out.println("Phone Number: ");
        input = new Scanner(System.in);
        String phoneNo = input.next();

        System.out.println("Choose a username: ");
        input = new Scanner(System.in);
        String username = input.next();

        System.out.println("Choose a password: ");
        input = new Scanner(System.in);
        String password = input.next();


        Account account = new Account(); //a new account
        account.setUsername(username);
        account.setPassword(password);


        Customer customer = new Customer(); //a new customer
        customer.setFname(fname);
        customer.setLname(lname);
        customer.setEmail(email);
        customer.setPhoneNo(phoneNo);
        customer.setUsername(username);


        accountDao.registerAccount(account);
        customerDao.registerCustomer(customer);
    }

    public static void customerMenu() {
        System.out.println("===============================");
        System.out.println("Ryan Air Flight Booking System:");
        System.out.println("===============================");
        System.out.println("Select Option: ");
        System.out.println("1. Register for an account\n" +
                "2. Login\n" +
                "3. Quit");
        try{
            Scanner input = new Scanner(System.in);
            int menuOption = input.nextInt();
            switch (menuOption){
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Goodbye, have a nice day.\n\n\n");
                    selectRole();
                    break;
                default:
                    System.out.println("Please select options 1, 2 or 3.\n\n\n");
                    selectRole();
            }
        } catch (InputMismatchException e) {
            System.out.println("Check you input!\n\n\n");
            customerMenu();
        }


    }

    public static void customerLoggedInMenu(Customer customer) {
        System.out.println("===============================");
        System.out.println("Welcome " + customer.getUsername() +"!");
        System.out.println("===============================");
        System.out.println("Select Option: ");
        System.out.println("1. Book Flights\n" +
                "2. View Booked Flights\n" +
                "3. Cancel Booking\n" +
                "4. Logout\n" +
                "5. Change Password\n" +
                "6. Quit");
        try{
            Scanner input = new Scanner(System.in);
            int menuOption = input.nextInt();
            switch (menuOption){
                case 1:
                    ArrayList<Flight> flightArrayList = new ArrayList<>();
                    viewFlights(customer, flightArrayList);
                    break;
                case 2:
                    boardingTicketDao.viewTickets(customer);
                    break;
                case 3:
                    cancelFlightTicket(customer);
                    break;
                case 4:
                    login();
                    break;
                case 5:
                    changePassword(customer);
                    break;
                case 6:
                    System.out.println("Goodbye, have a nice day.");
                    break;
                default:
                    System.out.println("Please only select options 1 to 6.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Check you input!\n\n\n");
            customerLoggedInMenu(customer);
        }

    }

    public static void adminMenu() {
        System.out.println("===============================");
        System.out.println("Ryan Air Administrative System:");
        System.out.println("===============================");
        System.out.println("Select Option: ");
        System.out.println("1. Add new airline\n" +
                "2. Add new aircraft\n" +
                "3. Add new flight\n" +
                "4. Quit");
        try {
            Scanner input = new Scanner(System.in);
            int menuOption = input.nextInt();
            switch (menuOption){
                case 1:
                    addAirline();
                    break;
                case 2:
                    addAircraft();
                    break;
                case 3:
                    addFlight();
                    break;
                case 4:
                    System.out.println("Goodbye.\n\n\n");
                    selectRole();
                    break;
                default:
                    System.out.println("Please select options 1, 2 or 3.\n\n\n");
                    adminMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("Check your input!\n\n\n");
            adminMenu();
        }

    }

    public static void selectRole() {
        System.out.println("==================================");
        System.out.println("Ryan Air Flight Management System:");
        System.out.println("==================================");
        System.out.println("Select your role: ");
        System.out.println("1. Admin\n2. Customer");
        try{
            Scanner input = new Scanner(System.in);
            int role = input.nextInt();
            switch (role) {
                case 1:
                    adminMenu();
                    break;
                case 2:
                    customerMenu();
                    break;
                default:
                    System.out.println("Please select options 1 or 2.\n\n\n");
                    selectRole();
            }
        } catch (InputMismatchException e){
            System.out.println("Check your input!\n\n\n");
            selectRole();
        }

    }

    public static void main(String[] args) {
        selectRole();
    }
}
