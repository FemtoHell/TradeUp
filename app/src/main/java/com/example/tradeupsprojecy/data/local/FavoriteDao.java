package com.example.tradeupsprojecy.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM favorite_items ORDER BY dateAdded DESC")
    LiveData<List<FavoriteItem>> getAllFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addFavorite(FavoriteItem item);

    @Query("DELETE FROM favorite_items WHERE itemId = :itemId")
    void removeFavorite(Long itemId);

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_items WHERE itemId = :itemId)")
    boolean isFavorite(Long itemId);
}