package com.example.tradeupsprojecy.data.repository;

import android.util.Log;
import com.example.tradeupsprojecy.data.models.AuthRequest;
import com.example.tradeupsprojecy.data.models.AuthResponse;
import com.example.tradeupsprojecy.data.models.GoogleAuthRequest;
import com.example.tradeupsprojecy.data.network.ApiServices;
import com.example.tradeupsprojecy.data.network.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private static final String TAG = "AuthRepository";
    private ApiServices apiServices;

    public AuthRepository() {
        this.apiServices = NetworkClient.getApiServices();
    }

    // Interface for callbacks
    public interface AuthCallback {
        void onSuccess(AuthResponse response);
        void onError(String error);
    }

    // Login with email/password
    public void login(String email, String password, AuthCallback callback) {
        AuthRequest request = new AuthRequest(email, password);

        Call<AuthResponse> call = apiServices.login(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.isSuccess()) {
                        Log.d(TAG, "Login successful");
                        callback.onSuccess(authResponse);
                    } else {
                        Log.e(TAG, "Login failed: " + authResponse.getMessage());
                        callback.onError(authResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "Login response error: " + response.code());
                    handleHttpError(response.code(), callback);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Login network error: " + t.getMessage());
                callback.onError("Lỗi kết nối mạng");
            }
        });
    }

    // Register new user
    public void register(String email, String password, String fullName, AuthCallback callback) {
        AuthRequest request = new AuthRequest(email, password, fullName);

        Call<AuthResponse> call = apiServices.register(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.isSuccess()) {
                        Log.d(TAG, "Register successful");
                        callback.onSuccess(authResponse);
                    } else {
                        Log.e(TAG, "Register failed: " + authResponse.getMessage());
                        callback.onError(authResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "Register response error: " + response.code());
                    handleHttpError(response.code(), callback);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Register network error: " + t.getMessage());
                callback.onError("Lỗi kết nối mạng");
            }
        });
    }

    // Google Sign In
    public void googleSignIn(GoogleAuthRequest request, AuthCallback callback) {
        Call<AuthResponse> call = apiServices.googleAuth(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.isSuccess()) {
                        Log.d(TAG, "Google sign in successful");
                        callback.onSuccess(authResponse);
                    } else {
                        Log.e(TAG, "Google sign in failed: " + authResponse.getMessage());
                        callback.onError(authResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "Google sign in response error: " + response.code());
                    handleHttpError(response.code(), callback);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Google sign in network error: " + t.getMessage());
                callback.onError("Lỗi kết nối mạng");
            }
        });
    }

    // Handle HTTP error codes
    private void handleHttpError(int code, AuthCallback callback) {
        switch (code) {
            case 401:
                callback.onError("Email hoặc mật khẩu không đúng");
                break;
            case 409:
                callback.onError("Email đã được sử dụng");
                break;
            case 422:
                callback.onError("Dữ liệu không hợp lệ");
                break;
            case 500:
                callback.onError("Lỗi server, thử lại sau");
                break;
            default:
                callback.onError("Có lỗi xảy ra, thử lại sau");
                break;
        }
    }
}