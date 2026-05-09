package com.hotel;

import com.hotel.exceptions.HotelNotFoundException;
import com.hotel.models.AlternativeHotel;
import com.hotel.services.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for RecommendationService.
 * Covers preloaded hotel data, getRecommendedHotels,
 * getHotel, hasAvailableHotels, getTotalHotels,
 * and displayRecommendedHotels.
 */
public class RecommendationServiceTest {

    private RecommendationService recommendationService;

    @BeforeEach
    public void setUp() {
        recommendationService = new RecommendationService();
    }

    // ─────────────────────────────────────────────
    // Preloaded data tests
    // ─────────────────────────────────────────────

    @Test
    public void testPreloadedHotels_CountIsThree() {
        assertEquals(3, recommendationService.getTotalHotels());
    }

    @Test
    public void testPreloadedHotels_KunalsHotelExists() throws Exception {
        AlternativeHotel hotel = recommendationService.getHotel("KH001");
        assertNotNull(hotel);
        assertEquals("Kunal's Hotel", hotel.getHotelName());
    }

    @Test
    public void testPreloadedHotels_ArmansHotelExists() throws Exception {
        AlternativeHotel hotel = recommendationService.getHotel("AH001");
        assertNotNull(hotel);
        assertEquals("Arman's Hotel", hotel.getHotelName());
    }

    @Test
    public void testPreloadedHotels_PineViewHotelExists() throws Exception {
        AlternativeHotel hotel = recommendationService.getHotel("PH001");
        assertNotNull(hotel);
        assertEquals("Pine View Hotel", hotel.getHotelName());
    }

    @Test
    public void testPreloadedHotels_KunalsHotel_HasThreeRooms() throws Exception {
        AlternativeHotel hotel = recommendationService.getHotel("KH001");
        assertEquals(3, hotel.getAvailableRooms().size());
    }

    @Test
    public void testPreloadedHotels_ArmansHotel_HasTwoRooms() throws Exception {
        AlternativeHotel hotel = recommendationService.getHotel("AH001");
        assertEquals(2, hotel.getAvailableRooms().size());
    }

    @Test
    public void testPreloadedHotels_PineViewHotel_HasTwoRooms() throws Exception {
        AlternativeHotel hotel = recommendationService.getHotel("PH001");
        assertEquals(2, hotel.getAvailableRooms().size());
    }

    @Test
    public void testPreloadedHotels_KunalsHotel_CommissionIs1000() throws Exception {
        AlternativeHotel hotel = recommendationService.getHotel("KH001");
        assertEquals(1000.0, hotel.getCommissionPerCustomer());
    }

    @Test
    public void testPreloadedHotels_ArmansHotel_CommissionIs800() throws Exception {
        AlternativeHotel hotel = recommendationService.getHotel("AH001");
        assertEquals(800.0, hotel.getCommissionPerCustomer());
    }

    @Test
    public void testPreloadedHotels_PineViewHotel_CommissionIs500() throws Exception {
        AlternativeHotel hotel = recommendationService.getHotel("PH001");
        assertEquals(500.0, hotel.getCommissionPerCustomer());
    }

    @Test
    public void testPreloadedHotels_InitialCustomersReferredIsZero() throws Exception {
        assertEquals(0, recommendationService.getHotel("KH001").getCustomersReferred());
        assertEquals(0, recommendationService.getHotel("AH001").getCustomersReferred());
        assertEquals(0, recommendationService.getHotel("PH001").getCustomersReferred());
    }

    // ─────────────────────────────────────────────
    // getRecommendedHotels() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetRecommendedHotels_ReturnsNonNullList() {
        assertNotNull(recommendationService.getRecommendedHotels());
    }

    @Test
    public void testGetRecommendedHotels_ReturnsThreeHotels() {
        ArrayList<AlternativeHotel> hotels =
                recommendationService.getRecommendedHotels();
        assertEquals(3, hotels.size());
    }

    @Test
    public void testGetRecommendedHotels_ContainsKunalsHotel() {
        ArrayList<AlternativeHotel> hotels =
                recommendationService.getRecommendedHotels();
        boolean found = hotels.stream()
                .anyMatch(h -> h.getHotelId().equals("KH001"));
        assertTrue(found);
    }

    // ─────────────────────────────────────────────
    // getHotel() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetHotel_ValidId_ReturnsCorrectHotel() throws Exception {
        AlternativeHotel hotel = recommendationService.getHotel("KH001");
        assertEquals("KH001", hotel.getHotelId());
    }

    @Test
    public void testGetHotel_InvalidId_ThrowsHotelNotFoundException() {
        assertThrows(HotelNotFoundException.class, () ->
                recommendationService.getHotel("XX999"));
    }

    @Test
    public void testGetHotel_EmptyId_ThrowsHotelNotFoundException() {
        assertThrows(HotelNotFoundException.class, () ->
                recommendationService.getHotel(""));
    }

    @Test
    public void testGetHotel_CaseSensitive_ThrowsHotelNotFoundException() {
        // IDs are stored as uppercase; lowercase should not match
        assertThrows(HotelNotFoundException.class, () ->
                recommendationService.getHotel("kh001"));
    }

    // ─────────────────────────────────────────────
    // hasAvailableHotels() tests
    // ─────────────────────────────────────────────

    @Test
    public void testHasAvailableHotels_ReturnsTrueWhenHotelsLoaded() {
        assertTrue(recommendationService.hasAvailableHotels());
    }

    // ─────────────────────────────────────────────
    // getTotalHotels() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetTotalHotels_IsThree() {
        assertEquals(3, recommendationService.getTotalHotels());
    }

    // ─────────────────────────────────────────────
    // displayRecommendedHotels() — smoke test
    // ─────────────────────────────────────────────

    @Test
    public void testDisplayRecommendedHotels_DoesNotThrow() {
        assertDoesNotThrow(() ->
                recommendationService.displayRecommendedHotels());
    }
}
