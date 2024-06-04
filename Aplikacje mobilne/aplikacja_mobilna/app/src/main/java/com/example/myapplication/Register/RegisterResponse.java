package com.example.myapplication.Register;

import com.example.myapplication.Login.LoginResponse;

public class RegisterResponse {
    // Dodaj pola zgodne z odpowiedzią serwera
    private String refresh;
    private String access;


    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}

