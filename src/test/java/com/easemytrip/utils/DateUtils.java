package com.easemytrip.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DateUtils provides date formatting and date computation functions
 * commonly used in travel booking searches (departure dates, return dates, check-in, check-out).
 */
public final class DateUtils {

    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    public static final String FLIGHT_DISPLAY_FORMAT = "dd MMM yyyy";

    private DateUtils() {
        // Prevent instantiation
    }

    public static String getCurrentDate() {
        return getCurrentDate(DEFAULT_DATE_FORMAT);
    }

    public static String getCurrentDate(String pattern) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getFutureDate(int daysAhead) {
        return getFutureDate(daysAhead, DEFAULT_DATE_FORMAT);
    }

    public static String getFutureDate(int daysAhead, String pattern) {
        return LocalDate.now().plusDays(daysAhead).format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT));
    }

    public static LocalDate parseDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
}
