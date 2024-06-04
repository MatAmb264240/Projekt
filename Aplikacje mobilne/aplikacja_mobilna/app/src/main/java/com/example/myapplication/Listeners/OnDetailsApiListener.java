package com.example.myapplication.Listeners;

import com.example.myapplication.Info.InfoResponse;

public interface OnDetailsApiListener {
    void onResponse(InfoResponse response);
    void onError(String message);
}
