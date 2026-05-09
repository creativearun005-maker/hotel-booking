package com.hotel.services;

import com.hotel.exceptions.HotelNotFoundException;
import com.hotel.exceptions.InvalidInputException;
import com.hotel.interfaces.ReferralOperations;
import com.hotel.models.AlternativeHotel;
import com.hotel.models.ReferralRequest;
import java.util.ArrayList;

/**
 * Handles all referral related operations.
 * When a customer is sent to an alternative hotel,
 * this service stores and manages that referral record.
 * Implements ReferralOperations interface.
 */
public class ReferralService implements ReferralOperations {

    // ArrayList to store all referral requests
    private ArrayList<ReferralRequest> referrals = new ArrayList<>();

    // counter to generate unique referral IDs
    private int referralCounter = 1;

    /**
     * Adds a new referral record when a customer
     * is sent to an alternative hotel.
     * Also increments the customer count for that hotel
     * which is used for commission calculation.
     */
    @Override
    public void addReferral(String customerName, AlternativeHotel hotel)
            throws InvalidInputException, HotelNotFoundException {

        // check if customer name is valid
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidInputException(
                    "Customer name cannot be empty."
            );
        }

        // check if hotel is valid
        if (hotel == null) {
            throw new HotelNotFoundException(
                    "Hotel does not exist in the system."
            );
        }

        // generate unique referral ID
        String referralId = "REF-" + String.format("%03d", referralCounter++);

        // create new referral request
        ReferralRequest referral = new ReferralRequest(
                referralId,
                customerName,
                hotel.getHotelId(),
                hotel.getHotelName()
        );

        // add referral to the list
        referrals.add(referral);

        // increase the referred customer count for this hotel
        // this count is used later for commission calculation
        hotel.incrementCustomersReferred();

        System.out.println("Referral recorded successfully.");
        System.out.println("Referral ID      : " + referralId);
        System.out.println("Your driver is waiting outside to drop you to "
                + hotel.getHotelName() + ". Have a safe journey!");
    }

    /**
     * Returns all referrals sent to a specific hotel.
     */
    @Override
    public ArrayList<ReferralRequest> getReferralsByHotel(String hotelId) {

        ArrayList<ReferralRequest> hotelReferrals = new ArrayList<>();

        for (ReferralRequest referral : referrals) {
            if (referral.getHotelId().equals(hotelId)) {
                hotelReferrals.add(referral);
            }
        }

        return hotelReferrals;
    }

    /**
     * Returns total number of referrals in the system.
     */
    @Override
    public int getTotalReferrals() {
        return referrals.size();
    }

    /**
     * Displays all referrals in the system.
     */
    @Override
    public void displayAllReferrals() {

        if (referrals.isEmpty()) {
            System.out.println("No referrals found in the system.");
            return;
        }

        System.out.println("------------------------------");
        System.out.println("All Referrals");
        System.out.println("------------------------------");

        for (ReferralRequest referral : referrals) {
            System.out.println(referral.toString());
            System.out.println("------------------------------");
        }
    }

    /**
     * Updates status of a referral to CONFIRMED.
     * Used when hotel confirms the customer has arrived.
     */
    public void confirmReferral(String referralId) {

        for (ReferralRequest referral : referrals) {
            if (referral.getReferralId().equals(referralId)) {
                referral.setStatus("CONFIRMED");
                System.out.println("Referral " + referralId
                        + " has been confirmed successfully.");
                return;
            }
        }

        System.out.println("Referral " + referralId + " not found in the system.");
    }
}