// app/src/main/java/com/example/tradeupsprojecy/data/repository/UserRepository.java
package com.example.tradeupsprojecy.data.repository;

import android.util.Log;
import com.example.tradeupsprojecy.data.models.User;
import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.data.network.ApiService; // FIX: Đúng package
import com.example.tradeupsprojecy.data.network.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private static final String TAG = "UserRepository";
    private ApiService apiService;

    public UserRepository() {
        this.apiService = NetworkClient.getApiService();
    }

    // Interface for callbacks
    public interface UserCallback {
        void onSuccess(User user);
        void onError(String error);
    }

    public interface UserProfileCallback {
        void onSuccess(ApiResponse<User> response);
        void onError(String error);
    }

    // Get user profile
    public void getUserProfile(String token, UserCallback callback) {
        Call<ApiResponse<User>> call = apiService.getProfile("Bearer " + token);
        call.enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        Log.d(TAG, "Get user profile successful");
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        Log.e(TAG, "Get user profile failed: " + apiResponse.getMessage());
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "Get user profile response error: " + response.code());
                    handleHttpError(response.code(), callback);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                Log.e(TAG, "Get user profile network error: " + t.getMessage());
                callback.onError("Lỗi kết nối mạng");
            }
        });
    }

    // Handle HTTP error codes
    private void handleHttpError(int code, UserCallback callback) {
        switch (code) {
            case 401:
                callback.onError("Phiên đăng nhập hết hạn");
                break;
            case 404:
                callback.onError("Không tìm thấy người dùng");
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