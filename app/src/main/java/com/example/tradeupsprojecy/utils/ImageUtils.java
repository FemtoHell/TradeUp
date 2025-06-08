package com.example.tradeupsprojecy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.webkit.MimeTypeMap;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {

    private static final int MAX_IMAGE_SIZE = 1024; // KB
    private static final int IMAGE_QUALITY = 80; // Compression quality 0-100
    private static final int MAX_DIMENSION = 1080; // Max width/height in pixels

    // Compress image from Uri
    public static Bitmap compressImage(Context context, Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);

            if (originalBitmap == null) return null;

            // Handle image rotation
            Bitmap rotatedBitmap = handleImageRotation(context, imageUri, originalBitmap);

            // Resize if too large
            Bitmap resizedBitmap = resizeBitmap(rotatedBitmap, MAX_DIMENSION);

            // Compress to target size
            return compressBitmapToSize(resizedBitmap, MAX_IMAGE_SIZE);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Handle image rotation based on EXIF data
    private static Bitmap handleImageRotation(Context context, Uri imageUri, Bitmap bitmap) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            ExifInterface exif = new ExifInterface(inputStream);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    break;
                default:
                    return bitmap;
            }

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    // Resize bitmap to fit within max dimensions
    private static Bitmap resizeBitmap(Bitmap bitmap, int maxDimension) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= maxDimension && height <= maxDimension) {
            return bitmap;
        }

        float ratio = Math.min((float) maxDimension / width, (float) maxDimension / height);
        int newWidth = Math.round(width * ratio);
        int newHeight = Math.round(height * ratio);

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    // Compress bitmap to target file size
    private static Bitmap compressBitmapToSize(Bitmap bitmap, int maxSizeKB) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int quality = IMAGE_QUALITY;

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);

        // Reduce quality if file size is too large
        while (stream.toByteArray().length / 1024 > maxSizeKB && quality > 10) {
            stream.reset();
            quality -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        }

        byte[] bitmapData = stream.toByteArray();
        return BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
    }

    // Convert bitmap to base64 string
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Convert base64 string to bitmap
    public static Bitmap base64ToBitmap(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Save bitmap to file
    public static File saveBitmapToFile(Context context, Bitmap bitmap, String filename) {
        try {
            File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "TradeUps");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, filename);
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream);
            outputStream.flush();
            outputStream.close();

            return file;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Generate unique filename
    public static String generateImageFilename() {
        SimpleDateFormat timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        return "IMG_" + timeStamp.format(new Date()) + ".jpg";
    }

    // Get file extension from Uri
    public static String getFileExtension(Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        if (mimeType != null) {
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        }
        return "jpg";
    }

    // Check if file is image
    public static boolean isImageFile(Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        return mimeType != null && mimeType.startsWith("image/");
    }

    // Get image size in KB
    public static int getImageSizeKB(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray().length / 1024;
    }

    // Create circular bitmap
    public static Bitmap createCircularBitmap(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        android.graphics.Canvas canvas = new android.graphics.Canvas(output);
        android.graphics.Paint paint = new android.graphics.Paint();
        android.graphics.Rect rect = new android.graphics.Rect(0, 0, size, size);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint);

        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}