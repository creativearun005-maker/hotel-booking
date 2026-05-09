package com.hotel.services;

import com.hotel.exceptions.InvalidInputException;
import com.hotel.exceptions.RoomAlreadyExistsException;
import com.hotel.exceptions.RoomNotFoundException;
import com.hotel.interfaces.RoomOperations;
import com.hotel.models.Room;
import java.util.ArrayList;

/**
 * Handles all room related operations.
 * Implements RoomOperations interface.
 */
public class RoomService implements RoomOperations {

    // ArrayList to store all rooms in the system
    private ArrayList<Room> rooms = new ArrayList<>();

    /**
     * Adds a new room to the system.
     * Validates all input before adding.
     */
    @Override
    public void addRoom(int roomNumber, String roomType, double pricePerNight)
            throws RoomAlreadyExistsException, InvalidInputException {

        // check if room number is valid
        if (roomNumber <= 0) {
            throw new InvalidInputException(
                    "Room number must be greater than zero."
            );
        }

        // check if price is valid
        if (pricePerNight <= 0) {
            throw new InvalidInputException(
                    "Price per night must be greater than zero."
            );
        }

        // check if room type is valid
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidInputException(
                    "Room type cannot be empty."
            );
        }

        // check if room already exists in the system
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                throw new RoomAlreadyExistsException(
                        "Room " + roomNumber + " already exists in the system."
                );
            }
        }

        // all validations passed, add room to the list
        rooms.add(new Room(roomNumber, roomType, pricePerNight));
        System.out.println("Room " + roomNumber + " added successfully.");
    }

    /**
     * Deletes a room from the system.
     */
    @Override
    public void deleteRoom(int roomNumber) throws RoomNotFoundException {

        // find the room first
        Room roomToDelete = getRoom(roomNumber);

        // remove it from the list
        rooms.remove(roomToDelete);
        System.out.println("Room " + roomNumber + " deleted successfully.");
    }

    /**
     * Returns a specific room by room number.
     * Throws exception if room is not found.
     */
    @Override
    public Room getRoom(int roomNumber) throws RoomNotFoundException {

        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }

        throw new RoomNotFoundException(
                "Room " + roomNumber + " does not exist in the system."
        );
    }

    /**
     * Returns list of all rooms in the system.
     */
    @Override
    public ArrayList<Room> getAllRooms() {
        return rooms;
    }

    /**
     * Returns list of all available rooms.
     */
    @Override
    public ArrayList<Room> getAvailableRooms() {

        ArrayList<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    /**
     * Checks if a specific room is available.
     */
    @Override
    public boolean isRoomAvailable(int roomNumber) throws RoomNotFoundException {

        Room room = getRoom(roomNumber);
        return room.isAvailable();
    }

    /**
     * Displays all rooms in the system.
     */
    @Override
    public void displayAllRooms() {

        if (rooms.isEmpty()) {
            System.out.println("No rooms found in the system.");
            return;
        }

        System.out.println("------------------------------");
        System.out.println("All Rooms");
        System.out.println("------------------------------");

        for (Room room : rooms) {
            System.out.println(room.toString());
            System.out.println("------------------------------");
        }
    }

    /**
     * Displays only available rooms in the system.
     */
    @Override
    public void displayAvailableRooms() {

        ArrayList<Room> availableRooms = getAvailableRooms();

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms are available at the moment.");
            return;
        }

        System.out.println("------------------------------");
        System.out.println("Available Rooms");
        System.out.println("------------------------------");

        for (Room room : availableRooms) {
            System.out.println(room.toString());
            System.out.println("------------------------------");
        }
    }

    /**
     * Returns total number of rooms in the system.
     * Used in test cases.
     */
    public int getTotalRooms() {
        return rooms.size();
    }
}