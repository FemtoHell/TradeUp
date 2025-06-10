// app/src/main/java/com/example/tradeupsprojecy/ui/activities/RegisterActivity.java
package com.example.tradeupsprojecy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.api.ApiClient;
import com.example.tradeupsprojecy.data.api.ApiService;
import com.example.tradeupsprojecy.data.models.request.AuthRequest;
import com.example.tradeupsprojecy.data.models.response.AuthResponse;
import com.example.tradeupsprojecy.data.local.SessionManager; // ✅ ĐÚNG IMPORT
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private MaterialButton registerButton;
    private MaterialCheckBox termsCheckBox;
    private ImageView backButton;

    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initServices();
        setupClickListeners();
        setupTextWatchers();
    }

    private void initViews() {
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        termsCheckBox = findViewById(R.id.termsCheckBox);
        backButton = findViewById(R.id.backButton);
    }

    private void initServices() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
    }

    private void setupClickListeners() {
        registerButton.setOnClickListener(v -> performRegister());

        findViewById(R.id.loginTextView).setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        if (termsCheckBox != null) {
            termsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> updateRegisterButtonState());
        }
    }

    private void setupTextWatchers() {
        android.text.TextWatcher textWatcher = new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                updateRegisterButtonState();
            }
        };

        nameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        confirmPasswordEditText.addTextChangedListener(textWatcher);
    }

    private void updateRegisterButtonState() {
        boolean isNameValid = nameEditText.getText().toString().trim().length() > 0;
        boolean isEmailValid = emailEditText.getText().toString().trim().length() > 0;
        boolean isPasswordValid = passwordEditText.getText().toString().trim().length() >= 8;
        boolean isConfirmPasswordValid = confirmPasswordEditText.getText().toString().trim().equals(passwordEditText.getText().toString().trim());
        boolean isTermsAccepted = termsCheckBox != null && termsCheckBox.isChecked();

        registerButton.setEnabled(isNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid && isTermsAccepted);
    }

    private void performRegister() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (!validateInputs(name, email, password, confirmPassword)) return;

        setLoading(true);

        AuthRequest request = new AuthRequest(email, password, name);

        apiService.register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.isSuccess()) {
                        AuthResponse.UserDto user = authResponse.getUser();
                        sessionManager.createLoginSession(
                                String.valueOf(user.getId()),
                                authResponse.getToken(),
                                user.getEmail(),
                                user.getFullName()
                        );

                        showMessage("Registration successful!");
                        navigateToMain();
                    } else {
                        showMessage(authResponse.getMessage());
                    }
                } else {
                    showMessage("Registration failed. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setLoading(false);
                showMessage("Network error: " + t.getMessage());
                Log.e("RegisterActivity", "Registration error", t);
            }
        });
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

        if (password.length() < 8) {
            passwordEditText.setError("Password must be at least 8 characters");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return false;
        }

        return true;
    }

    private void setLoading(boolean loading) {
        registerButton.setEnabled(!loading);
        registerButton.setText(loading ? "Creating Account..." : "Sign Up");
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