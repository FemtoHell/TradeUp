package com.example.tradeupsprojecy.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.example.tradeupsprojecy.ui.activities.LoginActivity;
import com.example.tradeupsprojecy.utils.Constants;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    protected SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);
        initProgressDialog();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tải...");
        progressDialog.setCancelable(false);
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // Check network connection
    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Handle network error
    protected void handleNetworkError() {
        if (!isNetworkAvailable()) {
            showToast(Constants.ERROR_NETWORK);
        } else {
            showToast(Constants.ERROR_SERVER);
        }
    }

    // Handle unauthorized error (token expired)
    protected void handleUnauthorizedError() {
        showToast(Constants.ERROR_UNAUTHORIZED);
        sessionManager.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Check if user is logged in
    protected boolean isUserLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    // Get current user token
    protected String getUserToken() {
        return sessionManager.getToken();
    }

    // Get current user ID
    protected String getUserId() {
        return sessionManager.getUserId();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}