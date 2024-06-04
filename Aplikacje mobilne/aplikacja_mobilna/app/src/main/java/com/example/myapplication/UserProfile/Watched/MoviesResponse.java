package com.example.myapplication.UserProfile.Watched;

import com.google.gson.annotations.SerializedName;
import com.example.myapplication.Info.InfoResponse;
import java.util.List;

public class MoviesResponse {
    @SerializedName("movies_watched")
    private List<InfoResponse> moviesWatched;

    public List<InfoResponse> getMoviesWatched() {
        return moviesWatched;
    }
    public void setSeriesWatched(List<InfoResponse> seriesWatched) {
        this.moviesWatched = seriesWatched;
    }
}
