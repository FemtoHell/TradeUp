// app/src/main/java/com/example/tradeupsprojecy/ui/fragments/HomeFragment.java
package com.example.tradeupsprojecy.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.ui.activities.MainActivity;
import com.example.tradeupsprojecy.data.api.ApiClient;
import com.example.tradeupsprojecy.data.api.ApiService;
// ✅ FIXED: Đúng imports này
import com.example.tradeupsprojecy.data.entities.Category;
import com.example.tradeupsprojecy.data.entities.Item;
import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.ui.adapters.CategoryAdapter;
import com.example.tradeupsprojecy.ui.adapters.ListingAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private LinearLayout searchLayout;
    private RecyclerView categoriesRecyclerView;
    private RecyclerView featuredRecyclerView;
    private RecyclerView recentListingsRecyclerView;

    private ApiService apiService;
    private CategoryAdapter categoryAdapter;
    private ListingAdapter featuredAdapter;
    private ListingAdapter recentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "HomeFragment created");

        initViews(view);
        initServices();
        setupClickListeners();
        setupRecyclerViews();
        loadData();
    }

    private void initViews(View view) {
        searchLayout = view.findViewById(R.id.searchLayout);
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView);
        featuredRecyclerView = view.findViewById(R.id.featuredRecyclerView);
        recentListingsRecyclerView = view.findViewById(R.id.recentListingsRecyclerView);
    }

    private void initServices() {
        apiService = ApiClient.getApiService();
    }

    private void setupClickListeners() {
        if (searchLayout != null) {
            searchLayout.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToSearch();
                }
            });
        }
    }

    private void setupRecyclerViews() {
        // Categories RecyclerView
        if (categoriesRecyclerView != null) {
            categoryAdapter = new CategoryAdapter(requireContext());
            categoriesRecyclerView.setLayoutManager(
                    new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            );
            categoriesRecyclerView.setAdapter(categoryAdapter);

            categoryAdapter.setOnCategoryClickListener((category, position) -> {
                Log.d(TAG, "Category clicked: " + category.getName());
                // ✅ FIXED: Navigate to search with category filter
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToSearch(category.getName());
                }
            });
        }

        // Featured RecyclerView
        if (featuredRecyclerView != null) {
            featuredAdapter = new ListingAdapter(requireContext(), new ArrayList<>());
            featuredRecyclerView.setLayoutManager(
                    new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            );
            featuredRecyclerView.setAdapter(featuredAdapter);

            featuredAdapter.setOnItemClickListener(item -> {
                Log.d(TAG, "Featured item clicked: " + item.getTitle());
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToItemDetail(item.getId());
                }
            });
        }

        // Recent Listings RecyclerView
        if (recentListingsRecyclerView != null) {
            recentAdapter = new ListingAdapter(requireContext(), new ArrayList<>());
            recentListingsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recentListingsRecyclerView.setAdapter(recentAdapter);

            recentAdapter.setOnItemClickListener(item -> {
                Log.d(TAG, "Recent item clicked: " + item.getTitle());
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToItemDetail(item.getId());
                }
            });
        }
    }

    private void loadData() {
        loadCategories();
        loadItems();
    }

    private void loadCategories() {
        Log.d(TAG, "Loading categories");

        apiService.getAllCategories().enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Category>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        Log.d(TAG, "Categories loaded: " + apiResponse.getData().size());
                        if (categoryAdapter != null) {
                            categoryAdapter.setCategories(apiResponse.getData());
                        }
                    } else {
                        Log.e(TAG, "Categories API error: " + apiResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "Categories API call failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable t) {
                Log.e(TAG, "Categories API call failed: " + t.getMessage());
            }
        });
    }

    private void loadItems() {
        Log.d(TAG, "Loading items");

        apiService.getAllItems().enqueue(new Callback<ApiResponse<List<Item>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Item>>> call, Response<ApiResponse<List<Item>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Item>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        List<Item> items = apiResponse.getData();
                        Log.d(TAG, "Items loaded: " + items.size());

                        // ✅ FIXED: Update adapters safely
                        if (featuredAdapter != null) {
                            featuredAdapter.updateItems(items);
                        }
                        if (recentAdapter != null) {
                            recentAdapter.updateItems(items);
                        }
                    } else {
                        Log.e(TAG, "Items API error: " + apiResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "Items API call failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Item>>> call, Throwable t) {
                Log.e(TAG, "Items API call failed: " + t.getMessage());
            }
        });
    }
}