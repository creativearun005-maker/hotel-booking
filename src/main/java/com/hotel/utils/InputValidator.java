package com.hotel.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class that provides input validation methods.
 * Used across the system to validate user inputs
 * before processing them.
 * All methods return true if input is valid
 * and false if input is invalid.
 */
public class InputValidator {

    // date format accepted by the system
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // list of valid room types accepted by the system
    private static final List<String> VALID_ROOM_TYPES = Arrays.asList(
            "Single", "Double", "Deluxe", "Suite"
    );

    /**
     * Checks if the given input string is not null and not empty.
     */
    public boolean isValidInput(String input) {
        return input != null && !input.trim().isEmpty();
    }

    /**
     * Checks if the given number is positive.
     */
    public boolean isPositiveNumber(double number) {
        return number > 0;
    }

    /**
     * Checks if the given room number is valid.
     * Room number must be a positive integer.
     */
    public boolean isValidRoomNumber(int roomNumber) {
        return roomNumber > 0;
    }

    /**
     * Checks if the given room type is valid.
     * Valid types are Single, Double, Deluxe, Suite.
     */
    public boolean isValidRoomType(String roomType) {
        if (roomType == null || roomType.trim().isEmpty()) {
            return false;
        }
        return VALID_ROOM_TYPES.contains(roomType.trim());
    }

    /**
     * Checks if the given date string is in valid format.
     * Valid format is DD-MM-YYYY.
     * Also checks if date is not in the past.
     */
    public boolean isValidDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return false;
        }
        try {
            LocalDate parsedDate = LocalDate.parse(date.trim(), DATE_FORMAT);
            // check if date is not in the past
            return !parsedDate.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if check out date is after check in date.
     * Both dates must be in DD-MM-YYYY format.
     */
    public boolean isValidDateRange(String checkInDate, String checkOutDate) {
        try {
            LocalDate checkIn  = LocalDate.parse(checkInDate.trim(), DATE_FORMAT);
            LocalDate checkOut = LocalDate.parse(checkOutDate.trim(), DATE_FORMAT);
            return checkOut.isAfter(checkIn);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if the given guest name is valid.
     * Name must not be empty and must contain
     * only letters and spaces.
     * Supports names with spaces like Arun Kumar.
     */
    public boolean isValidGuestName(String guestName) {
        if (guestName == null || guestName.trim().isEmpty()) {
            return false;
        }
        // name should only contain letters and spaces
        return guestName.trim().matches("[a-zA-Z ]+");
    }

    /**
     * Checks if the given booking ID is in valid format.
     * Valid format is BK-001, BK-002 etc.
     */
    public boolean isValidBookingId(String bookingId) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            return false;
        }
        return bookingId.trim().matches("BK-[0-9]{3}");
    }

    /**
     * Checks if the given referral ID is in valid format.
     * Valid format is REF-001, REF-002 etc.
     */
    public boolean isValidReferralId(String referralId) {
        if (referralId == null || referralId.trim().isEmpty()) {
            return false;
        }
        return referralId.trim().matches("REF-[0-9]{3}");
    }

    /**
     * Checks if the given price is valid.
     * Price must be greater than zero.
     */
    public boolean isValidPrice(double price) {
        return price > 0;
    }

    /**
     * Checks if the given hotel ID is in valid format.
     * Valid format is KH001, AH001, PH001 etc.
     */
    public boolean isValidHotelId(String hotelId) {
        if (hotelId == null || hotelId.trim().isEmpty()) {
            return false;
        }
        return hotelId.trim().matches("[A-Z]{2}[0-9]{3}");
    }
}