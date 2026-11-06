package com.easemytrip.utils;

import com.easemytrip.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

/**
 * TestDataGenerator provides programmatic creation and regeneration
 * of test data Excel workbooks mapped directly to EaseMyTrip test cases.
 */
public final class TestDataGenerator {

    private static final Logger LOGGER = LogManager.getLogger(TestDataGenerator.class);

    private TestDataGenerator() {
        // Prevent instantiation
    }

    /**
     * Generates or refreshes the default framework Excel test data file with all module sheets.
     */
    public static void generateDefaultTestDataFile() {
        File targetFile = new File(FrameworkConstants.TESTDATA_EXCEL_PATH);
        File parentDir = targetFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            font.setFontHeightInPoints((short) 10);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // 1. FlightSearch Sheet
            Sheet flightSheet = workbook.createSheet("FlightSearch");
            String[] flightHeaders = {"TestCaseId", "TestScenario", "SourceCity", "DestinationCity", "DepartureDaysOffset", "TravelClass", "ExpectedValidation"};
            createSheetWithData(flightSheet, headerStyle, flightHeaders, new Object[][]{
                    {"test_004", "TC004_ValidFlightSearch", "Pune", "Delhi", "7", "Economy", "Available"},
                    {"test_005", "TC005_SameSourceAndDestination", "Pune", "Pune", "7", "Economy", "IdenticalCitiesBlocked"},
                    {"test_006", "TC006_MissingSourceCity", "", "Delhi", "7", "Economy", "SourceRequired"},
                    {"test_007", "TC007_InvalidReturnDateRange", "Delhi", "Mumbai", "14", "Economy", "InvalidReturnDate"},
                    {"test_008", "TC008_PassengerCountTwoAdults", "Delhi", "Goa", "10", "Economy", "2 Adults"}
            });

            // 2. HotelSearch Sheet
            Sheet hotelSheet = workbook.createSheet("HotelSearch");
            String[] hotelHeaders = {"TestCaseId", "TestScenario", "DestinationCity", "CheckInDaysOffset", "CheckOutDaysOffset", "Rooms", "Adults", "ExpectedValidation"};
            createSheetWithData(hotelSheet, headerStyle, hotelHeaders, new Object[][]{
                    {"test_010", "TC010_ValidHotelSearch", "Mumbai", "5", "8", "1", "2", "Available"},
                    {"test_058", "TC058_DestinationSuggestion", "Goa", "10", "13", "1", "2", "SuggestionsDisplayed"},
                    {"test_059", "TC059_CheckOutBeforeCheckIn", "Delhi", "15", "12", "1", "2", "EarlierCheckOutDisabled"},
                    {"test_060", "TC060_RoomGuestSelection", "Bangalore", "7", "9", "2", "4", "SelectionUpdated"},
                    {"test_061", "TC061_EmptyDestination", "", "5", "7", "1", "2", "DestinationRequired"}
            });

            // 3. LoginValidation Sheet
            Sheet loginSheet = workbook.createSheet("LoginValidation");
            String[] loginHeaders = {"TestCaseId", "TestScenario", "InputCredential", "Password", "ValidationType"};
            createSheetWithData(loginSheet, headerStyle, loginHeaders, new Object[][]{
                    {"test_013", "TC013_InvalidPassword", "user@example.com", "wrong123", "InvalidCredentials"},
                    {"test_014", "TC014_UnregisteredEmail", "nouser@example.com", "Test@1234", "UnregisteredAccount"},
                    {"test_015", "TC015_EmptyFields", "", "", "MandatoryFieldsRequired"},
                    {"test_016", "TC016_InvalidEmailFormat", "user.example.com", "Test@1234", "InvalidEmailFormat"},
                    {"test_020", "TC020_IncorrectOTP", "9876543210", "000000", "InvalidOTP"}
            });

            // 4. TrainSearch Sheet
            Sheet trainSheet = workbook.createSheet("TrainSearch");
            String[] trainHeaders = {"TestCaseId", "TestScenario", "FromStation", "ToStation", "DepartureDaysOffset", "ClassType", "ExpectedValidation"};
            createSheetWithData(trainSheet, headerStyle, trainHeaders, new Object[][]{
                    {"test_072", "TC072_ValidTrainSearch", "Pune", "Delhi", "7", "3A", "Available"},
                    {"test_073", "TC073_IdenticalStations", "Pune", "Pune", "7", "SL", "SameStationBlocked"},
                    {"test_076", "TC076_InvalidPNR", "123", "", "", "", "InvalidPNRError"}
            });

            // 5. BusSearch Sheet
            Sheet busSheet = workbook.createSheet("BusSearch");
            String[] busHeaders = {"TestCaseId", "TestScenario", "FromCity", "ToCity", "DepartureDaysOffset", "BusType", "ExpectedValidation"};
            createSheetWithData(busSheet, headerStyle, busHeaders, new Object[][]{
                    {"test_078", "TC078_ValidBusSearch", "Pune", "Mumbai", "5", "AC", "Available"},
                    {"test_177", "TC177_SameCitiesBus", "Pune", "Pune", "5", "Non-AC", "SameCityBlocked"}
            });

            // 6. PaymentValidation Sheet
            Sheet paySheet = workbook.createSheet("PaymentValidation");
            String[] payHeaders = {"TestCaseId", "TestScenario", "CardNumber", "ExpiryDate", "CVV", "ExpectedValidation"};
            createSheetWithData(paySheet, headerStyle, payHeaders, new Object[][]{
                    {"test_087", "TC087_InvalidCardNumber", "1234 5678 9012 3456", "12/28", "123", "InvalidCardNumber"},
                    {"test_088", "TC088_ExpiredCardValidation", "4111 1111 1111 1111", "01/20", "123", "ExpiredCard"},
                    {"test_089", "TC089_CVVLengthValidation", "4111 1111 1111 1111", "12/28", "12", "CVVValidation"},
                    {"test_090", "TC090_InvalidUPI", "abc@invalid", "", "", "InvalidUPI"}
            });

            // 7. OffersData Sheet
            Sheet offersSheet = workbook.createSheet("OffersData");
            String[] offersHeaders = {"TestCaseId", "TestScenario", "CouponCode", "ExpectedResult"};
            createSheetWithData(offersSheet, headerStyle, offersHeaders, new Object[][]{
                    {"test_095", "TC095_ValidCoupon", "EMTFIRST", "DiscountApplied"},
                    {"test_096", "TC096_InvalidCoupon", "INVALID123", "InvalidCouponMessage"},
                    {"test_265", "TC265_ExpiredCoupon", "EXPIRED2020", "ExpiredCouponMessage"}
            });

            try (FileOutputStream fos = new FileOutputStream(targetFile)) {
                workbook.write(fos);
            }
            LOGGER.info("Generated comprehensive test data Excel successfully at: {}", targetFile.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.error("Failed to generate test data Excel: {}", e.getMessage(), e);
        }
    }

    private static void createSheetWithData(Sheet sheet, CellStyle headerStyle, String[] headers, Object[][] data) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell c = headerRow.createCell(i);
            c.setCellValue(headers[i]);
            c.setCellStyle(headerStyle);
        }
        for (int r = 0; r < data.length; r++) {
            Row row = sheet.createRow(r + 1);
            for (int c = 0; c < data[r].length; c++) {
                row.createCell(c).setCellValue(data[r][c].toString());
            }
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public static void main(String[] args) {
        generateDefaultTestDataFile();
    }
}
