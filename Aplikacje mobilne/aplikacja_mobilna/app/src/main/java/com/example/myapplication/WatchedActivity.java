package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.MovieAdapter;
import com.example.myapplication.Info.InfoResponse;
import com.example.myapplication.Listeners.OnWatchedMovieClickListener;
import com.example.myapplication.Register.Api;
import com.example.myapplication.Register.RetrofitClient;
import com.example.myapplication.UserProfile.Watched.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class WatchedActivity extends AppCompatActivity implements OnWatchedMovieClickListener {
    protected RecyclerView recyclerView;
    protected MovieAdapter movieAdapter;
    protected List<InfoResponse> moviesList;
    String username;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watched);

        recyclerView = findViewById(R.id.recycler_view_movies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // Use GridLayoutManager for grid view

        moviesList = new ArrayList<>();
        movieAdapter = new MovieAdapter(moviesList, this);
        recyclerView.setAdapter(movieAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs2", MODE_PRIVATE);
        username = sharedPreferences.getString("username", null);

        if (username == null) {
            startActivity(new Intent(WatchedActivity.this, LoginActivity.class));
            finish();
            return;
        }

        fetchWatchedItems();
    }

    @Override
    public void onMovieClicked(InfoResponse movie) {
        Intent intent = new Intent(WatchedActivity.this, MovieDetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    private void fetchWatchedItems() {
        Api apiService = RetrofitClient.getInstance(this).getApi();
        Call<MoviesResponse> call = getApiCall(apiService, username);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<InfoResponse> moviesWatched = response.body().getMoviesWatched();
                    moviesList.addAll(moviesWatched);
                    movieAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Toast.makeText(WatchedActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("WatchedActivity", "onFailure: ", t);
            }
        });
    }

    protected abstract Call<MoviesResponse> getApiCall(Api apiService, String username);
}