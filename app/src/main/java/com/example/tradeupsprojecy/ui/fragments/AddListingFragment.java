// app/src/main/java/com/example/tradeupsprojecy/ui/fragments/AddListingFragment.java
package com.example.tradeupsprojecy.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.api.ApiClient;
import com.example.tradeupsprojecy.data.api.ApiService;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.example.tradeupsprojecy.data.entities.Category;
import com.example.tradeupsprojecy.data.entities.Item;
import com.example.tradeupsprojecy.data.models.request.CreateItemRequest;
import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.ui.adapters.ImageSelectionAdapter;
import com.example.tradeupsprojecy.utils.ImageUtils;
import com.example.tradeupsprojecy.utils.LocationUtils;
import com.example.tradeupsprojecy.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddListingFragment extends Fragment {

    private static final String TAG = "AddListingFragment";
    private static final int MAX_IMAGES = 5;

    // Views
    private TextInputEditText titleEditText;
    private TextInputEditText descriptionEditText;
    private TextInputEditText priceEditText;
    private AutoCompleteTextView categorySpinner;
    private TextInputEditText locationInput; // Đổi tên
    private ChipGroup conditionChipGroup;
    private RecyclerView imageRecyclerView; // Đổi tên
    private MaterialButton addImageBtn; // Đổi tên
    private MaterialButton getLocationBtn; // Đổi tên
    private MaterialButton publishBtn; // Đổi tên
    private LinearLayout imageLayout; // Đổi tên

    // Services and utilities
    private ApiService apiService;
    private SessionManager sessionManager;
    private LocationUtils locationUtils;
    private ImageUtils imageUtils;

    // Data
    private List<Category> categories = new ArrayList<>();
    private List<Uri> selectedImages = new ArrayList<>();
    private ImageSelectionAdapter imageAdapter;
    private String selectedCondition = "";
    private Long selectedCategoryId = null; // Đổi thành Long

    // Activity result launchers
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<String[]> permissionLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_listing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "AddListingFragment created");

        initActivityResultLaunchers();
        initViews(view);
        initServices();
        setupClickListeners();
        setupTextWatchers();
        setupConditionChips();
        setupImageRecyclerView();
        loadCategories();
    }

    private void initActivityResultLaunchers() {
        // Image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        if (data.getClipData() != null) {
                            // Multiple images selected
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count && selectedImages.size() < MAX_IMAGES; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                selectedImages.add(imageUri);
                            }
                        } else if (data.getData() != null) {
                            // Single image selected
                            selectedImages.add(data.getData());
                        }

                        updateImageAdapter();
                        updatePublishButtonState();
                    }
                }
        );

        // Permission launcher
        permissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean readPermission = result.get(Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (readPermission != null && readPermission) {
                        openImagePicker();
                    } else {
                        showMessage("Permission denied. Cannot access images.");
                    }
                }
        );
    }

    private void initViews(View view) {
        titleEditText = view.findViewById(R.id.titleEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        priceEditText = view.findViewById(R.id.priceEditText);
        categorySpinner = view.findViewById(R.id.categorySpinner);
        locationInput = view.findViewById(R.id.locationInput);
        conditionChipGroup = view.findViewById(R.id.conditionChipGroup);
        imageRecyclerView = view.findViewById(R.id.imageRecyclerView);
        addImageBtn = view.findViewById(R.id.addImageBtn);
        getLocationBtn = view.findViewById(R.id.getLocationBtn);
        publishBtn = view.findViewById(R.id.publishBtn);
        imageLayout = view.findViewById(R.id.imageLayout);
    }

    private void initServices() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(requireContext());
        locationUtils = new LocationUtils(requireContext());
        imageUtils = new ImageUtils(requireContext());
    }

    private void setupClickListeners() {
        addImageBtn.setOnClickListener(v -> {
            if (selectedImages.size() >= MAX_IMAGES) {
                showMessage("Maximum " + MAX_IMAGES + " images allowed");
                return;
            }
            checkPermissionAndOpenImagePicker();
        });

        getLocationBtn.setOnClickListener(v -> getCurrentLocation());
        publishBtn.setOnClickListener(v -> publishListing());
    }

    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updatePublishButtonState();
            }
        };

        titleEditText.addTextChangedListener(textWatcher);
        descriptionEditText.addTextChangedListener(textWatcher);
        priceEditText.addTextChangedListener(textWatcher);
        locationInput.addTextChangedListener(textWatcher);
    }

    private void setupConditionChips() {
        String[] conditions = {"New", "Like New", "Good", "Fair", "Poor"};

        for (String condition : conditions) {
            Chip chip = new Chip(requireContext());
            chip.setText(condition);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Uncheck other chips
                    for (int i = 0; i < conditionChipGroup.getChildCount(); i++) {
                        Chip otherChip = (Chip) conditionChipGroup.getChildAt(i);
                        if (otherChip != chip) {
                            otherChip.setChecked(false);
                        }
                    }
                    selectedCondition = condition;
                } else {
                    selectedCondition = "";
                }
                updatePublishButtonState();
            });

            conditionChipGroup.addView(chip);
        }
    }

    private void setupImageRecyclerView() {
        imageAdapter = new ImageSelectionAdapter(requireContext(), selectedImages);
        imageRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        imageRecyclerView.setAdapter(imageAdapter);

        imageAdapter.setOnImageRemoveListener(position -> {
            selectedImages.remove(position);
            updateImageAdapter();
            updatePublishButtonState();
        });
    }

    private void loadCategories() {
        Log.d(TAG, "Loading categories for spinner");

        apiService.getAllCategories().enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Category>> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        categories = apiResponse.getData();
                        setupCategorySpinner();
                        Log.d(TAG, "Categories loaded: " + categories.size());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable t) {
                Log.e(TAG, "Failed to load categories: " + t.getMessage());
            }
        });
    }

    private void setupCategorySpinner() {
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                categoryNames
        );

        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemClickListener((parent, view, position, id) -> {
            selectedCategoryId = categories.get(position).getId();
            updatePublishButtonState();
        });
    }

    private void checkPermissionAndOpenImagePicker() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            openImagePicker();
        } else {
            permissionLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Images"));
    }

    private void getCurrentLocation() {
        showMessage("Getting current location...");

        locationUtils.getCurrentLocation(new LocationUtils.LocationCallback() {
            @Override
            public void onLocationReceived(String address) {
                if (locationInput != null) {
                    locationInput.setText(address);
                    updatePublishButtonState();
                }
                showMessage("Location updated");
            }

            @Override
            public void onLocationError(String error) {
                showMessage("Failed to get location: " + error);
            }
        });
    }

    private void updateImageAdapter() {
        if (imageAdapter != null) {
            imageAdapter.notifyDataSetChanged();
        }

        // Show/hide image container
        if (imageLayout != null) {
            imageLayout.setVisibility(selectedImages.isEmpty() ? View.GONE : View.VISIBLE);
        }
    }

    private void updatePublishButtonState() {
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String location = locationInput.getText().toString().trim();

        boolean hasTitle = !title.isEmpty();
        boolean hasDescription = !description.isEmpty();
        boolean hasPrice = !price.isEmpty();
        boolean hasLocation = !location.isEmpty();
        boolean hasCondition = !selectedCondition.isEmpty();
        boolean hasCategory = selectedCategoryId != null;
        boolean hasImages = !selectedImages.isEmpty();

        boolean canPublish = hasTitle && hasDescription && hasPrice && hasLocation &&
                hasCondition && hasCategory && hasImages;

        publishBtn.setEnabled(canPublish);
    }

    private void publishListing() {
        Log.d(TAG, "Publishing listing");

        if (!validateInputs()) {
            return;
        }

        setLoading(true);

        // Create request
        CreateItemRequest request = new CreateItemRequest();
        request.setTitle(titleEditText.getText().toString().trim());
        request.setDescription(descriptionEditText.getText().toString().trim());
        request.setPrice(new BigDecimal(priceEditText.getText().toString().trim()));
        request.setCategoryId(selectedCategoryId);
        request.setLocation(locationInput.getText().toString().trim());
        request.setCondition(selectedCondition);

        // Add placeholder image URLs
        List<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < selectedImages.size(); i++) {
            imageUrls.add("https://placeholder.image.url/" + i);
        }
        request.setImageUrls(imageUrls);

        createListing(request);
    }

    private boolean validateInputs() {
        String title = titleEditText.getText().toString().trim();
        String titleError = ValidationUtils.getTitleError(title);
        if (titleError != null) {
            titleEditText.setError(titleError);
            titleEditText.requestFocus();
            return false;
        }

        String description = descriptionEditText.getText().toString().trim();
        String descriptionError = ValidationUtils.getDescriptionError(description);
        if (descriptionError != null) {
            descriptionEditText.setError(descriptionError);
            descriptionEditText.requestFocus();
            return false;
        }

        String price = priceEditText.getText().toString().trim();
        String priceError = ValidationUtils.getPriceError(price);
        if (priceError != null) {
            priceEditText.setError(priceError);
            priceEditText.requestFocus();
            return false;
        }

        String location = locationInput.getText().toString().trim();
        if (location.isEmpty()) {
            locationInput.setError("Location is required");
            locationInput.requestFocus();
            return false;
        }

        if (selectedCondition.isEmpty()) {
            showMessage("Please select item condition");
            return false;
        }

        if (selectedCategoryId == null) {
            showMessage("Please select a category");
            return false;
        }

        if (selectedImages.isEmpty()) {
            showMessage("Please add at least one image");
            return false;
        }

        return true;
    }

    private void createListing(CreateItemRequest request) {
        Log.d(TAG, "Creating listing: " + request.getTitle());

        apiService.createItem(request).enqueue(new Callback<ApiResponse<Item>>() {
            @Override
            public void onResponse(Call<ApiResponse<Item>> call, Response<ApiResponse<Item>> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Item> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Log.d(TAG, "Listing created successfully");
                        showMessage("Listing published successfully!");
                        clearForm();

                        // Navigate back to home
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                    } else {
                        showMessage(apiResponse.getMessage() != null ?
                                apiResponse.getMessage() : "Failed to create listing");
                    }
                } else {
                    Log.e(TAG, "Create listing API failed: " + response.code());
                    showMessage("Failed to create listing. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Item>> call, Throwable t) {
                Log.e(TAG, "Create listing API call failed: " + t.getMessage());
                setLoading(false);
                showMessage("Network error: " + t.getMessage());
            }
        });
    }

    private void clearForm() {
        titleEditText.setText("");
        descriptionEditText.setText("");
        priceEditText.setText("");
        locationInput.setText("");
        categorySpinner.setText("");
        selectedImages.clear();
        selectedCondition = "";
        selectedCategoryId = null;

        // Clear condition chips
        for (int i = 0; i < conditionChipGroup.getChildCount(); i++) {
            Chip chip = (Chip) conditionChipGroup.getChildAt(i);
            chip.setChecked(false);
        }

        updateImageAdapter();
        updatePublishButtonState();
    }

    private void setLoading(boolean loading) {
        publishBtn.setEnabled(!loading);
        publishBtn.setText(loading ? "Publishing..." : "Publish Listing");
        titleEditText.setEnabled(!loading);
        descriptionEditText.setEnabled(!loading);
        priceEditText.setEnabled(!loading);
        categorySpinner.setEnabled(!loading);
        locationInput.setEnabled(!loading);
        addImageBtn.setEnabled(!loading);
        getLocationBtn.setEnabled(!loading);
    }

    private void showMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}