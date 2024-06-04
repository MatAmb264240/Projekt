package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.HomeRecyclerAdapter;
import com.example.myapplication.Listeners.OnMovieClickListener;
import com.example.myapplication.Listeners.OnSearchApiListener;
import com.example.myapplication.Info.SearchResponse;

public class MainActivity extends AppCompatActivity implements OnMovieClickListener {
    SearchView search_view;
    RecyclerView recycler_view_home;
    HomeRecyclerAdapter adapter;

    RequestManager manager;

    ProgressDialog dialog;

    Button buttonAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Sprawdzenie, czy użytkownik jest zalogowany

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs2", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);
        Log.d("EditProfileActivity", "Access Token FROM MAIN  ACTIV: " + accessToken);
        if (accessToken == null) {
            // Jeśli nie ma tokena dostępu, przejdź do LoginActivity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish(); // Zamknij MainActivity, aby nie można było wrócić do niej bez zalogowania
            return;
        }




        search_view = findViewById(R.id.search_view);
        recycler_view_home = findViewById(R.id.recycler_view_home);
        buttonAccount = findViewById(R.id.buttonAccount);

        dialog = new ProgressDialog(this);

        manager = new RequestManager(this);

        buttonAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, AccountActivity.class));
            }
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Please wait");
                dialog.show();
                manager.searchMovies(listener, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private final OnSearchApiListener listener = new OnSearchApiListener() {
        @Override
        public void onResponse(SearchResponse response) {
            dialog.dismiss();
            if (response == null) {
                Toast.makeText(MainActivity.this, "No data available!", Toast.LENGTH_LONG).show();
                return;
            }
            showResult(response);
        }

        @Override
        public void onError(String message) {
            dialog.dismiss();
            Toast.makeText(MainActivity.this, "An error occurred!", Toast.LENGTH_LONG).show();
        }
    };

    private void showResult(SearchResponse response) {
        recycler_view_home.setHasFixedSize(true);
        recycler_view_home.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        adapter = new HomeRecyclerAdapter(this, response.getSearch(), this);
        recycler_view_home.setAdapter(adapter);
    }

    @Override
    public void onMovieClicked(String id) {
        startActivity(new Intent(MainActivity.this, DetailsActivity.class)
                .putExtra("data", id));
    }
}
