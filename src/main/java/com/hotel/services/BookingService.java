package com.hotel.services;

import com.hotel.exceptions.BookingNotFoundException;
import com.hotel.exceptions.InvalidDateException;
import com.hotel.exceptions.InvalidInputException;
import com.hotel.exceptions.RoomNotFoundException;
import com.hotel.exceptions.RoomNotAvailableException;
import com.hotel.interfaces.BookingOperations;
import com.hotel.models.Booking;
import com.hotel.models.Room;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Handles all booking related operations.
 * Implements BookingOperations interface.
 */
public class BookingService implements BookingOperations {

    // date format accepted by the system
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ArrayList to store all bookings in the system
    private ArrayList<Booking> bookings = new ArrayList<>();

    // RoomService is needed to check room details
    private RoomService roomService;

    // counter to generate unique booking IDs
    private int bookingCounter = 1;

    // Constructor
    public BookingService(RoomService roomService) {
        this.roomService = roomService;
    }

    /**
     * Books a room for a guest.
     * Validates all input before booking.
     * Uses date based availability checking so that
     * a room can be booked by different guests on different dates.
     */
    @Override
    public void bookRoom(int roomNumber, String guestName,
                         String checkInDate, String checkOutDate)
            throws RoomNotFoundException, RoomNotAvailableException,
            InvalidDateException, InvalidInputException {

        // check if guest name is valid
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidInputException(
                    "Guest name cannot be empty."
            );
        }

        // check if room exists in the system
        roomService.getRoom(roomNumber);

        // parse and validate dates
        LocalDate checkIn;
        LocalDate checkOut;

        try {
            checkIn  = LocalDate.parse(checkInDate.trim(), DATE_FORMAT);
            checkOut = LocalDate.parse(checkOutDate.trim(), DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new InvalidDateException(
                    "Invalid date format. Please use DD-MM-YYYY format. Example: 10-05-2026"
            );
        }

        // check if check in date is in the past
        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidDateException(
                    "Check in date cannot be in the past."
            );
        }

        // check if check out date is after check in date
        if (!checkOut.isAfter(checkIn)) {
            throw new InvalidDateException(
                    "Check out date must be after check in date."
            );
        }

        // check if room is available for the given dates
        if (!isRoomAvailableForDates(roomNumber, checkIn, checkOut)) {
            throw new RoomNotAvailableException(
                    "Room " + roomNumber + " is not available for the selected dates."
            );
        }

        // generate unique booking ID
        String bookingId = "BK-" + String.format("%03d", bookingCounter++);

        // create new booking and add to list
        Booking booking = new Booking(bookingId, roomNumber,
                guestName, checkIn, checkOut);
        bookings.add(booking);

        System.out.println("Booking confirmed successfully.");
        System.out.println("Your Booking ID is : " + bookingId);
    }

    /**
     * Checks if a room is available for the given date range.
     * A room is available if no existing confirmed booking
     * overlaps with the requested dates.
     * This allows same room to be booked by different guests
     * on different dates.
     */
    private boolean isRoomAvailableForDates(int roomNumber,
                                            LocalDate checkIn,
                                            LocalDate checkOut) {
        for (Booking booking : bookings) {
            if (booking.getRoomNumber() == roomNumber
                    && !booking.getStatus().equals("CANCELLED")) {

                LocalDate existingCheckIn  = booking.getCheckInDate();
                LocalDate existingCheckOut = booking.getCheckOutDate();

                // check if requested dates overlap with existing booking
                boolean overlaps = checkIn.isBefore(existingCheckOut)
                        && checkOut.isAfter(existingCheckIn);

                if (overlaps) {
                    return false; // dates overlap, room not available
                }
            }
        }
        return true; // no overlap found, room is available
    }

    /**
     * Cancels an existing booking.
     * Updates booking status to CANCELLED.
     */
    @Override
    public void cancelBooking(String bookingId) throws BookingNotFoundException {

        Booking booking = getBooking(bookingId);

        // check if booking is already cancelled
        if (booking.getStatus().equals("CANCELLED")) {
            System.out.println("Booking " + bookingId + " is already cancelled.");
            return;
        }

        // update booking status to cancelled
        booking.setStatus("CANCELLED");
        System.out.println("Booking " + bookingId + " cancelled successfully.");
    }

    /**
     * Returns a specific booking by booking ID.
     * Throws exception if booking is not found.
     */
    @Override
    public Booking getBooking(String bookingId) throws BookingNotFoundException {

        for (Booking booking : bookings) {
            if (booking.getBookingId().equals(bookingId)) {
                return booking;
            }
        }

        throw new BookingNotFoundException(
                "Booking " + bookingId + " does not exist in the system."
        );
    }

    /**
     * Returns list of all bookings in the system.
     */
    @Override
    public ArrayList<Booking> getAllBookings() {
        return bookings;
    }

    /**
     * Displays all bookings in the system.
     */
    @Override
    public void displayAllBookings() {

        if (bookings.isEmpty()) {
            System.out.println("No bookings found in the system.");
            return;
        }

        System.out.println("------------------------------");
        System.out.println("All Bookings");
        System.out.println("------------------------------");

        for (Booking booking : bookings) {
            System.out.println(booking.toString());
            System.out.println("------------------------------");
        }
    }

    /**
     * Displays a specific booking by booking ID.
     */
    @Override
    public void displayBooking(String bookingId) throws BookingNotFoundException {

        Booking booking = getBooking(bookingId);
        System.out.println("------------------------------");
        System.out.println(booking.toString());
        System.out.println("------------------------------");
    }

    /**
     * Returns total number of bookings in the system.
     * Used in test cases.
     */
    public int getTotalBookings() {
        return bookings.size();
    }

    /**
     * Returns the last created booking ID.
     * Used in test cases.
     */
    public String getLastBookingId() {
        if (bookings.isEmpty()) {
            return null;
        }
        return bookings.get(bookings.size() - 1).getBookingId();
    }
}