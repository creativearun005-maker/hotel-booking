package com.hotel;

import com.hotel.utils.InputValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for InputValidator.
 * Covers all validation methods:
 * isValidInput, isPositiveNumber, isValidRoomNumber,
 * isValidRoomType, isValidDate, isValidDateRange,
 * isValidGuestName, isValidBookingId, isValidReferralId,
 * isValidPrice, isValidHotelId.
 */
public class InputValidatorTest {

    private InputValidator validator;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Helper: future date string N days from today
    private String futureDate(int days) {
        return LocalDate.now().plusDays(days).format(FMT);
    }

    // Helper: past date string N days before today
    private String pastDate(int days) {
        return LocalDate.now().minusDays(days).format(FMT);
    }

    @BeforeEach
    public void setUp() {
        validator = new InputValidator();
    }

    // ─────────────────────────────────────────────
    // isValidInput() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsValidInput_ValidString_ReturnsTrue() {
        assertTrue(validator.isValidInput("hello"));
    }

    @Test
    public void testIsValidInput_NullString_ReturnsFalse() {
        assertFalse(validator.isValidInput(null));
    }

    @Test
    public void testIsValidInput_EmptyString_ReturnsFalse() {
        assertFalse(validator.isValidInput(""));
    }

    @Test
    public void testIsValidInput_WhitespaceOnly_ReturnsFalse() {
        assertFalse(validator.isValidInput("   "));
    }

    @Test
    public void testIsValidInput_StringWithSpaces_ReturnsTrue() {
        assertTrue(validator.isValidInput("Arun Kumar"));
    }

    // ─────────────────────────────────────────────
    // isPositiveNumber() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsPositiveNumber_PositiveValue_ReturnsTrue() {
        assertTrue(validator.isPositiveNumber(100.0));
    }

    @Test
    public void testIsPositiveNumber_Zero_ReturnsFalse() {
        assertFalse(validator.isPositiveNumber(0));
    }

    @Test
    public void testIsPositiveNumber_NegativeValue_ReturnsFalse() {
        assertFalse(validator.isPositiveNumber(-50.0));
    }

    @Test
    public void testIsPositiveNumber_SmallPositive_ReturnsTrue() {
        assertTrue(validator.isPositiveNumber(0.01));
    }

    // ─────────────────────────────────────────────
    // isValidRoomNumber() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsValidRoomNumber_PositiveInt_ReturnsTrue() {
        assertTrue(validator.isValidRoomNumber(101));
    }

    @Test
    public void testIsValidRoomNumber_Zero_ReturnsFalse() {
        assertFalse(validator.isValidRoomNumber(0));
    }

    @Test
    public void testIsValidRoomNumber_Negative_ReturnsFalse() {
        assertFalse(validator.isValidRoomNumber(-5));
    }

    @Test
    public void testIsValidRoomNumber_One_ReturnsTrue() {
        assertTrue(validator.isValidRoomNumber(1));
    }

    // ─────────────────────────────────────────────
    // isValidRoomType() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsValidRoomType_Single_ReturnsTrue() {
        assertTrue(validator.isValidRoomType("Single"));
    }

    @Test
    public void testIsValidRoomType_Double_ReturnsTrue() {
        assertTrue(validator.isValidRoomType("Double"));
    }

    @Test
    public void testIsValidRoomType_Deluxe_ReturnsTrue() {
        assertTrue(validator.isValidRoomType("Deluxe"));
    }

    @Test
    public void testIsValidRoomType_Suite_ReturnsTrue() {
        assertTrue(validator.isValidRoomType("Suite"));
    }

    @Test
    public void testIsValidRoomType_InvalidType_ReturnsFalse() {
        assertFalse(validator.isValidRoomType("Presidential"));
    }

    @Test
    public void testIsValidRoomType_LowercaseSingle_ReturnsFalse() {
        // List contains "Single" (capitalised); "single" should not match
        assertFalse(validator.isValidRoomType("single"));
    }

    @Test
    public void testIsValidRoomType_NullType_ReturnsFalse() {
        assertFalse(validator.isValidRoomType(null));
    }

    @Test
    public void testIsValidRoomType_EmptyType_ReturnsFalse() {
        assertFalse(validator.isValidRoomType(""));
    }

    @Test
    public void testIsValidRoomType_WhitespaceOnly_ReturnsFalse() {
        assertFalse(validator.isValidRoomType("   "));
    }

    @Test
    public void testIsValidRoomType_TypeWithLeadingTrailingSpaces_ReturnsTrue() {
        // trim() is applied in the method before checking
        assertTrue(validator.isValidRoomType("  Deluxe  "));
    }

    // ─────────────────────────────────────────────
    // isValidDate() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsValidDate_FutureDate_ReturnsTrue() {
        assertTrue(validator.isValidDate(futureDate(5)));
    }

    @Test
    public void testIsValidDate_TodayDate_ReturnsTrue() {
        // today is not before today
        assertTrue(validator.isValidDate(LocalDate.now().format(FMT)));
    }

    @Test
    public void testIsValidDate_PastDate_ReturnsFalse() {
        assertFalse(validator.isValidDate(pastDate(1)));
    }

    @Test
    public void testIsValidDate_InvalidFormat_YYYYMMDD_ReturnsFalse() {
        assertFalse(validator.isValidDate("2026-05-10"));
    }

    @Test
    public void testIsValidDate_InvalidFormat_MMDDYYYY_ReturnsFalse() {
        assertFalse(validator.isValidDate("05/10/2026"));
    }

    @Test
    public void testIsValidDate_NullDate_ReturnsFalse() {
        assertFalse(validator.isValidDate(null));
    }

    @Test
    public void testIsValidDate_EmptyDate_ReturnsFalse() {
        assertFalse(validator.isValidDate(""));
    }

    @Test
    public void testIsValidDate_RandomString_ReturnsFalse() {
        assertFalse(validator.isValidDate("not-a-date"));
    }

    // ─────────────────────────────────────────────
    // isValidDateRange() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsValidDateRange_CheckOutAfterCheckIn_ReturnsTrue() {
        assertTrue(validator.isValidDateRange(futureDate(1), futureDate(5)));
    }

    @Test
    public void testIsValidDateRange_CheckOutBeforeCheckIn_ReturnsFalse() {
        assertFalse(validator.isValidDateRange(futureDate(5), futureDate(1)));
    }

    @Test
    public void testIsValidDateRange_SameDate_ReturnsFalse() {
        String date = futureDate(3);
        assertFalse(validator.isValidDateRange(date, date));
    }

    @Test
    public void testIsValidDateRange_InvalidDateFormat_ReturnsFalse() {
        assertFalse(validator.isValidDateRange("10/05/2026", "13/05/2026"));
    }

    @Test
    public void testIsValidDateRange_OneDayApart_ReturnsTrue() {
        assertTrue(validator.isValidDateRange(futureDate(1), futureDate(2)));
    }

    // ─────────────────────────────────────────────
    // isValidGuestName() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsValidGuestName_SingleName_ReturnsTrue() {
        assertTrue(validator.isValidGuestName("Arun"));
    }

    @Test
    public void testIsValidGuestName_FullName_ReturnsTrue() {
        assertTrue(validator.isValidGuestName("Arun Kumar"));
    }

    @Test
    public void testIsValidGuestName_LongName_ReturnsTrue() {
        assertTrue(validator.isValidGuestName("Arun Kumar Sharma Singh"));
    }

    @Test
    public void testIsValidGuestName_NameWithNumbers_ReturnsFalse() {
        assertFalse(validator.isValidGuestName("Arun123"));
    }

    @Test
    public void testIsValidGuestName_NameWithSpecialChars_ReturnsFalse() {
        assertFalse(validator.isValidGuestName("Arun@Kumar"));
    }

    @Test
    public void testIsValidGuestName_EmptyName_ReturnsFalse() {
        assertFalse(validator.isValidGuestName(""));
    }

    @Test
    public void testIsValidGuestName_NullName_ReturnsFalse() {
        assertFalse(validator.isValidGuestName(null));
    }

    @Test
    public void testIsValidGuestName_WhitespaceOnly_ReturnsFalse() {
        assertFalse(validator.isValidGuestName("   "));
    }

    @Test
    public void testIsValidGuestName_AllUppercase_ReturnsTrue() {
        assertTrue(validator.isValidGuestName("ARUN KUMAR"));
    }

    @Test
    public void testIsValidGuestName_AllLowercase_ReturnsTrue() {
        assertTrue(validator.isValidGuestName("arun kumar"));
    }

    // ─────────────────────────────────────────────
    // isValidBookingId() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsValidBookingId_BK001_ReturnsTrue() {
        assertTrue(validator.isValidBookingId("BK-001"));
    }

    @Test
    public void testIsValidBookingId_BK999_ReturnsTrue() {
        assertTrue(validator.isValidBookingId("BK-999"));
    }

    @Test
    public void testIsValidBookingId_WrongPrefix_ReturnsFalse() {
        assertFalse(validator.isValidBookingId("BKG-001"));
    }

    @Test
    public void testIsValidBookingId_TwoDigits_ReturnsFalse() {
        assertFalse(validator.isValidBookingId("BK-01"));
    }

    @Test
    public void testIsValidBookingId_FourDigits_ReturnsFalse() {
        assertFalse(validator.isValidBookingId("BK-0001"));
    }

    @Test
    public void testIsValidBookingId_Lowercase_ReturnsFalse() {
        assertFalse(validator.isValidBookingId("bk-001"));
    }

    @Test
    public void testIsValidBookingId_NullId_ReturnsFalse() {
        assertFalse(validator.isValidBookingId(null));
    }

    @Test
    public void testIsValidBookingId_EmptyId_ReturnsFalse() {
        assertFalse(validator.isValidBookingId(""));
    }

    // ─────────────────────────────────────────────
    // isValidReferralId() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsValidReferralId_REF001_ReturnsTrue() {
        assertTrue(validator.isValidReferralId("REF-001"));
    }

    @Test
    public void testIsValidReferralId_REF999_ReturnsTrue() {
        assertTrue(validator.isValidReferralId("REF-999"));
    }

    @Test
    public void testIsValidReferralId_WrongPrefix_ReturnsFalse() {
        assertFalse(validator.isValidReferralId("RF-001"));
    }

    @Test
    public void testIsValidReferralId_TwoDigits_ReturnsFalse() {
        assertFalse(validator.isValidReferralId("REF-01"));
    }

    @Test
    public void testIsValidReferralId_Lowercase_ReturnsFalse() {
        assertFalse(validator.isValidReferralId("ref-001"));
    }

    @Test
    public void testIsValidReferralId_NullId_ReturnsFalse() {
        assertFalse(validator.isValidReferralId(null));
    }

    @Test
    public void testIsValidReferralId_EmptyId_ReturnsFalse() {
        assertFalse(validator.isValidReferralId(""));
    }

    // ─────────────────────────────────────────────
    // isValidPrice() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsValidPrice_PositivePrice_ReturnsTrue() {
        assertTrue(validator.isValidPrice(2500.0));
    }

    @Test
    public void testIsValidPrice_Zero_ReturnsFalse() {
        assertFalse(validator.isValidPrice(0));
    }

    @Test
    public void testIsValidPrice_NegativePrice_ReturnsFalse() {
        assertFalse(validator.isValidPrice(-100.0));
    }

    @Test
    public void testIsValidPrice_SmallPositive_ReturnsTrue() {
        assertTrue(validator.isValidPrice(0.01));
    }

    // ─────────────────────────────────────────────
    // isValidHotelId() tests
    // ─────────────────────────────────────────────

    @Test
    public void testIsValidHotelId_KH001_ReturnsTrue() {
        assertTrue(validator.isValidHotelId("KH001"));
    }

    @Test
    public void testIsValidHotelId_AH001_ReturnsTrue() {
        assertTrue(validator.isValidHotelId("AH001"));
    }

    @Test
    public void testIsValidHotelId_PH001_ReturnsTrue() {
        assertTrue(validator.isValidHotelId("PH001"));
    }

    @Test
    public void testIsValidHotelId_LowercaseLetters_ReturnsFalse() {
        assertFalse(validator.isValidHotelId("kh001"));
    }

    @Test
    public void testIsValidHotelId_OneLetterPrefix_ReturnsFalse() {
        assertFalse(validator.isValidHotelId("K001"));
    }

    @Test
    public void testIsValidHotelId_ThreeLetterPrefix_ReturnsFalse() {
        assertFalse(validator.isValidHotelId("KH0001"));
    }

    @Test
    public void testIsValidHotelId_TwoDigits_ReturnsFalse() {
        assertFalse(validator.isValidHotelId("KH01"));
    }

    @Test
    public void testIsValidHotelId_FourDigits_ReturnsFalse() {
        assertFalse(validator.isValidHotelId("KH0001"));
    }

    @Test
    public void testIsValidHotelId_NullId_ReturnsFalse() {
        assertFalse(validator.isValidHotelId(null));
    }

    @Test
    public void testIsValidHotelId_EmptyId_ReturnsFalse() {
        assertFalse(validator.isValidHotelId(""));
    }

    @Test
    public void testIsValidHotelId_WithHyphen_ReturnsFalse() {
        // System IDs like KH001 have no hyphen
        assertFalse(validator.isValidHotelId("KH-001"));
    }
}
