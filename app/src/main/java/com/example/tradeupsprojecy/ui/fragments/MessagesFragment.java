// MessagesFragment.java
package com.example.tradeupsprojecy.ui.fragments;

import android.content.Intent;
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
import com.example.tradeupsprojecy.data.repository.ConversationRepository;
import com.example.tradeupsprojecy.ui.activities.ChatActivity;
import com.example.tradeupsprojecy.ui.adapters.ConversationAdapter;
import com.example.tradeupsprojecy.data.local.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment implements ConversationAdapter.OnConversationClickListener {
    private RecyclerView conversationsRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyStateText;

    private ConversationAdapter conversationAdapter;
    private List<Conversation> conversationList;
    private SessionManager sessionManager;
    private ConversationRepository conversationRepository;

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
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        emptyStateText = view.findViewById(R.id.emptyStateText);

        sessionManager = new SessionManager(getContext());
        conversationRepository = new ConversationRepository();
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
        String token = sessionManager.getToken();
        if (token == null) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        conversationRepository.getUserConversations(token, new ConversationRepository.ConversationsCallback() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (getActivity() != null && isAdded()) {
                    swipeRefreshLayout.setRefreshing(false);
                    conversationList.clear();
                    conversationList.addAll(conversations);
                    conversationAdapter.notifyDataSetChanged();

                    // Show/hide empty state
                    if (conversationList.isEmpty()) {
                        emptyStateText.setVisibility(View.VISIBLE);
                        conversationsRecyclerView.setVisibility(View.GONE);
                    } else {
                        emptyStateText.setVisibility(View.GONE);
                        conversationsRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null && isAdded()) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "Failed to load conversations: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConversationClick(Conversation conversation) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("conversation_id", conversation.getId());
        intent.putExtra("participant_name", conversation.getOtherUserName());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadConversations();
    }
}