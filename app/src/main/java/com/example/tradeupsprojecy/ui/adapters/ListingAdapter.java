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
        this.listings = listings != null ? listings : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addListings(List<Listing> newListings) {
        if (newListings != null && !newListings.isEmpty()) {
            int startPosition = this.listings.size();
            this.listings.addAll(newListings);
            notifyItemRangeInserted(startPosition, newListings.size());
        }
    }

    public void addListing(Listing listing) {
        if (listing != null) {
            this.listings.add(0, listing);
            notifyItemInserted(0);
        }
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

        // FIX: Match với IDs trong item_listing.xml
        private ImageView listingImageView;
        private TextView titleTextView;
        private TextView priceTextView;
        private TextView locationTextView;
        private TextView conditionTextView;
        private TextView ratingTextView;

        public ListingViewHolder(@NonNull View itemView) {
            super(itemView);

            // FIX: Sử dụng đúng ID từ layout
            listingImageView = itemView.findViewById(R.id.listingImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            conditionTextView = itemView.findViewById(R.id.conditionTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);

            // Click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && position < listings.size()) {
                        listener.onListingClick(listings.get(position));
                    }
                }
            });

            // FIX: Favorite click (sẽ cần thêm favorite button vào layout sau)
            // Tạm thời comment out vì layout chưa có favorite button
            /*
            if (favoriteButton != null) {
                favoriteButton.setOnClickListener(v -> {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION && position < listings.size()) {
                            listener.onFavoriteClick(listings.get(position));
                        }
                    }
                });
            }
            */
        }

        public void bind(Listing listing) {
            if (listing == null) return;

            // Set title
            if (titleTextView != null) {
                titleTextView.setText(listing.getTitle() != null ? listing.getTitle() : "No title");
            }

            // Set price (layout có priceTextView ở góc trên)
            if (priceTextView != null) {
                try {
                    if (listing.getPrice() != null) {
                        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        priceTextView.setText(formatter.format(listing.getPrice()));
                    } else {
                        priceTextView.setText("Liên hệ");
                    }
                } catch (Exception e) {
                    priceTextView.setText("Liên hệ");
                }
            }

            // Set location
            if (locationTextView != null) {
                locationTextView.setText(listing.getLocation() != null ? listing.getLocation() : "2.5 km away");
            }

            // Set condition
            if (conditionTextView != null) {
                conditionTextView.setText(getConditionText(listing.getCondition()));
            }

            // Set rating (fake rating cho demo)
            if (ratingTextView != null) {
                ratingTextView.setText("4.5");
            }

            // Load image
            if (listingImageView != null) {
                if (listing.getImageUrls() != null && !listing.getImageUrls().isEmpty()) {
                    try {
                        Glide.with(context)
                                .load(listing.getImageUrls().get(0))
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .error(android.R.drawable.ic_dialog_alert)
                                .centerCrop()
                                .into(listingImageView);
                    } catch (Exception e) {
                        listingImageView.setImageResource(android.R.drawable.ic_menu_gallery);
                    }
                } else {
                    listingImageView.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            }
        }

        private String getConditionText(String condition) {
            if (condition == null) return "Like New";

            switch (condition) {
                case Constants.CONDITION_NEW: return "New";
                case Constants.CONDITION_LIKE_NEW: return "Like New";
                case Constants.CONDITION_GOOD: return "Good";
                case Constants.CONDITION_FAIR: return "Fair";
                case Constants.CONDITION_POOR: return "Poor";
                default: return "Like New";
            }
        }
    }
}