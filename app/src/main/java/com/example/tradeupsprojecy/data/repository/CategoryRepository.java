// app/src/main/java/com/example/tradeupsprojecy/data/repository/CategoryRepository.java
package com.example.tradeupsprojecy.data.repository;

import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.data.models.Category;
import com.example.tradeupsprojecy.data.network.ApiService;
import com.example.tradeupsprojecy.data.network.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class CategoryRepository {
    private ApiService apiService;

    public CategoryRepository() {
        this.apiService = NetworkClient.getApiService();
    }

    public interface CategoriesCallback {
        void onSuccess(List<Category> categories);
        void onError(String error);
    }

    public void getCategories(CategoriesCallback callback) {
        Call<ApiResponse<List<Category>>> call = apiService.getCategories();
        call.enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Category>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Failed to load categories");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getAllCategories(CategoriesCallback callback) {
        Call<ApiResponse<List<Category>>> call = apiService.getAllCategories();
        call.enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Category>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Failed to load all categories");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}