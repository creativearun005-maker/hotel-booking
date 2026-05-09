package com.hotel.interfaces;

import com.hotel.exceptions.BookingNotFoundException;
import com.hotel.exceptions.InvalidDateException;
import com.hotel.exceptions.InvalidInputException;
import com.hotel.exceptions.RoomNotFoundException;
import com.hotel.exceptions.RoomNotAvailableException;
import com.hotel.models.Booking;
import java.util.ArrayList;

/**
 * Defines all operations that can be performed on bookings.
 * BookingService must implement all these methods.
 */
public interface BookingOperations {

    // book a room for a guest
    void bookRoom(int roomNumber, String guestName,
                  String checkInDate, String checkOutDate)
            throws RoomNotFoundException, RoomNotAvailableException,
            InvalidDateException, InvalidInputException;

    // cancel an existing booking
    void cancelBooking(String bookingId)
            throws BookingNotFoundException;

    // get a specific booking by booking ID
    Booking getBooking(String bookingId)
            throws BookingNotFoundException;

    // get list of all bookings
    ArrayList<Booking> getAllBookings();

    // display all bookings
    void displayAllBookings();

    // display a specific booking by ID
    void displayBooking(String bookingId)
            throws BookingNotFoundException;
}