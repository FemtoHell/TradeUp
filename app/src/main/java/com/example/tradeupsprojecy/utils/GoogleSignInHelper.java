// app/src/main/java/com/example/tradeupsprojecy/utils/GoogleSignInHelper.java
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

    public GoogleSignInHelper(Context context) {
        this.context = context;
        Log.d(TAG, "Initializing Google Sign-In");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        googleSignInClient = GoogleSignIn.getClient(context, gso);

        Log.d(TAG, "Google Sign-In client created successfully");
    }

    public Intent getSignInIntent() {
        Log.d(TAG, "Creating sign-in intent");
        return googleSignInClient.getSignInIntent();
    }

    public void handleSignInResult(ActivityResult result, GoogleSignInCallback callback) {
        Log.d(TAG, "Handling sign-in result");
        Log.d(TAG, "Result code: " + result.getResultCode());

        try {
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                    .getResult(ApiException.class);

            Log.d(TAG, "Sign-in successful");
            if (account != null) {
                Log.d(TAG, "Account email: " + account.getEmail());
                Log.d(TAG, "Account name: " + account.getDisplayName());
                Log.d(TAG, "Account ID: " + account.getId());
            }

            if (account != null && account.getEmail() != null) {
                callback.onResult(true, "Google Sign-In successful", account);
            } else {
                Log.e(TAG, "Account or email is null");
                callback.onResult(false, "Failed to get Google account information", null);
            }

        } catch (ApiException e) {
            Log.e(TAG, "ApiException: " + e.getStatusCode() + " - " + e.getMessage());

            String errorMessage;
            switch (e.getStatusCode()) {
                case 12501:
                    errorMessage = "Sign-in was cancelled";
                    break;
                case 12502:
                    errorMessage = "Sign-in is currently in progress";
                    break;
                case 12500:
                    errorMessage = "Sign-in configuration error";
                    break;
                case 7:
                    errorMessage = "Network error. Please check your internet connection";
                    break;
                default:
                    errorMessage = "Google Sign-In failed: " + e.getMessage();
                    break;
            }

            callback.onResult(false, errorMessage, null);

        } catch (Exception e) {
            Log.e(TAG, "Unexpected error: " + e.getMessage());
            e.printStackTrace();
            callback.onResult(false, "Unexpected error: " + e.getMessage(), null);
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