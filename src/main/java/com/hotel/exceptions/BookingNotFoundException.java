package com.hotel.exceptions;

/**
 * Thrown when a requested booking is not found in the system.
 */
public class BookingNotFoundException extends Exception {

    public BookingNotFoundException(String message) {
        super(message);
    }
}