package com.example.tradeupsprojecy.data.repository;

import com.example.tradeupsprojecy.data.models.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    public interface CategoriesCallback {
        void onSuccess(List<Category> categories);
        void onError(String error);
    }

    public void getCategories(CategoriesCallback callback) {
        new Thread(() -> {
            try {
                Thread.sleep(500);
                List<Category> categories = generateDemoCategories();
                callback.onSuccess(categories);
            } catch (Exception e) {
                callback.onError(e.getMessage());
            }
        }).start();
    }

    public void getAllCategories(CategoriesCallback callback) {
        getCategories(callback);
    }

    private List<Category> generateDemoCategories() {
        List<Category> categories = new ArrayList<>();

        String[] names = {"Điện tử", "Thời trang", "Xe cộ", "Nhà cửa", "Thể thao", "Sách", "Mỹ phẩm", "Đồ chơi"};
        String[] descriptions = {
                "Điện thoại, laptop, máy tính bảng",
                "Quần áo, giày dép, phụ kiện",
                "Xe máy, ô tô, phụ tung",
                "Nội thất, đồ gia dụng",
                "Dụng cụ thể thao, quần áo thể thao",
                "Sách giáo khoa, tiểu thuyết, truyện tranh",
                "Mỹ phẩm, chăm sóc da",
                "Đồ chơi trẻ em, mô hình"
        };

        for (int i = 0; i < names.length; i++) {
            Category category = new Category();
            category.setId((long) (i + 1));
            category.setName(names[i]);
            category.setDescription(descriptions[i]);
            categories.add(category);
        }

        return categories;
    }
}