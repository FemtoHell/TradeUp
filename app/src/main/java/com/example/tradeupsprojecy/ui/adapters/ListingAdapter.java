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

import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.entities.Item;
import com.example.tradeupsprojecy.utils.Constants; // ✅ THÊM IMPORT NÀY

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {

    private Context context;
    private List<Item> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public ListingAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);

        holder.titleTextView.setText(item.getTitle());
        holder.priceTextView.setText("$" + item.getPrice());
        holder.locationTextView.setText(item.getLocation());

        // ✅ SỬ DỤNG Constants
        String condition = item.getCondition();
        if (condition != null) {
            switch (condition) {
                case Constants.CONDITION_NEW:
                    holder.conditionTextView.setText("New");
                    break;
                case Constants.CONDITION_LIKE_NEW:
                    holder.conditionTextView.setText("Like New");
                    break;
                case Constants.CONDITION_GOOD:
                    holder.conditionTextView.setText("Good");
                    break;
                case Constants.CONDITION_FAIR:
                    holder.conditionTextView.setText("Fair");
                    break;
                case Constants.CONDITION_POOR:
                    holder.conditionTextView.setText("Poor");
                    break;
                default:
                    holder.conditionTextView.setText(condition);
                    break;
            }
        }

        // Set rating if available
        if (item.getSeller() != null && item.getSeller().getAverageRating() != null) {
            holder.ratingTextView.setText(String.valueOf(item.getSeller().getAverageRating()));
        } else {
            holder.ratingTextView.setText("0.0");
        }

        // TODO: Load image using Glide
        // Glide.with(context)
        //     .load(item.getImages())
        //     .placeholder(R.drawable.placeholder_image)
        //     .into(holder.listingImageView);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView listingImageView;
        TextView titleTextView;
        TextView priceTextView;
        TextView conditionTextView;
        TextView locationTextView;
        TextView ratingTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listingImageView = itemView.findViewById(R.id.listingImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            conditionTextView = itemView.findViewById(R.id.conditionTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
        }
    }
}