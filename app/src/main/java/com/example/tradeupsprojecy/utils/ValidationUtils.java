package com.example.tradeupsprojecy.utils;

import android.util.Patterns;
import java.util.regex.Pattern;

public class ValidationUtils {

    // Email validation
    public static boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    // Password validation
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    // Strong password validation
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        // At least one digit, one lowercase, one uppercase, one special character
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$");
        return pattern.matcher(password).matches();
    }

    // Phone number validation (Vietnam format)
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        String cleanPhone = phone.replaceAll("\\s+", "").replaceAll("-", "");

        // Vietnam phone number patterns
        Pattern pattern = Pattern.compile("^(\\+84|84|0)[1-9][0-9]{8,9}$");
        return pattern.matcher(cleanPhone).matches();
    }

    // Full name validation
    public static boolean isValidFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return false;
        }

        String trimmedName = fullName.trim();
        return trimmedName.length() >= 2 && trimmedName.length() <= 50 &&
                trimmedName.matches("^[a-zA-ZÀ-ỹ\\s]+$");
    }

    // Price validation
    public static boolean isValidPrice(String priceStr) {
        if (priceStr == null || priceStr.trim().isEmpty()) {
            return false;
        }

        try {
            double price = Double.parseDouble(priceStr.trim());
            return price > 0 && price <= 999999999; // Max 999 million
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Title validation
    public static boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty() &&
                title.trim().length() >= 5 && title.trim().length() <= 100;
    }

    // Description validation
    public static boolean isValidDescription(String description) {
        return description != null && !description.trim().isEmpty() &&
                description.trim().length() >= 10 && description.trim().length() <= 1000;
    }

    // Location validation
    public static boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty() &&
                location.trim().length() >= 3 && location.trim().length() <= 100;
    }

    // Get email error message
    public static String getEmailError(String email) {
        if (email == null || email.trim().isEmpty()) {
            return "Email không được để trống";
        }
        if (!isValidEmail(email)) {
            return "Email không hợp lệ";
        }
        return null;
    }

    // Get password error message
    public static String getPasswordError(String password) {
        if (password == null || password.isEmpty()) {
            return "Mật khẩu không được để trống";
        }
        if (!isValidPassword(password)) {
            return "Mật khẩu phải có ít nhất 6 ký tự";
        }
        return null;
    }

    // Get full name error message
    public static String getFullNameError(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "Họ tên không được để trống";
        }
        if (!isValidFullName(fullName)) {
            return "Họ tên không hợp lệ (2-50 ký tự, chỉ chữ cái)";
        }
        return null;
    }

    // Get phone error message
    public static String getPhoneError(String phone) {
        if (phone != null && !phone.trim().isEmpty() && !isValidPhoneNumber(phone)) {
            return "Số điện thoại không hợp lệ";
        }
        return null;
    }

    // Get price error message
    public static String getPriceError(String price) {
        if (price == null || price.trim().isEmpty()) {
            return "Giá không được để trống";
        }
        if (!isValidPrice(price)) {
            return "Giá không hợp lệ (phải là số dương)";
        }
        return null;
    }

    // Get title error message
    public static String getTitleError(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "Tiêu đề không được để trống";
        }
        if (!isValidTitle(title)) {
            return "Tiêu đề phải có 5-100 ký tự";
        }
        return null;
    }

    // Get description error message
    public static String getDescriptionError(String description) {
        if (description == null || description.trim().isEmpty()) {
            return "Mô tả không được để trống";
        }
        if (!isValidDescription(description)) {
            return "Mô tả phải có 10-1000 ký tự";
        }
        return null;
    }
}