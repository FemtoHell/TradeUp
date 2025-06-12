package com.example.tradeupsprojecy.data.local;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteItem.class}, version = 1, exportSchema = false)
public abstract class FavoritesDatabase extends RoomDatabase {
    private static FavoritesDatabase INSTANCE;

    public abstract FavoriteDao favoriteDao();

    public static FavoritesDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavoritesDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FavoritesDatabase.class, "favorites_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}