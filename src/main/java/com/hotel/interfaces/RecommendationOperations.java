package com.hotel.interfaces;

import com.hotel.exceptions.HotelNotFoundException;
import com.hotel.models.AlternativeHotel;
import java.util.ArrayList;

/**
 * Defines all operations related to hotel recommendations.
 * RecommendationService must implement all these methods.
 */
public interface RecommendationOperations {

    // get list of all recommended hotels
    ArrayList<AlternativeHotel> getRecommendedHotels();

    // get a specific hotel by hotel ID
    AlternativeHotel getHotel(String hotelId)
            throws HotelNotFoundException;

    // display all recommended hotels with their available rooms
    void displayRecommendedHotels();

    // check if any hotels are available for recommendation
    boolean hasAvailableHotels();
}