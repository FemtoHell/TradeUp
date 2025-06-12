// app/src/main/java/com/example/tradeupsprojecy/ui/fragments/AddListingFragment.java - FIX
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
import com.example.tradeupsprojecy.data.models.Item;  // ✅ ADD: Import Item
import com.example.tradeupsprojecy.data.repository.CategoryRepository;
import com.example.tradeupsprojecy.data.repository.ItemRepository;
import com.example.tradeupsprojecy.data.local.SessionManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddListingFragment extends Fragment {
    private EditText titleEditText, descriptionEditText, priceEditText, locationEditText;
    private Spinner categorySpinner, conditionSpinner;
    private Button submitButton;

    private List<Category> categories;
    private SessionManager sessionManager;
    private ItemRepository itemRepository;
    private CategoryRepository categoryRepository;

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
        locationEditText = view.findViewById(R.id.locationInput);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        conditionSpinner = view.findViewById(R.id.conditionChipGroup);
        submitButton = view.findViewById(R.id.publishBtn);

        sessionManager = new SessionManager(getContext());
        itemRepository = new ItemRepository();
        categoryRepository = new CategoryRepository();
        categories = new ArrayList<>();
    }

    private void setupSpinners() {
        // Setup condition spinner
        String[] conditions = {"New", "Like New", "Good", "Fair", "Poor"};
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, conditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void setupClickListeners() {
        submitButton.setOnClickListener(v -> submitListing());
    }

    private void loadCategories() {
        categoryRepository.getAllCategories(new CategoryRepository.CategoriesCallback() {
            @Override
            public void onSuccess(List<Category> categoriesList) {
                if (getActivity() != null && isAdded()) {
                    categories.clear();
                    categories.addAll(categoriesList);
                    setupCategorySpinner();
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null && isAdded()) {
                    Toast.makeText(getContext(), "Failed to load categories: " + error, Toast.LENGTH_SHORT).show();
                }
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

        String token = sessionManager.getToken();

        // Create request
        CreateItemRequest request = new CreateItemRequest();
        request.setTitle(titleEditText.getText().toString().trim());
        request.setDescription(descriptionEditText.getText().toString().trim());
        request.setPrice(new BigDecimal(priceEditText.getText().toString().trim()));
        request.setLocation(locationEditText.getText().toString().trim());
        request.setCondition("New"); // Default condition

        // Get selected category ID
        int selectedPosition = categorySpinner.getSelectedItemPosition();
        if (selectedPosition >= 0 && selectedPosition < categories.size()) {
            request.setCategoryId(categories.get(selectedPosition).getId());
        }

        // Add empty image URLs list for now
        request.setImageUrls(new ArrayList<>());

        // ✅ FIX: Correct callback implementation
        itemRepository.createItem(token, request, new ItemRepository.ItemCallback() {
            @Override
            public void onSuccess(Item item) {  // ✅ FIX: Use Item type
                if (getActivity() != null && isAdded()) {
                    Toast.makeText(getContext(), "Listing created successfully!", Toast.LENGTH_SHORT).show();
                    clearForm();
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null && isAdded()) {
                    Toast.makeText(getContext(), "Failed to create listing: " + error, Toast.LENGTH_SHORT).show();
                }
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
    }
}