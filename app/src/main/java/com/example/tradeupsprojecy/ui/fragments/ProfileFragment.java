// app/src/main/java/com/example/tradeupsprojecy/ui/fragments/ProfileFragment.java
package com.example.tradeupsprojecy.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.ui.activities.LoginActivity;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.example.tradeupsprojecy.utils.GoogleSignInHelper;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private SessionManager sessionManager;
    private GoogleSignInHelper googleSignInHelper;

    private TextView userNameTextView;
    private TextView userEmailTextView;
    private LinearLayout logoutLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initServices();
        setupClickListeners();
        loadUserData();
    }

    private void initViews(View view) {
        userNameTextView = view.findViewById(R.id.userNameTextView);
        userEmailTextView = view.findViewById(R.id.userEmailTextView);
        logoutLayout = view.findViewById(R.id.logoutLayout);
    }

    private void initServices() {
        sessionManager = new SessionManager(requireContext());
        googleSignInHelper = new GoogleSignInHelper(requireContext());
    }

    private void setupClickListeners() {
        if (logoutLayout != null) {
            logoutLayout.setOnClickListener(v -> logout());
        }
    }

    private void loadUserData() {
        if (sessionManager.isLoggedIn()) {
            String userName = sessionManager.getUserName();
            String userEmail = sessionManager.getUserEmail();

            if (userNameTextView != null) {
                userNameTextView.setText(userName != null ? userName : "User");
            }

            if (userEmailTextView != null) {
                userEmailTextView.setText(userEmail != null ? userEmail : "email@example.com");
            }
        }
    }

    private void logout() {
        Log.d(TAG, "Logging out user");

        // Sign out from Google if signed in
        if (googleSignInHelper.isSignedIn()) {
            googleSignInHelper.signOut(() -> {
                Log.d(TAG, "Google sign-out completed");
            });
        }

        // Clear session
        sessionManager.logout();

        // Navigate to login
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}