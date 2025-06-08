package com.example.tradeupsprojecy.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.ui.activities.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.example.tradeupsprojecy.data.local.SessionManager;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;
    private TextView userNameTextView, userEmailTextView;
    private ImageView profileImageView, settingsButton;
    private MaterialButton setupProfileButton;
    private LinearLayout logoutLayout, myListingsLayout,
            purchaseHistoryLayout, savedItemsLayout,
            helpSupportLayout, termsPrivacyLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        try {
            initViews(view);
            initSessionManager();
            setupUserInfo();
            setupClickListeners();
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi, chỉ hiển thị layout cơ bản
        }

        return view;
    }

    private void initViews(View view) {
        userNameTextView = view.findViewById(R.id.userNameTextView);
        userEmailTextView = view.findViewById(R.id.userEmailTextView);
        profileImageView = view.findViewById(R.id.profileImageView);
        settingsButton = view.findViewById(R.id.settingsButton);
        setupProfileButton = view.findViewById(R.id.setupProfileButton);
        logoutLayout = view.findViewById(R.id.logoutLayout);
        myListingsLayout = view.findViewById(R.id.myListingsLayout);
        purchaseHistoryLayout = view.findViewById(R.id.purchaseHistoryLayout);
        savedItemsLayout = view.findViewById(R.id.savedItemsLayout);
        helpSupportLayout = view.findViewById(R.id.helpSupportLayout);
        termsPrivacyLayout = view.findViewById(R.id.termsPrivacyLayout);
    }

    private void initSessionManager() {
        sessionManager = new SessionManager(requireContext());
    }

    private void setupUserInfo() {
        if (sessionManager != null && sessionManager.isLoggedIn()) {
            String userName = sessionManager.getUserName();
            String userEmail = sessionManager.getUserEmail();

            if (userNameTextView != null && !userName.isEmpty()) {
                userNameTextView.setText(userName);
            }

            if (userEmailTextView != null && !userEmail.isEmpty()) {
                userEmailTextView.setText(userEmail);
            }
        }
    }

    private void setupClickListeners() {
        // Settings button
        if (settingsButton != null) {
            settingsButton.setOnClickListener(v -> showMessage("Settings coming soon!"));
        }

        // Setup Profile button
        if (setupProfileButton != null) {
            setupProfileButton.setOnClickListener(v -> showMessage("Profile setup coming soon!"));
        }

        // My Listings
        if (myListingsLayout != null) {
            myListingsLayout.setOnClickListener(v -> showMessage("My listings coming soon!"));
        }

        // Purchase History
        if (purchaseHistoryLayout != null) {
            purchaseHistoryLayout.setOnClickListener(v -> showMessage("Purchase history coming soon!"));
        }

        // Saved Items
        if (savedItemsLayout != null) {
            savedItemsLayout.setOnClickListener(v -> showMessage("Saved items coming soon!"));
        }

        // Help & Support
        if (helpSupportLayout != null) {
            helpSupportLayout.setOnClickListener(v -> showMessage("Help & Support coming soon!"));
        }

        // Terms & Privacy
        if (termsPrivacyLayout != null) {
            termsPrivacyLayout.setOnClickListener(v -> showMessage("Terms & Privacy coming soon!"));
        }

        // Logout
        if (logoutLayout != null) {
            logoutLayout.setOnClickListener(v -> showLogoutDialog());
        }
    }

    private void showLogoutDialog() {
        try {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> performLogout())
                    .setNegativeButton("Cancel", null)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            performLogout(); // Fallback nếu dialog không hiển thị được
        }
    }

    private void performLogout() {
        try {
            // Clear session
            if (sessionManager != null) {
                sessionManager.logout();
            }

            // Show message
            showMessage("Logged out successfully");

            // Navigate to login
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            if (getActivity() != null) {
                getActivity().finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showMessage(String message) {
        try {
            if (getContext() != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}