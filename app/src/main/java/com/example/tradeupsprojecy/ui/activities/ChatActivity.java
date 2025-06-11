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
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Message;
import com.example.tradeupsprojecy.data.models.SendMessageRequest;
import com.example.tradeupsprojecy.data.repository.ConversationRepository;
import com.example.tradeupsprojecy.ui.adapters.MessageAdapter;
import com.example.tradeupsprojecy.data.local.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView messagesRecyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private TextView chatTitle;

    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private String conversationId;
    private String participantName;
    private SessionManager sessionManager;
    private ConversationRepository conversationRepository;

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
        chatTitle = findViewById(R.id.userNameText);

        sessionManager = new SessionManager(this);
        conversationRepository = new ConversationRepository();
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
        messageAdapter = new MessageAdapter(messageList, sessionManager.getUserId());
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

        String token = sessionManager.getToken();
        conversationRepository.getConversationMessages(conversationId, token, new ConversationRepository.MessagesCallback() {
            @Override
            public void onSuccess(List<Message> messages) {
                messageList.clear();
                messageList.addAll(messages);
                messageAdapter.notifyDataSetChanged();
                scrollToBottom();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ChatActivity.this, "Failed to load messages: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();
        if (TextUtils.isEmpty(messageText)) return;

        String token = sessionManager.getToken();
        SendMessageRequest request = new SendMessageRequest();
        try {
            request.setConversationId(Long.parseLong(conversationId));
        } catch (NumberFormatException e) {
            return;
        }
        request.setMessage(messageText);

        conversationRepository.sendMessage(token, request, new ConversationRepository.MessageCallback() {
            @Override
            public void onSuccess(Message message) {
                messageList.add(message);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                messageEditText.setText("");
                scrollToBottom();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ChatActivity.this, "Failed to send message: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scrollToBottom() {
        if (messageList.size() > 0) {
            messagesRecyclerView.scrollToPosition(messageList.size() - 1);
        }
    }
}