// app/src/main/java/com/example/tradeupsprojecy/ui/fragments/MessagesFragment.java
package com.example.tradeupsprojecy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Conversation;
import com.example.tradeupsprojecy.data.network.NetworkClient;
import com.example.tradeupsprojecy.ui.adapters.ConversationAdapter;
import com.example.tradeupsprojecy.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesFragment extends Fragment implements ConversationAdapter.OnConversationClickListener {
    private RecyclerView conversationsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyStateText;

    private ConversationAdapter conversationAdapter;
    private List<Conversation> conversationList;
    private PreferenceManager preferenceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        initViews(view);
        setupRecyclerView();
        setupSwipeRefresh();
        loadConversations();

        return view;
    }

    private void initViews(View view) {
        conversationsRecyclerView = view.findViewById(R.id.conversationsRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        emptyStateText = view.findViewById(R.id.emptyStateText);

        preferenceManager = new PreferenceManager(getContext());
        conversationList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        conversationAdapter = new ConversationAdapter(conversationList, this);
        conversationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        conversationsRecyclerView.setAdapter(conversationAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadConversations);
    }

    private void loadConversations() {
        String token = preferenceManager.getToken();

        Call<List<Conversation>> call = NetworkClient.getApiService()
                .getUserConversations("Bearer " + token);

        call.enqueue(new Callback<List<Conversation>>() {
            @Override
            public void onResponse(Call<List<Conversation>> call, Response<List<Conversation>> response) {
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    conversationList.clear();
                    conversationList.addAll(response.body());
                    conversationAdapter.notifyDataSetChanged();

                    // Show/hide empty state
                    if (conversationList.isEmpty()) {
                        emptyStateText.setVisibility(View.VISIBLE);
                        conversationsRecyclerView.setVisibility(View.GONE);
                    } else {
                        emptyStateText.setVisibility(View.GONE);
                        conversationsRecyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to load conversations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Conversation>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConversationClick(Conversation conversation) {
        // Navigate to ChatActivity
        // Intent intent = new Intent(getContext(), ChatActivity.class);
        // intent.putExtra("conversation_id", conversation.getId().toString());
        // intent.putExtra("participant_name", getOtherParticipantName(conversation));
        // startActivity(intent);

        Toast.makeText(getContext(), "Chat with " + getOtherParticipantName(conversation), Toast.LENGTH_SHORT).show();
    }

    private String getOtherParticipantName(Conversation conversation) {
        Long currentUserId = preferenceManager.getUserId();

        if (conversation.getParticipant1Id().equals(currentUserId)) {
            return conversation.getParticipant2Name();
        } else {
            return conversation.getParticipant1Name();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadConversations();
    }
}