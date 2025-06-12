// app/src/main/java/com/example/tradeupsprojecy/data/local/SessionManager.java
package com.example.tradeupsprojecy.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    private static final String PREF_NAME = "TradeUpSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_PHOTO = "userPhoto";
    private static final String KEY_TOKEN = "token"; // ✅ Add token key

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // ✅ Method 1: Google Sign-In session (original)
    public void createLoginSession(String name, String email, String userId, String photoUrl) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_PHOTO, photoUrl);
        editor.commit();

        Log.d("SessionManager", "Login session created for: " + email);
    }



    // ✅ Check if user is logged in
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // ✅ Get user info methods
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    public String getUserId() {
        return pref.getString(KEY_USER_ID, "");
    }

    public String getUserPhotoUrl() {
        return pref.getString(KEY_USER_PHOTO, "");
    }

    // ✅ MISSING METHOD: getToken()
    public String getToken() {
        return pref.getString(KEY_TOKEN, "");
    }

    // ✅ MISSING METHOD: logout() - alias for logoutUser()
    public void logout() {
        logoutUser();
    }

    // ✅ Original logout method
    public void logoutUser() {
        editor.clear();
        editor.commit();

        Log.d("SessionManager", "User logged out");
    }

    // ✅ Additional utility methods
    public boolean hasToken() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }

    public void updateToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.commit();
        Log.d("SessionManager", "Token updated");
    }

    public void updateUserInfo(String name, String email, String photoUrl) {
        if (name != null) editor.putString(KEY_USER_NAME, name);
        if (email != null) editor.putString(KEY_USER_EMAIL, email);
        if (photoUrl != null) editor.putString(KEY_USER_PHOTO, photoUrl);
        editor.commit();
        Log.d("SessionManager", "User info updated");
    }

    // ✅ Debug method
    public void printSessionInfo() {
        Log.d("SessionManager", "=== SESSION INFO ===");
        Log.d("SessionManager", "Logged in: " + isLoggedIn());
        Log.d("SessionManager", "User ID: " + getUserId());
        Log.d("SessionManager", "Name: " + getUserName());
        Log.d("SessionManager", "Email: " + getUserEmail());
        Log.d("SessionManager", "Has Token: " + hasToken());
        Log.d("SessionManager", "Photo URL: " + getUserPhotoUrl());
    }
}