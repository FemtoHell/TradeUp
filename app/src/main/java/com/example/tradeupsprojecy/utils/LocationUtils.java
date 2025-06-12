// app/src/main/java/com/example/tradeupsprojecy/utils/LocationUtils.java
package com.example.tradeupsprojecy.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {

    private static final String TAG = "LocationUtils";

    private final Context context;
    private final FusedLocationProviderClient fusedLocationClient;
    private final Geocoder geocoder;

    public interface LocationCallback {
        void onLocationReceived(String address);
        void onLocationError(String error);
    }

    public LocationUtils(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        this.geocoder = new Geocoder(context, Locale.getDefault());
    }

    public void getCurrentLocation(LocationCallback callback) {
        if (!hasLocationPermission()) {
            callback.onLocationError("Location permission not granted");
            return;
        }

        if (!isLocationEnabled()) {
            callback.onLocationError("Location services are disabled");
            return;
        }

        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            Log.d(TAG, "Location received: " + location.getLatitude() + ", " + location.getLongitude());
                            getAddressFromLocation(location.getLatitude(), location.getLongitude(), callback);
                        } else {
                            Log.e(TAG, "Location is null");
                            callback.onLocationError("Unable to get current location");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to get location: " + e.getMessage());
                        callback.onLocationError("Failed to get location: " + e.getMessage());
                    });
        } catch (SecurityException e) {
            Log.e(TAG, "Security exception: " + e.getMessage());
            callback.onLocationError("Location permission denied");
        }
    }

    private void getAddressFromLocation(double latitude, double longitude, LocationCallback callback) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = formatAddress(address);
                Log.d(TAG, "Address found: " + addressText);
                callback.onLocationReceived(addressText);
            } else {
                Log.e(TAG, "No address found for coordinates");
                callback.onLocationError("No address found for current location");
            }
        } catch (IOException e) {
            Log.e(TAG, "Geocoder error: " + e.getMessage());
            callback.onLocationError("Failed to get address: " + e.getMessage());
        }
    }

    private String formatAddress(Address address) {
        StringBuilder sb = new StringBuilder();

        if (address.getThoroughfare() != null) {
            sb.append(address.getThoroughfare());
        }

        if (address.getSubLocality() != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(address.getSubLocality());
        }

        if (address.getLocality() != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(address.getLocality());
        }

        if (address.getAdminArea() != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(address.getAdminArea());
        }

        if (address.getCountryName() != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(address.getCountryName());
        }

        return sb.toString();
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null &&
                (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in km

        return distance;
    }
}