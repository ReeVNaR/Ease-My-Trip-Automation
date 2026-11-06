package com.easemytrip.utils;

import com.easemytrip.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ExcelUtils provides utility methods to read data from Excel workbooks (.xlsx)
 * using Apache POI, designed for TestNG @DataProvider integration.
 */
public final class ExcelUtils {

    private static final Logger LOGGER = LogManager.getLogger(ExcelUtils.class);

    private ExcelUtils() {
        // Prevent instantiation
    }

    /**
     * Reads all test data rows from the default framework Excel file for a specified sheet.
     * The first row is treated as the column header.
     *
     * @param sheetName sheet name to read
     * @return 2D Object array suitable for TestNG @DataProvider
     */
    public static Object[][] getTestData(String sheetName) {
        return getTestData(FrameworkConstants.TESTDATA_EXCEL_PATH, sheetName);
    }

    /**
     * Reads all test data rows from a specified Excel file and sheet.
     *
     * @param filePath  path to .xlsx file
     * @param sheetName sheet name to read
     * @return 2D Object array suitable for TestNG @DataProvider
     */
    public static Object[][] getTestData(String filePath, String sheetName) {
        LOGGER.info("Reading test data from file: '{}', sheet: '{}'", filePath, sheetName);
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                LOGGER.error("Sheet '{}' not found in file '{}'", sheetName, filePath);
                throw new IllegalArgumentException("Sheet '" + sheetName + "' does not exist in: " + filePath);
            }

            int rowCount = sheet.getLastRowNum();
            if (rowCount < 1) {
                LOGGER.warn("Sheet '{}' has no data rows (rowCount={})", sheetName, rowCount);
                return new Object[0][0];
            }

            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();

            Object[][] data = new Object[rowCount][colCount];
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    if (row == null) {
                        data[i - 1][j] = "";
                    } else {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        data[i - 1][j] = formatter.formatCellValue(cell).trim();
                    }
                }
            }

            LOGGER.info("Successfully loaded {} data rows and {} columns from sheet '{}'", rowCount, colCount, sheetName);
            return data;
        } catch (IOException e) {
            LOGGER.error("Failed to read test data from Excel file: {}", filePath, e);
            throw new RuntimeException("Could not read test data from: " + filePath, e);
        }
    }

    /**
     * Reads test data as a List of Maps, where each Map key is the column header and value is the cell text.
     *
     * @param sheetName sheet name in the default test data workbook
     * @return List of Map<String, String> representing row records
     */
    public static List<Map<String, String>> getTestDataAsMapList(String sheetName) {
        return getTestDataAsMapList(FrameworkConstants.TESTDATA_EXCEL_PATH, sheetName);
    }

    /**
     * Reads test data as a List of Maps from a specified Excel file.
     *
     * @param filePath  path to Excel workbook
     * @param sheetName sheet name
     * @return List of Map<String, String> representing row records
     */
    public static List<Map<String, String>> getTestDataAsMapList(String filePath, String sheetName) {
        List<Map<String, String>> list = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' does not exist in: " + filePath);
            }

            DataFormatter formatter = new DataFormatter();
            Row headerRow = sheet.getRow(0);
            int colCount = headerRow.getLastCellNum();

            List<String> headers = new ArrayList<>();
            for (int c = 0; c < colCount; c++) {
                Cell cell = headerRow.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                headers.add(formatter.formatCellValue(cell).trim());
            }

            int rowCount = sheet.getLastRowNum();
            for (int r = 1; r <= rowCount; r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                Map<String, String> rowMap = new HashMap<>();
                for (int c = 0; c < colCount; c++) {
                    Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String header = headers.get(c);
                    String value = formatter.formatCellValue(cell).trim();
                    rowMap.put(header, value);
                }
                list.add(rowMap);
            }

            return list;
        } catch (IOException e) {
            LOGGER.error("Failed to read data as map from: {}", filePath, e);
            throw new RuntimeException("Could not read Excel sheet as map list: " + sheetName, e);
        }
    }
}
