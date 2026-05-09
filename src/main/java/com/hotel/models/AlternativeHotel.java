package com.hotel.models;

import java.util.ArrayList;

/**
 * Represents an alternative hotel that can be recommended
 * to a customer when their desired room is not available.
 * Also tracks how many customers have been referred to this hotel.
 */
public class AlternativeHotel {

    private String hotelId;
    private String hotelName;
    private String location;
    private String contactNumber;
    private int customersReferred;
    private double commissionPerCustomer;
    private ArrayList<Room> availableRooms;

    // Constructor
    public AlternativeHotel(String hotelId, String hotelName,
                            String location, String contactNumber,
                            double commissionPerCustomer) {
        this.hotelId               = hotelId;
        this.hotelName             = hotelName;
        this.location              = location;
        this.contactNumber         = contactNumber;
        this.commissionPerCustomer = commissionPerCustomer;
        this.customersReferred     = 0;
        this.availableRooms        = new ArrayList<>();
    }

    // Getters
    public String getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getLocation() {
        return location;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public int getCustomersReferred() {
        return customersReferred;
    }

    public double getCommissionPerCustomer() {
        return commissionPerCustomer;
    }

    public ArrayList<Room> getAvailableRooms() {
        return availableRooms;
    }

    // Setters
    public void setCommissionPerCustomer(double commissionPerCustomer) {
        this.commissionPerCustomer = commissionPerCustomer;
    }

    // Add a room to this hotel's available rooms list
    public void addAvailableRoom(Room room) {
        availableRooms.add(room);
    }

    // Increase referred customer count by 1
    public void incrementCustomersReferred() {
        this.customersReferred++;
    }

    // Display hotel details
    public String toString() {
        return "Hotel ID             : " + hotelId +
                "\nHotel Name           : " + hotelName +
                "\nLocation             : " + location +
                "\nContact              : " + contactNumber +
                "\nCustomers Referred   : " + customersReferred +
                "\nCommission/Customer  : Rs." + commissionPerCustomer +
                "\nAvailable Rooms      : " + availableRooms.size();
    }
}