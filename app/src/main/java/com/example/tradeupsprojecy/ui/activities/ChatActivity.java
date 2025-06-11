// app/src/main/java/com/example/tradeupsprojecy/ui/activities/ChatActivity.java
package com.example.tradeupsprojecy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tradeupsprojecy.data.models.request.*;
import com.example.tradeupsprojecy.data.models.response.*;
import com.example.tradeupsprojecy.data.models.*;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Message;
import com.example.tradeupsprojecy.data.models.SendMessageRequest;
import com.example.tradeupsprojecy.data.network.NetworkClient;
import com.example.tradeupsprojecy.ui.adapters.MessageAdapter;
import com.example.tradeupsprojecy.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView messagesRecyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private TextView chatTitle;

    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private String conversationId;
    private String participantName;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
        getIntentData();
        setupRecyclerView();
        loadMessages();
        setupClickListeners();
    }

    private void initViews() {
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        chatTitle = findViewById(R.id.chatTitle);

        preferenceManager = new PreferenceManager(this);
        messageList = new ArrayList<>();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        conversationId = intent.getStringExtra("conversation_id");
        participantName = intent.getStringExtra("participant_name");

        if (participantName != null) {
            chatTitle.setText(participantName);
        }
    }

    private void setupRecyclerView() {
        messageAdapter = new MessageAdapter(messageList, getCurrentUserId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRecyclerView.setAdapter(messageAdapter);
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> sendMessage());

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void loadMessages() {
        if (conversationId == null) return;

        String token = preferenceManager.getToken();

        Call<List<Message>> call = NetworkClient.getApiService()
                .getConversationMessages(conversationId, "Bearer " + token);

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    messageAdapter.notifyDataSetChanged();
                    scrollToBottom();
                } else {
                    Toast.makeText(ChatActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (TextUtils.isEmpty(messageText)) return;

        String token = preferenceManager.getToken();

        // Create request
        SendMessageRequest request = new SendMessageRequest();
        request.setConversationId(Long.parseLong(conversationId));
        request.setContent(messageText);

        Call<Message> call = NetworkClient.getApiService()
                .sendMessage("Bearer " + token, request);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Message newMessage = response.body();
                    messageList.add(newMessage);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    messageEditText.setText("");
                    scrollToBottom();
                } else {
                    Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scrollToBottom() {
        if (messageList.size() > 0) {
            messagesRecyclerView.scrollToPosition(messageList.size() - 1);
        }
    }

    private Long getCurrentUserId() {
        return preferenceManager.getUserId();
    }
}