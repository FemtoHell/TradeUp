// app/src/main/java/com/example/tradeupsprojecy/ui/activities/ItemDetailActivity.java - FIX COMPLETE
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
import com.example.tradeupsprojecy.data.models.Item;
import com.example.tradeupsprojecy.data.repository.ConversationRepository;
import com.example.tradeupsprojecy.data.repository.ItemRepository;
import com.example.tradeupsprojecy.ui.adapters.ImageSliderAdapter;
import com.example.tradeupsprojecy.data.local.SessionManager;

import java.text.NumberFormat;
import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {

    // ✅ DECLARE ALL MISSING VARIABLES
    private ViewPager2 imageViewPager;
    private TextView titleTextView, priceTextView, descriptionTextView, locationTextView, conditionTextView, sellerNameTextView;
    private Button contactSellerButton;
    private ImageView backButton, favoriteButton;

    private Item currentItem;
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
        // ✅ INITIALIZE ALL VIEWS
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

        // ✅ INITIALIZE SERVICES
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
            public void onSuccess(Item item) {
                currentItem = item;
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
        if (currentItem == null) return;

        titleTextView.setText(currentItem.getTitle());

        // Format price
        if (currentItem.getPrice() != null) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            priceTextView.setText(formatter.format(currentItem.getPrice()));
        }

        descriptionTextView.setText(currentItem.getDescription());
        locationTextView.setText(currentItem.getLocation());
        conditionTextView.setText(currentItem.getCondition());
        sellerNameTextView.setText(currentItem.getSellerName());

        // Setup image slider
        if (currentItem.getImageUrls() != null && !currentItem.getImageUrls().isEmpty()) {
            ImageSliderAdapter adapter = new ImageSliderAdapter(currentItem.getImageUrls());
            imageViewPager.setAdapter(adapter);
        }

        // Hide contact button if user is the seller
        try {
            Long currentUserId = Long.parseLong(sessionManager.getUserId());
            if (currentItem.getSellerId() != null && currentItem.getSellerId().equals(currentUserId)) {
                contactSellerButton.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            // Handle error parsing user ID
        }
    }

    private void contactSeller() {
        if (currentItem == null) return;

        String token = sessionManager.getToken();

        CreateConversationRequest request = new CreateConversationRequest();
        request.setItemId(currentItem.getId());
        request.setSellerId(currentItem.getSellerId() != null ? currentItem.getSellerId().toString() : null);
        request.setBuyerId(sessionManager.getUserId());

        conversationRepository.createConversation(token, request, new ConversationRepository.ConversationCallback() {
            @Override
            public void onSuccess(Conversation conversation) {
                // Navigate to chat
                Intent intent = new Intent(ItemDetailActivity.this, ChatActivity.class);
                intent.putExtra("conversation_id", conversation.getId());
                intent.putExtra("participant_name", currentItem.getSellerName());
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