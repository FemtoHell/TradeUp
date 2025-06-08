package com.example.tradeupsprojecy.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Listing;
import com.example.tradeupsprojecy.utils.Constants;
import com.example.tradeupsprojecy.utils.DateUtils;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {

    private Context context;
    private List<Listing> listings;
    private OnListingClickListener listener;

    public interface OnListingClickListener {
        void onListingClick(Listing listing);
        void onFavoriteClick(Listing listing);
    }

    public ListingAdapter(Context context) {
        this.context = context;
        this.listings = new ArrayList<>();
    }

    public void setOnListingClickListener(OnListingClickListener listener) {
        this.listener = listener;
    }

    public void setListings(List<Listing> listings) {
        this.listings = listings;
        notifyDataSetChanged();
    }

    public void addListings(List<Listing> newListings) {
        int startPosition = this.listings.size();
        this.listings.addAll(newListings);
        notifyItemRangeInserted(startPosition, newListings.size());
    }

    public void addListing(Listing listing) {
        this.listings.add(0, listing);
        notifyItemInserted(0);
    }

    public void removeListing(int position) {
        if (position >= 0 && position < listings.size()) {
            listings.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clearListings() {
        listings.clear();
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
        Listing listing = listings.get(position);
        holder.bind(listing);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    class ListingViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivListingImage;
        private TextView tvTitle;
        private TextView tvPrice;
        private TextView tvLocation;
        private TextView tvCondition;
        private TextView tvTime;
        private ImageView ivFavorite;
        private View layoutFeatured;

        public ListingViewHolder(@NonNull View itemView) {
            super(itemView);

            ivListingImage = itemView.findViewById(R.id.iv_listing_image);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvCondition = itemView.findViewById(R.id.tv_condition);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivFavorite = itemView.findViewById(R.id.iv_favorite);
            layoutFeatured = itemView.findViewById(R.id.layout_featured);

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onListingClick(listings.get(position));
                    }
                }
            });

            ivFavorite.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onFavoriteClick(listings.get(position));
                    }
                }
            });
        }

        public void bind(Listing listing) {
            // Set title
            tvTitle.setText(listing.getTitle());

            // Set price
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvPrice.setText(formatter.format(listing.getPrice()));

            // Set location
            tvLocation.setText(listing.getLocation() != null ? listing.getLocation() : "Chưa có địa chỉ");

            // Set condition
            tvCondition.setText(getConditionText(listing.getCondition()));

            // Set time
            tvTime.setText(DateUtils.getTimeAgo(listing.getCreatedAt()));

            // Set featured badge
            layoutFeatured.setVisibility(listing.getIsFeatured() ? View.VISIBLE : View.GONE);

            // Load image
            if (listing.getImageUrls() != null && !listing.getImageUrls().isEmpty()) {
                Glide.with(context)
                        .load(listing.getImageUrls().get(0))
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .centerCrop()
                        .into(ivListingImage);
            } else {
                ivListingImage.setImageResource(R.drawable.placeholder_image);
            }

            // Set favorite state (implement later with user preferences)
            // ivFavorite.setImageResource(listing.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        }

        private String getConditionText(String condition) {
            if (condition == null) return "Không rõ";

            switch (condition) {
                case Constants.CONDITION_NEW: return "Mới";
                case Constants.CONDITION_LIKE_NEW: return "Như mới";
                case Constants.CONDITION_GOOD: return "Tốt";
                case Constants.CONDITION_FAIR: return "Khá";
                case Constants.CONDITION_POOR: return "Cũ";
                default: return "Không rõ";
            }
        }
    }
}