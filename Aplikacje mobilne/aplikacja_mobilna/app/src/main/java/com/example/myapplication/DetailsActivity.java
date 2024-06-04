package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.RateRecylerAdapter;
import com.example.myapplication.Info.InfoResponse;
import com.example.myapplication.Listeners.OnDetailsApiListener;
import com.example.myapplication.UserProfile.Watched.DefaultResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    TextView textView_movie_plot, textView_movie_name, textView_movie_released, textView_movie_runtime, textView_movie_actors;
    ImageView imageView_movie_poster;
    RecyclerView recycler_movie_ratings;
    RatingBar user_movie_rating;
    EditText user_movie_text;
    Button submit_button;
    RateRecylerAdapter adapter;
    RequestManager manager;
    ProgressDialog dialog;
    Integer movieId = 1; // ID filmu w bazie danych Movie aplikacji
    String imdbID; // ID filmu z API
    int userId;
    InfoResponse movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textView_movie_plot = findViewById(R.id.textView_movie_plot);
        textView_movie_name = findViewById(R.id.textView_movie_name);
        textView_movie_released = findViewById(R.id.textView_movie_released);
        textView_movie_runtime = findViewById(R.id.textView_movie_runtime);
        textView_movie_actors = findViewById(R.id.textView_movie_actors);
        imageView_movie_poster = findViewById(R.id.imageView_movie_poster);
        recycler_movie_ratings = findViewById(R.id.recycler_movie_ratings);
        submit_button = findViewById(R.id.submit_button);
        user_movie_rating = findViewById(R.id.user_movie_rating);
        user_movie_text = findViewById(R.id.user_movie_text);

        manager = new RequestManager(this);


        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs2", MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", 1);

        imdbID = getIntent().getStringExtra("data");


        //tu powinno być pobranie / dodanie filmu do tablicy
        fetchMovieDetails();


        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait...");
        dialog.show();

        // Wyszukaj szczegóły filmu używając IMDb ID
        manager.searchMovieDetails(listener, imdbID);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview();
            }
        });

    }

    //ta funkcja będzie do pobrania movie_id
    private void fetchMovieDetails() {
        RequestManager requestManager = new RequestManager(this);
    }
    private void addReview()
    {

        String review = user_movie_text.getText().toString().trim();
        int rating = (int) user_movie_rating.getRating();

        RequestManager requestManager = new RequestManager(this);
        requestManager.addReview(userId, movieId, imdbID, review, rating, movie, new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DetailsActivity.this, "Failed to add review", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }




    private OnDetailsApiListener listener = new OnDetailsApiListener() {
        @Override
        public void onResponse(InfoResponse response) {
            dialog.dismiss();
            if (response == null) {
                Toast.makeText(DetailsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                return;
            }
            showResults(response);
            movie = response;

        }

        @Override
        public void onError(String message) {
            dialog.dismiss();
            Toast.makeText(DetailsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
        }
    };

    private void showResults(InfoResponse response)
    {
        textView_movie_name.setText(response.getTitle());
        textView_movie_released.setText("Year released: " + response.getYear());
        textView_movie_runtime.setText("Length: " + response.getRuntime());
        textView_movie_actors.setText("Actors: " + response.getActors());
        textView_movie_plot.setText(response.getPlot());

        try {
            Picasso.get().load(response.getPosterURL()).into(imageView_movie_poster);
        } catch (Exception e) {
            e.printStackTrace();
        }

        recycler_movie_ratings.setHasFixedSize(true);
        recycler_movie_ratings.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new RateRecylerAdapter(this, response.getRatings());
        recycler_movie_ratings.setAdapter(adapter);
    }
}
