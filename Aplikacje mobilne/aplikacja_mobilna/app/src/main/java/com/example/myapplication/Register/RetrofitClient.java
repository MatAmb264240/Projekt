package com.example.myapplication.Register;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.UserProfile.RefreshTokenRequest;
import com.example.myapplication.UserProfile.RefreshTokenResponse;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitClient {
    private static RetrofitClient instance;
    private Api api;
    private Retrofit retrofit;

    private RetrofitClient(Context context) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        // Logging interceptor for debugging
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(logging);

        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs2", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("access_token", null);


                Log.d("RetrofitClient", "Token from SharedPreferences: " + token);
                if (token != null) {
                    Request request = chain.request().newBuilder()
                            //.addHeader("Authorization", "Bearer " + token)
                            .build();

                    return chain.proceed(request);
                } else {
                    return chain.proceed(chain.request());
                }
            }
        });

        OkHttpClient client = clientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.56.1:8000/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(Api.class);
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
        }
        return instance;
    }

    public Api getApi() {
        return api;
    }
}
