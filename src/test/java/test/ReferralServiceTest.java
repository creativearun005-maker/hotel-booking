package com.hotel;

import com.hotel.exceptions.HotelNotFoundException;
import com.hotel.exceptions.InvalidInputException;
import com.hotel.models.AlternativeHotel;
import com.hotel.models.ReferralRequest;
import com.hotel.services.ReferralService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for ReferralService.
 * Covers addReferral, getReferralsByHotel,
 * getTotalReferrals, confirmReferral,
 * and displayAllReferrals.
 */
public class ReferralServiceTest {

    private ReferralService referralService;
    private AlternativeHotel kunalsHotel;
    private AlternativeHotel armansHotel;

    @BeforeEach
    public void setUp() {
        referralService = new ReferralService();

        kunalsHotel = new AlternativeHotel(
                "KH001", "Kunal's Hotel", "MG Road",
                "98XXXXXXXX", 1000.0);

        armansHotel = new AlternativeHotel(
                "AH001", "Arman's Hotel", "Mall Road",
                "97XXXXXXXX", 800.0);
    }

    // ─────────────────────────────────────────────
    // addReferral() — happy path
    // ─────────────────────────────────────────────

    @Test
    public void testAddReferral_Success() throws Exception {
        referralService.addReferral("Arun Kumar", kunalsHotel);
        assertEquals(1, referralService.getTotalReferrals());
    }

    @Test
    public void testAddReferral_ReferralIdFormat() throws Exception {
        referralService.addReferral("Arun Kumar", kunalsHotel);
        ArrayList<ReferralRequest> referrals =
                referralService.getReferralsByHotel("KH001");
        String referralId = referrals.get(0).getReferralId();
        assertTrue(referralId.matches("REF-[0-9]{3}"),
                "Referral ID should match REF-XXX format");
    }

    @Test
    public void testAddReferral_FirstReferralId_IsREF001() throws Exception {
        referralService.addReferral("Arun Kumar", kunalsHotel);
        ArrayList<ReferralRequest> referrals =
                referralService.getReferralsByHotel("KH001");
        assertEquals("REF-001", referrals.get(0).getReferralId());
    }

    @Test
    public void testAddReferral_DefaultStatusIsPending() throws Exception {
        referralService.addReferral("Arun Kumar", kunalsHotel);
        ArrayList<ReferralRequest> referrals =
                referralService.getReferralsByHotel("KH001");
        assertEquals("PENDING", referrals.get(0).getStatus());
    }

    @Test
    public void testAddReferral_CorrectDetailsStored() throws Exception {
        referralService.addReferral("Riya Sharma", kunalsHotel);
        ArrayList<ReferralRequest> referrals =
                referralService.getReferralsByHotel("KH001");
        ReferralRequest ref = referrals.get(0);
        assertEquals("Riya Sharma", ref.getCustomerName());
        assertEquals("KH001", ref.getHotelId());
        assertEquals("Kunal's Hotel", ref.getHotelName());
    }

    @Test
    public void testAddReferral_IncrementsHotelCustomerCount() throws Exception {
        assertEquals(0, kunalsHotel.getCustomersReferred());
        referralService.addReferral("Arun Kumar", kunalsHotel);
        assertEquals(1, kunalsHotel.getCustomersReferred());
    }

    @Test
    public void testAddReferral_MultipleReferrals_CountIncreasesCorrectly()
            throws Exception {
        referralService.addReferral("Guest One", kunalsHotel);
        referralService.addReferral("Guest Two", kunalsHotel);
        referralService.addReferral("Guest Three", kunalsHotel);
        assertEquals(3, kunalsHotel.getCustomersReferred());
        assertEquals(3, referralService.getTotalReferrals());
    }

    @Test
    public void testAddReferral_ReferralToTwoHotels_BothCountsUpdated()
            throws Exception {
        referralService.addReferral("Guest One", kunalsHotel);
        referralService.addReferral("Guest Two", armansHotel);
        assertEquals(1, kunalsHotel.getCustomersReferred());
        assertEquals(1, armansHotel.getCustomersReferred());
        assertEquals(2, referralService.getTotalReferrals());
    }

    @Test
    public void testAddReferral_IncrementingReferralIds() throws Exception {
        referralService.addReferral("Guest One", kunalsHotel);
        referralService.addReferral("Guest Two", armansHotel);
        ArrayList<ReferralRequest> kh = referralService.getReferralsByHotel("KH001");
        ArrayList<ReferralRequest> ah = referralService.getReferralsByHotel("AH001");
        assertEquals("REF-001", kh.get(0).getReferralId());
        assertEquals("REF-002", ah.get(0).getReferralId());
    }

    // ─────────────────────────────────────────────
    // addReferral() — validation failures
    // ─────────────────────────────────────────────

    @Test
    public void testAddReferral_EmptyCustomerName_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                referralService.addReferral("", kunalsHotel));
    }

    @Test
    public void testAddReferral_NullCustomerName_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                referralService.addReferral(null, kunalsHotel));
    }

    @Test
    public void testAddReferral_WhitespaceCustomerName_ThrowsInvalidInputException() {
        assertThrows(InvalidInputException.class, () ->
                referralService.addReferral("   ", kunalsHotel));
    }

    @Test
    public void testAddReferral_NullHotel_ThrowsHotelNotFoundException() {
        assertThrows(HotelNotFoundException.class, () ->
                referralService.addReferral("Arun Kumar", null));
    }

    // ─────────────────────────────────────────────
    // getReferralsByHotel() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetReferralsByHotel_ReturnsCorrectReferrals() throws Exception {
        referralService.addReferral("Guest One", kunalsHotel);
        referralService.addReferral("Guest Two", kunalsHotel);
        referralService.addReferral("Guest Three", armansHotel);
        ArrayList<ReferralRequest> kh =
                referralService.getReferralsByHotel("KH001");
        assertEquals(2, kh.size());
    }

    @Test
    public void testGetReferralsByHotel_EmptyListForHotelWithNoReferrals() {
        ArrayList<ReferralRequest> referrals =
                referralService.getReferralsByHotel("KH001");
        assertTrue(referrals.isEmpty());
    }

    @Test
    public void testGetReferralsByHotel_NonExistentHotelId_ReturnsEmptyList() {
        ArrayList<ReferralRequest> referrals =
                referralService.getReferralsByHotel("XX999");
        assertTrue(referrals.isEmpty());
    }

    // ─────────────────────────────────────────────
    // getTotalReferrals() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetTotalReferrals_ZeroInitially() {
        assertEquals(0, referralService.getTotalReferrals());
    }

    @Test
    public void testGetTotalReferrals_CorrectAfterMultipleReferrals()
            throws Exception {
        referralService.addReferral("Guest One", kunalsHotel);
        referralService.addReferral("Guest Two", armansHotel);
        assertEquals(2, referralService.getTotalReferrals());
    }

    // ─────────────────────────────────────────────
    // confirmReferral() tests
    // ─────────────────────────────────────────────

    @Test
    public void testConfirmReferral_StatusChangesToConfirmed() throws Exception {
        referralService.addReferral("Arun Kumar", kunalsHotel);
        referralService.confirmReferral("REF-001");
        ArrayList<ReferralRequest> referrals =
                referralService.getReferralsByHotel("KH001");
        assertEquals("CONFIRMED", referrals.get(0).getStatus());
    }

    @Test
    public void testConfirmReferral_NonExistentId_DoesNotThrow() {
        // confirmReferral prints a message but does not throw
        assertDoesNotThrow(() ->
                referralService.confirmReferral("REF-999"));
    }

    // ─────────────────────────────────────────────
    // displayAllReferrals() — smoke tests
    // ─────────────────────────────────────────────

    @Test
    public void testDisplayAllReferrals_DoesNotThrow() throws Exception {
        referralService.addReferral("Arun Kumar", kunalsHotel);
        assertDoesNotThrow(() -> referralService.displayAllReferrals());
    }

    @Test
    public void testDisplayAllReferrals_EmptySystem_DoesNotThrow() {
        assertDoesNotThrow(() -> referralService.displayAllReferrals());
    }
}
