// app/src/main/java/com/example/tradeupsprojecy/ui/activities/MainActivity.java
package com.example.tradeupsprojecy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.example.tradeupsprojecy.ui.fragments.AddListingFragment;
import com.example.tradeupsprojecy.ui.fragments.HomeFragment;
import com.example.tradeupsprojecy.ui.fragments.MessagesFragment;
import com.example.tradeupsprojecy.ui.fragments.ProfileFragment;
import com.example.tradeupsprojecy.ui.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity created");

        initViews();
        initServices();
        checkUserSession();
        setupBottomNavigation();

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void initServices() {
        sessionManager = new SessionManager(this);
    }

    private void checkUserSession() {
        if (!sessionManager.isLoggedIn()) {
            Log.d(TAG, "User not logged in, redirecting to LoginActivity");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        Log.d(TAG, "User logged in: " + sessionManager.getUserEmail());
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.navigation_search) {
                selectedFragment = new SearchFragment();
            } else if (itemId == R.id.navigation_add) {
                selectedFragment = new AddListingFragment();
            } else if (itemId == R.id.navigation_messages) {
                selectedFragment = new MessagesFragment();
            } else if (itemId == R.id.navigation_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    public void navigateToSearch() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
    }

    public void navigateToSearch(String query) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("search_query", query);
        searchFragment.setArguments(args);

        loadFragment(searchFragment);
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
    }

    public void navigateToItemDetail(Long itemId) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("item_id", itemId);
        startActivity(intent);
    }

    public void navigateToAddListing() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_add);
    }

    public void navigateToMessages() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_messages);
    }

    public void navigateToProfile() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (currentFragment instanceof HomeFragment) {
            super.onBackPressed();
        } else {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!sessionManager.isLoggedIn()) {
            checkUserSession();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity destroyed");
    }
}