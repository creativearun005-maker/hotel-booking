package com.hotel.models;

import java.time.LocalDate;

/**
 * Represents a referral request created when a customer
 * is sent to an alternative hotel.
 * This record is stored in the system for tracking purposes.
 */
public class ReferralRequest {

    private String referralId;
    private String customerName;
    private String hotelId;
    private String hotelName;
    private LocalDate referralDate;
    private String status;

    // Constructor
    public ReferralRequest(String referralId, String customerName,
                           String hotelId, String hotelName) {
        this.referralId    = referralId;
        this.customerName  = customerName;
        this.hotelId       = hotelId;
        this.hotelName     = hotelName;
        this.referralDate  = LocalDate.now(); // automatically sets today's date
        this.status        = "PENDING"; // default status when referral is created
    }

    // Getters
    public String getReferralId() {
        return referralId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public LocalDate getReferralDate() {
        return referralDate;
    }

    public String getStatus() {
        return status;
    }

    // Setter
    public void setStatus(String status) {
        this.status = status;
    }

    // Display referral details
    public String toString() {
        return "Referral ID    : " + referralId +
                "\nCustomer Name  : " + customerName +
                "\nHotel ID       : " + hotelId +
                "\nHotel Name     : " + hotelName +
                "\nReferral Date  : " + referralDate +
                "\nStatus         : " + status;
    }
}