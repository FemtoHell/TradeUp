// app/src/main/java/com/example/tradeupsprojecy/ui/fragments/MessagesFragment.java
package com.example.tradeupsprojecy.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.api.ApiClient;
import com.example.tradeupsprojecy.data.api.ApiService;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.example.tradeupsprojecy.data.entities.Conversation;
import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.ui.activities.ChatActivity;
import com.example.tradeupsprojecy.ui.adapters.ConversationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    private static final String TAG = "MessagesFragment";

    private RecyclerView conversationsRecyclerView;
    private SwipeRefreshLayout swipeRefresh; // Đổi tên
    private ProgressBar progressBar;
    private TextView emptyStateText; // Đổi tên

    private ApiService apiService;
    private SessionManager sessionManager;
    private ConversationAdapter conversationAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "MessagesFragment created");

        initViews(view);
        initServices();
        setupRecyclerView();
        setupSwipeRefresh();
        loadConversations();
    }

    private void initViews(View view) {
        conversationsRecyclerView = view.findViewById(R.id.conversationsRecyclerView);
        swipeRefresh = view.findViewById(R.id.swipeRefresh); // Đổi tên
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateText = view.findViewById(R.id.emptyStateText); // Đổi tên
    }

    private void initServices() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(requireContext());
    }

    private void setupRecyclerView() {
        conversationAdapter = new ConversationAdapter(requireContext(), new ArrayList<>());
        conversationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        conversationsRecyclerView.setAdapter(conversationAdapter);

        conversationAdapter.setOnConversationClickListener(conversation -> {
            Log.d(TAG, "Conversation clicked: " + conversation.getId());

            Intent intent = new Intent(requireContext(), ChatActivity.class);
            intent.putExtra("conversation_id", conversation.getId());
            intent.putExtra("other_user_name", conversation.getOtherUserName());
            intent.putExtra("item_title", conversation.getItemTitle());
            startActivity(intent);
        });
    }

    private void setupSwipeRefresh() {
        if (swipeRefresh != null) {
            swipeRefresh.setOnRefreshListener(() -> {
                Log.d(TAG, "Refreshing conversations");
                loadConversations();
            });
        }
    }

    private void loadConversations() {
        Log.d(TAG, "Loading conversations");

        if (swipeRefresh == null || !swipeRefresh.isRefreshing()) {
            showLoading(true);
        }
        hideEmptyState();

        String userId = sessionManager.getUserId();

        apiService.getUserConversations(userId).enqueue(new Callback<ApiResponse<List<Conversation>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Conversation>>> call, Response<ApiResponse<List<Conversation>>> response) {
                showLoading(false);
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Conversation>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        List<Conversation> conversations = apiResponse.getData();
                        Log.d(TAG, "Conversations loaded: " + conversations.size());

                        if (conversations.isEmpty()) {
                            showEmptyState();
                        } else {
                            hideEmptyState();
                            conversationAdapter.updateConversations(conversations);
                        }
                    } else {
                        Log.e(TAG, "Conversations API error: " + apiResponse.getMessage());
                        showEmptyState();
                    }
                } else {
                    Log.e(TAG, "Conversations API call failed: " + response.code());
                    showEmptyState();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Conversation>>> call, Throwable t) {
                Log.e(TAG, "Conversations API call failed: " + t.getMessage());
                showLoading(false);
                if (swipeRefresh != null) {
                    swipeRefresh.setRefreshing(false);
                }
                showEmptyState();
            }
        });
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void showEmptyState() {
        if (emptyStateText != null) {
            emptyStateText.setVisibility(View.VISIBLE);
            emptyStateText.setText("No conversations yet\nStart chatting with sellers!");
        }
        if (conversationsRecyclerView != null) {
            conversationsRecyclerView.setVisibility(View.GONE);
        }
    }

    private void hideEmptyState() {
        if (emptyStateText != null) {
            emptyStateText.setVisibility(View.GONE);
        }
        if (conversationsRecyclerView != null) {
            conversationsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh conversations when returning to this fragment
        loadConversations();
    }
}