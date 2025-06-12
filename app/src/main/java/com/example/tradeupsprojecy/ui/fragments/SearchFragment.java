// app/src/main/java/com/example/tradeupsprojecy/ui/fragments/SearchFragment.java
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
import androidx.recyclerview.widget.RecyclerView;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Listing;
import com.example.tradeupsprojecy.data.repository.ItemRepository;
import com.example.tradeupsprojecy.ui.activities.ItemDetailActivity;
import com.example.tradeupsprojecy.ui.adapters.ListingAdapter;
import com.example.tradeupsprojecy.ui.adapters.OnListingClickListener;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class SearchFragment extends Fragment implements OnListingClickListener {

    private TextInputEditText searchEditText;
    private RecyclerView searchResultsRecyclerView;
    private ListingAdapter searchAdapter;
    private ItemRepository itemRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        itemRepository = new ItemRepository();
    }

    private void initViews(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView);
    }

    private void setupRecyclerView() {
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        searchAdapter = new ListingAdapter(getContext());
        searchAdapter.setOnListingClickListener(this);
        searchResultsRecyclerView.setAdapter(searchAdapter);
    }

    @Override
    public void onListingClick(Listing listing) {
        Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
        intent.putExtra("item_id", listing.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Listing listing, int position) {
        Toast.makeText(getContext(), "Favorite feature coming soon!", Toast.LENGTH_SHORT).show();
    }
}