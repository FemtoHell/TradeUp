// app/src/main/java/com/example/tradeupsprojecy/utils/Constants.java (Cập nhật)
package com.example.tradeupsprojecy.utils;

public class Constants {
    // API Configuration - CẬP NHẬT URL
    public static final String BASE_URL = "http://10.0.2.2:8080/api/"; // Localhost cho emulator
    public static final String BASE_URL_REAL = "http://192.168.1.100:8080/api/"; // Thay IP thật
    public static final String WS_URL = "ws://10.0.2.2:8080/ws"; // WebSocket URL

    // Endpoints - CẬP NHẬT
    public static final String AUTH_LOGIN = "auth/login";
    public static final String AUTH_REGISTER = "auth/register";
    public static final String AUTH_GOOGLE = "auth/google";
    public static final String ITEMS = "items";
    public static final String CATEGORIES = "categories";
    public static final String MESSAGES = "messages";
    public static final String OFFERS = "offers";
    public static final String REVIEWS = "reviews";
    public static final String SAVED_ITEMS = "saved-items";

    // SharedPreferences Keys
    public static final String PREF_NAME = "TradeUpSession";
    public static final String KEY_TOKEN = "auth_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";

    // Item Conditions - SYNC với backend
    public static final String CONDITION_NEW = "NEW";
    public static final String CONDITION_LIKE_NEW = "LIKE_NEW";
    public static final String CONDITION_EXCELLENT = "EXCELLENT";
    public static final String CONDITION_VERY_GOOD = "VERY_GOOD";
    public static final String CONDITION_GOOD = "GOOD";
    public static final String CONDITION_FAIR = "FAIR";
    public static final String CONDITION_POOR = "POOR";

    // Item Status - SYNC với backend
    public static final String STATUS_AVAILABLE = "AVAILABLE";
    public static final String STATUS_RESERVED = "RESERVED";
    public static final String STATUS_SOLD = "SOLD";
    public static final String STATUS_EXPIRED = "EXPIRED";

    // Offer Status
    public static final String OFFER_PENDING = "PENDING";
    public static final String OFFER_ACCEPTED = "ACCEPTED";
    public static final String OFFER_REJECTED = "REJECTED";

    // Message Status
    public static final String MESSAGE_SENT = "SENT";
    public static final String MESSAGE_DELIVERED = "DELIVERED";
    public static final String MESSAGE_READ = "READ";

    // Error Messages - CẬP NHẬT
    public static final String ERROR_NETWORK = "Kiểm tra kết nối mạng";
    public static final String ERROR_SERVER = "Lỗi server, thử lại sau";
    public static final String ERROR_UNAUTHORIZED = "Phiên đăng nhập hết hạn";
    public static final String ERROR_VALIDATION = "Dữ liệu không hợp lệ";
    public static final String ERROR_GOOGLE_SIGNIN = "Google sign in failed";

    // Success Messages
    public static final String SUCCESS_LOGIN = "Đăng nhập thành công";
    public static final String SUCCESS_REGISTER = "Đăng ký thành công";
    public static final String SUCCESS_LOGOUT = "Đăng xuất thành công";
    public static final String SUCCESS_CREATE_LISTING = "Đăng tin thành công";
    public static final String SUCCESS_OFFER_SENT = "Đã gửi lời đề nghị";
    public static final String SUCCESS_ITEM_SAVED = "Đã lưu sản phẩm";

    // Pagination
    public static final int PAGE_SIZE = 20;
    public static final int FIRST_PAGE = 0;

    // Request Codes
    public static final int REQUEST_IMAGE_PICK = 1001;
    public static final int REQUEST_IMAGE_CAPTURE = 1002;
    public static final int REQUEST_LOCATION_PERMISSION = 1003;
    public static final int REQUEST_GOOGLE_SIGNIN = 9001;

    // Image Configuration
    public static final int MAX_IMAGE_SIZE = 1024; // KB
    public static final int IMAGE_QUALITY = 80;
    public static final int MAX_IMAGES_PER_LISTING = 5;
}