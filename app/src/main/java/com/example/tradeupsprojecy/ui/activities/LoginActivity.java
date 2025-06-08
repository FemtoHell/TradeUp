package com.example.tradeupsprojecy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.AuthRequest;
import com.example.tradeupsprojecy.data.models.AuthResponse;
import com.example.tradeupsprojecy.data.models.GoogleAuthRequest;
import com.example.tradeupsprojecy.data.network.ApiService;
import com.example.tradeupsprojecy.data.network.NetworkClient;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.tradeupsprojecy.data.local.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";

    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton loginButton, googleSignInButton;
    private TextView signUpTextView, forgotPasswordTextView;

    private SessionManager sessionManager;
    private GoogleSignInClient googleSignInClient;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initServices();
        setupGoogleSignIn();
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

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> performLogin());
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
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
        AuthRequest request = new AuthRequest(email, password);

        Log.d(TAG, "Attempting login for: " + email);

        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setLoading(false);

                Log.d(TAG, "Login response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "Login success: " + authResponse.isSuccess());

                    if (authResponse.isSuccess()) {
                        // Save session
                        AuthResponse.UserDto user = authResponse.getUser();
                        sessionManager.createLoginSession(
                                String.valueOf(user.getId()),
                                authResponse.getToken(),
                                user.getEmail(),
                                user.getFullName()
                        );

                        showMessage("Login successful!");
                        navigateToMain();
                    } else {
                        showMessage(authResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "Login failed with code: " + response.code());
                    showMessage("Login failed. Please check your credentials.");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setLoading(false);
                Log.e(TAG, "Login network error: " + t.getMessage());
                showMessage("Network error. Please check your connection.");
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleGoogleSignInSuccess(account);
            } catch (ApiException e) {
                Log.e(TAG, "Google sign in failed: " + e.getStatusCode());
                showMessage("Google sign in failed: " + e.getMessage());
            }
        }
    }

    private void handleGoogleSignInSuccess(GoogleSignInAccount account) {
        setLoading(true);

        GoogleAuthRequest request = new GoogleAuthRequest();
        request.setIdToken(account.getIdToken());
        request.setEmail(account.getEmail());
        request.setFullName(account.getDisplayName());
        if (account.getPhotoUrl() != null) {
            request.setProfileImageUrl(account.getPhotoUrl().toString());
        }

        Log.d(TAG, "Google login for: " + account.getEmail());

        apiService.googleLogin(request).enqueue(new Callback<AuthResponse>() {
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

                        showMessage("Google login successful!");
                        navigateToMain();
                    } else {
                        showMessage(authResponse.getMessage());
                    }
                } else {
                    Log.e(TAG, "Google login failed with code: " + response.code());
                    showMessage("Google login failed. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setLoading(false);
                Log.e(TAG, "Google login network error: " + t.getMessage());
                showMessage("Network error: " + t.getMessage());
            }
        });
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
        googleSignInButton.setEnabled(!loading);
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