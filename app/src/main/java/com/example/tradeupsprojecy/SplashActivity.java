package com.example.tradeupsprojecy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tradeupsprojecy.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Check authentication status after delay
        new Handler(Looper.getMainLooper()).postDelayed(this::checkAuthenticationStatus, SPLASH_DELAY);
    }

    private void checkAuthenticationStatus() {
        SessionManager sessionManager = new SessionManager(this);

        Intent intent;
        if (sessionManager.isLoggedIn()) {
            // User is logged in, go to main activity
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            // User is not logged in, go to login activity
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }
}