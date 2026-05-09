package com.hotel.services;

import com.hotel.exceptions.HotelNotFoundException;
import com.hotel.interfaces.CommissionOperations;
import com.hotel.models.AlternativeHotel;
import com.hotel.models.CommissionBill;
import java.util.ArrayList;

/**
 * Handles all commission related operations.
 * When our hotel refers customers to alternative hotels,
 * those hotels owe us a commission fee per customer.
 * This service generates and manages commission bills.
 *
 * Commission Formula:
 * Total Bill = Total Customers Referred x Commission Per Customer
 *
 * Example:
 * Kunal's Hotel - 10 customers referred - Rs.1000 per customer
 * Total Bill = 10 x 1000 = Rs.10000
 *
 * Implements CommissionOperations interface.
 */
public class CommissionService implements CommissionOperations {

    // ArrayList to store all commission bills
    private ArrayList<CommissionBill> bills = new ArrayList<>();

    // counter to generate unique bill IDs
    private int billCounter = 1;

    /**
     * Generates a commission bill for a specific hotel.
     * Bill is calculated based on total customers referred
     * and commission per customer rate of that hotel.
     */
    @Override
    public CommissionBill generateBill(AlternativeHotel hotel)
            throws HotelNotFoundException {

        if (hotel == null) {
            throw new HotelNotFoundException(
                    "Hotel does not exist in the system."
            );
        }

        if (hotel.getCustomersReferred() == 0) {
            System.out.println("No customers have been referred to "
                    + hotel.getHotelName() + " yet.");
            System.out.println("Commission bill cannot be generated.");
            return null;
        }

        // check if bill already exists for this hotel
        // if yes, UPDATE it with latest customer count
        for (CommissionBill bill : bills) {
            if (bill.getHotelId().equals(hotel.getHotelId())) {

                // remove old bill and regenerate with updated count
                bills.remove(bill);

                String billId = bill.getBillId(); // keep same bill ID

                CommissionBill updatedBill = new CommissionBill(
                        billId,
                        hotel.getHotelId(),
                        hotel.getHotelName(),
                        hotel.getCustomersReferred(),
                        hotel.getCommissionPerCustomer()
                );

                bills.add(updatedBill);

                System.out.println("Commission bill updated successfully.");
                System.out.println("Bill ID             : " + billId);
                System.out.println("Hotel Name          : " + hotel.getHotelName());
                System.out.println("Total Customers     : " + hotel.getCustomersReferred());
                System.out.println("Commission/Customer : Rs." + hotel.getCommissionPerCustomer());
                System.out.println("Total Bill Amount   : Rs." + updatedBill.getTotalBillAmount());

                return updatedBill;
            }
        }

        // generate new bill
        String billId = "BILL-" + String.format("%03d", billCounter++);

        CommissionBill bill = new CommissionBill(
                billId,
                hotel.getHotelId(),
                hotel.getHotelName(),
                hotel.getCustomersReferred(),
                hotel.getCommissionPerCustomer()
        );

        bills.add(bill);

        System.out.println("Commission bill generated successfully.");
        System.out.println("Bill ID             : " + billId);
        System.out.println("Hotel Name          : " + hotel.getHotelName());
        System.out.println("Total Customers     : " + hotel.getCustomersReferred());
        System.out.println("Commission/Customer : Rs." + hotel.getCommissionPerCustomer());
        System.out.println("Total Bill Amount   : Rs." + bill.getTotalBillAmount());

        return bill;
    }

    /**
     * Returns commission bill of a specific hotel.
     * Throws exception if bill is not found.
     */
    @Override
    public CommissionBill getBill(String hotelId) throws HotelNotFoundException {

        for (CommissionBill bill : bills) {
            if (bill.getHotelId().equals(hotelId)) {
                return bill;
            }
        }

        throw new HotelNotFoundException(
                "No commission bill found for hotel ID " + hotelId + "."
        );
    }

    /**
     * Returns list of all commission bills.
     */
    @Override
    public ArrayList<CommissionBill> getAllBills() {
        return bills;
    }

    /**
     * Marks a commission bill as paid.
     * Used when hotel pays the commission amount.
     */
    @Override
    public void markBillAsPaid(String hotelId) throws HotelNotFoundException {

        CommissionBill bill = getBill(hotelId);

        // check if bill is already paid
        if (bill.getBillStatus().equals("PAID")) {
            System.out.println("Commission bill for hotel ID "
                    + hotelId + " is already paid.");
            return;
        }

        // mark bill as paid
        bill.setBillStatus("PAID");
        System.out.println("Commission bill for " + bill.getHotelName()
                + " has been marked as paid successfully.");
    }

    /**
     * Displays commission bill of a specific hotel.
     */
    @Override
    public void displayBill(String hotelId) throws HotelNotFoundException {

        CommissionBill bill = getBill(hotelId);
        System.out.println("------------------------------");
        System.out.println("Commission Bill");
        System.out.println("------------------------------");
        System.out.println(bill.toString());
        System.out.println("------------------------------");
    }

    /**
     * Displays all commission bills in the system.
     */
    @Override
    public void displayAllBills() {

        if (bills.isEmpty()) {
            System.out.println("No commission bills found in the system.");
            return;
        }

        System.out.println("------------------------------");
        System.out.println("All Commission Bills");
        System.out.println("------------------------------");

        for (CommissionBill bill : bills) {
            System.out.println(bill.toString());
            System.out.println("------------------------------");
        }
    }

    /**
     * Returns total number of commission bills.
     * Used in test cases.
     */
    public int getTotalBills() {
        return bills.size();
    }
}