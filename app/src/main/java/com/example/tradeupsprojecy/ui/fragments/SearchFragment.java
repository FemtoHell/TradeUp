package com.example.tradeupsprojecy.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Item;
import com.example.tradeupsprojecy.data.repository.ItemRepository;
import com.example.tradeupsprojecy.ui.activities.ItemDetailActivity;
import com.example.tradeupsprojecy.ui.adapters.ItemAdapter;
import com.example.tradeupsprojecy.ui.adapters.SearchSuggestionsAdapter;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment implements ItemAdapter.OnItemClickListener {

    private TextInputEditText searchEditText;
    private RecyclerView searchResultsRecyclerView;
    private RecyclerView suggestionsRecyclerView;
    private LinearLayout suggestionsLayout;
    private LinearLayout emptyStateLayout;

    private ItemAdapter searchAdapter;
    private SearchSuggestionsAdapter suggestionsAdapter;
    private ItemRepository itemRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerViews();
        setupSearchListener();
        loadSearchSuggestions();

        // Check if we have a search query from arguments
        Bundle args = getArguments();
        if (args != null && args.containsKey("search_query")) {
            String query = args.getString("search_query");
            if (query != null && !query.isEmpty()) {
                searchEditText.setText(query);
                performSearch(query);
            }
        }
    }

    private void initViews(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);
        suggestionsRecyclerView = view.findViewById(R.id.recentSearchesRecyclerView);
        suggestionsLayout = view.findViewById(R.id.suggestionsLayout);
        emptyStateLayout = view.findViewById(R.id.noResultsLayout);

        itemRepository = new ItemRepository();
    }

    private void setupRecyclerViews() {
        // Search results
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        searchAdapter = new ItemAdapter(getContext());
        searchAdapter.setOnItemClickListener(this);
        searchResultsRecyclerView.setAdapter(searchAdapter);

        // Suggestions
        if (suggestionsRecyclerView != null) {
            suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    showSearchResults();
                    performSearch(s.toString());
                } else {
                    showSuggestions();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadSearchSuggestions() {
        // Show popular searches and categories
        List<String> suggestions = Arrays.asList(
                "iPhone", "Samsung", "Laptop", "Xe máy", "Quần áo",
                "Đồng hồ", "Túi xách", "Giày dép", "Điện thoại", "Máy tính"
        );

        if (suggestionsRecyclerView != null) {
            suggestionsAdapter = new SearchSuggestionsAdapter(suggestions, suggestion -> {
                searchEditText.setText(suggestion);
                performSearch(suggestion);
            });
            suggestionsRecyclerView.setAdapter(suggestionsAdapter);
        }
    }

    private void showSuggestions() {
        if (suggestionsLayout != null) {
            suggestionsLayout.setVisibility(View.VISIBLE);
        }
        searchResultsRecyclerView.setVisibility(View.GONE);
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    private void showSearchResults() {
        if (suggestionsLayout != null) {
            suggestionsLayout.setVisibility(View.GONE);
        }
        searchResultsRecyclerView.setVisibility(View.VISIBLE);
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.GONE);
        }
    }

    private void showEmptyState() {
        if (suggestionsLayout != null) {
            suggestionsLayout.setVisibility(View.GONE);
        }
        searchResultsRecyclerView.setVisibility(View.GONE);
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.VISIBLE);
        }
    }

    private void performSearch(String query) {
        // For now, show all items as search results
        // TODO: Implement actual search when backend supports it
        itemRepository.getAllItems(new ItemRepository.ItemsCallback() {
            @Override
            public void onSuccess(List<Item> items) {
                if (getActivity() != null && isAdded()) {
                    // Filter items by query (simple contains check)
                    List<Item> filteredItems = new java.util.ArrayList<>();
                    for (Item item : items) {
                        if (item.getTitle() != null &&
                                item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                            filteredItems.add(item);
                        }
                    }

                    searchAdapter.setItems(filteredItems);
                    if (filteredItems.isEmpty()) {
                        showEmptyState();
                    } else {
                        showSearchResults();
                    }
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null && isAdded()) {
                    Toast.makeText(getContext(), "Search error: " + error, Toast.LENGTH_SHORT).show();
                    showEmptyState();
                }
            }
        });
    }

    @Override
    public void onItemClick(Item item) {
        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
        intent.putExtra("item_id", item.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Item item, int position) {
        Toast.makeText(getContext(), "Favorite functionality!", Toast.LENGTH_SHORT).show();
    }
}