package com.example.myapplication.Login;

// LoginCallback.java
public interface LoginCallback {
    void onSuccess(LoginResponse response);

    void onError(String message);
}
