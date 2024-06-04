package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;



public class AccountActivity extends AppCompatActivity {

    private Button buttonLogout, buttonEditProfile, buttonMoviesWatched, buttonSeriesWatched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs2", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);
        Log.d("EditProfileActivity", "Access Token FROM ACCOUT  ACTIV: " + accessToken);
        if (accessToken == null) {
            // Jeśli nie ma tokena dostępu, przejdź do LoginActivity
            startActivity(new Intent(AccountActivity.this, MainActivity.class));
            finish();
            return;
        }


        buttonLogout = findViewById(R.id.buttonLogout);
        buttonEditProfile = findViewById(R.id.buttonEditProfile);
        buttonMoviesWatched = findViewById(R.id.buttonMoviesWatched);
        buttonSeriesWatched=findViewById(R.id.buttonSeriesWatched);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementuj funkcję wylogowania
                logout();
            }
        });

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Otwórz aktywność edycji profilu
                Intent intent = new Intent(AccountActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        buttonMoviesWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Otwórz aktywność edycji profilu
                Intent intent = new Intent(AccountActivity.this, MoviesWatchedActivity.class);
                startActivity(intent);
            }
        });
        buttonSeriesWatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Otwórz aktywność edycji profilu
                Intent intent = new Intent(AccountActivity.this, SeriesWatchedActivity.class);
                startActivity(intent);
            }
        });
    }

    private void logout() {
        // Implementuj funkcję wylogowania, np. wyczyszczenie tokenów i przeniesienie do ekranu logowania
        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}