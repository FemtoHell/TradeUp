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
import com.example.tradeupsprojecy.data.local.FavoriteItem;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder> {

    private Context context;
    private List<FavoriteItem> favorites;
    private OnFavoriteItemClickListener listener;

    public interface OnFavoriteItemClickListener {
        void onItemClick(FavoriteItem favoriteItem);
        void onRemoveFromFavorites(FavoriteItem favoriteItem);
    }

    public FavoritesAdapter(Context context) {
        this.context = context;
        this.favorites = new ArrayList<>();
    }

    public void setOnFavoriteItemClickListener(OnFavoriteItemClickListener listener) {
        this.listener = listener;
    }

    public void setFavorites(List<FavoriteItem> favorites) {
        this.favorites = favorites != null ? favorites : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteItem favorite = favorites.get(position);
        holder.bind(favorite);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage, removeIcon;
        private TextView titleText, priceText, locationText;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            removeIcon = itemView.findViewById(R.id.removeIcon);
            titleText = itemView.findViewById(R.id.titleText);
            priceText = itemView.findViewById(R.id.priceText);
            locationText = itemView.findViewById(R.id.locationText);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(favorites.get(position));
                    }
                }
            });

            removeIcon.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRemoveFromFavorites(favorites.get(position));
                    }
                }
            });
        }

        public void bind(FavoriteItem favorite) {
            titleText.setText(favorite.getTitle());

            // Format price
            if (favorite.getPrice() != null && favorite.getPrice() > 0) {
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                priceText.setText(formatter.format(favorite.getPrice()));
            } else {
                priceText.setText("Liên hệ");
            }

            locationText.setText(favorite.getLocation() != null ? favorite.getLocation() : "Không xác định");

            // Load image
            if (favorite.getImageUrl() != null && !favorite.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(favorite.getImageUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .centerCrop()
                        .into(itemImage);
            } else {
                itemImage.setImageResource(R.drawable.placeholder_image);
            }
        }
    }
}