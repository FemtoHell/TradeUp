// app/src/main/java/com/example/tradeupsprojecy/ui/adapters/OnListingClickListener.java
package com.example.tradeupsprojecy.ui.adapters;

import com.example.tradeupsprojecy.data.models.Listing;

public interface OnListingClickListener {
    /**
     * Called when a listing item is clicked
     * @param listing The listing that was clicked
     */
    void onListingClick(Listing listing);

    /**
     * Called when the favorite button on a listing is clicked
     * @param listing The listing whose favorite button was clicked
     * @param position The position of the listing in the adapter
     */
    void onFavoriteClick(Listing listing, int position);
}