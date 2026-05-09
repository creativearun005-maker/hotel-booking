package com.hotel.exceptions;

/**
 * Thrown when user provides an invalid date format or
 * when check out date is before check in date.
 */
public class InvalidDateException extends Exception {

    public InvalidDateException(String message) {
        super(message);
    }
}