// app/src/main/java/com/example/tradeupsprojecy/ui/adapters/ItemAdapter.java - NEW FILE
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
import com.example.tradeupsprojecy.data.models.Item;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context context;
    private List<Item> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
        void onFavoriteClick(Item item, int position);
    }

    public ItemAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items != null ? items : new ArrayList<>();
    }

    public ItemAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Item> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addItems(List<Item> newItems) {
        if (newItems != null) {
            int startPosition = this.items.size();
            this.items.addAll(newItems);
            notifyItemRangeInserted(startPosition, newItems.size());
        }
    }

    public void clearItems() {
        this.items.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage, favoriteIcon;
        private TextView titleText, priceText, locationText, conditionText;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
            titleText = itemView.findViewById(R.id.titleText);
            priceText = itemView.findViewById(R.id.priceText);
            locationText = itemView.findViewById(R.id.locationText);
            conditionText = itemView.findViewById(R.id.conditionText);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(items.get(position));
                    }
                }
            });

            favoriteIcon.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onFavoriteClick(items.get(position), position);
                    }
                }
            });
        }

        public void bind(Item item) {
            titleText.setText(item.getTitle());

            // Format price
            if (item.getPrice() != null) {
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                priceText.setText(formatter.format(item.getPrice()));
            } else {
                priceText.setText("Liên hệ");
            }

            locationText.setText(item.getLocation() != null ? item.getLocation() : "Không xác định");
            conditionText.setText(item.getCondition() != null ? item.getCondition() : "Mới");

            // Load image từ parsed imageUrls
            String firstImage = item.getFirstImage();
            if (firstImage != null && !firstImage.isEmpty()) {
                Glide.with(context)
                        .load(firstImage)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .centerCrop()
                        .into(itemImage);
            } else {
                itemImage.setImageResource(R.drawable.placeholder_image);
            }

            favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
        }
    }
}