package com.hotel.models;

import java.time.LocalDate;

/**
 * Represents a commission bill generated for an alternative hotel.
 * When our hotel refers customers to another hotel,
 * that hotel owes us a commission fee per customer referred.
 *
 * Bill Amount = Total Customers Referred x Commission Per Customer
 *
 * Example:
 * Customers Referred   : 10
 * Commission/Customer  : Rs.1000
 * Total Bill Amount    : Rs.10000
 */
public class CommissionBill {

    private String billId;
    private String hotelId;
    private String hotelName;
    private int totalCustomersReferred;
    private double commissionPerCustomer;
    private double totalBillAmount;
    private String billStatus;
    private LocalDate billGeneratedDate;

    // Constructor
    public CommissionBill(String billId, String hotelId,
                          String hotelName, int totalCustomersReferred,
                          double commissionPerCustomer) {
        this.billId                 = billId;
        this.hotelId                = hotelId;
        this.hotelName              = hotelName;
        this.totalCustomersReferred = totalCustomersReferred;
        this.commissionPerCustomer  = commissionPerCustomer;
        this.totalBillAmount        = totalCustomersReferred * commissionPerCustomer;
        this.billStatus             = "UNPAID"; // default status when bill is generated
        this.billGeneratedDate      = LocalDate.now();
    }

    // Getters
    public String getBillId() {
        return billId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public int getTotalCustomersReferred() {
        return totalCustomersReferred;
    }

    public double getCommissionPerCustomer() {
        return commissionPerCustomer;
    }

    public double getTotalBillAmount() {
        return totalBillAmount;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public LocalDate getBillGeneratedDate() {
        return billGeneratedDate;
    }

    // Setter
    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    // Display commission bill details
    public String toString() {
        return "Bill ID              : " + billId +
                "\nHotel ID             : " + hotelId +
                "\nHotel Name           : " + hotelName +
                "\nTotal Customers      : " + totalCustomersReferred +
                "\nCommission/Customer  : Rs." + commissionPerCustomer +
                "\n------------------------------" +
                "\nTotal Bill Amount    : Rs." + totalBillAmount +
                "\nBill Status          : " + billStatus +
                "\nBill Generated Date  : " + billGeneratedDate;
    }
}