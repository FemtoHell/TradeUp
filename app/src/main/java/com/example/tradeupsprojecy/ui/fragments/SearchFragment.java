package com.example.tradeupsprojecy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.tradeupsprojecy.R;

public class SearchFragment extends Fragment {

    private SearchView searchView;
    private ImageView filterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initViews(view);
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        searchView = view.findViewById(R.id.searchView);
        filterButton = view.findViewById(R.id.filterButton);
    }

    private void setupClickListeners() {
        if (filterButton != null) {
            filterButton.setOnClickListener(v ->
                    showMessage("Filter feature coming soon!"));
        }

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    showMessage("Searching for: " + query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}