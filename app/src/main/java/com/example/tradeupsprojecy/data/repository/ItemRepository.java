package com.example.tradeupsprojecy.data.repository;

import com.example.tradeupsprojecy.data.models.Category;
import com.example.tradeupsprojecy.data.models.CreateItemRequest;
import com.example.tradeupsprojecy.data.models.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ItemRepository {

    public interface ItemsCallback {
        void onSuccess(List<Item> items);
        void onError(String error);
    }

    public interface ItemCallback {
        void onSuccess(Item item);
        void onError(String error);
    }

    public void getAllItems(ItemsCallback callback) {
        // Generate demo data
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate network delay
                List<Item> items = generateDemoItems();
                callback.onSuccess(items);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public void getFeaturedItems(ItemsCallback callback) {
        new Thread(() -> {
            try {
                Thread.sleep(800);
                List<Item> items = generateDemoItems();
                callback.onSuccess(items.subList(0, Math.min(5, items.size())));
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public void getRecentItems(ItemsCallback callback) {
        new Thread(() -> {
            try {
                Thread.sleep(600);
                List<Item> items = generateDemoItems();
                callback.onSuccess(items);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public void getItemById(Long itemId, ItemCallback callback) {
        new Thread(() -> {
            try {
                Thread.sleep(500);
                Item item = generateDemoItem(itemId);
                callback.onSuccess(item);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    // THÊM MỚI: Create item method
    public void createItem(String token, CreateItemRequest request, ItemCallback callback) {
        new Thread(() -> {
            try {
                Thread.sleep(1000); // Simulate network delay

                // Create demo item from request
                Item newItem = new Item();
                newItem.setId(System.currentTimeMillis()); // Demo ID
                newItem.setTitle(request.getTitle());
                newItem.setDescription(request.getDescription());
                newItem.setPrice(request.getPrice());
                newItem.setLocation(request.getLocation());
                newItem.setCondition(request.getCondition());
                newItem.setCategoryId(request.getCategoryId());
                newItem.setImageUrls(request.getImageUrls());
                newItem.setSellerId("demo_user_id");
                newItem.setSellerName("Demo User");

                callback.onSuccess(newItem);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    private List<Item> generateDemoItems() {
        List<Item> items = new ArrayList<>();
        String[] titles = {
                "iPhone 15 Pro Max", "Samsung Galaxy S24", "MacBook Air M3", "AirPods Pro 2",
                "iPad Pro 12.9", "Sony WH-1000XM5", "Nintendo Switch OLED", "PlayStation 5",
                "Canon EOS R6", "Apple Watch Series 9", "Surface Laptop 5", "Kindle Paperwhite"
        };

        String[] locations = {
                "Quận 1, TP.HCM", "Quận 3, TP.HCM", "Quận 7, TP.HCM", "Thủ Đức, TP.HCM",
                "Hà Nội", "Đà Nẵng", "Cần Thơ", "Nha Trang"
        };

        String[] conditions = {"Mới", "Như mới", "Đã sử dụng", "Cũ"};
        String[] sellerNames = {"Nguyễn Văn A", "Trần Thị B", "Lê Văn C", "Phạm Thị D", "Hoàng Văn E"};

        Random random = new Random();

        for (int i = 0; i < titles.length; i++) {
            Item item = new Item();
            item.setId((long) (i + 1));
            item.setTitle(titles[i]);
            item.setDescription("Mô tả chi tiết cho " + titles[i] + ". Sản phẩm chất lượng cao, bảo hành đầy đủ.");
            item.setPrice(new BigDecimal(random.nextInt(50000000) + 1000000)); // 1M - 50M VND
            item.setLocation(locations[random.nextInt(locations.length)]);
            item.setCondition(conditions[random.nextInt(conditions.length)]);
            item.setCategoryId((long) (random.nextInt(5) + 1));

            // THÊM MỚI: Seller information
            item.setSellerId("seller_" + (i + 1));
            item.setSellerName(sellerNames[random.nextInt(sellerNames.length)]);

            // Add demo images
            List<String> imageUrls = Arrays.asList(
                    "https://picsum.photos/400/300?random=" + (i * 3 + 1),
                    "https://picsum.photos/400/300?random=" + (i * 3 + 2),
                    "https://picsum.photos/400/300?random=" + (i * 3 + 3)
            );
            item.setImageUrls(imageUrls);

            items.add(item);
        }

        return items;
    }

    private Item generateDemoItem(Long itemId) {
        List<Item> items = generateDemoItems();
        return items.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElse(items.get(0));
    }
}