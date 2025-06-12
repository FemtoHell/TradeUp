package com.example.tradeupsprojecy.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Category;
import com.example.tradeupsprojecy.data.models.CreateItemRequest;
import com.example.tradeupsprojecy.data.models.Item;
import com.example.tradeupsprojecy.data.repository.CategoryRepository;
import com.example.tradeupsprojecy.data.repository.ItemRepository;
import com.example.tradeupsprojecy.data.local.SessionManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddListingFragment extends Fragment {

    private EditText titleEditText, descriptionEditText, priceEditText, locationEditText;
    private AutoCompleteTextView categorySpinner;
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
        setupValidation();
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
        submitButton = view.findViewById(R.id.publishBtn);

        sessionManager = new SessionManager(getContext());
        itemRepository = new ItemRepository();
        categoryRepository = new CategoryRepository();
        categories = new ArrayList<>();
    }

    private void setupValidation() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSubmitButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        titleEditText.addTextChangedListener(textWatcher);
        descriptionEditText.addTextChangedListener(textWatcher);
        priceEditText.addTextChangedListener(textWatcher);
        locationEditText.addTextChangedListener(textWatcher);

        // Initial state
        updateSubmitButtonState();
    }

    private void updateSubmitButtonState() {
        boolean isValid = !titleEditText.getText().toString().trim().isEmpty() &&
                !descriptionEditText.getText().toString().trim().isEmpty() &&
                !priceEditText.getText().toString().trim().isEmpty() &&
                !locationEditText.getText().toString().trim().isEmpty();

        submitButton.setEnabled(isValid);
        submitButton.setAlpha(isValid ? 1.0f : 0.5f);
    }

    private void setupClickListeners() {
        submitButton.setOnClickListener(v -> {
            if (validateForm()) {
                submitListing();
            }
        });
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
                    // Add default categories
                    setupDefaultCategories();
                }
            }
        });
    }

    private void setupDefaultCategories() {
        categories.clear();
        // Add some default categories
        Category electronics = new Category("Electronics", "Electronic devices");
        electronics.setId(1L);
        Category fashion = new Category("Fashion", "Clothing and accessories");
        fashion.setId(2L);
        Category home = new Category("Home & Garden", "Home and garden items");
        home.setId(3L);

        categories.add(electronics);
        categories.add(fashion);
        categories.add(home);

        setupCategorySpinner();
    }

    private void setupCategorySpinner() {
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_dropdown_item_1line, categoryNames);
        categorySpinner.setAdapter(adapter);
    }

    private boolean validateForm() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceStr = priceEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        if (title.length() < 5) {
            titleEditText.setError("Title must be at least 5 characters");
            titleEditText.requestFocus();
            return false;
        }

        if (description.length() < 10) {
            descriptionEditText.setError("Description must be at least 10 characters");
            descriptionEditText.requestFocus();
            return false;
        }

        try {
            double price = Double.parseDouble(priceStr);
            if (price <= 0) {
                priceEditText.setError("Price must be greater than 0");
                priceEditText.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            priceEditText.setError("Invalid price format");
            priceEditText.requestFocus();
            return false;
        }

        if (location.length() < 3) {
            locationEditText.setError("Location is required");
            locationEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void submitListing() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(getContext(), "Authentication required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading
        submitButton.setEnabled(false);
        submitButton.setText("Publishing...");

        // Create request
        CreateItemRequest request = new CreateItemRequest();
        request.setTitle(titleEditText.getText().toString().trim());
        request.setDescription(descriptionEditText.getText().toString().trim());
        request.setPrice(new BigDecimal(priceEditText.getText().toString().trim()));
        request.setLocation(locationEditText.getText().toString().trim());
        request.setCondition("New"); // Default condition

        // Get selected category ID
        String selectedCategory = categorySpinner.getText().toString();
        for (Category category : categories) {
            if (category.getName().equals(selectedCategory)) {
                request.setCategoryId(category.getId());
                break;
            }
        }

        // Add demo images
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add("https://picsum.photos/400/300?random=" + System.currentTimeMillis());
        imageUrls.add("https://picsum.photos/400/300?random=" + (System.currentTimeMillis() + 1));
        request.setImageUrls(imageUrls);

        itemRepository.createItem(token, request, new ItemRepository.ItemCallback() {
            @Override
            public void onSuccess(Item item) {
                if (getActivity() != null && isAdded()) {
                    Toast.makeText(getContext(), "Listing created successfully!", Toast.LENGTH_SHORT).show();
                    clearForm();
                    resetSubmitButton();
                }
            }

            @Override
            public void onError(String error) {
                if (getActivity() != null && isAdded()) {
                    Toast.makeText(getContext(), "Failed to create listing: " + error, Toast.LENGTH_SHORT).show();
                    resetSubmitButton();
                }
            }
        });
    }

    private void resetSubmitButton() {
        submitButton.setEnabled(true);
        submitButton.setText("Publish Listing");
        updateSubmitButtonState();
    }

    private void clearForm() {
        titleEditText.setText("");
        descriptionEditText.setText("");
        priceEditText.setText("");
        locationEditText.setText("");
        categorySpinner.setText("");
    }
}