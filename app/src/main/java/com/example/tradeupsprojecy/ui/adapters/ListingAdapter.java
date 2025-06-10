// app/src/main/java/com/example/tradeupsprojecy/ui/adapters/ListingAdapter.java
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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.entities.Item;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {

    private final Context context;
    private List<Item> items;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public ListingAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void updateItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false);
        return new ListingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListingViewHolder holder, int position) {
        Item item = items.get(position);

        // Title
        holder.titleTextView.setText(item.getTitle() != null ? item.getTitle() : "No title");

        // Price
        if (item.getPrice() != null) {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            holder.priceTextView.setText(formatter.format(item.getPrice()));
        } else {
            holder.priceTextView.setText("Price not available");
        }

        // Location
        holder.locationTextView.setText(item.getLocation() != null ? item.getLocation() : "Location not specified");

        // Seller name
        holder.sellerText.setText(item.getSellerName() != null ? item.getSellerName() : "Unknown seller"); // Đổi tên

        // Image
        if (item.getImageUrls() != null && !item.getImageUrls().isEmpty()) {
            String imageUrl = item.getImageUrls().get(0);
            Glide.with(context)
                    .load(imageUrl)
                    .transform(new CenterCrop(), new RoundedCorners(16))
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_close_clear_cancel)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // Click listener
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ListingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView priceTextView;
        TextView locationTextView;
        TextView sellerText; // Đổi tên

        public ListingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            sellerText = itemView.findViewById(R.id.sellerText); // Đổi tên
        }
    }
}