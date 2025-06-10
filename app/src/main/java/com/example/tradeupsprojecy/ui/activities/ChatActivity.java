// app/src/main/java/com/example/tradeupsprojecy/ui/activities/ChatActivity.java
package com.example.tradeupsprojecy.ui.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.api.ApiClient;
import com.example.tradeupsprojecy.data.api.ApiService;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.example.tradeupsprojecy.data.entities.Message;
import com.example.tradeupsprojecy.data.models.request.SendMessageRequest;
import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.ui.adapters.MessageAdapter;
import com.google.android.material.button.MaterialButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private ImageView backButton;
    private TextView userNameText; // Đổi tên
    private TextView itemTitleTextView;
    private RecyclerView messagesRecyclerView;
    private EditText messageEditText;
    private MaterialButton sendButton;

    private ApiService apiService;
    private SessionManager sessionManager;
    private MessageAdapter messageAdapter;

    private String conversationId;
    private String otherUserName;
    private String itemTitle;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Log.d(TAG, "ChatActivity created");

        getIntentData();
        initViews();
        initServices();
        setupRecyclerView();
        setupClickListeners();
        setupTextWatcher();
        setupUI();
        loadMessages();
    }

    private void getIntentData() {
        conversationId = getIntent().getStringExtra("conversation_id");
        otherUserName = getIntent().getStringExtra("other_user_name");
        itemTitle = getIntent().getStringExtra("item_title");

        Log.d(TAG, "Conversation ID: " + conversationId);
        Log.d(TAG, "Other user: " + otherUserName);
        Log.d(TAG, "Item: " + itemTitle);
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        userNameText = findViewById(R.id.userNameText); // Đổi tên
        itemTitleTextView = findViewById(R.id.itemTitleTextView);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
    }

    private void initServices() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
        currentUserId = sessionManager.getUserId();
    }

    private void setupRecyclerView() {
        messageAdapter = new MessageAdapter(this, new ArrayList<>(), currentUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // Start from bottom
        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRecyclerView.setAdapter(messageAdapter);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void setupTextWatcher() {
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                sendButton.setEnabled(!s.toString().trim().isEmpty());
            }
        });
    }

    private void setupUI() {
        if (userNameText != null && otherUserName != null) {
            userNameText.setText(otherUserName);
        }

        if (itemTitleTextView != null && itemTitle != null) {
            itemTitleTextView.setText(itemTitle);
        }

        sendButton.setEnabled(false);
    }

    private void loadMessages() {
        Log.d(TAG, "Loading messages for conversation: " + conversationId);

        if (conversationId == null) {
            Log.e(TAG, "Conversation ID is null");
            showMessage("Error: Invalid conversation");
            return;
        }

        apiService.getConversationMessages(conversationId).enqueue(new Callback<ApiResponse<List<Message>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Message>>> call, Response<ApiResponse<List<Message>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Message>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        List<Message> messages = apiResponse.getData();
                        Log.d(TAG, "Messages loaded: " + messages.size());

                        messageAdapter.updateMessages(messages);

                        // Scroll to bottom
                        if (!messages.isEmpty()) {
                            messagesRecyclerView.scrollToPosition(messages.size() - 1);
                        }
                    } else {
                        Log.e(TAG, "Messages API error: " + apiResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "Messages API call failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Message>>> call, Throwable t) {
                Log.e(TAG, "Messages API call failed: " + t.getMessage());
                showMessage("Failed to load messages");
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();

        if (messageText.isEmpty()) {
            return;
        }

        Log.d(TAG, "Sending message: " + messageText);

        SendMessageRequest request = new SendMessageRequest();
        request.setConversationId(conversationId);
        request.setContent(messageText);
        request.setSenderId(currentUserId);

        // Clear input immediately for better UX
        messageEditText.setText("");
        sendButton.setEnabled(false);

        apiService.sendMessage(request).enqueue(new Callback<ApiResponse<Message>>() {
            @Override
            public void onResponse(Call<ApiResponse<Message>> call, Response<ApiResponse<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Message> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        Message newMessage = apiResponse.getData();
                        Log.d(TAG, "Message sent successfully");

                        // Add message to adapter
                        messageAdapter.addMessage(newMessage);

                        // Scroll to bottom
                        messagesRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                    } else {
                        Log.e(TAG, "Send message API error: " + apiResponse.getMessage());
                        showMessage("Failed to send message");
                        // Restore text if failed
                        messageEditText.setText(messageText);
                    }
                } else {
                    Log.e(TAG, "Send message API call failed: " + response.code());
                    showMessage("Failed to send message");
                    // Restore text if failed
                    messageEditText.setText(messageText);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Message>> call, Throwable t) {
                Log.e(TAG, "Send message API call failed: " + t.getMessage());
                showMessage("Network error");
                // Restore text if failed
                messageEditText.setText(messageText);
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh messages when returning to chat
        loadMessages();
    }
}