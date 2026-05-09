package com.hotel.interfaces;

import com.hotel.exceptions.HotelNotFoundException;
import com.hotel.models.AlternativeHotel;
import com.hotel.models.CommissionBill;
import java.util.ArrayList;

/**
 * Defines all operations related to commission billing.
 * CommissionService must implement all these methods.
 */
public interface CommissionOperations {

    // generate commission bill for a specific hotel
    CommissionBill generateBill(AlternativeHotel hotel)
            throws HotelNotFoundException;

    // get commission bill of a specific hotel
    CommissionBill getBill(String hotelId)
            throws HotelNotFoundException;

    // get list of all commission bills
    ArrayList<CommissionBill> getAllBills();

    // mark a commission bill as paid
    void markBillAsPaid(String hotelId)
            throws HotelNotFoundException;

    // display commission bill of a specific hotel
    void displayBill(String hotelId)
            throws HotelNotFoundException;

    // display all commission bills
    void displayAllBills();
}