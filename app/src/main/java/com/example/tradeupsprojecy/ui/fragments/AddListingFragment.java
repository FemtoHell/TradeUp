// app/src/main/java/com/example/tradeupsprojecy/ui/fragments/AddListingFragment.java
package com.example.tradeupsprojecy.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Category;
import com.example.tradeupsprojecy.data.models.CreateItemRequest;
import com.example.tradeupsprojecy.data.models.Listing;
import com.example.tradeupsprojecy.data.network.NetworkClient;
import com.example.tradeupsprojecy.utils.PreferenceManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddListingFragment extends Fragment {
    private EditText titleEditText, descriptionEditText, priceEditText, locationEditText;
    private Spinner categorySpinner, conditionSpinner;
    private Button submitButton;

    private List<Category> categories;
    private PreferenceManager preferenceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_listing, container, false);

        initViews(view);
        setupSpinners();
        loadCategories();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        priceEditText = view.findViewById(R.id.priceEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        conditionSpinner = view.findViewById(R.id.conditionSpinner);
        submitButton = view.findViewById(R.id.submitButton);

        preferenceManager = new PreferenceManager(getContext());
        categories = new ArrayList<>();
    }

    private void setupSpinners() {
        // Setup condition spinner
        String[] conditions = {"New", "Like New", "Good", "Fair", "Poor"};
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, conditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);
    }

    private void setupClickListeners() {
        submitButton.setOnClickListener(v -> submitListing());
    }

    private void loadCategories() {
        Call<List<Category>> call = NetworkClient.getApiService().getAllCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories.clear();
                    categories.addAll(response.body());
                    setupCategorySpinner();
                } else {
                    Toast.makeText(getContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategorySpinner() {
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void submitListing() {
        // Validate inputs
        if (!validateInputs()) return;

        String token = preferenceManager.getToken();

        // Create request
        CreateItemRequest request = new CreateItemRequest();
        request.setTitle(titleEditText.getText().toString().trim());
        request.setDescription(descriptionEditText.getText().toString().trim());
        request.setPrice(new BigDecimal(priceEditText.getText().toString().trim()));
        request.setLocation(locationEditText.getText().toString().trim());
        request.setCondition(conditionSpinner.getSelectedItem().toString());

        // Get selected category ID
        int selectedPosition = categorySpinner.getSelectedItemPosition();
        if (selectedPosition >= 0 && selectedPosition < categories.size()) {
            request.setCategoryId(categories.get(selectedPosition).getId());
        }

        // Add empty image URLs list for now
        request.setImageUrls(new ArrayList<>());

        Call<Listing> call = NetworkClient.getApiService()
                .createItem("Bearer " + token, request);

        call.enqueue(new Callback<Listing>() {
            @Override
            public void onResponse(Call<Listing> call, Response<Listing> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Listing created successfully!", Toast.LENGTH_SHORT).show();
                    clearForm();
                } else {
                    Toast.makeText(getContext(), "Failed to create listing", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Listing> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(titleEditText.getText())) {
            titleEditText.setError("Title is required");
            return false;
        }

        if (TextUtils.isEmpty(descriptionEditText.getText())) {
            descriptionEditText.setError("Description is required");
            return false;
        }

        if (TextUtils.isEmpty(priceEditText.getText())) {
            priceEditText.setError("Price is required");
            return false;
        }

        try {
            new BigDecimal(priceEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            priceEditText.setError("Invalid price format");
            return false;
        }

        if (TextUtils.isEmpty(locationEditText.getText())) {
            locationEditText.setError("Location is required");
            return false;
        }

        return true;
    }

    private void clearForm() {
        titleEditText.setText("");
        descriptionEditText.setText("");
        priceEditText.setText("");
        locationEditText.setText("");
        categorySpinner.setSelection(0);
        conditionSpinner.setSelection(0);
    }
}