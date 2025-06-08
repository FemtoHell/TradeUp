package com.example.tradeupsprojecy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.tradeupsprojecy.models.*;
import com.example.tradeupsprojecy.network.*;
import com.example.tradeupsprojecy.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton loginButton, googleSignInButton;
    private TextView signUpTextView, forgotPasswordTextView;

    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initServices();
        setupListeners();

        // Check if already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToMain();
        }
    }

    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        signUpTextView = findViewById(R.id.signUpTextView);
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);

        // Hide Google Sign-In for now
        googleSignInButton.setVisibility(View.GONE);
        findViewById(R.id.orTextView).setVisibility(View.GONE);

        // Enable login button when both fields are filled
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateForm();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
    }

    private void initServices() {
        sessionManager = new SessionManager(this);
        apiService = NetworkClient.getApiService();
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> performLogin());
        signUpTextView.setOnClickListener(v -> navigateToRegister());
        forgotPasswordTextView.setOnClickListener(v -> handleForgotPassword());
    }

    private void validateForm() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        boolean isValid = !email.isEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !password.isEmpty() &&
                password.length() >= 6;

        loginButton.setEnabled(isValid);
    }

    private void performLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!validateInputs(email, password)) return;

        setLoading(true);

        // For demo purposes, simulate successful login
        // Replace this with actual API call when backend is ready
        simulateLogin(email, password);
    }

    private void simulateLogin(String email, String password) {
        // Simulate network delay
        loginButton.postDelayed(() -> {
            setLoading(false);

            // Save demo session
            sessionManager.saveAuthToken("demo_token_123");
            sessionManager.saveUserDetails(email, "Demo User");

            showMessage("Login successful!");
            navigateToMain();
        }, 1500);
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    private void setLoading(boolean loading) {
        loginButton.setEnabled(!loading);
        loginButton.setText(loading ? "Logging in..." : "Login");
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void handleForgotPassword() {
        showMessage("Forgot password feature coming soon!");
    }
}