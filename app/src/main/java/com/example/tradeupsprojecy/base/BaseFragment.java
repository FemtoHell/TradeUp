package com.example.tradeupsprojecy.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.example.tradeupsprojecy.ui.activities.LoginActivity;
import com.example.tradeupsprojecy.utils.Constants;

public abstract class BaseFragment extends Fragment {

    private ProgressDialog progressDialog;
    protected SessionManager sessionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            sessionManager = new SessionManager(getActivity());
            initProgressDialog();
        }
    }

    private void initProgressDialog() {
        if (getActivity() != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Đang tải...");
            progressDialog.setCancelable(false);
        }
    }

    // Show loading dialog
    protected void showLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    protected void showLoading(String message) {
        if (progressDialog != null) {
            progressDialog.setMessage(message);
            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    // Hide loading dialog
    protected void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    // Show toast messages
    protected void showToast(String message) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showLongToast(String message) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }

    // Check network connection
    protected boolean isNetworkAvailable() {
        if (getActivity() != null) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    // Handle network error
    protected void handleNetworkError() {
        if (!isNetworkAvailable()) {
            showToast(Constants.ERROR_NETWORK);
        } else {
            showToast(Constants.ERROR_SERVER);
        }
    }

    // Handle unauthorized error
    protected void handleUnauthorizedError() {
        showToast(Constants.ERROR_UNAUTHORIZED);
        if (sessionManager != null) {
            sessionManager.logout();
        }
        if (getActivity() != null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }
    }

    // Check if user is logged in
    protected boolean isUserLoggedIn() {
        return sessionManager != null && sessionManager.isLoggedIn();
    }

    // Get current user token
    protected String getUserToken() {
        return sessionManager != null ? sessionManager.getToken() : null;
    }

    // Get current user ID
    protected String getUserId() {
        return sessionManager != null ? sessionManager.getUserId() : null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}