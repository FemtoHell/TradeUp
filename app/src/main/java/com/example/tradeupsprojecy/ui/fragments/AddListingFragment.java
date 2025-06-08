package com.example.tradeupsprojecy.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.example.tradeupsprojecy.R;
import com.google.android.material.button.MaterialButton;

public class AddListingFragment extends Fragment {

    private MaterialButton previewButton, postButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_listing, container, false);

        initViews(view);
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        previewButton = view.findViewById(R.id.previewButton);
        postButton = view.findViewById(R.id.postButton);
    }

    private void setupClickListeners() {
        if (previewButton != null) {
            previewButton.setOnClickListener(v ->
                    showMessage("Preview feature coming soon!"));
        }

        if (postButton != null) {
            postButton.setOnClickListener(v ->
                    showMessage("Post listing feature coming soon!"));
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}