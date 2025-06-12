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
import com.example.tradeupsprojecy.R;
import com.example.tradeupsprojecy.data.models.Listing;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingViewHolder> {

    private Context context;
    private List<Listing> listings;
    private OnListingClickListener listener;

    public ListingAdapter(Context context, List<Listing> listings) {
        this.context = context;
        this.listings = listings != null ? listings : new ArrayList<>();
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
        if (newListings != null) {
            int startPosition = this.listings.size();
            this.listings.addAll(newListings);
            notifyItemRangeInserted(startPosition, newListings.size());
        }
    }

    public void clearListings() {
        this.listings.clear();
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
        private ImageView itemImage, favoriteIcon;
        private TextView titleText, priceText, locationText, conditionText;

        public ListingViewHolder(@NonNull View itemView) {
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
                        listener.onListingClick(listings.get(position));
                    }
                }
            });

            favoriteIcon.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onFavoriteClick(listings.get(position), position);
                    }
                }
            });
        }

        public void bind(Listing listing) {
            titleText.setText(listing.getTitle());

            // Format price
            if (listing.getPrice() != null) {
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                priceText.setText(formatter.format(listing.getPrice()));
            } else {
                priceText.setText("Liên hệ");
            }

            locationText.setText(listing.getLocation() != null ? listing.getLocation() : "Không xác định");
            conditionText.setText(listing.getCondition() != null ? listing.getCondition() : "Mới");

            // Load image
            if (listing.getImages() != null && !listing.getImages().isEmpty()) {
                Glide.with(context)
                        .load(listing.getImages().get(0))
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .centerCrop()
                        .into(itemImage);
            } else {
                itemImage.setImageResource(R.drawable.placeholder_image);
            }

            // Set favorite state (you can implement this based on your requirements)
            favoriteIcon.setImageResource(R.drawable.ic_favorite_border);
        }
    }
}