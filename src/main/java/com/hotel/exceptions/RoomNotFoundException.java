package com.hotel.exceptions;

/**
 * Thrown when a requested room is not found in the system.
 */
public class RoomNotFoundException extends Exception {

    public RoomNotFoundException(String message) {
        super(message);
    }
}