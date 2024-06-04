package com.example.myapplication.UserProfile.Watched;

import com.example.myapplication.Info.InfoResponse;
import com.example.myapplication.Info.Review;

import java.util.List;

public class ReviewResponse {
    private InfoResponse movie;
    private List<Review> reviews;

    public InfoResponse getMovie() {
        return movie;
    }

    public void setMovie(InfoResponse movie) {
        this.movie = movie;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
