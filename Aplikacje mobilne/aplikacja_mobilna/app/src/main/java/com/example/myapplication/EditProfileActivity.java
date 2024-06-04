package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Register.RetrofitClient;
import com.example.myapplication.UserProfile.UserProfileRequest;
import com.example.myapplication.UserProfile.UserProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmail;
    private Button buttonSave;
    private String accessToken;
    private String refreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Pobranie tokenów z SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs2", MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token", null);
        refreshToken = sharedPreferences.getString("refresh_token", null);

        // Debugging statements to check token values
        Log.d("EditProfileActivity", "Access Token: " + accessToken);
        Log.d("EditProfileActivity", "Refresh Token: " + refreshToken);

        if (accessToken == null) {
            Log.e("EditProfileActivity", "Access Token is null in onCreate");
        }

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Log.d("EditProfileActivity", "Access Token before updateProfile: " + accessToken);

                if (accessToken == null) {
                    Log.e("EditProfileActivity", "Access Token is null before calling updateProfile");
                    Toast.makeText(EditProfileActivity.this, "Error: Access Token is null", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateProfile(firstName, lastName, email);
            }
        });
    }

    private void updateProfile(String firstName, String lastName, String email) {
        UserProfileRequest request = new UserProfileRequest(firstName, lastName, email);

        // Formatowanie nagłówka Authorization
        String authorizationHeader = "Bearer " + accessToken;

        // Tworzenie zapytania z uwzględnieniem nagłówka Authorization
        Call<UserProfileResponse> call = RetrofitClient.getInstance(this)
                .getApi()
                .updateProfile(authorizationHeader, request);

        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("EditProfileError", "Error body: " + errorBody);
                        Toast.makeText(EditProfileActivity.this, "Profile update failed: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("EditProfileError", "Error parsing error body", e);
                        Toast.makeText(EditProfileActivity.this, "Profile update failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Log.e("EditProfileError", "onFailure: " + t.getMessage());
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
