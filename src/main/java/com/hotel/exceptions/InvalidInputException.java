package com.hotel.exceptions;

/**
 * Thrown when user provides invalid or incorrect input.
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }
}