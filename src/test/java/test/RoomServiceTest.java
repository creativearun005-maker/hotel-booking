package com.hotel;

import com.hotel.exceptions.InvalidInputException;
import com.hotel.exceptions.RoomAlreadyExistsException;
import com.hotel.exceptions.RoomNotFoundException;
import com.hotel.models.Room;
import com.hotel.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for RoomService.
 * Covers addRoom, deleteRoom, getRoom,
 * getAllRooms, getAvailableRooms, isRoomAvailable,
 * getTotalRooms, and display methods.
 */
public class RoomServiceTest {

    private RoomService roomService;

    // Fresh RoomService before every test
    @BeforeEach
    public void setUp() {
        roomService = new RoomService();
    }

    // ─────────────────────────────────────────────
    // addRoom() tests
    // ─────────────────────────────────────────────

    @Test
    public void testAddRoom_Success() throws Exception {
        roomService.addRoom(101, "Deluxe", 2500);
        assertEquals(1, roomService.getTotalRooms());
    }

    @Test
    public void testAddRoom_RoomIsAvailableByDefault() throws Exception {
        roomService.addRoom(101, "Deluxe", 2500);
        Room room = roomService.getRoom(101);
        assertTrue(room.isAvailable());
    }

    @Test
    public void testAddRoom_CorrectDetailsStored() throws Exception {
        roomService.addRoom(102, "Suite", 5000);
        Room room = roomService.getRoom(102);
        assertEquals(102, room.getRoomNumber());
        assertEquals("Suite", room.getRoomType());
        assertEquals(5000, room.getPricePerNight());
    }

    @Test
    public void testAddRoom_DuplicateRoomNumber_ThrowsRoomAlreadyExistsException() throws Exception {
        roomService.addRoom(101, "Deluxe", 2500);
        assertThrows(RoomAlreadyExistsException.class, () ->
                roomService.addRoom(101, "Single", 1200));
    }

    @Test
    public void testAddRoom_InvalidRoomNumber_Zero_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                roomService.addRoom(0, "Deluxe", 2500));
    }

    @Test
    public void testAddRoom_InvalidRoomNumber_Negative_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                roomService.addRoom(-5, "Deluxe", 2500));
    }

    @Test
    public void testAddRoom_InvalidPrice_Zero_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                roomService.addRoom(101, "Deluxe", 0));
    }

    @Test
    public void testAddRoom_InvalidPrice_Negative_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                roomService.addRoom(101, "Deluxe", -100));
    }

    @Test
    public void testAddRoom_EmptyRoomType_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                roomService.addRoom(101, "", 2500));
    }

    @Test
    public void testAddRoom_NullRoomType_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                roomService.addRoom(101, null, 2500));
    }

    @Test
    public void testAddRoom_WhitespaceRoomType_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                roomService.addRoom(101, "   ", 2500));
    }

    @Test
    public void testAddRoom_MultipleRooms_CountIsCorrect() throws Exception {
        roomService.addRoom(101, "Single", 1000);
        roomService.addRoom(102, "Double", 1500);
        roomService.addRoom(103, "Suite", 3000);
        assertEquals(3, roomService.getTotalRooms());
    }

    // ─────────────────────────────────────────────
    // deleteRoom() tests
    // ─────────────────────────────────────────────

    @Test
    public void testDeleteRoom_Success() throws Exception {
        roomService.addRoom(101, "Deluxe", 2500);
        roomService.deleteRoom(101);
        assertEquals(0, roomService.getTotalRooms());
    }

    @Test
    public void testDeleteRoom_RoomNoLongerExists_AfterDeletion() throws Exception {
        roomService.addRoom(101, "Deluxe", 2500);
        roomService.deleteRoom(101);
        assertThrows(RoomNotFoundException.class, () ->
                roomService.getRoom(101));
    }

    @Test
    public void testDeleteRoom_NonExistentRoom_ThrowsRoomNotFoundException() {
        assertThrows(RoomNotFoundException.class, () ->
                roomService.deleteRoom(999));
    }

    @Test
    public void testDeleteRoom_OnlyDeletesTargetRoom() throws Exception {
        roomService.addRoom(101, "Single", 1000);
        roomService.addRoom(102, "Double", 1500);
        roomService.deleteRoom(101);
        assertEquals(1, roomService.getTotalRooms());
        assertNotNull(roomService.getRoom(102));
    }

    // ─────────────────────────────────────────────
    // getRoom() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetRoom_Success() throws Exception {
        roomService.addRoom(101, "Deluxe", 2500);
        Room room = roomService.getRoom(101);
        assertNotNull(room);
        assertEquals(101, room.getRoomNumber());
    }

    @Test
    public void testGetRoom_NonExistentRoom_ThrowsRoomNotFoundException() {
        assertThrows(RoomNotFoundException.class, () ->
                roomService.getRoom(999));
    }

    // ─────────────────────────────────────────────
    // getAllRooms() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetAllRooms_EmptyList_WhenNoRoomsAdded() {
        ArrayList<Room> rooms = roomService.getAllRooms();
        assertTrue(rooms.isEmpty());
    }

    @Test
    public void testGetAllRooms_ReturnsAllAddedRooms() throws Exception {
        roomService.addRoom(101, "Single", 1000);
        roomService.addRoom(102, "Double", 1500);
        assertEquals(2, roomService.getAllRooms().size());
    }

    // ─────────────────────────────────────────────
    // getAvailableRooms() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetAvailableRooms_AllRoomsAvailableByDefault() throws Exception {
        roomService.addRoom(101, "Single", 1000);
        roomService.addRoom(102, "Double", 1500);
        assertEquals(2, roomService.getAvailableRooms().size());
    }

    @Test
    public void testGetAvailableRooms_ExcludesUnavailableRooms() throws Exception {
        roomService.addRoom(101, "Single", 1000);
        roomService.addRoom(102, "Double", 1500);
        // mark room 101 as unavailable
        roomService.getRoom(101).setAvailable(false);
        ArrayList<Room> available = roomService.getAvailableRooms();
        assertEquals(1, available.size());
        assertEquals(102, available.get(0).getRoomNumber());
    }

    @Test
    public void testGetAvailableRooms_EmptyWhenAllUnavailable() throws Exception {
        roomService.addRoom(101, "Single", 1000);
        roomService.getRoom(101).setAvailable(false);
        assertTrue(roomService.getAvailableRooms().isEmpty());
    }

    // ─────────────────────────────────────────────
    // isRoomAvailable() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsRoomAvailable_ReturnsTrueByDefault() throws Exception {
        roomService.addRoom(101, "Deluxe", 2500);
        assertTrue(roomService.isRoomAvailable(101));
    }

    @Test
    public void testIsRoomAvailable_ReturnsFalseWhenMarkedUnavailable() throws Exception {
        roomService.addRoom(101, "Deluxe", 2500);
        roomService.getRoom(101).setAvailable(false);
        assertFalse(roomService.isRoomAvailable(101));
    }

    @Test
    public void testIsRoomAvailable_NonExistentRoom_ThrowsRoomNotFoundException() {
        assertThrows(RoomNotFoundException.class, () ->
                roomService.isRoomAvailable(999));
    }

    // ─────────────────────────────────────────────
    // getTotalRooms() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetTotalRooms_ZeroWhenEmpty() {
        assertEquals(0, roomService.getTotalRooms());
    }

    @Test
    public void testGetTotalRooms_CorrectCountAfterAddAndDelete() throws Exception {
        roomService.addRoom(101, "Single", 1000);
        roomService.addRoom(102, "Double", 1500);
        roomService.deleteRoom(101);
        assertEquals(1, roomService.getTotalRooms());
    }

    // ─────────────────────────────────────────────
    // displayAllRooms() / displayAvailableRooms() — smoke tests
    // ─────────────────────────────────────────────

    @Test
    public void testDisplayAllRooms_DoesNotThrow() throws Exception {
        roomService.addRoom(101, "Single", 1000);
        assertDoesNotThrow(() -> roomService.displayAllRooms());
    }

    @Test
    public void testDisplayAllRooms_EmptySystem_DoesNotThrow() {
        assertDoesNotThrow(() -> roomService.displayAllRooms());
    }

    @Test
    public void testDisplayAvailableRooms_DoesNotThrow() throws Exception {
        roomService.addRoom(101, "Deluxe", 2500);
        assertDoesNotThrow(() -> roomService.displayAvailableRooms());
    }
}
