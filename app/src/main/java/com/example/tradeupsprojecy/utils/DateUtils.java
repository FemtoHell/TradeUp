package com.example.tradeupsprojecy.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private static final SimpleDateFormat MESSAGE_TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat MESSAGE_DATE_FORMAT = new SimpleDateFormat("dd/MM", Locale.getDefault());

    // Get time ago string (e.g., "2 phút trước", "1 giờ trước")
    public static String getTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) return "Không rõ";

        LocalDateTime now = LocalDateTime.now();

        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        long hours = ChronoUnit.HOURS.between(dateTime, now);
        long days = ChronoUnit.DAYS.between(dateTime, now);

        if (minutes < 1) {
            return "Vừa xong";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days < 7) {
            return days + " ngày trước";
        } else if (days < 30) {
            long weeks = days / 7;
            return weeks + " tuần trước";
        } else if (days < 365) {
            long months = days / 30;
            return months + " tháng trước";
        } else {
            long years = days / 365;
            return years + " năm trước";
        }
    }

    // Format time for messages (today: HH:mm, yesterday: Hôm qua, older: dd/MM)
    public static String formatMessageTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";

        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = now.toLocalDate().atStartOfDay();
        LocalDateTime yesterday = today.minusDays(1);

        if (dateTime.isAfter(today)) {
            // Today - show time only
            return TIME_FORMAT.format(date);
        } else if (dateTime.isAfter(yesterday)) {
            // Yesterday
            return "Hôm qua";
        } else {
            // Older - show date
            return MESSAGE_DATE_FORMAT.format(date);
        }
    }

    // Format time for conversation list
    public static String formatConversationTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";

        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = now.toLocalDate().atStartOfDay();
        LocalDateTime yesterday = today.minusDays(1);
        LocalDateTime thisWeek = today.minusDays(6);

        if (dateTime.isAfter(today)) {
            // Today - show time only
            return TIME_FORMAT.format(date);
        } else if (dateTime.isAfter(yesterday)) {
            // Yesterday
            return "Hôm qua";
        } else if (dateTime.isAfter(thisWeek)) {
            // This week - show day name
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            return dayFormat.format(date);
        } else {
            // Older - show date
            return MESSAGE_DATE_FORMAT.format(date);
        }
    }

    // Format full date time
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";

        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        return DATETIME_FORMAT.format(date);
    }

    // Format date only
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "";

        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        return DATE_FORMAT.format(date);
    }

    // Format time only
    public static String formatTime(LocalDateTime dateTime) {
        if (dateTime == null) return "";

        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        return TIME_FORMAT.format(date);
    }

    // Check if date is today
    public static boolean isToday(LocalDateTime dateTime) {
        if (dateTime == null) return false;

        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);

        return dateTime.isAfter(today) && dateTime.isBefore(tomorrow);
    }

    // Check if date is yesterday
    public static boolean isYesterday(LocalDateTime dateTime) {
        if (dateTime == null) return false;

        LocalDateTime yesterday = LocalDateTime.now().toLocalDate().atStartOfDay().minusDays(1);
        LocalDateTime today = yesterday.plusDays(1);

        return dateTime.isAfter(yesterday) && dateTime.isBefore(today);
    }

    // Get current timestamp string
    public static String getCurrentTimestamp() {
        return DATETIME_FORMAT.format(new Date());
    }
}