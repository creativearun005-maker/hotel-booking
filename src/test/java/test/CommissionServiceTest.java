package com.hotel;

import com.hotel.exceptions.HotelNotFoundException;
import com.hotel.models.AlternativeHotel;
import com.hotel.models.CommissionBill;
import com.hotel.services.CommissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for CommissionService.
 * Covers generateBill, getBill, getAllBills,
 * markBillAsPaid, getTotalBills, and display methods.
 */
public class CommissionServiceTest {

    private CommissionService commissionService;
    private AlternativeHotel kunalsHotel;
    private AlternativeHotel armansHotel;

    @BeforeEach
    public void setUp() {
        commissionService = new CommissionService();

        kunalsHotel = new AlternativeHotel(
                "KH001", "Kunal's Hotel", "MG Road",
                "98XXXXXXXX", 1000.0);

        armansHotel = new AlternativeHotel(
                "AH001", "Arman's Hotel", "Mall Road",
                "97XXXXXXXX", 800.0);

        // Simulate referrals: 3 customers sent to Kunal's Hotel
        kunalsHotel.incrementCustomersReferred();
        kunalsHotel.incrementCustomersReferred();
        kunalsHotel.incrementCustomersReferred();
    }

    // ─────────────────────────────────────────────
    // generateBill() — happy path
    // ─────────────────────────────────────────────

    @Test
    public void testGenerateBill_Success() throws Exception {
        CommissionBill bill = commissionService.generateBill(kunalsHotel);
        assertNotNull(bill);
    }

    @Test
    public void testGenerateBill_BillIdFormat() throws Exception {
        CommissionBill bill = commissionService.generateBill(kunalsHotel);
        assertTrue(bill.getBillId().matches("BILL-[0-9]{3}"),
                "Bill ID should match BILL-XXX format");
    }

    @Test
    public void testGenerateBill_FirstBillId_IsBILL001() throws Exception {
        CommissionBill bill = commissionService.generateBill(kunalsHotel);
        assertEquals("BILL-001", bill.getBillId());
    }

    @Test
    public void testGenerateBill_TotalAmountCalculatedCorrectly() throws Exception {
        // 3 customers x Rs.1000 = Rs.3000
        CommissionBill bill = commissionService.generateBill(kunalsHotel);
        assertEquals(3000.0, bill.getTotalBillAmount());
    }

    @Test
    public void testGenerateBill_DefaultStatusIsUnpaid() throws Exception {
        CommissionBill bill = commissionService.generateBill(kunalsHotel);
        assertEquals("UNPAID", bill.getBillStatus());
    }

    @Test
    public void testGenerateBill_CorrectDetailsStored() throws Exception {
        CommissionBill bill = commissionService.generateBill(kunalsHotel);
        assertEquals("KH001", bill.getHotelId());
        assertEquals("Kunal's Hotel", bill.getHotelName());
        assertEquals(3, bill.getTotalCustomersReferred());
        assertEquals(1000.0, bill.getCommissionPerCustomer());
    }

    @Test
    public void testGenerateBill_BillDateIsToday() throws Exception {
        CommissionBill bill = commissionService.generateBill(kunalsHotel);
        assertEquals(java.time.LocalDate.now(), bill.getBillGeneratedDate());
    }

    @Test
    public void testGenerateBill_TwoDifferentHotels_TwoBillsCreated()
            throws Exception {
        armansHotel.incrementCustomersReferred();
        commissionService.generateBill(kunalsHotel);
        commissionService.generateBill(armansHotel);
        assertEquals(2, commissionService.getTotalBills());
    }

    @Test
    public void testGenerateBill_SameHotelTwice_UpdatesExistingBill()
            throws Exception {
        commissionService.generateBill(kunalsHotel);
        // simulate one more referral
        kunalsHotel.incrementCustomersReferred();
        CommissionBill updated = commissionService.generateBill(kunalsHotel);
        // bill count should still be 1 (updated, not duplicated)
        assertEquals(1, commissionService.getTotalBills());
        // updated amount: 4 customers x 1000 = 4000
        assertEquals(4000.0, updated.getTotalBillAmount());
    }

    @Test
    public void testGenerateBill_SameHotelTwice_KeepsSameBillId()
            throws Exception {
        CommissionBill original = commissionService.generateBill(kunalsHotel);
        String originalId = original.getBillId();
        kunalsHotel.incrementCustomersReferred();
        CommissionBill updated = commissionService.generateBill(kunalsHotel);
        assertEquals(originalId, updated.getBillId());
    }

    // ─────────────────────────────────────────────
    // generateBill() — validation failures
    // ─────────────────────────────────────────────

    @Test
    public void testGenerateBill_NullHotel_ThrowsHotelNotFoundException() {
        assertThrows(HotelNotFoundException.class, () ->
                commissionService.generateBill(null));
    }

    @Test
    public void testGenerateBill_ZeroCustomersReferred_ReturnsNull()
            throws Exception {
        // armansHotel has 0 referrals by default
        CommissionBill bill = commissionService.generateBill(armansHotel);
        assertNull(bill);
    }

    @Test
    public void testGenerateBill_ZeroCustomersReferred_NoBillAdded()
            throws Exception {
        commissionService.generateBill(armansHotel);
        assertEquals(0, commissionService.getTotalBills());
    }

    // ─────────────────────────────────────────────
    // getBill() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetBill_Success() throws Exception {
        commissionService.generateBill(kunalsHotel);
        CommissionBill bill = commissionService.getBill("KH001");
        assertNotNull(bill);
        assertEquals("KH001", bill.getHotelId());
    }

    @Test
    public void testGetBill_NonExistentHotelId_ThrowsHotelNotFoundException() {
        assertThrows(HotelNotFoundException.class, () ->
                commissionService.getBill("XX999"));
    }

    @Test
    public void testGetBill_BeforeGenerating_ThrowsHotelNotFoundException() {
        assertThrows(HotelNotFoundException.class, () ->
                commissionService.getBill("KH001"));
    }

    // ─────────────────────────────────────────────
    // getAllBills() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetAllBills_EmptyWhenNoBillsGenerated() {
        ArrayList<CommissionBill> bills = commissionService.getAllBills();
        assertTrue(bills.isEmpty());
    }

    @Test
    public void testGetAllBills_ReturnsAllBills() throws Exception {
        armansHotel.incrementCustomersReferred();
        commissionService.generateBill(kunalsHotel);
        commissionService.generateBill(armansHotel);
        assertEquals(2, commissionService.getAllBills().size());
    }

    // ─────────────────────────────────────────────
    // markBillAsPaid() tests
    // ─────────────────────────────────────────────

    @Test
    public void testMarkBillAsPaid_Success() throws Exception {
        commissionService.generateBill(kunalsHotel);
        commissionService.markBillAsPaid("KH001");
        CommissionBill bill = commissionService.getBill("KH001");
        assertEquals("PAID", bill.getBillStatus());
    }

    @Test
    public void testMarkBillAsPaid_AlreadyPaid_DoesNotThrow() throws Exception {
        commissionService.generateBill(kunalsHotel);
        commissionService.markBillAsPaid("KH001");
        // Marking as paid again should not throw
        assertDoesNotThrow(() -> commissionService.markBillAsPaid("KH001"));
    }

    @Test
    public void testMarkBillAsPaid_NonExistentHotelId_ThrowsHotelNotFoundException() {
        assertThrows(HotelNotFoundException.class, () ->
                commissionService.markBillAsPaid("XX999"));
    }

    // ─────────────────────────────────────────────
    // getTotalBills() tests
    // ─────────────────────────────────────────────

    @Test
    public void testGetTotalBills_ZeroInitially() {
        assertEquals(0, commissionService.getTotalBills());
    }

    @Test
    public void testGetTotalBills_CorrectCountAfterGenerating() throws Exception {
        armansHotel.incrementCustomersReferred();
        commissionService.generateBill(kunalsHotel);
        commissionService.generateBill(armansHotel);
        assertEquals(2, commissionService.getTotalBills());
    }

    // ─────────────────────────────────────────────
    // Commission formula verification
    // ─────────────────────────────────────────────

    @Test
    public void testCommissionFormula_TenCustomers() throws Exception {
        // 10 referrals x 1000 commission = 10000
        for (int i = 0; i < 7; i++) {
            kunalsHotel.incrementCustomersReferred(); // already has 3
        }
        CommissionBill bill = commissionService.generateBill(kunalsHotel);
        assertEquals(10000.0, bill.getTotalBillAmount());
    }

    @Test
    public void testCommissionFormula_ArmansHotel_FiveCustomers() throws Exception {
        for (int i = 0; i < 5; i++) {
            armansHotel.incrementCustomersReferred();
        }
        CommissionBill bill = commissionService.generateBill(armansHotel);
        // 5 x 800 = 4000
        assertEquals(4000.0, bill.getTotalBillAmount());
    }

    // ─────────────────────────────────────────────
    // display methods — smoke tests
    // ─────────────────────────────────────────────

    @Test
    public void testDisplayBill_DoesNotThrow() throws Exception {
        commissionService.generateBill(kunalsHotel);
        assertDoesNotThrow(() -> commissionService.displayBill("KH001"));
    }

    @Test
    public void testDisplayBill_NonExistentHotelId_ThrowsHotelNotFoundException() {
        assertThrows(HotelNotFoundException.class, () ->
                commissionService.displayBill("XX999"));
    }

    @Test
    public void testDisplayAllBills_DoesNotThrow() throws Exception {
        commissionService.generateBill(kunalsHotel);
        assertDoesNotThrow(() -> commissionService.displayAllBills());
    }

    @Test
    public void testDisplayAllBills_EmptySystem_DoesNotThrow() {
        assertDoesNotThrow(() -> commissionService.displayAllBills());
    }
}
