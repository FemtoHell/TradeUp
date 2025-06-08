package com.example.tradeupsprojecy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.example.tradeupsprojecy.models.*;
import com.example.tradeupsprojecy.network.*;
import com.example.tradeupsprojecy.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;

    private TextInputEditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private MaterialCheckBox termsCheckBox;
    private MaterialButton registerButton, googleSignUpButton;
    private TextView loginTextView;
    private ImageView backButton;

    private SessionManager sessionManager;
    private GoogleSignInClient googleSignInClient;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initServices();
        setupGoogleSignIn();
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

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());
        registerButton.setOnClickListener(v -> performRegister());
        googleSignUpButton.setOnClickListener(v -> signUpWithGoogle());
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
        AuthRequest request = new AuthRequest(email, password, name);

        apiService.register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.isSuccess()) {
                        // Save session
                        sessionManager.saveAuthToken(authResponse.getToken());
                        sessionManager.saveUserDetails(
                                authResponse.getUser().getEmail(),
                                authResponse.getUser().getFullName()
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
            }
        });
    }

    private void signUpWithGoogle() {
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
                handleGoogleSignUpSuccess(account);
            } catch (ApiException e) {
                showMessage("Google sign up failed: " + e.getMessage());
            }
        }
    }

    private void handleGoogleSignUpSuccess(GoogleSignInAccount account) {
        setLoading(true);

        GoogleAuthRequest request = new GoogleAuthRequest();
        request.setIdToken(account.getIdToken());
        request.setEmail(account.getEmail());
        request.setFullName(account.getDisplayName());
        if (account.getPhotoUrl() != null) {
            request.setProfileImageUrl(account.getPhotoUrl().toString());
        }

        apiService.googleLogin(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if (authResponse.isSuccess()) {
                        sessionManager.saveAuthToken(authResponse.getToken());
                        sessionManager.saveUserDetails(
                                authResponse.getUser().getEmail(),
                                authResponse.getUser().getFullName()
                        );

                        showMessage("Google sign up successful!");
                        navigateToMain();
                    } else {
                        showMessage(authResponse.getMessage());
                    }
                } else {
                    showMessage("Google sign up failed. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setLoading(false);
                showMessage("Network error: " + t.getMessage());
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
        googleSignUpButton.setEnabled(!loading);
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