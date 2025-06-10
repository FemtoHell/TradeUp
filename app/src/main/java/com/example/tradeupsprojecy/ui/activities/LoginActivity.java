// app/src/main/java/com/example/tradeupsprojecy/ui/activities/LoginActivity.java
package com.example.tradeupsprojecy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.api.ApiClient;
import com.example.tradeupsprojecy.data.api.ApiService;
import com.example.tradeupsprojecy.data.models.request.AuthRequest;
import com.example.tradeupsprojecy.data.models.response.AuthResponse;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Views
    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton loginButton, googleSignInButton;
    private TextView signUpTextView, forgotPasswordTextView;

    // Services
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "LoginActivity started");

        initViews();
        initServices();
        setupClickListeners();
        setupTextWatchers();

        // Check if already logged in
        if (sessionManager.isLoggedIn()) {
            Log.d(TAG, "User already logged in, navigating to MainActivity");
            navigateToMain();
        }
    }

    private void initViews() {
        Log.d(TAG, "Initializing views");

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        signUpTextView = findViewById(R.id.signUpTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);

        // Verify views are found
        if (emailEditText == null) Log.e(TAG, "emailEditText not found!");
        if (passwordEditText == null) Log.e(TAG, "passwordEditText not found!");
        if (loginButton == null) Log.e(TAG, "loginButton not found!");

        Log.d(TAG, "Views initialized successfully");
    }

    private void initServices() {
        Log.d(TAG, "Initializing services");

        try {
            apiService = ApiClient.getApiService();
            sessionManager = new SessionManager(this);
            Log.d(TAG, "Services initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing services: " + e.getMessage());
            showMessage("Initialization error: " + e.getMessage());
        }
    }

    private void setupClickListeners() {
        Log.d(TAG, "Setting up click listeners");

        loginButton.setOnClickListener(v -> {
            Log.d(TAG, "Login button clicked");
            performLogin();
        });

        if (signUpTextView != null) {
            signUpTextView.setOnClickListener(v -> {
                Log.d(TAG, "Sign up text clicked");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            });
        }

        if (googleSignInButton != null) {
            googleSignInButton.setOnClickListener(v -> {
                Log.d(TAG, "Google sign in clicked");
                showMessage("Google Sign In - Coming Soon!");
            });
        }

        if (forgotPasswordTextView != null) {
            forgotPasswordTextView.setOnClickListener(v -> {
                Log.d(TAG, "Forgot password clicked");
                showMessage("Forgot Password - Coming Soon!");
            });
        }

        Log.d(TAG, "Click listeners setup completed");
    }

    private void setupTextWatchers() {
        Log.d(TAG, "Setting up text watchers");

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateLoginButtonState();
            }
        };

        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);

        // Initial button state
        updateLoginButtonState();

        Log.d(TAG, "Text watchers setup completed");
    }

    private void updateLoginButtonState() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        boolean isEmailValid = email.length() > 0;
        boolean isPasswordValid = password.length() > 0;
        boolean shouldEnable = isEmailValid && isPasswordValid;

        loginButton.setEnabled(shouldEnable);

        Log.d(TAG, "Login button state updated - enabled: " + shouldEnable);
    }

    private void performLogin() {
        Log.d(TAG, "Starting login process");

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.d(TAG, "Login attempt - Email: " + email + ", Password length: " + password.length());

        if (!validateInputs(email, password)) {
            Log.w(TAG, "Input validation failed");
            return;
        }

        setLoading(true);

        AuthRequest request = new AuthRequest(email, password);
        Log.d(TAG, "Making API call to login endpoint");

        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d(TAG, "Login API response received");
                Log.d(TAG, "Response code: " + response.code());
                Log.d(TAG, "Response successful: " + response.isSuccessful());

                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "Response body received - Success: " + authResponse.isSuccess());
                    Log.d(TAG, "Response message: " + authResponse.getMessage());

                    if (authResponse.isSuccess()) {
                        Log.d(TAG, "Login successful, saving session");

                        // Save session
                        AuthResponse.UserDto user = authResponse.getUser();
                        if (user != null) {
                            Log.d(TAG, "User data - ID: " + user.getId() + ", Email: " + user.getEmail() + ", Name: " + user.getFullName());

                            sessionManager.createLoginSession(
                                    String.valueOf(user.getId()),
                                    authResponse.getToken(),
                                    user.getEmail(),
                                    user.getFullName()
                            );

                            Log.d(TAG, "Session saved successfully");
                            showMessage("Login successful!");
                            navigateToMain();
                        } else {
                            Log.e(TAG, "User data is null in response");
                            showMessage("Login failed: User data missing");
                        }
                    } else {
                        Log.w(TAG, "Login failed: " + authResponse.getMessage());
                        showMessage(authResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "Login API call failed - Response code: " + response.code());

                    // Try to get error details
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error response body: " + errorBody);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error reading error body: " + e.getMessage());
                    }

                    showMessage("Login failed. Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Login API call failed with exception: " + t.getMessage());
                Log.e(TAG, "Exception type: " + t.getClass().getSimpleName());

                if (t.getCause() != null) {
                    Log.e(TAG, "Exception cause: " + t.getCause().getMessage());
                }

                setLoading(false);
                showMessage("Network error: " + t.getMessage());
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        Log.d(TAG, "Validating inputs");

        if (email.isEmpty()) {
            Log.w(TAG, "Email is empty");
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.w(TAG, "Email format is invalid");
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            Log.w(TAG, "Password is empty");
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            Log.w(TAG, "Password too short");
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return false;
        }

        Log.d(TAG, "Input validation passed");
        return true;
    }

    private void setLoading(boolean loading) {
        Log.d(TAG, "Setting loading state: " + loading);

        runOnUiThread(() -> {
            loginButton.setEnabled(!loading);
            loginButton.setText(loading ? "Signing in..." : "Login");

            // Disable other interactive elements during loading
            emailEditText.setEnabled(!loading);
            passwordEditText.setEnabled(!loading);

            if (googleSignInButton != null) {
                googleSignInButton.setEnabled(!loading);
            }

            if (signUpTextView != null) {
                signUpTextView.setEnabled(!loading);
            }
        });
    }

    private void showMessage(String message) {
        Log.d(TAG, "Showing message: " + message);
        runOnUiThread(() -> {
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
        });
    }

    private void navigateToMain() {
        Log.d(TAG, "Navigating to MainActivity");

        try {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            Log.d(TAG, "Navigation to MainActivity completed");
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to MainActivity: " + e.getMessage());
            showMessage("Navigation error: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "LoginActivity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "LoginActivity paused");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "LoginActivity destroyed");
    }
}