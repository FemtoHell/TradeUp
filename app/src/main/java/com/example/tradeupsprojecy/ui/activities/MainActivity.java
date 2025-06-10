// app/src/main/java/com/example/tradeupsprojecy/ui/activities/MainActivity.java
package com.example.tradeupsprojecy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.api.ApiClient;
import com.example.tradeupsprojecy.data.api.ApiService;
import com.example.tradeupsprojecy.data.models.entities.User;
import com.example.tradeupsprojecy.data.models.entities.Category;
import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.ui.fragments.HomeFragment;
import com.example.tradeupsprojecy.ui.fragments.SearchFragment;
import com.example.tradeupsprojecy.ui.fragments.AddListingFragment;
import com.example.tradeupsprojecy.ui.fragments.MessagesFragment;
import com.example.tradeupsprojecy.ui.fragments.ProfileFragment;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity started");

        initServices();
        initViews();
        checkUserSession();
        setupBottomNavigation();

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Test backend connection after initialization
        testBackendConnection();
    }

    private void initServices() {
        Log.d(TAG, "Initializing services");

        try {
            apiService = ApiClient.getApiService();
            sessionManager = new SessionManager(this);
            Log.d(TAG, "Services initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing services: " + e.getMessage());
            showMessage("Service initialization error: " + e.getMessage());
        }
    }

    private void initViews() {
        Log.d(TAG, "Initializing views");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (bottomNavigationView == null) {
            Log.e(TAG, "BottomNavigationView not found!");
            showMessage("Navigation setup error");
            return;
        }

        Log.d(TAG, "Views initialized successfully");
    }

    private void checkUserSession() {
        Log.d(TAG, "Checking user session");

        if (!sessionManager.isLoggedIn()) {
            Log.d(TAG, "User not logged in, redirecting to LoginActivity");
            navigateToLogin();
            return;
        }

        String userName = sessionManager.getUserName();
        String userEmail = sessionManager.getUserEmail();
        Log.d(TAG, "User logged in - Name: " + userName + ", Email: " + userEmail);
    }

    private void setupBottomNavigation() {
        Log.d(TAG, "Setting up bottom navigation");

        if (bottomNavigationView == null) {
            Log.e(TAG, "Cannot setup navigation - bottomNavigationView is null");
            return;
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String title = item.getTitle().toString();

            Log.d(TAG, "Navigation item selected: " + title);

            switch (title) {
                case "Home":
                    selectedFragment = new HomeFragment();
                    break;
                case "Search":
                    selectedFragment = new SearchFragment();
                    break;
                case "Sell":
                    selectedFragment = new AddListingFragment();
                    break;
                case "Messages":
                    selectedFragment = new MessagesFragment();
                    break;
                case "Profile":
                    selectedFragment = new ProfileFragment();
                    break;
                default:
                    Log.w(TAG, "Unknown navigation item: " + title);
                    break;
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

        Log.d(TAG, "Bottom navigation setup completed");
    }

    private void loadFragment(Fragment fragment) {
        Log.d(TAG, "Loading fragment: " + fragment.getClass().getSimpleName());

        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment);
            transaction.commit();

            Log.d(TAG, "Fragment loaded successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error loading fragment: " + e.getMessage());
            showMessage("Fragment loading error");
        }
    }

    // ✅ NAVIGATION METHODS
    public void navigateToSearch() {
        Log.d(TAG, "Navigating to search");

        if (bottomNavigationView != null && bottomNavigationView.getMenu().size() > 1) {
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
        loadFragment(new SearchFragment());
    }

    public void navigateToSearch(String query) {
        Log.d(TAG, "Navigating to search with query: " + query);

        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("search_query", query);
        searchFragment.setArguments(bundle);

        if (bottomNavigationView != null && bottomNavigationView.getMenu().size() > 1) {
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
        loadFragment(searchFragment);
    }

    public void navigateToAddListing() {
        Log.d(TAG, "Navigating to add listing");

        if (bottomNavigationView != null && bottomNavigationView.getMenu().size() > 2) {
            bottomNavigationView.getMenu().getItem(2).setChecked(true);
        }
        loadFragment(new AddListingFragment());
    }

    public void navigateToMessages() {
        Log.d(TAG, "Navigating to messages");

        if (bottomNavigationView != null && bottomNavigationView.getMenu().size() > 3) {
            bottomNavigationView.getMenu().getItem(3).setChecked(true);
        }
        loadFragment(new MessagesFragment());
    }

    public void navigateToProfile() {
        Log.d(TAG, "Navigating to profile");

        if (bottomNavigationView != null && bottomNavigationView.getMenu().size() > 4) {
            bottomNavigationView.getMenu().getItem(4).setChecked(true);
        }
        loadFragment(new ProfileFragment());
    }

    public void navigateToItemDetail(Long itemId) {
        Log.d(TAG, "Navigating to item detail: " + itemId);
        // TODO: Create ItemDetailActivity
        showMessage("Item detail - Coming Soon!");
    }

    public void navigateToLogin() {
        Log.d(TAG, "Navigating to login");

        try {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to login: " + e.getMessage());
            showMessage("Navigation error");
        }
    }

    // ✅ BACKEND CONNECTION TEST (MULTIPLE APPROACHES)
    private void testBackendConnection() {
        Log.d(TAG, "Testing backend connection...");

        // Test 1: Try simple endpoint that returns array
        testUsersEndpoint();
    }

    private void testUsersEndpoint() {
        Log.d(TAG, "Testing /api/users endpoint");

        try {
            // Fix: Use the correct return type ApiResponse<List<User>>
            Call<ApiResponse<List<User>>> call = apiService.getAllUsers();
            call.enqueue(new Callback<ApiResponse<List<User>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<User>>> call, Response<ApiResponse<List<User>>> response) {
                    Log.d(TAG, "Users endpoint response received");
                    Log.d(TAG, "Response code: " + response.code());
                    Log.d(TAG, "Response successful: " + response.isSuccessful());

                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<List<User>> apiResponse = response.body();

                        if (apiResponse.isSuccess()) {
                            List<User> users = apiResponse.getData();
                            int userCount = users != null ? users.size() : 0;

                            Log.d(TAG, "✅ Backend connected successfully!");
                            Log.d(TAG, "Users count: " + userCount);

                            runOnUiThread(() -> {
                                showMessage("✅ Backend Connected! (" + userCount + " users)");
                            });

                            // Test categories as backup
                            testCategoriesEndpoint();
                        } else {
                            Log.e(TAG, "❌ API response indicates failure: " + apiResponse.getMessage());

                            runOnUiThread(() -> {
                                showMessage("❌ API Error: " + apiResponse.getMessage());
                            });

                            testCategoriesEndpoint();
                        }
                    } else {
                        Log.e(TAG, "❌ Users API call failed. Response code: " + response.code());

                        // Try error body
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e(TAG, "Error body: " + errorBody);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error reading error body: " + e.getMessage());
                        }

                        runOnUiThread(() -> {
                            showMessage("❌ API Failed: " + response.code());
                        });

                        // Try alternative endpoint
                        testCategoriesEndpoint();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<List<User>>> call, Throwable t) {
                    Log.e(TAG, "❌ Users endpoint network error: " + t.getMessage());
                    Log.e(TAG, "Exception type: " + t.getClass().getSimpleName());

                    if (t.getCause() != null) {
                        Log.e(TAG, "Exception cause: " + t.getCause().getMessage());
                    }

                    runOnUiThread(() -> {
                        showMessage("❌ Network Error: " + t.getMessage());
                    });

                    // Try alternative endpoint
                    testCategoriesEndpoint();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error making users API call: " + e.getMessage());
            showMessage("❌ API Setup Error: " + e.getMessage());
        }
    }

    private void testCategoriesEndpoint() {
        Log.d(TAG, "Testing /api/categories endpoint as fallback");

        try {
            Call<ApiResponse<List<Category>>> call = apiService.getAllCategories();
            call.enqueue(new Callback<ApiResponse<List<Category>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                    Log.d(TAG, "Categories endpoint response received");
                    Log.d(TAG, "Response code: " + response.code());

                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<List<Category>> apiResponse = response.body();

                        Log.d(TAG, "✅ Categories endpoint working!");
                        Log.d(TAG, "Response success: " + apiResponse.isSuccess());

                        if (apiResponse.isSuccess()) {
                            List<Category> categories = apiResponse.getData();
                            int categoryCount = categories != null ? categories.size() : 0;

                            runOnUiThread(() -> {
                                showMessage("✅ Backend Categories OK! (" + categoryCount + " categories)");
                            });
                        } else {
                            runOnUiThread(() -> {
                                showMessage("❌ Categories API Error: " + apiResponse.getMessage());
                            });
                        }
                    } else {
                        Log.e(TAG, "❌ Categories endpoint failed: " + response.code());

                        runOnUiThread(() -> {
                            showMessage("❌ Categories Failed: " + response.code());
                        });
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable t) {
                    Log.e(TAG, "❌ Categories endpoint error: " + t.getMessage());

                    runOnUiThread(() -> {
                        showMessage("❌ Backend Unreachable");
                    });
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error testing categories endpoint: " + e.getMessage());
        }
    }

    private void showMessage(String message) {
        Log.d(TAG, "Showing message: " + message);

        runOnUiThread(() -> {
            try {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e(TAG, "Error showing toast: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity paused");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity destroyed");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "Back button pressed");

        // Handle back navigation for fragments
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (currentFragment instanceof HomeFragment) {
            // If on home fragment, ask to exit app
            super.onBackPressed();
        } else {
            // Navigate back to home
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            loadFragment(new HomeFragment());
        }
    }
}