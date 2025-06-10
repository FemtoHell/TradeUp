// app/src/main/java/com/example/tradeupsprojecy/data/repository/UserRepository.java
package com.example.tradeupsprojecy.data.repository;

import com.example.tradeupsprojecy.data.api.ApiService;
import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.data.models.entities.User;
import com.example.tradeupsprojecy.data.models.request.UpdateUserRequest;

import java.util.List;
import retrofit2.Call;

public class UserRepository {
    private ApiService apiService;

    public UserRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Call<ApiResponse<List<User>>> getAllUsers() {
        return apiService.getAllUsers();
    }

    public Call<ApiResponse<User>> getUserById(Long id) {
        return apiService.getUserById(id);
    }

    public Call<ApiResponse<User>> updateUser(Long id, UpdateUserRequest request) {
        return apiService.updateUser(id, request);
    }

    public Call<ApiResponse<Void>> deleteUser(Long id) {
        return apiService.deleteUser(id);
    }
}