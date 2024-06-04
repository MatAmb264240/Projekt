package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Info.InfoResponse;
import com.example.myapplication.Register.Api;
import com.example.myapplication.Register.RetrofitClient;
import com.example.myapplication.UserProfile.Watched.ReviewResponse;
import com.example.myapplication.Info.Review;
import com.example.myapplication.UserProfile.Watched.ReviewRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    private ImageView moviePoster;
    private TextView movieTitle, movieYear, moviePlot, movieRate, movieOpinion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        moviePoster = findViewById(R.id.movie_poster);
        movieTitle = findViewById(R.id.movie_title);
        movieYear = findViewById(R.id.movie_year);
        moviePlot = findViewById(R.id.movie_plot);
        movieRate = findViewById(R.id.movie_rate);
        movieOpinion = findViewById(R.id.movie_opinion);

        InfoResponse movie = (InfoResponse) getIntent().getSerializableExtra("movie");

        if (movie != null) {
            fetchMovieDetails(movie.getImdbID());
        }
    }

    private void fetchMovieDetails(String imdbID) {
        Api apiService = RetrofitClient.getInstance(this).getApi();
        Call<ReviewResponse> call = apiService.getMovieDetails(imdbID);

        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ReviewResponse movieDetail = response.body();
                    InfoResponse movie = movieDetail.getMovie();

                    movieTitle.setText(movie.getTitle());
                    movieYear.setText(movie.getYear());
                    moviePlot.setText(movie.getPlot());
                    Picasso.get().load(movie.getPoster()).into(moviePoster);

                    List<Review> reviews = movieDetail.getReviews();
                    StringBuilder reviewsText = new StringBuilder();
                    for (Review review : reviews) {
                        reviewsText.append("User: ").append(review.getUser()).append("\n")
                                .append("Rating: ").append(review.getRate()).append("\n")
                                .append("Review: ").append(review.getText()).append("\n\n");
                    }
                    movieOpinion.setText(reviewsText.toString());
                } else {
                    Toast.makeText(MovieDetailActivity.this, "Failed to load movie details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(MovieDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MovieDetailActivity", "onFailure: ", t);
            }
        });
    }
}
