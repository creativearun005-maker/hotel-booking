package com.hotel.cli;

import com.hotel.exceptions.BookingNotFoundException;
import com.hotel.exceptions.HotelNotFoundException;
import com.hotel.exceptions.InvalidDateException;
import com.hotel.exceptions.InvalidInputException;
import com.hotel.exceptions.RoomAlreadyExistsException;
import com.hotel.exceptions.RoomNotFoundException;
import com.hotel.exceptions.RoomNotAvailableException;
import com.hotel.models.AlternativeHotel;
import com.hotel.services.BookingService;
import com.hotel.services.CommissionService;
import com.hotel.services.RecommendationService;
import com.hotel.services.ReferralService;
import com.hotel.services.RoomService;
import com.hotel.utils.InputValidator;
import java.awt.Frame;
import java.awt.Label;
import java.util.ArrayList;
import java.util.Scanner;
import com.hotel.models.Room;
/**
 * Handles all user commands and routes them to
 * the appropriate service.
 * This is the main interaction point between
 * the user and the system.
 */
public class CommandHandler {

    /* Needed Objects */
    private RoomService roomService;
    private BookingService bookingService;
    private RecommendationService recommendationService;
    private ReferralService referralService;
    private CommissionService commissionService;
    private InputValidator inputValidator;
    private Scanner scanner;
    private Frame cheatSheetFrame;

    // Constructor
    public CommandHandler() {
        this.roomService           = new RoomService();
        this.bookingService        = new BookingService(roomService);
        this.recommendationService = new RecommendationService();
        this.referralService       = new ReferralService();
        this.commissionService     = new CommissionService();
        this.inputValidator        = new InputValidator();
        this.scanner               = new Scanner(System.in);
    }

    /**
     * Starts the main command loop.
     * Keeps running until user types exit.
     */
    public void start() {

        System.out.println("------------------------------");
        System.out.println("Welcome to Hotel Management System");
        System.out.println("------------------------------");
        System.out.println("Type password 'admin123' to see all commands.");
        System.out.println("------------------------------");

        while (true) {

            System.out.print("\nhotel> ");
            String input = scanner.nextLine().trim();

            // skip empty input
            if (input.isEmpty()) {
                continue;
            }

            // exit the system

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using Hotel Management System. Goodbye!");
                // dispose cheat sheet frame if it is open
                if (cheatSheetFrame != null) {
                    cheatSheetFrame.dispose();
                }
                break;
            }

            // route command to appropriate handler
            handleCommand(input);
        }
    }

    /**
     * Routes the user command to the appropriate handler method.
     */
    private void handleCommand(String input) {

        String[] parts   = input.split(" ");
        String command   = parts[0].toLowerCase();

        switch (command) {

            case "admin123":
                displayHelp();
                break;

            case "add-room":
                handleAddRoom(parts);
                break;

            case "delete-room":
                handleDeleteRoom(parts);
                break;

            case "list-rooms":
                roomService.displayAllRooms();
                break;

            case "available-rooms":
                roomService.displayAvailableRooms();
                break;

            case "book":
                handleBookRoom(parts);
                break;

            case "cancel":
                handleCancelBooking(parts);
                break;

            case "list-bookings":
                bookingService.displayAllBookings();
                break;

            case "view-booking":
                handleViewBooking(parts);
                break;

            case "list-referrals":
                referralService.displayAllReferrals();
                break;

            case "generate-commission":
                handleGenerateCommission(parts);
                break;

            case "view-commission":
                handleViewCommission(parts);
                break;

            case "view-all-commissions":
                commissionService.displayAllBills();
                break;

            case "mark-paid":
                handleMarkCommissionPaid(parts);
                break;

            default:
                System.out.println("Unknown command. Type 'admin123' to see all commands.");
                break;
        }
    }

    /**
     * Handles add-room command.
     * Usage: add-room <roomNumber> <roomType> <pricePerNight>
     */
    private void handleAddRoom(String[] parts) {

        if (parts.length < 4) {
            System.out.println("Usage: add-room <roomNumber> <roomType> <pricePerNight>");
            System.out.println("Example: add-room 101 Deluxe 2500");
            return;
        }

        try {
            int roomNumber       = Integer.parseInt(parts[1]);
            String roomType      = parts[2];
            double pricePerNight = Double.parseDouble(parts[3]);

            //we have given room details to the add room method
            // so that it can store it in the list and provide service/list when asked

            roomService.addRoom(roomNumber, roomType, pricePerNight);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Room number and price must be numbers.");
        } catch (RoomAlreadyExistsException | InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles delete-room command.
     * Usage: delete-room <roomNumber>
     */
    private void handleDeleteRoom(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Usage: delete-room <roomNumber>");
            System.out.println("Example: delete-room 101");
            return;
        }

        try {
            int roomNumber = Integer.parseInt(parts[1]);
            roomService.deleteRoom(roomNumber);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Room number must be a number.");
        } catch (RoomNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles book command.
     * Supports guest names with spaces like Arun Kumar.
     * Usage: book <roomNumber> <guestName> <checkInDate> <checkOutDate>
     * Date format: DD-MM-YYYY
     */
    private void handleBookRoom(String[] parts) {

        if (parts.length < 5) {
            System.out.println("Usage: book <roomNumber> <guestName> <checkInDate> <checkOutDate>");
            System.out.println("Example: book 101 Arun Kumar 10-05-2026 13-05-2026");
            return;
        }

        // extract guest name before try block so it's accessible in all catch blocks
        StringBuilder nameBuilder = new StringBuilder();
        for (int i = 2; i < parts.length - 2; i++) {
            nameBuilder.append(parts[i]);
            if (i < parts.length - 3) {
                nameBuilder.append(" ");
            }
        }
        String guestName = nameBuilder.toString();

        try {
            int roomNumber  = Integer.parseInt(parts[1]);
            String checkIn  = parts[parts.length - 2];
            String checkOut = parts[parts.length - 1];

            bookingService.bookRoom(roomNumber, guestName, checkIn, checkOut);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Room number must be a number.");

        } catch (RoomNotAvailableException e) {
            System.out.println("Error: " + e.getMessage());
            handleRoomNotAvailable(guestName, Integer.parseInt(parts[1]));

        } catch (RoomNotFoundException e) {
            System.out.println("Error: Room " + parts[1] + " does not exist in the system, please check available rooms.");
            System.out.println("------------------------------");
            roomService.displayAvailableRooms();

            if (recommendationService.hasAvailableHotels()) {
                System.out.println("Would you like us to recommend another hotel?");
                System.out.println("[1] Yes Please");
                System.out.println("[0] No Thank You");
                System.out.print("Your choice: ");
                String choice = scanner.nextLine().trim();
                if (choice.equals("1")) {
                    recommendationService.displayRecommendedHotels();
                    handleHotelSelection(guestName);
                } else {
                    System.out.println("Thank you for visiting us. Have a great day!");
                }
            }

        } catch (InvalidDateException | InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the case when a room is not available.
     * Recommends alternative hotels to the customer.
     */
    private void handleRoomNotAvailable(String guestName, int bookedRoomNumber) {

        // show available rooms excluding the attempted room
        System.out.println("------------------------------");
        System.out.println("Other Available Rooms in Our Hotel");
        System.out.println("------------------------------");

        boolean anyAvailable = false;
        for (Room room : roomService.getAllRooms()) {
            if (room.getRoomNumber() != bookedRoomNumber && room.isAvailable()) {
                System.out.println(room.toString());
                System.out.println("------------------------------");
                anyAvailable = true;
            }
        }

        if (!anyAvailable) {
            System.out.println("No other rooms available.");
            System.out.println("------------------------------");
        }

        if (!recommendationService.hasAvailableHotels()) {
            System.out.println("Sorry, no alternative hotels are available at the moment.");
            return;
        }

        System.out.println("Would you like us to recommend another hotel?");
        System.out.println("Your driver is ready to drop you there!");
        System.out.println("[1] Yes Please");
        System.out.println("[0] No Thank You");
        System.out.print("Your choice: ");

        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            recommendationService.displayRecommendedHotels();
            handleHotelSelection(guestName);
        } else {
            System.out.println("Thank you for visiting us. Have a great day!");
        }
    }
    /**
     * Handles hotel selection after recommendation is shown.
     * Stores referral record when customer selects a hotel.
     */
    private void handleHotelSelection(String guestName) {

        System.out.print("Enter hotel number from the list: ");
        String choice = scanner.nextLine().trim();

        try {
            int hotelIndex = Integer.parseInt(choice);

            if (hotelIndex == 0) {
                System.out.println("Thank you for visiting us. Have a great day!");
                return;
            }

            ArrayList<AlternativeHotel> hotels =
                    recommendationService.getRecommendedHotels();

            if (hotelIndex < 1 || hotelIndex > hotels.size()) {
                System.out.println("Invalid choice. Please select a valid hotel number.");
                return;
            }

            AlternativeHotel selectedHotel = hotels.get(hotelIndex - 1);

            // store referral record in the system
            referralService.addReferral(guestName, selectedHotel);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        } catch (InvalidInputException | HotelNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles cancel command.
     * Usage: cancel <bookingId>
     */
    private void handleCancelBooking(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Usage: cancel <bookingId>");
            System.out.println("Example: cancel BK-001");
            return;
        }

        try {
            String bookingId = parts[1];
            bookingService.cancelBooking(bookingId);

        } catch (BookingNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles view-booking command.
     * Usage: view-booking <bookingId>
     */
    private void handleViewBooking(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Usage: view-booking <bookingId>");
            System.out.println("Example: view-booking BK-001");
            return;
        }

        try {
            String bookingId = parts[1];
            bookingService.displayBooking(bookingId);

        } catch (BookingNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles generate-commission command.
     * Generates commission bill for a specific hotel.
     * Usage: generate-commission <hotelId>
     */
    private void handleGenerateCommission(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Usage: generate-commission <hotelId>");
            System.out.println("Example: generate-commission KH001");
            return;
        }

        try {
            String hotelId = parts[1];
            AlternativeHotel hotel = recommendationService.getHotel(hotelId);
            commissionService.generateBill(hotel);

        } catch (HotelNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles view-commission command.
     * Displays commission bill for a specific hotel.
     * Usage: view-commission <hotelId>
     */
    private void handleViewCommission(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Usage: view-commission <hotelId>");
            System.out.println("Example: view-commission KH001");
            return;
        }

        try {
            String hotelId = parts[1];
            commissionService.displayBill(hotelId);

        } catch (HotelNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Handles mark-paid command.
     * Marks commission bill of a hotel as paid.
     * Usage: mark-paid <hotelId>
     */
    private void handleMarkCommissionPaid(String[] parts) {

        if (parts.length < 2) {
            System.out.println("Usage: mark-paid <hotelId>");
            System.out.println("Example: mark-paid KH001");
            return;
        }

        try {
            String hotelId = parts[1];
            commissionService.markBillAsPaid(hotelId);

        } catch (HotelNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays all available commands in console.
     * Also opens a cheat sheet frame for easy reference.
     */
    private void displayHelp() {

        // console output
        System.out.println("------------------------------");
        System.out.println("Available Commands");
        System.out.println("------------------------------");
        System.out.println("add-room <roomNumber> <roomType> <price>");
        System.out.println("    Example: add-room 101 Deluxe 2500");
        System.out.println();
        System.out.println("delete-room <roomNumber>");
        System.out.println("    Example: delete-room 101");
        System.out.println();
        System.out.println("list-rooms");
        System.out.println("    Shows all rooms in the system");
        System.out.println();
        System.out.println("available-rooms");
        System.out.println("    Shows all available rooms");
        System.out.println();
        System.out.println("book <roomNumber> <guestName> <checkInDate> <checkOutDate>");
        System.out.println("    Example: book 101 Arun Kumar 10-05-2026 13-05-2026");
        System.out.println();
        System.out.println("cancel <bookingId>");
        System.out.println("    Example: cancel BK-001");
        System.out.println();
        System.out.println("list-bookings");
        System.out.println("    Shows all bookings in the system");
        System.out.println();
        System.out.println("view-booking <bookingId>");
        System.out.println("    Example: view-booking BK-001");
        System.out.println();
        System.out.println("list-referrals");
        System.out.println("    Shows all referrals in the system");
        System.out.println();
        System.out.println("generate-commission <hotelId>");
        System.out.println("    Example: generate-commission KH001 ");
        System.out.println("hotelIds: KH001, AH001, PH001");
        System.out.println();
        System.out.println("view-commission <hotelId>");
        System.out.println("    Example: view-commission KH001");
        System.out.println();
        System.out.println("view-all-commissions");
        System.out.println("    Shows all commission bills");
        System.out.println();
        System.out.println("mark-paid <hotelId>");
        System.out.println("    Example: mark-paid KH001");
        System.out.println();
        System.out.println("exit");
        System.out.println("    Exit the system");
        System.out.println("------------------------------");
        System.out.println("NOTE:- So that you don't scroll up to check the commands and then write \n We have provided a cheatsheet as a Frame");
        System.out.println("so use it to avoid scrolling for commands");

        // open cheat sheet frame
        openCheatSheet();
    }

    /**
     * Opens a cheat sheet frame with all commands listed.
     * Single column layout, dark background, light bold text.
     * Acts as a reference guide while using the system.
     * Frame is stored as class variable so it can be
     * disposed when user types exit.
     */
    private void openCheatSheet() {

        // using class variable so we can dispose it on exit
        cheatSheetFrame = new Frame("Command Cheat Sheet");
        cheatSheetFrame.setSize(600, 750);
        cheatSheetFrame.setLayout(new java.awt.GridLayout(0, 1, 0, 2));
        cheatSheetFrame.setBackground(new java.awt.Color(30, 30, 30));

// always stay on top of other windows
        cheatSheetFrame.setAlwaysOnTop(true);

// disable resizing so restore down button is disabled
        cheatSheetFrame.setResizable(false);

        // define fonts
        java.awt.Font titleFont   = new java.awt.Font("Arial", java.awt.Font.BOLD, 16);
        java.awt.Font commandFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 13);
        java.awt.Font exampleFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 12);

        // define colors
        java.awt.Color titleColor   = new java.awt.Color(255, 215, 0);   // gold
        java.awt.Color commandColor = new java.awt.Color(255, 255, 255); // white
        java.awt.Color exampleColor = new java.awt.Color(180, 180, 180); // light grey
        java.awt.Color bgColor      = new java.awt.Color(30, 30, 30);    // dark background

        // title label
        Label title = new Label("  Hotel Management System - Command Cheat Sheet");
        title.setFont(titleFont);
        title.setForeground(titleColor);
        title.setBackground(bgColor);

        // separator
        Label sep1 = new Label("  ------------------------------------------------");
        sep1.setFont(commandFont);
        sep1.setForeground(commandColor);
        sep1.setBackground(bgColor);

        // add-room command
        Label addRoomTitle = new Label("  1. add-room <roomNumber> <roomType> <price>");
        addRoomTitle.setFont(commandFont);
        addRoomTitle.setForeground(commandColor);
        addRoomTitle.setBackground(bgColor);

        Label addRoomExample = new Label("     Example: add-room 101 Deluxe 2500");
        addRoomExample.setFont(exampleFont);
        addRoomExample.setForeground(exampleColor);
        addRoomExample.setBackground(bgColor);

        // delete-room command
        Label deleteRoomTitle = new Label("  2. delete-room <roomNumber>");
        deleteRoomTitle.setFont(commandFont);
        deleteRoomTitle.setForeground(commandColor);
        deleteRoomTitle.setBackground(bgColor);

        Label deleteRoomExample = new Label("     Example: delete-room 101");
        deleteRoomExample.setFont(exampleFont);
        deleteRoomExample.setForeground(exampleColor);
        deleteRoomExample.setBackground(bgColor);

        // list-rooms command
        Label listRoomsTitle = new Label("  3. list-rooms");
        listRoomsTitle.setFont(commandFont);
        listRoomsTitle.setForeground(commandColor);
        listRoomsTitle.setBackground(bgColor);

        Label listRoomsDesc = new Label("     Shows all rooms in the system");
        listRoomsDesc.setFont(exampleFont);
        listRoomsDesc.setForeground(exampleColor);
        listRoomsDesc.setBackground(bgColor);

        // available-rooms command
        Label availableRoomsTitle = new Label("  4. available-rooms");
        availableRoomsTitle.setFont(commandFont);
        availableRoomsTitle.setForeground(commandColor);
        availableRoomsTitle.setBackground(bgColor);

        Label availableRoomsDesc = new Label("     Shows all available rooms");
        availableRoomsDesc.setFont(exampleFont);
        availableRoomsDesc.setForeground(exampleColor);
        availableRoomsDesc.setBackground(bgColor);

        // book command
        Label bookTitle = new Label("  5. book <roomNumber> <guestName> <checkIn> <checkOut>");
        bookTitle.setFont(commandFont);
        bookTitle.setForeground(commandColor);
        bookTitle.setBackground(bgColor);

        Label bookExample = new Label("     Example: book 101 Arun Kumar 10-05-2026 13-05-2026");
        bookExample.setFont(exampleFont);
        bookExample.setForeground(exampleColor);
        bookExample.setBackground(bgColor);

        // cancel command
        Label cancelTitle = new Label("  6. cancel <bookingId>");
        cancelTitle.setFont(commandFont);
        cancelTitle.setForeground(commandColor);
        cancelTitle.setBackground(bgColor);

        Label cancelExample = new Label("     Example: cancel BK-001");
        cancelExample.setFont(exampleFont);
        cancelExample.setForeground(exampleColor);
        cancelExample.setBackground(bgColor);

        // list-bookings command
        Label listBookingsTitle = new Label("  7. list-bookings");
        listBookingsTitle.setFont(commandFont);
        listBookingsTitle.setForeground(commandColor);
        listBookingsTitle.setBackground(bgColor);

        Label listBookingsDesc = new Label("     Shows all bookings in the system");
        listBookingsDesc.setFont(exampleFont);
        listBookingsDesc.setForeground(exampleColor);
        listBookingsDesc.setBackground(bgColor);

        // view-booking command
        Label viewBookingTitle = new Label("  8. view-booking <bookingId>");
        viewBookingTitle.setFont(commandFont);
        viewBookingTitle.setForeground(commandColor);
        viewBookingTitle.setBackground(bgColor);

        Label viewBookingExample = new Label("     Example: view-booking BK-001");
        viewBookingExample.setFont(exampleFont);
        viewBookingExample.setForeground(exampleColor);
        viewBookingExample.setBackground(bgColor);

        // list-referrals command
        Label listReferralsTitle = new Label("  9. list-referrals");
        listReferralsTitle.setFont(commandFont);
        listReferralsTitle.setForeground(commandColor);
        listReferralsTitle.setBackground(bgColor);

        Label listReferralsDesc = new Label("     Shows all referrals in the system");
        listReferralsDesc.setFont(exampleFont);
        listReferralsDesc.setForeground(exampleColor);
        listReferralsDesc.setBackground(bgColor);

        // generate-commission command
        Label generateCommissionTitle = new Label("  10. generate-commission <hotelId>");
        generateCommissionTitle.setFont(commandFont);
        generateCommissionTitle.setForeground(commandColor);
        generateCommissionTitle.setBackground(bgColor);

        Label generateCommissionExample = new Label("      Example: generate-commission KH001");
        generateCommissionExample.setFont(exampleFont);
        generateCommissionExample.setForeground(exampleColor);
        generateCommissionExample.setBackground(bgColor);

        // view-commission command
        Label viewCommissionTitle = new Label("  11. view-commission <hotelId>");
        viewCommissionTitle.setFont(commandFont);
        viewCommissionTitle.setForeground(commandColor);
        viewCommissionTitle.setBackground(bgColor);

        Label viewCommissionExample = new Label("      Example: view-commission KH001     NOTE- hotelIds KH001, PH001, AH001");
        viewCommissionExample.setFont(exampleFont);
        viewCommissionExample.setForeground(exampleColor);
        viewCommissionExample.setBackground(bgColor);



        // view-all-commissions command
        Label viewAllCommissionsTitle = new Label("  12. view-all-commissions");
        viewAllCommissionsTitle.setFont(commandFont);
        viewAllCommissionsTitle.setForeground(commandColor);
        viewAllCommissionsTitle.setBackground(bgColor);

        Label viewAllCommissionsDesc = new Label("      Shows all commission bills");
        viewAllCommissionsDesc.setFont(exampleFont);
        viewAllCommissionsDesc.setForeground(exampleColor);
        viewAllCommissionsDesc.setBackground(bgColor);

        // mark-paid command
        Label markPaidTitle = new Label("  13. mark-paid <hotelId>");
        markPaidTitle.setFont(commandFont);
        markPaidTitle.setForeground(commandColor);
        markPaidTitle.setBackground(bgColor);

        Label markPaidExample = new Label("      Example: mark-paid KH001");
        markPaidExample.setFont(exampleFont);
        markPaidExample.setForeground(exampleColor);
        markPaidExample.setBackground(bgColor);

        // exit command
        Label exitTitle = new Label("  14. exit");
        exitTitle.setFont(commandFont);
        exitTitle.setForeground(commandColor);
        exitTitle.setBackground(bgColor);

        Label exitDesc = new Label("      Exit the system");
        exitDesc.setFont(exampleFont);
        exitDesc.setForeground(exampleColor);
        exitDesc.setBackground(bgColor);

        // separator at bottom
        Label sep2 = new Label("  ------------------------------------------------");
        sep2.setFont(commandFont);
        sep2.setForeground(commandColor);
        sep2.setBackground(bgColor);

        // adding all labels to frame
        cheatSheetFrame.add(title);
        cheatSheetFrame.add(sep1);
        cheatSheetFrame.add(addRoomTitle);
        cheatSheetFrame.add(addRoomExample);
        cheatSheetFrame.add(deleteRoomTitle);
        cheatSheetFrame.add(deleteRoomExample);
        cheatSheetFrame.add(listRoomsTitle);
        cheatSheetFrame.add(listRoomsDesc);
        cheatSheetFrame.add(availableRoomsTitle);
        cheatSheetFrame.add(availableRoomsDesc);
        cheatSheetFrame.add(bookTitle);
        cheatSheetFrame.add(bookExample);
        cheatSheetFrame.add(cancelTitle);
        cheatSheetFrame.add(cancelExample);
        cheatSheetFrame.add(listBookingsTitle);
        cheatSheetFrame.add(listBookingsDesc);
        cheatSheetFrame.add(viewBookingTitle);
        cheatSheetFrame.add(viewBookingExample);
        cheatSheetFrame.add(listReferralsTitle);
        cheatSheetFrame.add(listReferralsDesc);
        cheatSheetFrame.add(generateCommissionTitle);
        cheatSheetFrame.add(generateCommissionExample);
        cheatSheetFrame.add(viewCommissionTitle);
        cheatSheetFrame.add(viewCommissionExample);
        cheatSheetFrame.add(viewAllCommissionsTitle);
        cheatSheetFrame.add(viewAllCommissionsDesc);
        cheatSheetFrame.add(markPaidTitle);
        cheatSheetFrame.add(markPaidExample);
        cheatSheetFrame.add(exitTitle);
        cheatSheetFrame.add(exitDesc);
        cheatSheetFrame.add(sep2);

        // make frame visible
        cheatSheetFrame.setVisible(true);

        // close frame when X is clicked
        cheatSheetFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                cheatSheetFrame.dispose();
            }
        });
    }
}