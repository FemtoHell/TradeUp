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
import com.example.tradeupsprojecy.data.repository.ConversationRepository;
import com.example.tradeupsprojecy.data.repository.ItemRepository;
import com.example.tradeupsprojecy.ui.adapters.ImageSliderAdapter;
import com.example.tradeupsprojecy.data.local.SessionManager;

import java.text.NumberFormat;
import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {
    private ViewPager2 imageViewPager;
    private TextView titleTextView, priceTextView, descriptionTextView, locationTextView, conditionTextView, sellerNameTextView;
    private Button contactSellerButton;
    private ImageView backButton, favoriteButton;

    private Listing currentListing;
    private SessionManager sessionManager;
    private ItemRepository itemRepository;
    private ConversationRepository conversationRepository;
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
        titleTextView = findViewById(R.id.titleTextView);
        priceTextView = findViewById(R.id.priceTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        locationTextView = findViewById(R.id.locationTextView);
        conditionTextView = findViewById(R.id.conditionTextView);
        sellerNameTextView = findViewById(R.id.sellerNameTextView);
        contactSellerButton = findViewById(R.id.messageSellerBtn);
        backButton = findViewById(R.id.backButton);
        favoriteButton = findViewById(R.id.favoriteBtn);

        sessionManager = new SessionManager(this);
        itemRepository = new ItemRepository();
        conversationRepository = new ConversationRepository();
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

        itemRepository.getItemById(itemId, new ItemRepository.ItemCallback() {
            @Override
            public void onSuccess(Listing listing) {
                currentListing = listing;
                displayItemDetails();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ItemDetailActivity.this, "Failed to load item details: " + error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayItemDetails() {
        if (currentListing == null) return;

        titleTextView.setText(currentListing.getTitle());

        // Format price
        if (currentListing.getPrice() != null) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            priceTextView.setText(formatter.format(currentListing.getPrice()));
        }

        descriptionTextView.setText(currentListing.getDescription());
        locationTextView.setText(currentListing.getLocation());
        conditionTextView.setText(currentListing.getCondition());
        sellerNameTextView.setText(currentListing.getSellerName());

        // Setup image slider
        if (currentListing.getImages() != null && !currentListing.getImages().isEmpty()) {
            ImageSliderAdapter adapter = new ImageSliderAdapter(currentListing.getImages());
            imageViewPager.setAdapter(adapter);
        }

        // Hide contact button if user is the seller
        try {
            Long currentUserId = Long.parseLong(sessionManager.getUserId());
            if (currentListing.getSellerId().equals(currentUserId)) {
                contactSellerButton.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            // Handle error parsing user ID
        }
    }

    private void contactSeller() {
        if (currentListing == null) return;

        String token = sessionManager.getToken();

        CreateConversationRequest request = new CreateConversationRequest();
        request.setItemId(currentListing.getId());
        request.setSellerId(currentListing.getSellerId().toString());
        request.setBuyerId(sessionManager.getUserId());

        conversationRepository.createConversation(token, request, new ConversationRepository.ConversationCallback() {
            @Override
            public void onSuccess(Conversation conversation) {
                // Navigate to chat
                Intent intent = new Intent(ItemDetailActivity.this, ChatActivity.class);
                intent.putExtra("conversation_id", conversation.getId());
                intent.putExtra("participant_name", currentListing.getSellerName());
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ItemDetailActivity.this, "Failed to start conversation: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleFavorite() {
        Toast.makeText(this, "Favorite functionality coming soon", Toast.LENGTH_SHORT).show();
    }
}