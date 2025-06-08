package com.example.tradeupsprojecy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;

import com.example.tradeupsprojecy.R;

public class MessagesFragment extends Fragment {

    private LinearLayout emptyStateLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        initViews(view);
        showEmptyState();

        return view;
    }

    private void initViews(View view) {
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
    }

    private void showEmptyState() {
        if (emptyStateLayout != null) {
            emptyStateLayout.setVisibility(View.VISIBLE);
        }
    }
}