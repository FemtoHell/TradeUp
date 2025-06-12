// app/src/main/java/com/example/tradeupsprojecy/utils/ImageUtils.java
package com.example.tradeupsprojecy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    private static final String TAG = "ImageUtils";
    private static final int MAX_IMAGE_SIZE = 1024; // Max width/height in pixels
    private static final int JPEG_QUALITY = 80; // JPEG compression quality

    private final Context context;

    public ImageUtils(Context context) {
        this.context = context;
    }

    public byte[] compressImage(Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            if (originalBitmap == null) {
                Log.e(TAG, "Failed to decode image");
                return null;
            }

            // Resize bitmap if too large
            Bitmap resizedBitmap = resizeBitmap(originalBitmap);

            // Compress to JPEG
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, outputStream);
            byte[] compressedData = outputStream.toByteArray();

            // Clean up
            outputStream.close();
            if (resizedBitmap != originalBitmap) {
                resizedBitmap.recycle();
            }
            originalBitmap.recycle();

            Log.d(TAG, "Image compressed. Original size: ~" + (compressedData.length / 1024) + "KB");
            return compressedData;

        } catch (FileNotFoundException e) {
            Log.e(TAG, "Image file not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IO error compressing image: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Error compressing image: " + e.getMessage());
        }
        return null;
    }

    private Bitmap resizeBitmap(Bitmap originalBitmap) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();

        // Calculate new dimensions
        float scale = Math.min(
                (float) MAX_IMAGE_SIZE / width,
                (float) MAX_IMAGE_SIZE / height
        );

        if (scale >= 1.0f) {
            // No need to resize
            return originalBitmap;
        }

        int newWidth = Math.round(width * scale);
        int newHeight = Math.round(height * scale);

        Log.d(TAG, "Resizing image from " + width + "x" + height + " to " + newWidth + "x" + newHeight);

        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
    }

    public static String getImageMimeType(Uri imageUri, Context context) {
        try {
            return context.getContentResolver().getType(imageUri);
        } catch (Exception e) {
            Log.e(TAG, "Error getting MIME type: " + e.getMessage());
            return "image/jpeg"; // Default fallback
        }
    }

    public static boolean isValidImageUri(Uri imageUri, Context context) {
        if (imageUri == null) return false;

        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            if (inputStream == null) return false;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            return options.outWidth > 0 && options.outHeight > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error validating image URI: " + e.getMessage());
            return false;
        }
    }
}