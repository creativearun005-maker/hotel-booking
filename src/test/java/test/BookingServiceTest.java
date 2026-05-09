package com.hotel;

import com.hotel.exceptions.BookingNotFoundException;
import com.hotel.exceptions.InvalidDateException;
import com.hotel.exceptions.InvalidInputException;
import com.hotel.exceptions.RoomNotFoundException;
import com.hotel.exceptions.RoomNotAvailableException;
import com.hotel.models.Booking;
import com.hotel.services.BookingService;
import com.hotel.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for BookingService.
 * Covers bookRoom, cancelBooking, getBooking,
 * getAllBookings, getTotalBookings, getLastBookingId,
 * and display methods.
 */
public class BookingServiceTest {

    private RoomService roomService;
    private BookingService bookingService;

    // Date formatter matching the system format DD-MM-YYYY
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Helper: returns a future date string N days from today
    private String futureDate(int daysFromNow) {
        return LocalDate.now().plusDays(daysFromNow).format(FMT);
    }

    @BeforeEach
    public void setUp() throws Exception {
        roomService    = new RoomService();
        bookingService = new BookingService(roomService);
        // Add a room available for booking tests
        roomService.addRoom(101, "Deluxe", 2500);
    }

    // ─────────────────────────────────────────────
    // bookRoom() — happy path
    // ─────────────────────────────────────────────

    @Test
    public void testBookRoom_Success() throws Exception {
        bookingService.bookRoom(101, "Arun Kumar", futureDate(1), futureDate(3));
        assertEquals(1, bookingService.getTotalBookings());
    }

    @Test
    public void testBookRoom_BookingIdFormat() throws Exception {
        bookingService.bookRoom(101, "Arun Kumar", futureDate(1), futureDate(3));
        String lastId = bookingService.getLastBookingId();
        assertNotNull(lastId);
        assertTrue(lastId.matches("BK-[0-9]{3}"),
                "Booking ID should match BK-XXX format");
    }

    @Test
    public void testBookRoom_FirstBookingId_IsBK001() throws Exception {
        bookingService.bookRoom(101, "Arun Kumar", futureDate(1), futureDate(3));
        assertEquals("BK-001", bookingService.getLastBookingId());
    }

    @Test
    public void testBookRoom_BookingStatusIsConfirmedByDefault() throws Exception {
        bookingService.bookRoom(101, "Arun Kumar", futureDate(1), futureDate(3));
        Booking booking = bookingService.getBooking("BK-001");
        assertEquals("CONFIRMED", booking.getStatus());
    }

    @Test
    public void testBookRoom_CorrectDetailsStored() throws Exception {
        bookingService.bookRoom(101, "Riya Sharma",
                futureDate(2), futureDate(5));
        Booking booking = bookingService.getBooking("BK-001");
        assertEquals(101, booking.getRoomNumber());
        assertEquals("Riya Sharma", booking.getGuestName());
    }

    @Test
    public void testBookRoom_MultiSpaceGuestName_StoredCorrectly() throws Exception {
        bookingService.bookRoom(101, "Arun Kumar Sharma",
                futureDate(1), futureDate(4));
        Booking booking = bookingService.getBooking("BK-001");
        assertEquals("Arun Kumar Sharma", booking.getGuestName());
    }

    @Test
    public void testBookRoom_SameRoom_DifferentNonOverlappingDates_BothSucceed()
            throws Exception {
        // First booking: days 1-3
        bookingService.bookRoom(101, "Guest One", futureDate(1), futureDate(3));
        // Second booking: days 5-8 (no overlap)
        bookingService.bookRoom(101, "Guest Two", futureDate(5), futureDate(8));
        assertEquals(2, bookingService.getTotalBookings());
    }

    @Test
    public void testBookRoom_IncrementingBookingIds() throws Exception {
        roomService.addRoom(102, "Suite", 5000);
        bookingService.bookRoom(101, "Guest One", futureDate(1), futureDate(3));
        bookingService.bookRoom(102, "Guest Two", futureDate(1), futureDate(3));
        assertEquals("BK-001", bookingService.getAllBookings().get(0).getBookingId());
        assertEquals("BK-002", bookingService.getAllBookings().get(1).getBookingId());
    }

    // ─────────────────────────────────────────────
    // bookRoom() — validation failures
    // ─────────────────────────────────────────────

    @Test
    public void testBookRoom_EmptyGuestName_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                bookingService.bookRoom(101, "", futureDate(1), futureDate(3)));
    }

    @Test
    public void testBookRoom_NullGuestName_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                bookingService.bookRoom(101, null, futureDate(1), futureDate(3)));
    }

    @Test
    public void testBookRoom_WhitespaceGuestName_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                bookingService.bookRoom(101, "   ", futureDate(1), futureDate(3)));
    }

    @Test
    public void testBookRoom_NonExistentRoom_ThrowsRoomNotFoundException() {
        assertThrows(RoomNotFoundException.class, () ->
                bookingService.bookRoom(999, "Arun Kumar", futureDate(1), futureDate(3)));
    }

    @Test
    public void testBookRoom_InvalidDateFormat_ThrowsInvalidDateException() {
        assertThrows(InvalidDateException.class, () ->
                bookingService.bookRoom(101, "Arun Kumar", "2026/05/10", "2026/05/13"));
    }

    @Test
    public void testBookRoom_PastCheckInDate_ThrowsInvalidDateException() {
        String pastDate = LocalDate.now().minusDays(1).format(FMT);
        assertThrows(InvalidDateException.class, () ->
                bookingService.bookRoom(101, "Arun Kumar", pastDate, futureDate(3)));
    }

    @Test
    public void testBookRoom_CheckOutBeforeCheckIn_ThrowsInvalidDateException() {
        assertThrows(InvalidDateException.class, () ->
                bookingService.bookRoom(101, "Arun Kumar", futureDate(5), futureDate(2)));
    }

    @Test
    public void testBookRoom_CheckOutSameAsCheckIn_ThrowsInvalidDateException() {
        String date = futureDate(3);
        assertThrows(InvalidDateException.class, () ->
                bookingService.bookRoom(101, "Arun Kumar", date, date));
    }

    @Test
    public void testBookRoom_OverlappingDates_ThrowsRoomNotAvailableException()
            throws Exception {
        // Book room for days 1–5
        bookingService.bookRoom(101, "Guest One", futureDate(1), futureDate(5));
        // Try to book same room for overlapping days 3–7
        assertThrows(RoomNotAvailableException.class, () ->
                bookingService.bookRoom(101, "Guest Two", futureDate(3), futureDate(7)));
    }

    @Test
    public void testBookRoom_PartialOverlap_ThrowsRoomNotAvailableException()
            throws Exception {
        bookingService.bookRoom(101, "Guest One", futureDate(3), futureDate(7));
        // Starts before existing booking ends
        assertThrows(RoomNotAvailableException.class, () ->
                bookingService.bookRoom(101, "Guest Two", futureDate(5), futureDate(9)));
    }

    // ─────────────────────────────────────────────
    // cancelBooking() tests
    // ─────────────────────────────────────────────

    @Test
    public void testCancelBooking_Success() throws Exception {
        bookingService.bookRoom(101, "Arun Kumar", futureDate(1), futureDate(3));
        bookingService.cancelBooking("BK-001");
        Booking booking = bookingService.getBooking("BK-001");
        assertEquals("CANCELLED", booking.getStatus());
    }

    @Test
    public void testCancelBooking_AlreadyCancelled_DoesNotThrow() throws Exception {
        bookingService.bookRoom(101, "Arun Kumar", futureDate(1), futureDate(3));
        bookingService.cancelBooking("BK-001");
        // Cancelling again should not throw
        assertDoesNotThrow(() -> bookingService.cancelBooking("BK-001"));
    }

    @Test
    public void testCancelBooking_NonExistentBookingId_ThrowsBookingNotFoundException() {
        assertThrows(BookingNotFoundException.class, () ->
                bookingService.cancelBooking("BK-999"));
    }

    @Test
    public void testCancelBooking_AfterCancellation_RoomCanBeBookedAgain()
            throws Exception {
        bookingService.bookRoom(101, "Guest One", futureDate(1), futureDate(5));
        bookingService.cancelBooking("BK-001");
        // Should succeed because cancelled booking doesn't block dates
        assertDoesNotThrow(() ->
                bookingService.bookRoom(101, "Guest Two", futureDate(1), futureDate(5)));
    }

    // ─────────────────────────────────────────────
    // getBooking() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetBooking_Success() throws Exception {
        bookingService.bookRoom(101, "Arun Kumar", futureDate(1), futureDate(3));
        Booking booking = bookingService.getBooking("BK-001");
        assertNotNull(booking);
        assertEquals("BK-001", booking.getBookingId());
    }

    @Test
    public void testGetBooking_NonExistentId_ThrowsBookingNotFoundException() {
        assertThrows(BookingNotFoundException.class, () ->
                bookingService.getBooking("BK-999"));
    }

    // ─────────────────────────────────────────────
    // getAllBookings() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetAllBookings_EmptyWhenNoBookings() {
        ArrayList<Booking> bookings = bookingService.getAllBookings();
        assertTrue(bookings.isEmpty());
    }

    @Test
    public void testGetAllBookings_ReturnsAllBookings() throws Exception {
        roomService.addRoom(102, "Suite", 5000);
        bookingService.bookRoom(101, "Guest One", futureDate(1), futureDate(3));
        bookingService.bookRoom(102, "Guest Two", futureDate(1), futureDate(3));
        assertEquals(2, bookingService.getAllBookings().size());
    }

    // ─────────────────────────────────────────────
    // getTotalBookings() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetTotalBookings_ZeroInitially() {
        assertEquals(0, bookingService.getTotalBookings());
    }

    @Test
    public void testGetTotalBookings_IncludesCancelledBookings() throws Exception {
        bookingService.bookRoom(101, "Arun Kumar", futureDate(1), futureDate(3));
        bookingService.cancelBooking("BK-001");
        // Cancelled bookings still exist in the list
        assertEquals(1, bookingService.getTotalBookings());
    }

    // ─────────────────────────────────────────────
    // getLastBookingId() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetLastBookingId_NullWhenNoBookings() {
        assertNull(bookingService.getLastBookingId());
    }

    @Test
    public void testGetLastBookingId_ReturnsLastCreated() throws Exception {
        roomService.addRoom(102, "Suite", 5000);
        bookingService.bookRoom(101, "Guest One", futureDate(1), futureDate(3));
        bookingService.bookRoom(102, "Guest Two", futureDate(1), futureDate(3));
        assertEquals("BK-002", bookingService.getLastBookingId());
    }

    // ─────────────────────────────────────────────
    // display methods — smoke tests
    // ─────────────────────────────────────────────

    @Test
    public void testDisplayAllBookings_DoesNotThrow() throws Exception {
        bookingService.bookRoom(101, "Arun Kumar", futureDate(1), futureDate(3));
        assertDoesNotThrow(() -> bookingService.displayAllBookings());
    }

    @Test
    public void testDisplayAllBookings_EmptySystem_DoesNotThrow() {
        assertDoesNotThrow(() -> bookingService.displayAllBookings());
    }

    @Test
    public void testDisplayBooking_DoesNotThrow() throws Exception {
        bookingService.bookRoom(101, "Arun Kumar", futureDate(1), futureDate(3));
        assertDoesNotThrow(() -> bookingService.displayBooking("BK-001"));
    }

    @Test
    public void testDisplayBooking_NonExistentId_ThrowsBookingNotFoundException() {
        assertThrows(BookingNotFoundException.class, () ->
                bookingService.displayBooking("BK-999"));
    }
}
