// app/src/main/java/com/example/tradeupsprojecy/ui/fragments/SearchFragment.java
package com.example.tradeupsprojecy.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.api.ApiClient;
import com.example.tradeupsprojecy.data.api.ApiService;
import com.example.tradeupsprojecy.data.entities.Item;
import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.ui.adapters.ListingAdapter;
import com.example.tradeupsprojecy.ui.activities.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private SearchView searchView;
    private RecyclerView searchResultsRecyclerView;
    private ProgressBar progressBar;
    private View emptyStateLayout;

    private ApiService apiService;
    private ListingAdapter searchAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Handle search query if passed from HomeFragment
        Bundle args = getArguments();
        if (args != null) {
            String searchQuery = args.getString("search_query");
            if (searchQuery != null) {
                Log.d(TAG, "Search query from arguments: " + searchQuery);
                // Will set this after views are initialized
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "SearchFragment created");

        initViews(view);
        initServices();
        setupSearchView();
        setupRecyclerView();

        // Handle search query from arguments
        Bundle args = getArguments();
        if (args != null) {
            String searchQuery = args.getString("search_query");
            if (searchQuery != null && searchView != null) {
                searchView.setQuery(searchQuery, true);
            }
        }
    }

    private void initViews(View view) {
        searchView = view.findViewById(R.id.searchView);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
    }

    private void initServices() {
        apiService = ApiClient.getApiService();
    }

    private void setupSearchView() {
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d(TAG, "Search submitted: " + query);
                    performSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Perform search with delay for real-time search
                    if (newText.length() >= 3) {
                        performSearch(newText);
                    } else if (newText.isEmpty()) {
                        clearResults();
                    }
                    return true;
                }
            });
        }
    }

    private void setupRecyclerView() {
        if (searchResultsRecyclerView != null) {
            searchAdapter = new ListingAdapter(requireContext(), new ArrayList<>());
            searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            searchResultsRecyclerView.setAdapter(searchAdapter);

            searchAdapter.setOnItemClickListener(item -> {
                Log.d(TAG, "Search result clicked: " + item.getTitle());
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToItemDetail(item.getId());
                }
            });
        }
    }

    private void performSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            clearResults();
            return;
        }

        Log.d(TAG, "Performing search for: " + query);

        showLoading(true);
        hideEmptyState();

        // For now, search through all items
        // In the future, you can create a dedicated search endpoint
        apiService.getAllItems().enqueue(new Callback<ApiResponse<List<Item>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Item>>> call, Response<ApiResponse<List<Item>>> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Item>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        List<Item> allItems = apiResponse.getData();
                        List<Item> filteredItems = filterItems(allItems, query);

                        Log.d(TAG, "Search results: " + filteredItems.size() + " items found");

                        if (filteredItems.isEmpty()) {
                            showEmptyState();
                        } else {
                            hideEmptyState();
                            searchAdapter.updateItems(filteredItems);
                        }
                    } else {
                        Log.e(TAG, "Search API error: " + apiResponse.getMessage());
                        showEmptyState();
                    }
                } else {
                    Log.e(TAG, "Search API call failed: " + response.code());
                    showEmptyState();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Item>>> call, Throwable t) {
                Log.e(TAG, "Search API call failed: " + t.getMessage());
                showLoading(false);
                showEmptyState();
            }
        });
    }

    // ✅ FIXED: Sửa logic filter để không gọi getCategoryName() không tồn tại
    private List<Item> filterItems(List<Item> items, String query) {
        List<Item> filteredItems = new ArrayList<>();
        String lowerQuery = query.toLowerCase().trim();

        for (Item item : items) {
            if (item.getTitle() != null && item.getTitle().toLowerCase().contains(lowerQuery)) {
                filteredItems.add(item);
            } else if (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerQuery)) {
                filteredItems.add(item);
            } else if (item.getCategoryName() != null && item.getCategoryName().toLowerCase().contains(lowerQuery)) {
                filteredItems.add(item);
            }
        }

        return filteredItems;
    }

    private void clearResults() {
        if (searchAdapter != null) {
            searchAdapter.updateItems(new ArrayList<>());
        }
        hideEmptyState();
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void showEmptyState() {
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.VISIBLE);
        }
        if (searchResultsRecyclerView != null) {
            searchResultsRecyclerView.setVisibility(View.GONE);
        }
    }

    private void hideEmptyState() {
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.GONE);
        }
        if (searchResultsRecyclerView != null) {
            searchResultsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}