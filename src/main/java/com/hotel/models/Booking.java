package com.hotel.models;

import java.time.LocalDate;

/**
 * Represents a hotel room booking made by a guest.
 * Stores all details related to a booking.
 */
public class Booking {

    private String bookingId;
    private int roomNumber;
    private String guestName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String status;

    // Constructor
    public Booking(String bookingId, int roomNumber, String guestName,
                   LocalDate checkInDate, LocalDate checkOutDate) {
        this.bookingId     = bookingId;
        this.roomNumber    = roomNumber;
        this.guestName     = guestName;
        this.checkInDate   = checkInDate;
        this.checkOutDate  = checkOutDate;
        this.status        = "CONFIRMED"; // default status when booking is created
    }

    // Getters
    public String getBookingId() {
        return bookingId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getGuestName() {
        return guestName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    // Setter
    public void setStatus(String status) {
        this.status = status;
    }

    // Display booking details
    public String toString() {
        return "Booking ID   : " + bookingId +
                "\nGuest Name   : " + guestName +
                "\nRoom Number  : " + roomNumber +
                "\nCheck In     : " + checkInDate +
                "\nCheck Out    : " + checkOutDate +
                "\nStatus       : " + status;
    }
}