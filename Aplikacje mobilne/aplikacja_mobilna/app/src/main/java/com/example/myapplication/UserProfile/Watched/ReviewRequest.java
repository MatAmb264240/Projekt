package com.example.myapplication.UserProfile.Watched;

import com.example.myapplication.Info.InfoResponse;

public class ReviewRequest {
    private int userId;
    private int movieId;
    private String imdbID;
    private String text;
    private int rate;

    private InfoResponse movie;


    public ReviewRequest(int userId, int movieId, String imdbID, String text, int rate, InfoResponse movie) {
        this.userId = userId;
        this.movieId = movieId;
        this.imdbID = imdbID;
        this.text = text;
        this.rate = rate;
        this.movie = movie;
    }

    public int getUserId() {
        return userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getText() {
        return text;
    }

    public int getRate() {
        return rate;
    }
}
