// app/src/main/java/com/example/tradeupsprojecy/data/api/ApiService.java
package com.example.tradeupsprojecy.data.api;

import com.example.tradeupsprojecy.data.models.request.AuthRequest;
import com.example.tradeupsprojecy.data.models.request.CreateItemRequest;
import com.example.tradeupsprojecy.data.models.request.GoogleAuthRequest;
import com.example.tradeupsprojecy.data.models.request.UpdateUserRequest;
import com.example.tradeupsprojecy.data.models.response.AuthResponse;
import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.data.models.entities.User;
import com.example.tradeupsprojecy.data.models.entities.Item;
import com.example.tradeupsprojecy.data.models.entities.Category;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // ============ AUTHENTICATION ============
    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("auth/register")
    Call<AuthResponse> register(@Body AuthRequest request);

    @POST("auth/google")
    Call<AuthResponse> googleLogin(@Body GoogleAuthRequest request);

    @GET("auth/profile")
    Call<AuthResponse.UserDto> getProfile(@Header("Authorization") String token);

    // ============ USERS ============
    @GET("users")
    Call<ApiResponse<List<User>>> getAllUsers();

    @GET("users/{id}")
    Call<ApiResponse<User>> getUserById(@Path("id") Long id);

    @PUT("users/{id}")
    Call<ApiResponse<User>> updateUser(@Path("id") Long id, @Body UpdateUserRequest request);

    @DELETE("users/{id}")
    Call<ApiResponse<Void>> deleteUser(@Path("id") Long id);

    // ============ CATEGORIES ============
    @GET("categories")
    Call<ApiResponse<List<Category>>> getAllCategories();

    @GET("categories/{id}")
    Call<ApiResponse<Category>> getCategoryById(@Path("id") Long id);

    @POST("categories")
    Call<ApiResponse<Category>> createCategory(@Body Category category);

    @PUT("categories/{id}")
    Call<ApiResponse<Category>> updateCategory(@Path("id") Long id, @Body Category category);

    @DELETE("categories/{id}")
    Call<ApiResponse<String>> deleteCategory(@Path("id") Long id);

    // ============ ITEMS ============
    @GET("items")
    Call<ApiResponse<List<Item>>> getAllItems();

    @GET("items/{id}")
    Call<ApiResponse<Item>> getItemById(@Path("id") Long id);

    @POST("items")
    Call<ApiResponse<Item>> createItem(@Body CreateItemRequest request);

    @PUT("items/{id}")
    Call<ApiResponse<Item>> updateItem(@Path("id") Long id, @Body CreateItemRequest request);

    @DELETE("items/{id}")
    Call<ApiResponse<Void>> deleteItem(@Path("id") Long id);

    @GET("items/category/{categoryId}")
    Call<ApiResponse<List<Item>>> getItemsByCategory(@Path("categoryId") Long categoryId);

    @GET("items/seller/{sellerId}")
    Call<ApiResponse<List<Item>>> getItemsBySeller(@Path("sellerId") Long sellerId);

    @GET("items/search")
    Call<ApiResponse<List<Item>>> searchItems(@Query("q") String query);

    @GET("items/featured")
    Call<ApiResponse<List<Item>>> getFeaturedItems();
}