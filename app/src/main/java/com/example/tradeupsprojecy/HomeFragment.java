package com.example.tradeupsprojecy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Add click listener to search bar
        View searchLayout = view.findViewById(R.id.searchLayout);
        if (searchLayout != null) {
            searchLayout.setOnClickListener(v -> {
                // Navigate to search fragment
                if (getActivity() != null) {
                    // You can add navigation logic here
                }
            });
        }

        return view;
    }
}