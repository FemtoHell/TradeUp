// app/src/main/java/com/example/tradeupsprojecy/ui/activities/RegisterActivity.java (tiếp tục)
package com.example.tradeupsprojecy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.api.ApiClient;
import com.example.tradeupsprojecy.data.network.ApiService;
import com.example.tradeupsprojecy.data.models.request.AuthRequest;
import com.example.tradeupsprojecy.data.models.response.AuthResponse;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.example.tradeupsprojecy.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private TextInputEditText nameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText confirmPasswordEditText;
    private MaterialButton registerButton;
    private MaterialCheckBox termsCheckBox;
    private ImageView backButton;
    private TextView loginTextView;

    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.d(TAG, "RegisterActivity started");

        initViews();
        initServices();
        setupClickListeners();
        setupTextWatchers();
    }

    private void initViews() {
        Log.d(TAG, "Initializing views");

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        termsCheckBox = findViewById(R.id.termsCheckBox);
        backButton = findViewById(R.id.backButton);
        loginTextView = findViewById(R.id.loginTextView);

        Log.d(TAG, "Views initialized successfully");
    }

    private void initServices() {
        Log.d(TAG, "Initializing services");
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
    }

    private void setupClickListeners() {
        Log.d(TAG, "Setting up click listeners");

        registerButton.setOnClickListener(v -> performRegister());

        if (loginTextView != null) {
            loginTextView.setOnClickListener(v -> {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
        }

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        if (termsCheckBox != null) {
            termsCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> updateRegisterButtonState());
        }
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
                updateRegisterButtonState();
            }
        };

        nameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        confirmPasswordEditText.addTextChangedListener(textWatcher);

        // Initial button state
        updateRegisterButtonState();
    }

    private void updateRegisterButtonState() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        boolean isNameValid = !name.isEmpty();
        boolean isEmailValid = !email.isEmpty();
        boolean isPasswordValid = password.length() >= 6;
        boolean isConfirmPasswordValid = password.equals(confirmPassword) && !confirmPassword.isEmpty();
        boolean isTermsAccepted = termsCheckBox != null && termsCheckBox.isChecked();

        boolean shouldEnable = isNameValid && isEmailValid && isPasswordValid && isConfirmPasswordValid && isTermsAccepted;
        registerButton.setEnabled(shouldEnable);

        Log.d(TAG, "Register button state updated - enabled: " + shouldEnable);
    }

    private void performRegister() {
        Log.d(TAG, "Starting registration process");

        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        Log.d(TAG, "Register attempt - Name: " + name + ", Email: " + email);

        if (!validateInputs(name, email, password, confirmPassword)) {
            return;
        }

        setLoading(true);

        AuthRequest request = new AuthRequest(email, password, name);

        apiService.register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d(TAG, "Register API response received");
                Log.d(TAG, "Response code: " + response.code());

                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "Register response - Success: " + authResponse.isSuccess());

                    if (authResponse.isSuccess()) {
                        handleRegisterSuccess(authResponse);
                    } else {
                        showMessage(authResponse.getMessage() != null ?
                                authResponse.getMessage() : "Registration failed");
                    }
                } else {
                    Log.e(TAG, "Register API call failed - Response code: " + response.code());
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Register API call failed: " + t.getMessage());
                setLoading(false);
                showMessage("Network error: " + t.getMessage());
            }
        });
    }

    private boolean validateInputs(String name, String email, String password, String confirmPassword) {
        Log.d(TAG, "Validating inputs");

        // Validate name
        String nameError = ValidationUtils.getFullNameError(name);
        if (nameError != null) {
            nameEditText.setError(nameError);
            nameEditText.requestFocus();
            return false;
        }

        // Validate email
        String emailError = ValidationUtils.getEmailError(email);
        if (emailError != null) {
            emailEditText.setError(emailError);
            emailEditText.requestFocus();
            return false;
        }

        // Validate password
        String passwordError = ValidationUtils.getPasswordError(password);
        if (passwordError != null) {
            passwordEditText.setError(passwordError);
            passwordEditText.requestFocus();
            return false;
        }

        // Validate confirm password
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Mật khẩu xác nhận không khớp");
            confirmPasswordEditText.requestFocus();
            return false;
        }

        Log.d(TAG, "Input validation passed");
        return true;
    }

    private void handleRegisterSuccess(AuthResponse authResponse) {
        Log.d(TAG, "Registration successful, saving session");

        AuthResponse.UserDto user = authResponse.getUser();
        if (user != null) {
            sessionManager.createLoginSession(
                    String.valueOf(user.getId()),
                    authResponse.getToken() != null ? authResponse.getToken() : "",
                    user.getEmail() != null ? user.getEmail() : "",
                    user.getFullName() != null ? user.getFullName() : ""
            );

            showMessage("Registration successful!");
            navigateToMain();
        } else {
            showMessage("Registration failed: User data missing");
        }
    }

    private void handleApiError(Response<AuthResponse> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
            Log.e(TAG, "Error response body: " + errorBody);

            switch (response.code()) {
                case 400:
                    showMessage("Invalid registration data");
                    break;
                case 409:
                    showMessage("Email already exists");
                    break;
                case 500:
                    showMessage("Server error. Please try again later");
                    break;
                default:
                    showMessage("Registration failed. Error: " + response.code());
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading error body: " + e.getMessage());
            showMessage("Registration failed. Please try again");
        }
    }

    private void setLoading(boolean loading) {
        Log.d(TAG, "Setting loading state: " + loading);

        runOnUiThread(() -> {
            registerButton.setEnabled(!loading);
            registerButton.setText(loading ? "Creating Account..." : "Sign Up");

            nameEditText.setEnabled(!loading);
            emailEditText.setEnabled(!loading);
            passwordEditText.setEnabled(!loading);
            confirmPasswordEditText.setEnabled(!loading);

            if (termsCheckBox != null) {
                termsCheckBox.setEnabled(!loading);
            }
        });
    }

    private void showMessage(String message) {
        Log.d(TAG, "Showing message: " + message);
        runOnUiThread(() -> Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show());
    }

    private void navigateToMain() {
        Log.d(TAG, "Navigating to MainActivity");

        try {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to MainActivity: " + e.getMessage());
            showMessage("Navigation error: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "RegisterActivity destroyed");
    }
}