package com.example.myapplication;

import android.content.Context;
import android.widget.Toast;

import com.example.myapplication.Info.InfoResponse;
import com.example.myapplication.Listeners.OnDetailsApiListener;
import com.example.myapplication.Listeners.OnSearchApiListener;
import com.example.myapplication.Login.LoginCallback;
import com.example.myapplication.Login.LoginRequest;
import com.example.myapplication.Login.LoginResponse;
import com.example.myapplication.Register.Api;
import com.example.myapplication.Info.SearchResponse;
import com.example.myapplication.UserProfile.Watched.DefaultResponse;
import com.example.myapplication.UserProfile.Watched.ReviewRequest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class RequestManager {
    Context context;

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.56.1:8000/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit omdbRetrofit = new Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private Api api = retrofit.create(Api.class);

x    public RequestManager(Context context) {
        this.context = context;
    }

    public Api getApi() {
        return api;
    }

    public void login(String username, String password, final LoginCallback callback) {
        Api authService = retrofit.create(Api.class);
        Call<LoginResponse> call = authService.login(new LoginRequest(username, password));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Login failed");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public void addReview(int userId, int movieId, String imdbID, String text, int rate, InfoResponse movie,  Callback<DefaultResponse> callback) {
        ReviewRequest request = new ReviewRequest(userId, movieId, imdbID, text, rate, movie);
        api.addReview(request).enqueue(callback);

    }

    public void searchMovies(OnSearchApiListener listener, String movie_name) {
        getMovies getMovies = omdbRetrofit.create(RequestManager.getMovies.class);
        Call<SearchResponse> call = getMovies.getMovieList("790c537", movie_name);

        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(context, "Couldn't fetch Data!", Toast.LENGTH_LONG).show();
                    return;
                }
                listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public void searchMovieDetails(OnDetailsApiListener listener, String movie_id) {
        getMovieDetails movieDetails = omdbRetrofit.create(RequestManager.getMovieDetails.class);
        Call<InfoResponse> call = movieDetails.calMovieDetails("790c537", movie_id);

        call.enqueue(new Callback<InfoResponse>() {
            @Override
            public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(context, "Couldn't fetch Data!", Toast.LENGTH_LONG).show();
                    return;
                }
                listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<InfoResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public interface getMovies {
        @Headers({
                "Content-Type: application/json;charset=utf-8",
                "Accept: application/json"
        })
        @GET("/")
        Call<SearchResponse> getMovieList(@Query("apikey") String apikey, @Query("s") String movie_name);
    }

    public interface getMovieDetails {
        @Headers({
                "Content-Type: application/json;charset=utf-8",
                "Accept: application/json"
        })
        @GET("/")
        Call<InfoResponse> calMovieDetails(@Query("apikey") String apikey, @Query("i") String movie_id);
    }
}
