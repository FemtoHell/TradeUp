package com.example.tradeupsprojecy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.example.tradeupsprojecy.models.*;
import com.example.tradeupsprojecy.network.*;
import com.example.tradeupsprojecy.utils.SessionManager;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private MaterialCheckBox termsCheckBox;
    private MaterialButton registerButton, googleSignUpButton;
    private TextView loginTextView;
    private ImageView backButton;

    private SessionManager sessionManager;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initServices();
        setupListeners();
    }

    private void initViews() {
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        termsCheckBox = findViewById(R.id.termsCheckBox);
        registerButton = findViewById(R.id.registerButton);
        googleSignUpButton = findViewById(R.id.googleSignUpButton);
        loginTextView = findViewById(R.id.loginTextView);
        backButton = findViewById(R.id.backButton);

        // Hide Google Sign-In for now
        googleSignUpButton.setVisibility(View.GONE);
        findViewById(R.id.orTextView).setVisibility(View.GONE);

        // Enable register button when all conditions are met
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

        nameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        confirmPasswordEditText.addTextChangedListener(textWatcher);
        termsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> validateForm());
    }

    private void initServices() {
        sessionManager = new SessionManager(this);
        apiService = NetworkClient.getApiService();
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());
        registerButton.setOnClickListener(v -> performRegister());
        loginTextView.setOnClickListener(v -> finish());
    }

    private void validateForm() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        boolean isValid = !name.isEmpty() &&
                !email.isEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !password.isEmpty() &&
                password.length() >= 8 &&
                !confirmPassword.isEmpty() &&
                password.equals(confirmPassword) &&
                termsCheckBox.isChecked();

        registerButton.setEnabled(isValid);
    }

    private void performRegister() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (!validateInputs(name, email, password, confirmPassword)) return;

        setLoading(true);

        // For demo purposes, simulate successful registration
        simulateRegistration(name, email, password);
    }

    private void simulateRegistration(String name, String email, String password) {
        // Simulate network delay
        registerButton.postDelayed(() -> {
            setLoading(false);

            // Save demo session
            sessionManager.saveAuthToken("demo_token_123");
            sessionManager.saveUserDetails(email, name);

            showMessage("Registration successful!");
            navigateToMain();
        }, 1500);
    }

    private boolean validateInputs(String name, String email, String password, String confirmPassword) {
        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            return false;
        }

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

        if (password.length() < 8) {
            passwordEditText.setError("Password must be at least 8 characters");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords don't match");
            return false;
        }

        if (!termsCheckBox.isChecked()) {
            showMessage("Please accept the terms and conditions");
            return false;
        }

        return true;
    }

    private void setLoading(boolean loading) {
        registerButton.setEnabled(!loading);
        registerButton.setText(loading ? "Creating account..." : "Sign Up");
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToMain() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}