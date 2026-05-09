package com.hotel.exceptions;

/**
 * Thrown when a requested alternative hotel
 * is not found in the system.
 */
public class HotelNotFoundException extends Exception {

    public HotelNotFoundException(String message) {
        super(message);
    }
}