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
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.local.FavoriteItem;
import com.example.tradeupsprojecy.data.local.FavoritesDatabase;
import com.example.tradeupsprojecy.data.models.Item;
import com.example.tradeupsprojecy.ui.activities.ItemDetailActivity;
import com.example.tradeupsprojecy.ui.adapters.FavoritesAdapter;
import com.google.android.material.textview.MaterialTextView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements FavoritesAdapter.OnFavoriteItemClickListener {

    private RecyclerView favoritesRecyclerView;
    private FavoritesAdapter favoritesAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialTextView emptyStateText;
    private FavoritesDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupSwipeRefresh();
        loadFavorites();
    }

    private void initViews(View view) {
        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        emptyStateText = view.findViewById(R.id.emptyStateText);
        database = FavoritesDatabase.getDatabase(getContext());
    }

    private void setupRecyclerView() {
        favoritesAdapter = new FavoritesAdapter(getContext());
        favoritesAdapter.setOnFavoriteItemClickListener(this);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        favoritesRecyclerView.setLayoutManager(layoutManager);
        favoritesRecyclerView.setAdapter(favoritesAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this::loadFavorites);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
    }

    private void loadFavorites() {
        swipeRefreshLayout.setRefreshing(true);

        database.favoriteDao().getAllFavorites().observe(this, new Observer<List<FavoriteItem>>() {
            @Override
            public void onChanged(List<FavoriteItem> favoriteItems) {
                swipeRefreshLayout.setRefreshing(false);
                if (favoriteItems != null) {
                    favoritesAdapter.setFavorites(favoriteItems);
                    checkEmptyState(favoriteItems.isEmpty());
                } else {
                    checkEmptyState(true);
                }
            }
        });
    }

    private void checkEmptyState(boolean isEmpty) {
        if (emptyStateText != null) {
            emptyStateText.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            favoritesRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(FavoriteItem favoriteItem) {
        // Convert FavoriteItem to Item and navigate to detail
        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
        intent.putExtra("item_id", favoriteItem.getItemId());
        startActivity(intent);
    }

    @Override
    public void onRemoveFromFavorites(FavoriteItem favoriteItem) {
        // Remove from database
        new Thread(() -> {
            database.favoriteDao().removeFavorite(favoriteItem.getItemId());
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}