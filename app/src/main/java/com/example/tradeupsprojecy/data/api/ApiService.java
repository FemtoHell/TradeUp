// app/src/main/java/com/example/tradeupsprojecy/data/api/ApiService.java
package com.example.tradeupsprojecy.data.api;

import com.example.tradeupsprojecy.data.entities.Category;
import com.example.tradeupsprojecy.data.entities.Conversation;
import com.example.tradeupsprojecy.data.entities.Item;
import com.example.tradeupsprojecy.data.entities.Message;
import com.example.tradeupsprojecy.data.models.request.AuthRequest;
import com.example.tradeupsprojecy.data.models.request.CreateConversationRequest;
import com.example.tradeupsprojecy.data.models.request.CreateItemRequest;
import com.example.tradeupsprojecy.data.models.request.GoogleAuthRequest;
import com.example.tradeupsprojecy.data.models.request.SendMessageRequest;
import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.data.models.response.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import java.util.List;

public interface ApiService {

    // Authentication
    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("auth/register")
    Call<AuthResponse> register(@Body AuthRequest request);

    @POST("auth/google")
    Call<AuthResponse> googleLogin(@Body GoogleAuthRequest request);

    // Categories
    @GET("categories")
    Call<ApiResponse<List<Category>>> getAllCategories();

    // Items
    @GET("items")
    Call<ApiResponse<List<Item>>> getAllItems();

    @GET("items/{itemId}")
    Call<ApiResponse<Item>> getItemById(@Path("itemId") Long itemId);

    @POST("items")
    Call<ApiResponse<Item>> createItem(@Body CreateItemRequest request);

    // Messages and Conversations
    @GET("conversations/user/{userId}")
    Call<ApiResponse<List<Conversation>>> getUserConversations(@Path("userId") String userId);

    @POST("conversations")
    Call<ApiResponse<String>> createConversation(@Body CreateConversationRequest request);

    @GET("conversations/{conversationId}/messages")
    Call<ApiResponse<List<Message>>> getConversationMessages(@Path("conversationId") String conversationId);

    @POST("messages")
    Call<ApiResponse<Message>> sendMessage(@Body SendMessageRequest request);
}