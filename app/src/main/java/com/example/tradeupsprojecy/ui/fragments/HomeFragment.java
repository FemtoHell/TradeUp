package com.example.tradeupsprojecy.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Category;
import com.example.tradeupsprojecy.data.models.Item;
import com.example.tradeupsprojecy.data.repository.CategoryRepository;
import com.example.tradeupsprojecy.data.repository.ItemRepository;
import com.example.tradeupsprojecy.ui.activities.ItemDetailActivity;
import com.example.tradeupsprojecy.ui.activities.MainActivity;
import com.example.tradeupsprojecy.ui.adapters.CategoryAdapter;
import com.example.tradeupsprojecy.ui.adapters.ItemAdapter;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class HomeFragment extends Fragment implements ItemAdapter.OnItemClickListener, CategoryAdapter.OnCategoryClickListener {

    private RecyclerView categoriesRecyclerView, featuredRecyclerView, recentRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ItemAdapter featuredAdapter, recentAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextInputEditText searchEditText;

    private CategoryRepository categoryRepository;
    private ItemRepository itemRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initRepositories();
        setupRecyclerViews();
        setupSwipeRefresh();
        setupSearchClick();
        loadData();
    }

    private void initViews(View view) {
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        featuredRecyclerView = view.findViewById(R.id.featuredRecyclerView);
        recentRecyclerView = view.findViewById(R.id.recentRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        searchEditText = view.findViewById(R.id.searchEditText);
    }

    private void initRepositories() {
        categoryRepository = new CategoryRepository();
        itemRepository = new ItemRepository();
    }

    private void setupRecyclerViews() {
        // Categories
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(getContext());
        categoryAdapter.setOnCategoryClickListener(this);
        categoriesRecyclerView.setAdapter(categoryAdapter);

        // Featured items
        featuredRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        featuredAdapter = new ItemAdapter(getContext());
        featuredAdapter.setOnItemClickListener(this);
        featuredRecyclerView.setAdapter(featuredAdapter);

        // Recent items
        recentRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recentAdapter = new ItemAdapter(getContext());
        recentAdapter.setOnItemClickListener(this);
        recentRecyclerView.setAdapter(recentAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadData);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
    }

    private void setupSearchClick() {
        if (searchEditText != null) {
            searchEditText.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToSearch();
                }
            });
        }
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        loadCategories();
        loadFeaturedItems();
        loadRecentItems();
    }

    private void loadCategories() {
        categoryRepository.getCategories(new CategoryRepository.CategoriesCallback() {
            @Override
            public void onSuccess(List<Category> categories) {
                if (isAdded()) {
                    categoryAdapter.setCategories(categories);
                }
            }

            @Override
            public void onError(String error) {
                if (isAdded()) {
                    showError("Failed to load categories");
                }
            }
        });
    }

    private void loadFeaturedItems() {
        itemRepository.getFeaturedItems(new ItemRepository.ItemsCallback() {
            @Override
            public void onSuccess(List<Item> items) {
                if (isAdded()) {
                    featuredAdapter.setItems(items);
                }
            }

            @Override
            public void onError(String error) {
                if (isAdded()) {
                    showError("Failed to load featured items");
                }
            }
        });
    }

    private void loadRecentItems() {
        itemRepository.getRecentItems(new ItemRepository.ItemsCallback() {
            @Override
            public void onSuccess(List<Item> items) {
                if (isAdded()) {
                    recentAdapter.setItems(items);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(String error) {
                if (isAdded()) {
                    showError("Failed to load recent items");
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(Item item) {
        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
        intent.putExtra("item_id", item.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Item item, int position) {
        Toast.makeText(getContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClick(Category category, int position) {
        Toast.makeText(getContext(), "Category: " + category.getName(), Toast.LENGTH_SHORT).show();
    }
}