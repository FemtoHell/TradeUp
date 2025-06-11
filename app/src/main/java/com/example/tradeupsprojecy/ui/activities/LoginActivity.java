// app/src/main/java/com/example/tradeupsprojecy/ui/activities/LoginActivity.java
package com.example.tradeupsprojecy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.*;
import com.example.tradeupsprojecy.data.api.ApiClient;
import com.example.tradeupsprojecy.data.network.ApiService;
import com.example.tradeupsprojecy.data.models.request.AuthRequest;
import com.example.tradeupsprojecy.data.models.response.AuthResponse;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.example.tradeupsprojecy.utils.GoogleSignInHelper;
import com.example.tradeupsprojecy.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
// ✅ FIXED: Import GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    // Views
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private MaterialButton googleSignInButton;
    private TextView signUpTextView;
    private TextView forgotPasswordTextView;

    // Services
    private ApiService apiService;
    private SessionManager sessionManager;
    private GoogleSignInHelper googleSignInHelper;

    // Google Sign-In launcher
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "LoginActivity started");

        initGoogleSignInLauncher();
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

    private void initGoogleSignInLauncher() {
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d(TAG, "Google Sign-In result received");
                    googleSignInHelper.handleSignInResult(result, new GoogleSignInHelper.GoogleSignInCallback() {
                        @Override
                        public void onResult(boolean success, String message, GoogleSignInAccount account) {
                            setLoading(false);

                            if (success && account != null) {
                                Log.d(TAG, "Google Sign-In successful: " + account.getEmail());

                                // Validate required data
                                if (account.getEmail() == null || account.getEmail().isEmpty()) {
                                    showMessage("Google account missing email address");
                                    return;
                                }

                                // Create request for backend
                                GoogleAuthRequest googleAuthRequest = new GoogleAuthRequest();
                                googleAuthRequest.setEmail(account.getEmail());
                                googleAuthRequest.setFullName(account.getDisplayName() != null ?
                                        account.getDisplayName() : "Google User");
                                googleAuthRequest.setProfileImageUrl(account.getPhotoUrl() != null ?
                                        account.getPhotoUrl().toString() : null);
                                googleAuthRequest.setIdToken(account.getIdToken());

                                performGoogleLogin(googleAuthRequest);
                            } else {
                                Log.e(TAG, "Google Sign-In failed: " + message);
                                showMessage(message != null ? message : "Google Sign-In failed");
                            }
                        }
                    });
                }
        );
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
        if (googleSignInButton == null) Log.e(TAG, "googleSignInButton not found!");

        Log.d(TAG, "Views initialized successfully");
    }

    private void initServices() {
        Log.d(TAG, "Initializing services");

        try {
            apiService = ApiClient.getApiService();
            sessionManager = new SessionManager(this);
            googleSignInHelper = new GoogleSignInHelper(this);
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

        if (googleSignInButton != null) {
            googleSignInButton.setOnClickListener(v -> {
                Log.d(TAG, "Google sign in button clicked");
                performGoogleSignIn();
            });
        }

        if (signUpTextView != null) {
            signUpTextView.setOnClickListener(v -> {
                Log.d(TAG, "Sign up text clicked");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
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

        boolean isEmailValid = !email.isEmpty();
        boolean isPasswordValid = !password.isEmpty();
        boolean shouldEnable = isEmailValid && isPasswordValid;

        loginButton.setEnabled(shouldEnable);

        Log.d(TAG, "Login button state updated - enabled: " + shouldEnable);
    }

    private void performLogin() {
        Log.d(TAG, "Starting login process");

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.d(TAG, "Login attempt - Email: " + email + ", Password length: " + password.length());

        // Validate inputs
        String emailError = ValidationUtils.getEmailError(email);
        if (emailError != null) {
            emailEditText.setError(emailError);
            emailEditText.requestFocus();
            return;
        }

        String passwordError = ValidationUtils.getPasswordError(password);
        if (passwordError != null) {
            passwordEditText.setError(passwordError);
            passwordEditText.requestFocus();
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

                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "Response success: " + authResponse.isSuccess());

                    if (authResponse.isSuccess()) {
                        handleLoginSuccess(authResponse);
                    } else {
                        showMessage(authResponse.getMessage() != null ?
                                authResponse.getMessage() : "Login failed");
                    }
                } else {
                    Log.e(TAG, "Login API call failed - Response code: " + response.code());
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Login API call failed: " + t.getMessage());
                setLoading(false);
                showMessage("Network error: " + t.getMessage());
            }
        });
    }

    private void performGoogleSignIn() {
        Log.d(TAG, "Starting Google Sign-In");

        try {
            setLoading(true);
            Intent signInIntent = googleSignInHelper.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        } catch (Exception e) {
            Log.e(TAG, "Error starting Google Sign-In: " + e.getMessage());
            setLoading(false);
            showMessage("Google Sign-In not available: " + e.getMessage());
        }
    }

    private void performGoogleLogin(GoogleAuthRequest request) {
        Log.d(TAG, "Performing Google login with backend");
        Log.d(TAG, "Google email: " + request.getEmail());
        Log.d(TAG, "Google name: " + request.getFullName());

        apiService.googleLogin(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d(TAG, "Google login API response received");
                Log.d(TAG, "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "Google login response - Success: " + authResponse.isSuccess());

                    if (authResponse.isSuccess()) {
                        handleLoginSuccess(authResponse);
                    } else {
                        showMessage(authResponse.getMessage() != null ?
                                authResponse.getMessage() : "Google login failed");
                    }
                } else {
                    Log.e(TAG, "Google login API call failed - Response code: " + response.code());
                    handleApiError(response);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Google login API call failed: " + t.getMessage());
                showMessage("Network error: " + t.getMessage());
            }
        });
    }

    private void handleLoginSuccess(AuthResponse authResponse) {
        Log.d(TAG, "Login successful, saving session");

        AuthResponse.UserDto user = authResponse.getUser();
        if (user != null) {
            Log.d(TAG, "User data - ID: " + user.getId() + ", Email: " + user.getEmail() + ", Name: " + user.getFullName());

            sessionManager.createLoginSession(
                    String.valueOf(user.getId()),
                    authResponse.getToken() != null ? authResponse.getToken() : "",
                    user.getEmail() != null ? user.getEmail() : "",
                    user.getFullName() != null ? user.getFullName() : ""
            );

            showMessage("Login successful!");
            navigateToMain();
        } else {
            Log.e(TAG, "User data is null in response");
            showMessage("Login failed: User data missing");
        }
    }

    private void handleApiError(Response<AuthResponse> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : null;
            Log.e(TAG, "Error response body: " + errorBody);

            switch (response.code()) {
                case 400:
                    showMessage("Invalid credentials");
                    break;
                case 401:
                    showMessage("Email or password incorrect");
                    break;
                case 404:
                    showMessage("Service not found");
                    break;
                case 500:
                    showMessage("Server error. Please try again later");
                    break;
                default:
                    showMessage("Login failed. Error: " + response.code());
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error reading error body: " + e.getMessage());
            showMessage("Login failed. Please try again");
        }
    }

    private void setLoading(boolean loading) {
        Log.d(TAG, "Setting loading state: " + loading);

        runOnUiThread(() -> {
            loginButton.setEnabled(!loading);
            loginButton.setText(loading ? "Signing in..." : "Login");

            emailEditText.setEnabled(!loading);
            passwordEditText.setEnabled(!loading);

            if (googleSignInButton != null) {
                googleSignInButton.setEnabled(!loading);
            }
        });
    }

    private void showMessage(String message) {
        Log.d(TAG, "Showing message: " + message);
        runOnUiThread(() -> Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show());
    }

    private void navigateToMain() {
        Log.d(TAG, "Navigating to MainActivity");

        try {
            Intent intent = new Intent(this, MainActivity.class);
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
        Log.d(TAG, "LoginActivity destroyed");
    }
}