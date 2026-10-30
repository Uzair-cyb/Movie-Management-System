
import models.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final ArrayList<Show> shows = new ArrayList<>();
    private static final ArrayList<Booking> bookings = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("MOVIE MANAGEMENT SYSTEM");

        initializeShows();
        mainMenu();

        scanner.close();
    }

    private static void initializeShows() {

        Movie movie1 = new Movie("Avengers: Endgame", 160, "Action");
        Movie movie2 = new Movie("The Dark Knight", 152, "Crime");
        Movie movie3 = new Movie("Inception", 148, "Sci-Fi");

        shows.add(new Show(movie1, "7:30 PM"));
        shows.add(new Show(movie2, "9:00 PM"));
        shows.add(new Show(movie3, "6:00 PM"));
    }

    private static void mainMenu() {

        boolean isRunning = true;

        while (isRunning) {

            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. View Available Shows");
            System.out.println("2. Book a Ticket");
            System.out.println("3. View My Bookings");
            System.out.println("4. Exit");
            System.out.println("================================");

            System.out.print("Enter your choice (1-4): ");

            int choice = getIntInput();

            switch (choice) {

                case 1:
                    viewAvailableShows();
                    break;

                case 2:
                    bookTicket();
                    break;

                case 3:
                    viewMyBookings();
                    break;

                case 4:
                    System.out.println("\nThank you for using Movie Management System.");
                    isRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAvailableShows() {

        System.out.println("\n========== AVAILABLE SHOWS ==========\n");

        for (int i = 0; i < shows.size(); i++) {

            Show show = shows.get(i);

            System.out.println((i + 1) + ". " + show.getMovie().getTitle());
            System.out.println("   Time: " + show.getShowTime());
            System.out.println("   Available Seats: " + show.getAvailableSeatsCount() + "/25");
            System.out.println();
        }
    }

    private static void bookTicket() {

        System.out.println("\n========== BOOK A TICKET ==========\n");

        viewAvailableShows();

        System.out.print("Enter show number (0 to cancel): ");

        int showChoice = getIntInput();

        if (showChoice == 0) {
            System.out.println("Booking cancelled.");
            return;
        }

        if (showChoice < 1 || showChoice > shows.size()) {
            System.out.println("Invalid show selection.");
            return;
        }

        Show selectedShow = shows.get(showChoice - 1);

        System.out.println("\n--- Customer Information ---");

        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        System.out.print("Enter your phone number: ");
        String customerPhone = scanner.nextLine();

        Customer customer = new Customer(customerName, customerPhone);

        selectedShow.displaySeats();

        while (true) {

            System.out.print("Enter seat number (e.g., A1, B3): ");

            String seatChoice = scanner.nextLine().trim().toUpperCase();

            Seat selectedSeat = selectedShow.findSeat(seatChoice);

            if (selectedSeat == null) {
                System.out.println("Seat does not exist. Please try again.");
                continue;
            }

            if (!selectedSeat.isAvailable()) {

                System.out.println("Seat is already booked.");

                Seat suggestedSeat = selectedShow.suggestAvailableSeat();

                if (suggestedSeat == null) {
                    System.out.println("No available seats for this show.");
                    return;
                }

                System.out.println("Available alternative: " + suggestedSeat.getSeatNumber());

                System.out.print("Would you like to book this seat? (yes/no): ");

                String response = scanner.nextLine().trim().toLowerCase();

                if (response.equals("yes") || response.equals("y")) {
                    selectedSeat = suggestedSeat;
                    break;
                }

                continue;
            }

            break;
        }

        if (selectedSeat.book()) {

            Booking booking = new Booking(customer, selectedShow, selectedSeat);

            bookings.add(booking);

            booking.displayConfirmation();

            System.out.println("Ticket booked successfully.");

        } else {

            System.out.println("Failed to book seat. Please try again.");
        }
    }

    private static void viewMyBookings() {

        if (bookings.isEmpty()) {
            System.out.println("\nNo bookings found.");
            return;
        }

        System.out.println("\n========== MY BOOKINGS ==========\n");

        for (int i = 0; i < bookings.size(); i++) {

            Booking booking = bookings.get(i);

            System.out.println((i + 1) + ". Booking ID: " + booking.getBookingId());
            System.out.println("   Customer: " + booking.getCustomer().getName());
            System.out.println("   Movie: " + booking.getShow().getMovie().getTitle());
            System.out.println("   Seat: " + booking.getSeat().getSeatNumber());
            System.out.println("   Time: " + booking.getShow().getShowTime());
            System.out.println();
        }
    }

    private static int getIntInput() {

        try {
            int value = scanner.nextInt();
            scanner.nextLine();
            return value;

        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
}
