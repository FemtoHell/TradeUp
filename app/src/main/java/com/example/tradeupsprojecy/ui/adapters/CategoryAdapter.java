package com.example.tradeupsprojecy.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categories;
    private OnCategoryClickListener listener;
    private int selectedPosition = -1;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
    }

    public CategoryAdapter(Context context) {
        this.context = context;
        this.categories = new ArrayList<>();
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories != null ? categories : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        int oldPosition = selectedPosition;
        selectedPosition = position;

        if (oldPosition != -1) {
            notifyItemChanged(oldPosition);
        }
        if (selectedPosition != -1) {
            notifyItemChanged(selectedPosition);
        }
    }

    public Category getSelectedCategory() {
        if (selectedPosition != -1 && selectedPosition < categories.size()) {
            return categories.get(selectedPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCategoryIcon;
        private TextView tvCategoryName;
        private View categoryContainer;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCategoryIcon = itemView.findViewById(R.id.iv_category_icon);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            categoryContainer = itemView.findViewById(R.id.category_container);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && position < categories.size()) {
                        setSelectedPosition(position);
                        listener.onCategoryClick(categories.get(position), position);
                    }
                }
            });
        }

        public void bind(Category category, boolean isSelected) {
            if (category == null) return;

            // Set category name
            if (tvCategoryName != null) {
                tvCategoryName.setText(category.getName() != null ? category.getName() : "Unknown");
            }

            // Set selected state
            if (categoryContainer != null) {
                categoryContainer.setSelected(isSelected);
                // Change background color based on selection
                categoryContainer.setBackgroundColor(isSelected ?
                        context.getResources().getColor(android.R.color.holo_blue_light) :
                        context.getResources().getColor(android.R.color.transparent));
            }

            // Load category icon
            if (ivCategoryIcon != null) {
                if (category.getIconUrl() != null && !category.getIconUrl().isEmpty()) {
                    try {
                        Glide.with(context)
                                .load(category.getIconUrl())
                                .placeholder(android.R.drawable.ic_menu_info_details)
                                .error(android.R.drawable.ic_menu_info_details)
                                .into(ivCategoryIcon);
                    } catch (Exception e) {
                        ivCategoryIcon.setImageResource(getDefaultCategoryIcon(category.getName()));
                    }
                } else {
                    // Set default icon based on category name
                    ivCategoryIcon.setImageResource(getDefaultCategoryIcon(category.getName()));
                }
            }
        }

        private int getDefaultCategoryIcon(String categoryName) {
            if (categoryName == null) return android.R.drawable.ic_menu_info_details;

            // Use system drawables as fallback
            switch (categoryName.toLowerCase()) {
                case "điện tử":
                    return android.R.drawable.ic_menu_call;
                case "thời trang":
                    return android.R.drawable.ic_menu_gallery;
                case "xe cộ":
                    return android.R.drawable.ic_menu_directions;
                case "nhà cửa":
                    return android.R.drawable.ic_menu_myplaces;
                case "sách":
                    return android.R.drawable.ic_menu_agenda;
                case "thể thao":
                    return android.R.drawable.ic_menu_compass;
                case "sức khỏe":
                    return android.R.drawable.ic_menu_help;
                default:
                    return android.R.drawable.ic_menu_info_details;
            }
        }
    }
}