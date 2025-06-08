// LoginActivity.java
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;

    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton loginButton, googleSignInButton;
    private TextView signUpTextView, forgotPasswordTextView;
    private ProgressBar progressBar;

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
                .requestIdToken(getString(R.string.default_web_client_id)) // Add this to strings.xml
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

        apiService.login(request).enqueue(new Callback<AuthResponse>() {
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

                        showMessage("Login successful!");
                        navigateToMain();
                    } else {
                        showMessage(authResponse.getMessage());
                    }
                } else {
                    showMessage("Login failed. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setLoading(false);
                showMessage("Network error: " + t.getMessage());
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

                        showMessage("Google login successful!");
                        navigateToMain();
                    } else {
                        showMessage(authResponse.getMessage());
                    }
                } else {
                    showMessage("Google login failed. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                setLoading(false);
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
        // You can add a progress bar here
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
        // Implement forgot password logic
        showMessage("Forgot password feature coming soon!");
    }
}