package com.example.medibloomappv2.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DateUtils {

    public static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter FORMAT_DISPLAY = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    public static String today() {
        return LocalDate.now().format(FORMAT_DATE);
    }

    public static String daysAgo(int days) {
        return LocalDate.now().minusDays(days).format(FORMAT_DATE);
    }

    public static String daysFromNow(int days) {
        return LocalDate.now().plusDays(days).format(FORMAT_DATE);
    }

    public static String weekStartDate() {
        return LocalDate.now().minusDays(6).format(FORMAT_DATE);
    }

    public static String monthStartDate() {
        LocalDate now = LocalDate.now();
        return LocalDate.of(now.getYear(), now.getMonth(), 1).format(FORMAT_DATE);
    }

    public static String toDisplayDate(String dateKey) {
        try {
            return LocalDate.parse(dateKey, FORMAT_DATE).format(FORMAT_DISPLAY);
        } catch (Exception e) {
            return dateKey;
        }
    }

    public static String formatTime(int hour, int minute) {
        String amPm = hour >= 12 ? "PM" : "AM";
        int displayHour = hour % 12;
        if (displayHour == 0) displayHour = 12;
        return String.format("%02d:%02d %s", displayHour, minute, amPm);
    }

    public static long getScheduledTimestampForToday(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static String getGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 12) return "Good Morning 🌸";
        if (hour < 17) return "Good Afternoon ☀️";
        return "Good Evening 🌙";
    }
}

