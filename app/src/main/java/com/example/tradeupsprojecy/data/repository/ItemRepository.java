// app/src/main/java/com/example/tradeupsprojecy/data/repository/ItemRepository.java
package com.example.tradeupsprojecy.data.repository;

import com.example.tradeupsprojecy.data.models.*;
import com.example.tradeupsprojecy.data.models.request.*;
import com.example.tradeupsprojecy.data.models.response.*;
import com.example.tradeupsprojecy.data.network.ApiService;
import com.example.tradeupsprojecy.data.network.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class ItemRepository {
    private ApiService apiService;

    public ItemRepository() {
        this.apiService = NetworkClient.getApiService();
    }

    public interface ItemCallback {
        void onSuccess(Listing item);
        void onError(String error);
    }

    public interface ItemsCallback {
        void onSuccess(List<Listing> items);
        void onError(String error);
    }

    // Add missing methods
    public void getFeaturedItems(ItemsCallback callback) {
        Call<ApiResponse<List<Listing>>> call = apiService.getFeaturedItems();
        call.enqueue(new Callback<ApiResponse<List<Listing>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Listing>>> call, Response<ApiResponse<List<Listing>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Listing>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Failed to load featured items");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Listing>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getRecentItems(ItemsCallback callback) {
        Call<ApiResponse<List<Listing>>> call = apiService.getRecentItems();
        call.enqueue(new Callback<ApiResponse<List<Listing>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Listing>>> call, Response<ApiResponse<List<Listing>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Listing>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Failed to load recent items");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Listing>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void createItem(String token, CreateItemRequest request, ItemCallback callback) {
        Call<ApiResponse<Listing>> call = apiService.createItem("Bearer " + token, request);
        call.enqueue(new Callback<ApiResponse<Listing>>() {
            @Override
            public void onResponse(Call<ApiResponse<Listing>> call, Response<ApiResponse<Listing>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Listing> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Failed to create item");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Listing>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getItemById(Long itemId, ItemCallback callback) {
        Call<ApiResponse<Listing>> call = apiService.getItemById(itemId);
        call.enqueue(new Callback<ApiResponse<Listing>>() {
            @Override
            public void onResponse(Call<ApiResponse<Listing>> call, Response<ApiResponse<Listing>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Listing> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Failed to load item");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Listing>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    // Add other existing methods...
}