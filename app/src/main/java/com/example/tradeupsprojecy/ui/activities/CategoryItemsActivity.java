package com.example.tradeupsprojecy.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Item;
import com.example.tradeupsprojecy.data.repository.ItemRepository;
import com.example.tradeupsprojecy.ui.adapters.ItemAdapter;
import java.util.List;

public class CategoryItemsActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {

    private RecyclerView itemsRecyclerView;
    private ItemAdapter itemAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemRepository itemRepository;

    private Long categoryId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);

        getIntentData();
        setupToolbar();
        initViews();
        setupRecyclerView();
        loadCategoryItems();
    }

    private void getIntentData() {
        categoryId = getIntent().getLongExtra("category_id", -1);
        categoryName = getIntent().getStringExtra("category_name");
        if (categoryName == null) categoryName = "Category Items";
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(categoryName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViews() {
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        itemRepository = new ItemRepository();
    }

    private void setupRecyclerView() {
        itemAdapter = new ItemAdapter(this);
        itemAdapter.setOnItemClickListener(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        itemsRecyclerView.setLayoutManager(layoutManager);
        itemsRecyclerView.setAdapter(itemAdapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadCategoryItems);
    }

    private void loadCategoryItems() {
        swipeRefreshLayout.setRefreshing(true);

        if (categoryId == -1) {
            itemRepository.getAllItems(new ItemRepository.ItemsCallback() {
                @Override
                public void onSuccess(List<Item> items) {
                    swipeRefreshLayout.setRefreshing(false);
                    itemAdapter.setItems(items);
                }

                @Override
                public void onError(String error) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(CategoryItemsActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // TODO: Implement getItemsByCategory when backend is ready
            itemRepository.getAllItems(new ItemRepository.ItemsCallback() {
                @Override
                public void onSuccess(List<Item> items) {
                    swipeRefreshLayout.setRefreshing(false);
                    itemAdapter.setItems(items);
                }

                @Override
                public void onError(String error) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(CategoryItemsActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onItemClick(Item item) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("item_id", item.getId());
        startActivity(intent);
    }

    @Override
    public void onFavoriteClick(Item item, int position) {
        toggleFavorite(item);
    }

    private void toggleFavorite(Item item) {
        // TODO: Implement favorite toggle
        Toast.makeText(this, "Favorite feature coming soon!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}