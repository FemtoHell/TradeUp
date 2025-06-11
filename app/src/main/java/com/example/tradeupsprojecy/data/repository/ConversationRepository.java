// app/src/main/java/com/example/tradeupsprojecy/data/repository/ConversationRepository.java
package com.example.tradeupsprojecy.data.repository;
import com.example.tradeupsprojecy.data.models.*;
import com.example.tradeupsprojecy.data.models.response.*;
import com.example.tradeupsprojecy.data.network.ApiService;
import com.example.tradeupsprojecy.data.network.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class ConversationRepository {
    private ApiService apiService;

    public ConversationRepository() {
        this.apiService = NetworkClient.getApiService();
    }

    public interface ConversationsCallback {
        void onSuccess(List<Conversation> conversations);
        void onError(String error);
    }

    public interface MessagesCallback {
        void onSuccess(List<Message> messages);
        void onError(String error);
    }

    public interface MessageCallback {
        void onSuccess(Message message);
        void onError(String error);
    }

    public interface ConversationCallback {
        void onSuccess(Conversation conversation);
        void onError(String error);
    }

    public void getUserConversations(String token, ConversationsCallback callback) {
        Call<ApiResponse<List<Conversation>>> call = apiService.getUserConversations("Bearer " + token);
        call.enqueue(new Callback<ApiResponse<List<Conversation>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Conversation>>> call, Response<ApiResponse<List<Conversation>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Conversation>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Failed to load conversations");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Conversation>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void getConversationMessages(String conversationId, String token, MessagesCallback callback) {
        Call<ApiResponse<List<Message>>> call = apiService.getConversationMessages(conversationId, "Bearer " + token);
        call.enqueue(new Callback<ApiResponse<List<Message>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Message>>> call, Response<ApiResponse<List<Message>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Message>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Failed to load messages");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Message>>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void sendMessage(String token, SendMessageRequest request, MessageCallback callback) {
        Call<ApiResponse<Message>> call = apiService.sendMessage("Bearer " + token, request);
        call.enqueue(new Callback<ApiResponse<Message>>() {
            @Override
            public void onResponse(Call<ApiResponse<Message>> call, Response<ApiResponse<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Message> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Failed to send message");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Message>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void createConversation(String token, CreateConversationRequest request, ConversationCallback callback) {
        Call<ApiResponse<Conversation>> call = apiService.createConversation("Bearer " + token, request);
        call.enqueue(new Callback<ApiResponse<Conversation>>() {
            @Override
            public void onResponse(Call<ApiResponse<Conversation>> call, Response<ApiResponse<Conversation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Conversation> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage());
                    }
                } else {
                    callback.onError("Failed to create conversation");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Conversation>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}