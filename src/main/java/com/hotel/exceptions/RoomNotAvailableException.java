package com.hotel.exceptions;

/**
 * Thrown when a requested room is already booked
 * and not available for the selected dates.
 */
public class RoomNotAvailableException extends Exception {

    public RoomNotAvailableException(String message) {
        super(message);
    }
}