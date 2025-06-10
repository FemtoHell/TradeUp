// app/src/main/java/com/example/tradeupsprojecy/data/api/ApiClient.java
package com.example.tradeupsprojecy.data.api;

import com.example.tradeupsprojecy.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;

    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getClient().create(ApiService.class);
        }
        return apiService;
    }

    private static Retrofit getClient() {
        if (retrofit == null) {
            // Logging interceptor
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp client with timeouts
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            // Gson configuration
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}