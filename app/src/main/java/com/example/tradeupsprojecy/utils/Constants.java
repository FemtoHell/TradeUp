package com.example.tradeupsprojecy.utils;

public class Constants {

    // API Configuration
    public static final String BASE_URL = "http://10.0.2.2:8080/api/"; // Localhost cho emulator
    public static final String BASE_URL_REAL = "http://192.168.1.100:8080/api/"; // Thay IP thật của bạn

    // Endpoints
    public static final String AUTH_LOGIN = "auth/login";
    public static final String AUTH_REGISTER = "auth/register";
    public static final String AUTH_GOOGLE = "auth/google";
    public static final String LISTINGS = "listings";
    public static final String CATEGORIES = "categories";
    public static final String USERS = "users";
    public static final String MESSAGES = "messages";

    // SharedPreferences Keys
    public static final String PREF_NAME = "TradeUpsPrefs";
    public static final String KEY_TOKEN = "auth_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_PROFILE_IMAGE = "profile_image";

    // Request Codes
    public static final int REQUEST_IMAGE_PICK = 1001;
    public static final int REQUEST_IMAGE_CAPTURE = 1002;
    public static final int REQUEST_LOCATION_PERMISSION = 1003;
    public static final int REQUEST_STORAGE_PERMISSION = 1004;

    // Intent Keys
    public static final String EXTRA_LISTING_ID = "listing_id";
    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_CATEGORY_ID = "category_id";
    public static final String EXTRA_CONVERSATION_ID = "conversation_id";

    // Item Conditions
    public static final String CONDITION_NEW = "NEW";
    public static final String CONDITION_LIKE_NEW = "LIKE_NEW";
    public static final String CONDITION_GOOD = "GOOD";
    public static final String CONDITION_FAIR = "FAIR";
    public static final String CONDITION_POOR = "POOR";

    // Listing Status
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_SOLD = "SOLD";
    public static final String STATUS_RESERVED = "RESERVED";
    public static final String STATUS_EXPIRED = "EXPIRED";

    // Error Messages
    public static final String ERROR_NETWORK = "Kiểm tra kết nối mạng";
    public static final String ERROR_SERVER = "Lỗi server, thử lại sau";
    public static final String ERROR_UNAUTHORIZED = "Phiên đăng nhập hết hạn";
    public static final String ERROR_VALIDATION = "Dữ liệu không hợp lệ";

    // Success Messages
    public static final String SUCCESS_LOGIN = "Đăng nhập thành công";
    public static final String SUCCESS_REGISTER = "Đăng ký thành công";
    public static final String SUCCESS_LOGOUT = "Đăng xuất thành công";
    public static final String SUCCESS_CREATE_LISTING = "Đăng tin thành công";

    // Image Configuration
    public static final int MAX_IMAGE_SIZE = 1024; // KB
    public static final int IMAGE_QUALITY = 80; // Compression quality
    public static final int MAX_IMAGES_PER_LISTING = 5;

    // Pagination
    public static final int PAGE_SIZE = 20;
    public static final int FIRST_PAGE = 0;

    // Categories (Default)
    public static final String[] DEFAULT_CATEGORIES = {
            "Điện tử", "Thời trang", "Xe cộ", "Nhà cửa",
            "Sách", "Thể thao", "Sức khỏe", "Khác"
    };
}