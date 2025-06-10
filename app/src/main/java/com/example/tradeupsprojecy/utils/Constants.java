// app/src/main/java/com/example/tradeupsprojecy/utils/Constants.java
package com.example.tradeupsprojecy.utils;

public class Constants {
    // API Configuration
    public static final String BASE_URL = "http://10.0.2.2:8080/api/";

    // SharedPreferences Keys
    public static final String PREF_NAME = "TradeUpPrefs";
    public static final String KEY_TOKEN = "auth_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";

    // Item Conditions - ✅ CHÍNH XÁC CÁC CONSTANTS NÀY
    public static final String CONDITION_NEW = "NEW";
    public static final String CONDITION_LIKE_NEW = "LIKE_NEW";
    public static final String CONDITION_GOOD = "GOOD";
    public static final String CONDITION_FAIR = "FAIR";
    public static final String CONDITION_POOR = "POOR";

    // Item Status
    public static final String ITEM_STATUS_AVAILABLE = "AVAILABLE";
    public static final String ITEM_STATUS_SOLD = "SOLD";
    public static final String ITEM_STATUS_RESERVED = "RESERVED";

    // Request Codes
    public static final int REQUEST_IMAGE_CAPTURE = 1001;
    public static final int REQUEST_IMAGE_GALLERY = 1002;
    public static final int REQUEST_LOCATION_PERMISSION = 1003;

    // Intent Keys
    public static final String EXTRA_ITEM_ID = "item_id";
    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_CATEGORY_ID = "category_id";

    // Error Messages
    public static final String ERROR_NETWORK = "Network error. Please check your connection.";
    public static final String ERROR_SERVER = "Server error. Please try again later.";
    public static final String ERROR_UNAUTHORIZED = "Please login again.";
}