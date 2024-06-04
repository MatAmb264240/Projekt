package com.example.myapplication.Register;

public class RegisterRequest {
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private String password;
    private String confirm_password; // Dodaj to pole, jeśli serwer tego wymaga

    public RegisterRequest(String username, String email, String first_name, String last_name, String password, String confirm_password) {
        this.username = username;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setfirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setlast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setconfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getfirst_name() {
        return first_name;
    }

    public String getlast_name() {
        return last_name;
    }

    public String getPassword() {
        return password;
    }

    public String getconfirm_password() {
        return confirm_password;
    }
// Dodaj gettery, jeśli to konieczne
}
