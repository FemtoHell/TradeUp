package com.example.tradeupsprojecy.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.example.tradeupsprojecy.R;

public class GoogleSignInHelper {

    private static final String TAG = "GoogleSignInHelper";
    private final GoogleSignInClient googleSignInClient;
    private final Context context;

    public interface GoogleSignInCallback {
        void onResult(boolean success, String message, GoogleSignInAccount account);
    }

    public interface SimpleCallback {
        void onComplete();
    }

    // ✅ SINGLE Constructor
    public GoogleSignInHelper(Context context) {
        this.context = context;
        Log.d(TAG, "=== GOOGLE CLOUD CONSOLE SETUP ===");

        // ✅ Web Client ID for ID Token
        String webClientId = context.getString(R.string.default_web_client_id);
        Log.d(TAG, "Web Client ID: " + webClientId);
        Log.d(TAG, "Package: " + context.getPackageName());
        Log.d(TAG, "Expected SHA-1: 6D:8B:41:49:34:29:AE:BD:1E:B3:71:DE:3B:56:75:D8:4D:22:E7:53");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)  // ✅ Web Client ID
                .requestEmail()
                .requestProfile()
                .build();

        googleSignInClient = GoogleSignIn.getClient(context, gso);
        Log.d(TAG, "✅ Complete Google Cloud Console setup");
    }

    public Intent getSignInIntent() {
        Log.d(TAG, "Creating sign-in intent");
        return googleSignInClient.getSignInIntent();
    }

    public void handleSignInResult(ActivityResult result, GoogleSignInCallback callback) {
        Log.d(TAG, "=== GOOGLE SIGN IN DEBUG ===");
        Log.d(TAG, "Result code: " + result.getResultCode());
        Log.d(TAG, "Web Client ID: " + context.getString(R.string.default_web_client_id));

        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            GoogleSignInAccount account = task.getResult(ApiException.class);

            Log.d(TAG, "🎉 GOOGLE SIGN-IN SUCCESS!");
            Log.d(TAG, "Name: " + account.getDisplayName());
            Log.d(TAG, "Email: " + account.getEmail());
            Log.d(TAG, "Photo: " + account.getPhotoUrl());
            Log.d(TAG, "ID Token present: " + (account.getIdToken() != null));

            // ✅ Success callback
            callback.onResult(true, "Sign-in successful", account);

        } catch (ApiException e) {
            Log.e(TAG, "❌ GOOGLE SIGN IN FAILED!");
            Log.e(TAG, "Status Code: " + e.getStatusCode());
            Log.e(TAG, "Status Message: " + e.getStatusMessage());
            Log.e(TAG, "Error Details: " + e.toString());

            String errorMessage;
            switch (e.getStatusCode()) {
                case 10:
                    errorMessage = "DEVELOPER_ERROR - Check SHA-1, package name, and client ID";
                    Log.e(TAG, "❌ Configuration issue detected!");
                    Log.e(TAG, "- Verify SHA-1 fingerprint matches debug keystore");
                    Log.e(TAG, "- Verify package name: com.example.tradeupsprojecy");
                    Log.e(TAG, "- Verify Web Client ID in strings.xml");
                    break;
                case 12500:
                    errorMessage = "Sign-in configuration error";
                    break;
                case 12501:
                    errorMessage = "Sign-in was cancelled by user";
                    break;
                case 12502:
                    errorMessage = "Sign-in is currently in progress";
                    break;
                case 7:
                    errorMessage = "Network error - Check internet connection";
                    break;
                default:
                    errorMessage = "Google Sign-In failed: " + e.getMessage();
                    break;
            }

            Log.e(TAG, "Final error message: " + errorMessage);
            callback.onResult(false, errorMessage, null);
        }
    }

    public void signOut(SimpleCallback callback) {
        Log.d(TAG, "Signing out from Google");
        googleSignInClient.signOut()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Google sign-out successful");
                    } else {
                        Log.e(TAG, "Google sign-out failed: " +
                                (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    }
                    if (callback != null) {
                        callback.onComplete();
                    }
                });
    }

    public void signOut() {
        signOut(null);
    }

    public void revokeAccess(SimpleCallback callback) {
        Log.d(TAG, "Revoking Google access");
        googleSignInClient.revokeAccess()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Google access revoked successfully");
                    } else {
                        Log.e(TAG, "Failed to revoke Google access: " +
                                (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    }
                    if (callback != null) {
                        callback.onComplete();
                    }
                });
    }

    public void revokeAccess() {
        revokeAccess(null);
    }

    public GoogleSignInAccount getLastSignedInAccount() {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    public boolean isSignedIn() {
        return getLastSignedInAccount() != null;
    }
}