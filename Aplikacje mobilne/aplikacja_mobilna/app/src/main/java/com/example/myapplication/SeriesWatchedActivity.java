package com.example.myapplication;

import com.example.myapplication.Register.Api;
import com.example.myapplication.UserProfile.Watched.MoviesResponse;

import retrofit2.Call;

public class SeriesWatchedActivity extends WatchedActivity {
    @Override
    protected Call<MoviesResponse> getApiCall(Api apiService, String username) {
        return apiService.getSeriesWatched(username);
    }
}
