package com.example.tradeupsprojecy;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tradeupsprojecy.models.AuthRequest;
import com.example.tradeupsprojecy.models.AuthResponse;
import com.example.tradeupsprojecy.network.NetworkClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button testButton = new Button(this);
        testButton.setText("Test Backend Connection");
        testButton.setOnClickListener(v -> testBackendConnection());
        setContentView(testButton);
    }

    private void testBackendConnection() {
        // Test register endpoint
        AuthRequest request = new AuthRequest("test@example.com", "password123", "Test User");

        NetworkClient.getApiService().register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.d(TAG, "Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Toast.makeText(TestActivity.this,
                            "Backend connection successful: " + authResponse.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TestActivity.this,
                            "Response not successful: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Connection failed: " + t.getMessage());
                Toast.makeText(TestActivity.this,
                        "Connection failed: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}