package GradeTracker;


import java.util.ArrayList;
import java.util.Scanner;

class Room {
    int roomNumber;
    String category;
    double pricePerNight;
    boolean available;

    Room(int roomNumber, String category, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.available = true;
    }

    void display() {
        System.out.printf("%-8d %-12s Rs. %-10.2f %-10s%n",
                roomNumber, category, pricePerNight,
                available ? "Available" : "Booked");
    }
}

class Reservation {
    int bookingId;
    String guestName;
    int roomNumber;
    String category;
    int nights;
    double totalAmount;
    boolean paid;
    boolean active;

    Reservation(int bookingId, String guestName,
                Room room, int nights) {

        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomNumber = room.roomNumber;
        this.category = room.category;
        this.nights = nights;
        this.totalAmount = room.pricePerNight * nights;
        this.paid = false;
        this.active = true;
    }

    void display() {
        System.out.printf(
                "%-8d %-18s %-8d %-12s %-8d Rs. %-10.2f %-10s %-10s%n",
                bookingId, guestName, roomNumber, category,
                nights, totalAmount,
                paid ? "Paid" : "Pending",
                active ? "Active" : "Cancelled");
    }
}

public class HotelReservationSystem {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Room> rooms = new ArrayList<>();

    static ArrayList<Reservation> reservations = new ArrayList<>();

    static int nextBookingId = 1001;

    static void loadRooms() {

        rooms.add(new Room(101, "Standard", 1500));
        rooms.add(new Room(102, "Standard", 1500));
        rooms.add(new Room(103, "Standard", 1500));

        rooms.add(new Room(201, "Deluxe", 2500));
        rooms.add(new Room(202, "Deluxe", 2500));
        rooms.add(new Room(203, "Deluxe", 2500));

        rooms.add(new Room(301, "Suite", 4500));
        rooms.add(new Room(302, "Suite", 4500));
    }

    static void showAllRooms() {

        System.out.println("\n--- HOTEL ROOMS ---");

        System.out.printf("%-8s %-12s %-15s %s%n",
                "Room No.", "Category", "Price/Night", "Status");

        for (Room room : rooms) {
            room.display();
        }
    }

    static void searchRooms() {

        System.out.println("\nRoom Categories:");
        System.out.println("Standard");
        System.out.println("Deluxe");
        System.out.println("Suite");

        System.out.print("Enter category: ");
        String category = sc.next();

        boolean found = false;

        System.out.printf("%-8s %-12s %-15s %s%n",
                "Room No.", "Category", "Price/Night", "Status");

        for (Room room : rooms) {

            if (room.category.equalsIgnoreCase(category)
                    && room.available) {

                room.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No available rooms found.");
        }
    }

    static Room findRoom(int roomNumber) {

        for (Room room : rooms) {

            if (room.roomNumber == roomNumber) {
                return room;
            }
        }

        return null;
    }

    static void bookRoom() {

        showAllRooms();

        System.out.print("\nEnter guest name: ");
        sc.nextLine();
        String guestName = sc.nextLine();

        System.out.print("Enter room number: ");
        int roomNumber = sc.nextInt();

        Room room = findRoom(roomNumber);

        if (room == null || !room.available) {
            System.out.println("Room not available.");
            return;
        }

        System.out.print("Enter number of nights: ");
        int nights = sc.nextInt();

        if (nights <= 0) {
            System.out.println("Invalid number of nights.");
            return;
        }

        Reservation reservation = new Reservation(
                nextBookingId++, guestName, room, nights);

        reservations.add(reservation);

        room.available = false;

        System.out.println("\nBooking successful!");

        System.out.println("Booking ID: " + reservation.bookingId);

        System.out.printf("Total Amount: Rs. %.2f%n",
                reservation.totalAmount);

        System.out.print("Proceed with payment? (yes/no): ");

        String answer = sc.next();

        if (answer.equalsIgnoreCase("yes")) {

            System.out.println("\nPayment Methods:");
            System.out.println("1. UPI");
            System.out.println("2. Card");
            System.out.println("3. Cash");

            System.out.print("Choose payment method: ");
            int method = sc.nextInt();

            if (method >= 1 && method <= 3) {

                reservation.paid = true;

                System.out.println("Payment successful!");

            } else {
                System.out.println("Invalid payment method.");
                System.out.println("Payment remains pending.");
            }

        } else {

            System.out.println("Payment is pending.");
        }
    }

    static void cancelBooking() {

        System.out.print("Enter booking ID: ");
        int id = sc.nextInt();

        for (Reservation reservation : reservations) {

            if (reservation.bookingId == id
                    && reservation.active) {

                reservation.active = false;

                Room room = findRoom(reservation.roomNumber);

                if (room != null) {
                    room.available = true;
                }

                System.out.println("Booking cancelled successfully.");

                if (reservation.paid) {
                    System.out.println(
                            "Refund is not processed in this payment simulation.");
                }

                return;
            }
        }

        System.out.println("Active booking not found.");
    }

    static void showBookings() {

        System.out.println("\n--- BOOKING DETAILS ---");

        if (reservations.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        System.out.printf(
                "%-8s %-18s %-8s %-12s %-8s %-15s %-10s %-10s%n",
                "ID", "Guest", "Room", "Category",
                "Nights", "Total", "Payment", "Status");

        for (Reservation reservation : reservations) {
            reservation.display();
        }
    }

    public static void main(String[] args) {

        loadRooms();

        System.out.println("===== HOTEL RESERVATION SYSTEM =====");

        int choice;

        do {

            System.out.println("\n1. View All Rooms");
            System.out.println("2. Search Available Rooms");
            System.out.println("3. Book a Room");
            System.out.println("4. Cancel Booking");
            System.out.println("5. View Booking Details");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    showAllRooms();
                    break;

                case 2:
                    searchRooms();
                    break;

                case 3:
                    bookRoom();
                    break;

                case 4:
                    cancelBooking();
                    break;

                case 5:
                    showBookings();
                    break;

                case 6:
                    System.out.println(
                            "Thank you for using Hotel Reservation System!");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);

        sc.close();
    }
}
