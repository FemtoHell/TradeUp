// app/src/main/java/com/example/tradeupsprojecy/data/repository/AuthRepository.java
package com.example.tradeupsprojecy.data.repository;

import com.example.tradeupsprojecy.data.api.ApiService;
import com.example.tradeupsprojecy.data.models.request.AuthRequest;
import com.example.tradeupsprojecy.data.models.response.AuthResponse;

import retrofit2.Call;

public class AuthRepository {
    private ApiService apiService;

    public AuthRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Call<AuthResponse> login(AuthRequest request) {
        return apiService.login(request);
    }

    public Call<AuthResponse> register(AuthRequest request) {
        return apiService.register(request);
    }

    public Call<AuthResponse.UserDto> getProfile(String token) {
        return apiService.getProfile("Bearer " + token);
    }
}