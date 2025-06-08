package com.example.tradeupsprojecy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.ui.activities.MainActivity;

public class HomeFragment extends Fragment {

    private View searchLayout;
    private TextView seeAllCategoriesTextView, seeAllRecentTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        searchLayout = view.findViewById(R.id.searchLayout);
        seeAllCategoriesTextView = view.findViewById(R.id.seeAllCategoriesTextView);
        seeAllRecentTextView = view.findViewById(R.id.seeAllRecentTextView);
    }

    private void setupClickListeners() {
        // Search bar click
        if (searchLayout != null) {
            searchLayout.setOnClickListener(v -> {
                // Navigate to search fragment
                if (getActivity() != null) {
                    ((MainActivity) getActivity()).navigateToSearch();
                }
            });
        }

        // See all categories
        if (seeAllCategoriesTextView != null) {
            seeAllCategoriesTextView.setOnClickListener(v ->
                    showMessage("Categories page coming soon!"));
        }

        // See all recent listings
        if (seeAllRecentTextView != null) {
            seeAllRecentTextView.setOnClickListener(v ->
                    showMessage("All listings page coming soon!"));
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}