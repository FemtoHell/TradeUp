// app/src/main/java/com/example/tradeupsprojecy/ui/activities/ItemDetailActivity.java
package com.example.tradeupsprojecy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.api.ApiClient;
import com.example.tradeupsprojecy.data.api.ApiService;
import com.example.tradeupsprojecy.data.local.SessionManager;
import com.example.tradeupsprojecy.data.entities.Item;
import com.example.tradeupsprojecy.data.models.request.CreateConversationRequest;
import com.example.tradeupsprojecy.data.models.response.ApiResponse;
import com.example.tradeupsprojecy.ui.adapters.ImagePagerAdapter;
import com.google.android.material.button.MaterialButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {

    private static final String TAG = "ItemDetailActivity";

    // Views
    private ImageView backButton;
    private ImageView favoriteBtn;
    private ViewPager2 imageViewPager;
    private LinearLayout imageIndicator;
    private TextView titleTextView;
    private TextView priceTextView;
    private TextView conditionTextView;
    private TextView categoryText;
    private TextView locationTextView;
    private TextView descriptionTextView;
    private TextView sellerNameTextView;
    private TextView postedDateText;
    private MaterialButton messageSellerBtn;
    private MaterialButton callSellerBtn;
    private ProgressBar progressBar;
    private LinearLayout contentContainer;
    private LinearLayout errorContainer;
    private TextView errorText;
    private MaterialButton retryBtn;

    // Services
    private ApiService apiService;
    private SessionManager sessionManager;

    // Data
    private Long itemId;
    private Item currentItem;
    private ImagePagerAdapter imagePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Log.d(TAG, "ItemDetailActivity created");

        getIntentData();
        initViews();
        initServices();
        setupClickListeners();
        setupImagePager();
        loadItemDetail();
    }

    private void getIntentData() {
        itemId = getIntent().getLongExtra("item_id", -1L);
        if (itemId == -1L) {
            Log.e(TAG, "No item ID provided");
            showError("Invalid item");
            return;
        }
        Log.d(TAG, "Item ID: " + itemId);
    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        favoriteBtn = findViewById(R.id.favoriteBtn);
        imageViewPager = findViewById(R.id.imageViewPager);
        imageIndicator = findViewById(R.id.imageIndicator);
        titleTextView = findViewById(R.id.titleTextView);
        priceTextView = findViewById(R.id.priceTextView);
        conditionTextView = findViewById(R.id.conditionTextView);
        categoryText = findViewById(R.id.categoryText);
        locationTextView = findViewById(R.id.locationTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        sellerNameTextView = findViewById(R.id.sellerNameTextView);
        postedDateText = findViewById(R.id.postedDateText);
        messageSellerBtn = findViewById(R.id.messageSellerBtn);
        callSellerBtn = findViewById(R.id.callSellerBtn);
        progressBar = findViewById(R.id.progressBar);
        contentContainer = findViewById(R.id.contentContainer);
        errorContainer = findViewById(R.id.errorContainer);
        errorText = findViewById(R.id.errorText);
        retryBtn = findViewById(R.id.retryBtn);
    }

    private void initServices() {
        apiService = ApiClient.getApiService();
        sessionManager = new SessionManager(this);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        favoriteBtn.setOnClickListener(v -> toggleFavorite());

        messageSellerBtn.setOnClickListener(v -> messageSellerClicked());

        callSellerBtn.setOnClickListener(v -> callSellerClicked());

        retryBtn.setOnClickListener(v -> loadItemDetail());
    }

    private void setupImagePager() {
        imagePagerAdapter = new ImagePagerAdapter(this, new ArrayList<>());
        imageViewPager.setAdapter(imagePagerAdapter);

        imageViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateImageIndicators(position);
            }
        });
    }

    private void loadItemDetail() {
        Log.d(TAG, "Loading item detail for ID: " + itemId);

        showLoading(true);
        hideError();

        apiService.getItemById(itemId).enqueue(new Callback<ApiResponse<Item>>() {
            @Override
            public void onResponse(Call<ApiResponse<Item>> call, Response<ApiResponse<Item>> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Item> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        currentItem = apiResponse.getData();
                        Log.d(TAG, "Item loaded: " + currentItem.getTitle());
                        displayItemDetails();
                    } else {
                        Log.e(TAG, "Item API error: " + apiResponse.getMessage());
                        showError(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Failed to load item");
                    }
                } else {
                    Log.e(TAG, "Item API call failed: " + response.code());
                    showError("Failed to load item details");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Item>> call, Throwable t) {
                Log.e(TAG, "Item API call failed: " + t.getMessage());
                showLoading(false);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void displayItemDetails() {
        if (currentItem == null) return;

        showContent(true);

        // Title
        titleTextView.setText(currentItem.getTitle());

        // Price
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        priceTextView.setText(formatter.format(currentItem.getPrice()));

        // Condition
        conditionTextView.setText(currentItem.getCondition() != null ? currentItem.getCondition() : "Not specified");

        // Category
        categoryText.setText(currentItem.getCategoryName() != null ? currentItem.getCategoryName() : "No category");

        // Location
        locationTextView.setText(currentItem.getLocation() != null ? currentItem.getLocation() : "Location not specified");

        // Description
        descriptionTextView.setText(currentItem.getDescription() != null ? currentItem.getDescription() : "No description available");

        // Seller info
        sellerNameTextView.setText(currentItem.getSellerName() != null ? currentItem.getSellerName() : "Unknown seller");

        // Posted date
        if (currentItem.getCreatedAt() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            postedDateText.setText("Posted on " + sdf.format(currentItem.getCreatedAt()));
        }

        // Images
        setupImages();

        // Check if this is user's own item
        String currentUserId = sessionManager.getUserId();
        boolean isOwnItem = currentItem.getSellerId() != null && currentItem.getSellerId().equals(currentUserId);

        if (isOwnItem) {
            messageSellerBtn.setText("Edit Listing");
            callSellerBtn.setVisibility(View.GONE);
        } else {
            messageSellerBtn.setText("Message Seller");
            callSellerBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setupImages() {
        if (currentItem.getImageUrls() != null && !currentItem.getImageUrls().isEmpty()) {
            imagePagerAdapter.updateImages(currentItem.getImageUrls());
            setupImageIndicators(currentItem.getImageUrls().size());
            imageViewPager.setVisibility(View.VISIBLE);
        } else {
            // Show placeholder image
            List<String> placeholderImages = new ArrayList<>();
            placeholderImages.add(""); // Empty URL will show placeholder
            imagePagerAdapter.updateImages(placeholderImages);
            imageIndicator.setVisibility(View.GONE);
        }
    }

    private void setupImageIndicators(int count) {
        imageIndicator.removeAllViews();

        if (count <= 1) {
            imageIndicator.setVisibility(View.GONE);
            return;
        }

        imageIndicator.setVisibility(View.VISIBLE);

        for (int i = 0; i < count; i++) {
            ImageView indicator = new ImageView(this);
            indicator.setImageResource(android.R.drawable.radiobutton_off_background);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    32, 32
            );
            params.setMargins(8, 0, 8, 0);
            indicator.setLayoutParams(params);

            imageIndicator.addView(indicator);
        }

        // Set first indicator as active
        if (imageIndicator.getChildCount() > 0) {
            ((ImageView) imageIndicator.getChildAt(0)).setImageResource(android.R.drawable.radiobutton_on_background);
        }
    }

    private void updateImageIndicators(int selectedPosition) {
        for (int i = 0; i < imageIndicator.getChildCount(); i++) {
            ImageView indicator = (ImageView) imageIndicator.getChildAt(i);
            if (i == selectedPosition) {
                indicator.setImageResource(android.R.drawable.radiobutton_on_background);
            } else {
                indicator.setImageResource(android.R.drawable.radiobutton_off_background);
            }
        }
    }

    private void toggleFavorite() {
        Toast.makeText(this, "Favorite feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    private void messageSellerClicked() {
        if (currentItem == null) return;

        String currentUserId = sessionManager.getUserId();
        boolean isOwnItem = currentItem.getSellerId() != null && currentItem.getSellerId().equals(currentUserId);

        if (isOwnItem) {
            Toast.makeText(this, "Edit listing feature coming soon!", Toast.LENGTH_SHORT).show();
            return;
        }

        createConversationWithSeller();
    }

    private void createConversationWithSeller() {
        Log.d(TAG, "Creating conversation with seller");

        CreateConversationRequest request = new CreateConversationRequest();
        request.setItemId(currentItem.getId());
        request.setSellerId(currentItem.getSellerId());
        request.setBuyerId(sessionManager.getUserId());

        apiService.createConversation(request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        String conversationId = apiResponse.getData();
                        Log.d(TAG, "Conversation created: " + conversationId);

                        // Navigate to chat
                        Intent intent = new Intent(ItemDetailActivity.this, ChatActivity.class);
                        intent.putExtra("conversation_id", conversationId);
                        intent.putExtra("other_user_name", currentItem.getSellerName());
                        intent.putExtra("item_title", currentItem.getTitle());
                        startActivity(intent);
                    } else {
                        showMessage(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Failed to start conversation");
                    }
                } else {
                    Log.e(TAG, "Create conversation failed: " + response.code());
                    showMessage("Failed to start conversation");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Log.e(TAG, "Create conversation API call failed: " + t.getMessage());
                showMessage("Network error: " + t.getMessage());
            }
        });
    }

    private void callSellerClicked() {
        Toast.makeText(this, "Call feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showContent(boolean show) {
        contentContainer.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showError(String message) {
        errorContainer.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);
        errorText.setText(message);
    }

    private void hideError() {
        errorContainer.setVisibility(View.GONE);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}