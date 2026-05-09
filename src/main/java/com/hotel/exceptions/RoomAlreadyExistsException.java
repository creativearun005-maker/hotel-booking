package com.hotel.exceptions;

/**
 * Thrown when trying to add a room that already exists in the system.
 */
public class RoomAlreadyExistsException extends Exception {

    public RoomAlreadyExistsException(String message) {
        super(message);
    }
}