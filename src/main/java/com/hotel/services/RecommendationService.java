package com.hotel.services;

import com.hotel.exceptions.HotelNotFoundException;
import com.hotel.interfaces.RecommendationOperations;
import com.hotel.models.AlternativeHotel;
import com.hotel.models.Room;
import java.util.ArrayList;

/**
 * Handles all hotel recommendation related operations.
 * When a room is not available in our hotel,
 * this service suggests alternative hotels to the customer.
 * Implements RecommendationOperations interface.
 */
public class RecommendationService implements RecommendationOperations {

    // ArrayList to store all alternative hotels
    private ArrayList<AlternativeHotel> hotels = new ArrayList<>();

    // Constructor - preloads some alternative hotels into the system
    public RecommendationService() {
        loadAlternativeHotels();
    }

    /**
     * Preloads alternative hotels into the system.
     * Each hotel has its own commission per customer rate.
     * In a real system this data would come from a database.
     */
    private void loadAlternativeHotels() {

        // creating Kunal's Hotel with commission Rs.1000 per customer
        AlternativeHotel kunalsHotel = new AlternativeHotel(
                "KH001",
                "Kunal's Hotel",
                "MG Road",
                "98XXXXXXXX",
                1000.0
        );
        kunalsHotel.addAvailableRoom(new Room(201, "Deluxe", 2500));
        kunalsHotel.addAvailableRoom(new Room(202, "Suite", 4000));
        kunalsHotel.addAvailableRoom(new Room(203, "Single", 1200));

        // creating Arman's Hotel with commission Rs.800 per customer
        AlternativeHotel armansHotel = new AlternativeHotel(
                "AH001",
                "Arman's Hotel",
                "Mall Road",
                "97XXXXXXXX",
                800.0
        );
        armansHotel.addAvailableRoom(new Room(101, "Double", 1800));
        armansHotel.addAvailableRoom(new Room(102, "Suite", 3500));

        // creating Pine View Hotel with commission Rs.500 per customer
        AlternativeHotel pineViewHotel = new AlternativeHotel(
                "PH001",
                "Pine View Hotel",
                "Lakkar Bazar",
                "96XXXXXXXX",
                500.0
        );
        pineViewHotel.addAvailableRoom(new Room(301, "Single", 1000));
        pineViewHotel.addAvailableRoom(new Room(302, "Double", 2000));

        // adding all hotels to the list
        hotels.add(kunalsHotel);
        hotels.add(armansHotel);
        hotels.add(pineViewHotel);
    }

    /**
     * Returns list of all recommended hotels.
     */
    @Override
    public ArrayList<AlternativeHotel> getRecommendedHotels() {
        return hotels;
    }

    /**
     * Returns a specific hotel by hotel ID.
     * Throws exception if hotel is not found.
     */
    @Override
    public AlternativeHotel getHotel(String hotelId) throws HotelNotFoundException {

        for (AlternativeHotel hotel : hotels) {
            if (hotel.getHotelId().equals(hotelId)) {
                return hotel;
            }
        }

        throw new HotelNotFoundException(
                "Hotel with ID " + hotelId + " does not exist in the system."
        );
    }

    /**
     * Displays all recommended hotels with their available rooms.
     * Customer can choose from this list.
     */
    @Override
    public void displayRecommendedHotels() {

        if (hotels.isEmpty()) {
            System.out.println("No alternative hotels available at the moment.");
            return;
        }

        System.out.println("------------------------------");
        System.out.println("Recommended Hotels");
        System.out.println("------------------------------");

        int hotelCount = 1;

        for (AlternativeHotel hotel : hotels) {

            System.out.println("[" + hotelCount + "] " + hotel.getHotelName());
            System.out.println("    Location         : " + hotel.getLocation());
            System.out.println("    Contact          : " + hotel.getContactNumber());
            System.out.println("    Available Rooms  :");

            // display available rooms of this hotel
            ArrayList<Room> rooms = hotel.getAvailableRooms();

            if (rooms.isEmpty()) {
                System.out.println("    No rooms available.");
            } else {
                for (Room room : rooms) {
                    System.out.println("    - Room " + room.getRoomNumber()
                            + " | " + room.getRoomType()
                            + " | Rs." + room.getPricePerNight()
                            + " per night");
                }
            }

            System.out.println("------------------------------");
            hotelCount++;
        }

        System.out.println("[0] No thank you");
        System.out.println("------------------------------");
    }

    /**
     * Checks if any alternative hotels are available.
     */
    @Override
    public boolean hasAvailableHotels() {
        return !hotels.isEmpty();
    }

    /**
     * Returns total number of alternative hotels.
     * Used in test cases.
     */
    public int getTotalHotels() {
        return hotels.size();
    }
}