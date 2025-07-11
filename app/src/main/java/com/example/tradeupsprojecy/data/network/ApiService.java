// app/src/main/java/com/example/tradeupsprojecy/data/network/ApiService.java - ADD MISSING ENDPOINTS
package com.example.tradeupsprojecy.data.network;

import com.example.tradeupsprojecy.data.models.*;
import com.example.tradeupsprojecy.data.models.response.*;
import com.example.tradeupsprojecy.data.models.request.*;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface ApiService {

    // ============= AUTH ENDPOINTS =============
    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("auth/register")
    Call<AuthResponse> register(@Body AuthRequest request);

    @POST("auth/google")
    Call<AuthResponse> googleLogin(@Body GoogleAuthRequest request);

    @GET("auth/profile")
    Call<ApiResponse<User>> getProfile(@Header("Authorization") String token);

    // ============= ITEMS ENDPOINTS =============
    @GET("items")
    Call<ApiResponse<List<Item>>> getAllItems();

    @GET("items")
    Call<ApiResponse<List<Item>>> getAllItems(
            @Query("page") int page,
            @Query("size") int size,
            @Query("sortBy") String sortBy,
            @Query("sortDir") String sortDir
    );

    @GET("items/{id}")
    Call<ApiResponse<Item>> getItemById(@Path("id") Long id);

    @POST("items")
    Call<ApiResponse<Item>> createItem(
            @Header("Authorization") String token,
            @Body CreateItemRequest request
    );

    @PUT("items/{id}")
    Call<ApiResponse<Item>> updateItem(
            @Path("id") Long id,
            @Header("Authorization") String token,
            @Body Item item
    );

    @DELETE("items/{id}")
    Call<ApiResponse<String>> deleteItem(
            @Path("id") Long id,
            @Header("Authorization") String token
    );

    @GET("items/category/{categoryId}")
    Call<ApiResponse<List<Item>>> getItemsByCategory(@Path("categoryId") Long categoryId);

    @GET("items/seller/{sellerId}")
    Call<ApiResponse<List<Item>>> getItemsBySeller(@Path("sellerId") Long sellerId);

    @GET("items/my-items")
    Call<ApiResponse<List<Item>>> getMyItems(@Header("Authorization") String token);

    @GET("items/search")
    Call<ApiResponse<List<Item>>> searchItems(@Query("keyword") String keyword);

    @GET("items/featured")
    Call<ApiResponse<List<Item>>> getFeaturedItems();

    @GET("items/recent")
    Call<ApiResponse<List<Item>>> getRecentItems();

    @PUT("items/{id}/mark-sold")
    Call<ApiResponse<Item>> markItemAsSold(
            @Path("id") Long id,
            @Header("Authorization") String token
    );

    // ============= CATEGORIES ENDPOINTS =============
    @GET("categories")
    Call<ApiResponse<List<Category>>> getCategories();

    @GET("categories/all")
    Call<ApiResponse<List<Category>>> getAllCategories();

    // ============= CONVERSATIONS/MESSAGES ENDPOINTS - ✅ ADD MISSING =============
    @POST("conversations")
    Call<ApiResponse<Conversation>> createConversation(
            @Header("Authorization") String token,
            @Body CreateConversationRequest request
    );

    @GET("conversations")
    Call<ApiResponse<List<Conversation>>> getUserConversations(@Header("Authorization") String token);

    @GET("conversations/{conversationId}/messages")
    Call<ApiResponse<List<Message>>> getConversationMessages(
            @Path("conversationId") String conversationId,
            @Header("Authorization") String token
    );

    @POST("messages")
    Call<ApiResponse<Message>> sendMessage(
            @Header("Authorization") String token,
            @Body SendMessageRequest request
    );

    // ============= ALTERNATIVE MESSAGE ENDPOINTS =============
    @POST("messages/send")
    Call<ApiResponse<Message>> sendDirectMessage(
            @Header("Authorization") String token,
            @Body SendMessageRequest request
    );

    @GET("messages/conversation/{userId}")
    Call<ApiResponse<List<Message>>> getDirectConversation(
            @Path("userId") Long userId,
            @Header("Authorization") String token
    );

    @GET("messages/my-conversations")
    Call<ApiResponse<List<Message>>> getMyConversations(@Header("Authorization") String token);
}