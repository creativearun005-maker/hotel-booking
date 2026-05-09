package com.hotel.interfaces;

import com.hotel.exceptions.InvalidInputException;
import com.hotel.exceptions.RoomAlreadyExistsException;
import com.hotel.exceptions.RoomNotFoundException;
import com.hotel.models.Room;
import java.util.ArrayList;

/**
 * Defines all operations that can be performed on rooms.
 * RoomService must implement all these methods.
 */
public interface RoomOperations {

    // add a new room to the system
    void addRoom(int roomNumber, String roomType, double pricePerNight)
            throws RoomAlreadyExistsException, InvalidInputException;

    // delete a room from the system
    void deleteRoom(int roomNumber)
            throws RoomNotFoundException;

    // get a specific room by room number
    Room getRoom(int roomNumber)
            throws RoomNotFoundException;

    // get list of all rooms
    ArrayList<Room> getAllRooms();

    // get list of all available rooms
    ArrayList<Room> getAvailableRooms();

    // check if a specific room is available
    boolean isRoomAvailable(int roomNumber)
            throws RoomNotFoundException;

    // display all rooms
    void displayAllRooms();

    // display only available rooms
    void displayAvailableRooms();
}