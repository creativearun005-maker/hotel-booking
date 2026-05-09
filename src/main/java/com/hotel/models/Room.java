package com.hotel.models;

/**
 * Represents a hotel room in the system.
 * Stores all details related to a room.
 */
public class Room {

    private int roomNumber;
    private String roomType;
    private double pricePerNight;
    private boolean isAvailable;

    // Constructor
    public Room(int roomNumber, String roomType, double pricePerNight) {
        this.roomNumber    = roomNumber;
        this.roomType      = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable   = true; // room is available by default when added
    }

    // Getters
    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setters
    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    // Display room details
    public String toString() {
        return "Room Number  : " + roomNumber +
                "\nRoom Type    : " + roomType +
                "\nPrice/Night  : Rs." + pricePerNight +
                "\nAvailability : " + (isAvailable ? "Available" : "Not Available");
    }
}