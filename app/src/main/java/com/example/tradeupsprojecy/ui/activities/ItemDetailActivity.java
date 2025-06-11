// app/src/main/java/com/example/tradeupsprojecy/ui/activities/ItemDetailActivity.java
package com.example.tradeupsprojecy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Conversation;
import com.example.tradeupsprojecy.data.models.CreateConversationRequest;
import com.example.tradeupsprojecy.data.models.Listing;
import com.example.tradeupsprojecy.data.network.NetworkClient;
import com.example.tradeupsprojecy.ui.adapters.ImageSliderAdapter;
import com.example.tradeupsprojecy.utils.PreferenceManager;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity {
    private ViewPager2 imageViewPager;
    private TextView titleText, priceText, descriptionText, locationText, conditionText, sellerNameText;
    private Button contactSellerButton;
    private ImageView backButton, favoriteButton;

    private Listing currentListing;
    private PreferenceManager preferenceManager;
    private Long itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        initViews();
        getIntentData();
        loadItemDetails();
        setupClickListeners();
    }

    private void initViews() {
        imageViewPager = findViewById(R.id.imageViewPager);
        titleText = findViewById(R.id.titleText);
        priceText = findViewById(R.id.priceText);
        descriptionText = findViewById(R.id.descriptionText);
        locationText = findViewById(R.id.locationText);
        conditionText = findViewById(R.id.conditionText);
        sellerNameText = findViewById(R.id.sellerNameText);
        contactSellerButton = findViewById(R.id.contactSellerButton);
        backButton = findViewById(R.id.backButton);
        favoriteButton = findViewById(R.id.favoriteButton);

        preferenceManager = new PreferenceManager(this);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        itemId = intent.getLongExtra("item_id", -1);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());

        contactSellerButton.setOnClickListener(v -> contactSeller());

        favoriteButton.setOnClickListener(v -> toggleFavorite());
    }

    private void loadItemDetails() {
        if (itemId == -1) {
            Toast.makeText(this, "Invalid item ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Call<Listing> call = NetworkClient.getApiService().getItemById(itemId);

        call.enqueue(new Callback<Listing>() {
            @Override
            public void onResponse(Call<Listing> call, Response<Listing> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentListing = response.body();
                    displayItemDetails();
                } else {
                    Toast.makeText(ItemDetailActivity.this, "Failed to load item details", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Listing> call, Throwable t) {
                Toast.makeText(ItemDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayItemDetails() {
        if (currentListing == null) return;

        titleText.setText(currentListing.getTitle());

        // Format price
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        priceText.setText(formatter.format(currentListing.getPrice()));

        descriptionText.setText(currentListing.getDescription());
        locationText.setText(currentListing.getLocation());
        conditionText.setText(currentListing.getCondition());
        sellerNameText.setText(currentListing.getSellerName());

        // Setup image slider
        if (currentListing.getImages() != null && !currentListing.getImages().isEmpty()) {
            ImageSliderAdapter adapter = new ImageSliderAdapter(currentListing.getImages());
            imageViewPager.setAdapter(adapter);
        }

        // Hide contact button if user is the seller
        if (currentListing.getSellerId().equals(preferenceManager.getUserId())) {
            contactSellerButton.setVisibility(View.GONE);
        }
    }

    private void contactSeller() {
        if (currentListing == null) return;

        String token = preferenceManager.getToken();

        CreateConversationRequest request = new CreateConversationRequest();
        request.setParticipantId(currentListing.getSellerId());
        request.setItemId(currentListing.getId());
        request.setMessage("Hi, I'm interested in your " + currentListing.getTitle());

        Call<Conversation> call = NetworkClient.getApiService()
                .createConversation("Bearer " + token, request);

        call.enqueue(new Callback<Conversation>() {
            @Override
            public void onResponse(Call<Conversation> call, Response<Conversation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Conversation conversation = response.body();

                    // Navigate to chat
                    Intent intent = new Intent(ItemDetailActivity.this, ChatActivity.class);
                    intent.putExtra("conversation_id", conversation.getId().toString());
                    intent.putExtra("participant_name", currentListing.getSellerName());
                    startActivity(intent);
                } else {
                    Toast.makeText(ItemDetailActivity.this, "Failed to start conversation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Conversation> call, Throwable t) {
                Toast.makeText(ItemDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleFavorite() {
        // TODO: Implement favorite functionality
        Toast.makeText(this, "Favorite functionality coming soon", Toast.LENGTH_SHORT).show();
    }
}