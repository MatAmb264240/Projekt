package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Login.LoginCallback;
import com.example.myapplication.Login.LoginRequest;
import com.example.myapplication.Login.LoginResponse;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonRegister;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        dialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Please wait");
                dialog.show();
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }

                RequestManager requestManager = new RequestManager(LoginActivity.this);
                requestManager.login(username, password, new LoginCallback() {
                    @Override
                    public void onSuccess(LoginResponse response) {
                        dialog.dismiss();
                        // Store tokens and user_id
                        String accessToken = response.getAccess();
                        String refreshToken = response.getRefresh();
                        int userId = response.getUserId(); // Pobierz user_id

                        // Save tokens and user_id in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs2", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("access_token", accessToken);
                        editor.putString("refresh_token", refreshToken);
                        editor.putString("username", username);
                        editor.putInt("user_id", userId); // Zapisz user_id
                        editor.commit();

                        // Debugging statements to check token values
                        Log.d("user_id", "your user id: " + userId);
                        Log.d("LoginActivity", "Access Token (from response): " + accessToken);
                        Log.d("LoginActivity", "Refresh Token (from response): " + refreshToken);
                        Log.d("LoginActivity", "Access Token (from SharedPreferences): " + sharedPreferences.getString("access_token", null));
                        Log.d("LoginActivity", "Refresh Token (from SharedPreferences): " + sharedPreferences.getString("refresh_token", null));
                        Log.d("LoginActivity", "User ID (from SharedPreferences): " + sharedPreferences.getString("user_id", null));

                        // Navigate to MainActivity
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish(); // Close LoginActivity
                    }

                    @Override
                    public void onError(String message) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
