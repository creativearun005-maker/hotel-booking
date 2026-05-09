package com.hotel.interfaces;

import com.hotel.exceptions.HotelNotFoundException;
import com.hotel.exceptions.InvalidInputException;
import com.hotel.models.AlternativeHotel;
import com.hotel.models.ReferralRequest;
import java.util.ArrayList;

/**
 * Defines all operations related to customer referrals.
 * ReferralService must implement all these methods.
 */
public interface ReferralOperations {

    // add a new referral when customer is sent to another hotel
    void addReferral(String customerName, AlternativeHotel hotel)
            throws InvalidInputException, HotelNotFoundException;

    // get all referrals sent to a specific hotel
    ArrayList<ReferralRequest> getReferralsByHotel(String hotelId);

    // get total number of referrals
    int getTotalReferrals();

    // display all referrals
    void displayAllReferrals();
}