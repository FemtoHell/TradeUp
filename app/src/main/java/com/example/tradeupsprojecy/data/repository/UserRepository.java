// app/src/main/java/com/example/tradeupsprojecy/data/repositories/UserRepository.java
package com.example.tradeupsprojecy.data.repository;

import com.example.tradeupsprojecy.data.api.ApiService;

public class UserRepository {
    private final ApiService apiService;

    public UserRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    // Remove all methods that are not defined in ApiService
    // If you need these methods, add them to ApiService first
}