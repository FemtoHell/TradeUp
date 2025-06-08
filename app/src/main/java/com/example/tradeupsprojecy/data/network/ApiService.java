package com.example.tradeupsprojecy.data.network;

import com.example.tradeupsprojecy.data.models.AuthRequest;
import com.example.tradeupsprojecy.data.models.AuthResponse;
import com.example.tradeupsprojecy.data.models.GoogleAuthRequest;
import com.example.tradeupsprojecy.data.models.User;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("auth/register")
    Call<AuthResponse> register(@Body AuthRequest request);

    @POST("auth/google")
    Call<AuthResponse> googleLogin(@Body GoogleAuthRequest request);

    @GET("auth/profile")
    Call<ApiResponse<User>> getProfile(@Header("Authorization") String token);

    @POST("auth/logout")
    Call<ApiResponse<Void>> logout(@Header("Authorization") String token);
}