package com.example.tradeupsprojecy.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.ui.fragments.AddListingFragment;
import com.example.tradeupsprojecy.ui.fragments.FavoritesFragment;
import com.example.tradeupsprojecy.ui.fragments.HomeFragment;
import com.example.tradeupsprojecy.ui.fragments.ProfileFragment;
import com.example.tradeupsprojecy.ui.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupBottomNavigation();

        // Load home fragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    private void initViews() {
        bottomNavigation = findViewById(R.id.bottomNavigationView);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
            fragment = new HomeFragment();
        } else if (itemId == R.id.navigation_search) {
            fragment = new SearchFragment();
        } else if (itemId == R.id.nav_add) {
            fragment = new AddListingFragment();
        } else if (itemId == R.id.nav_favorites) {
            fragment = new FavoritesFragment();
        } else if (itemId == R.id.navigation_profile) {
            fragment = new ProfileFragment();
        }

        if (fragment != null) {
            loadFragment(fragment);
            return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    // Method để navigate to search từ HomeFragment
    public void navigateToSearch() {
        navigateToSearch(null);
    }

    // Method để navigate to search với query
    public void navigateToSearch(String query) {
        SearchFragment searchFragment = new SearchFragment();

        // Truyền query nếu có
        if (query != null && !query.isEmpty()) {
            Bundle args = new Bundle();
            args.putString("search_query", query);
            searchFragment.setArguments(args);
        }

        loadFragment(searchFragment);

        // Update bottom navigation
        bottomNavigation.setSelectedItemId(R.id.navigation_search);
    }
}