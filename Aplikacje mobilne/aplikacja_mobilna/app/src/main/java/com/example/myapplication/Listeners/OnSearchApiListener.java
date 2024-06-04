package com.example.myapplication.Listeners;

import com.example.myapplication.Info.SearchResponse;

public interface OnSearchApiListener {
    void onResponse(SearchResponse response);
    void onError(String message);
}
