// app/src/main/java/com/example/tradeupsprojecy/data/repository/ItemRepository.java - FIX COMPLETE
package com.example.tradeupsprojecy.data.repository;

import com.example.tradeupsprojecy.data.models.*;
import com.example.tradeupsprojecy.data.models.response.*;
import com.example.tradeupsprojecy.data.network.ApiService;
import com.example.tradeupsprojecy.data.network.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import android.util.Log;

public class ItemRepository {
    private static final String TAG = "ItemRepository";
    private ApiService apiService;

    public ItemRepository() {
        this.apiService = NetworkClient.getApiService();
    }

    public interface ItemCallback {
        void onSuccess(Item item);
        void onError(String error);
    }

    public interface ItemsCallback {
        void onSuccess(List<Item> items);
        void onError(String error);
    }

    public void getAllItems(ItemsCallback callback) {
        Log.d(TAG, "Getting all items");
        Call<ApiResponse<List<Item>>> call = apiService.getAllItems();
        call.enqueue(new Callback<ApiResponse<List<Item>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Item>>> call, Response<ApiResponse<List<Item>>> response) {
                Log.d(TAG, "Response received - Code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Item>> apiResponse = response.body();
                    Log.d(TAG, "API Response - Success: " + apiResponse.isSuccess());
                    if (apiResponse.isSuccess()) {
                        List<Item> items = apiResponse.getData();
                        Log.d(TAG, "Items received: " + (items != null ? items.size() : 0));
                        callback.onSuccess(items);
                    } else {
                        Log.e(TAG, "API Error: " + apiResponse.getMessage());
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "Response not successful");
                    callback.onError("Failed to load items");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Item>>> call, Throwable t) {
                Log.e(TAG, "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getFeaturedItems(ItemsCallback callback) {
        Log.d(TAG, "Getting featured items");
        Call<ApiResponse<List<Item>>> call = apiService.getFeaturedItems();
        call.enqueue(new Callback<ApiResponse<List<Item>>>() {  // ✅ FIX: Proper callback syntax
            @Override
            public void onResponse(Call<ApiResponse<List<Item>>> call, Response<ApiResponse<List<Item>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Item>> apiResponse = response.body();
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
            public void onFailure(Call<ApiResponse<List<Item>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getRecentItems(ItemsCallback callback) {
        Log.d(TAG, "Getting recent items");
        Call<ApiResponse<List<Item>>> call = apiService.getRecentItems();
        call.enqueue(new Callback<ApiResponse<List<Item>>>() {  // ✅ FIX: Remove call2, proper syntax
            @Override
            public void onResponse(Call<ApiResponse<List<Item>>> call, Response<ApiResponse<List<Item>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Item>> apiResponse = response.body();
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
            public void onFailure(Call<ApiResponse<List<Item>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getItemById(Long itemId, ItemCallback callback) {
        Call<ApiResponse<Item>> call = apiService.getItemById(itemId);
        call.enqueue(new Callback<ApiResponse<Item>>() {
            @Override
            public void onResponse(Call<ApiResponse<Item>> call, Response<ApiResponse<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Item> apiResponse = response.body();
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
            public void onFailure(Call<ApiResponse<Item>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void createItem(String token, CreateItemRequest request, ItemCallback callback) {
        Call<ApiResponse<Item>> call = apiService.createItem("Bearer " + token, request);
        call.enqueue(new Callback<ApiResponse<Item>>() {
            @Override
            public void onResponse(Call<ApiResponse<Item>> call, Response<ApiResponse<Item>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Item> apiResponse = response.body();
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
            public void onFailure(Call<ApiResponse<Item>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}