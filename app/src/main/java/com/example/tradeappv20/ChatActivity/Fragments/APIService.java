package com.example.tradeappv20.ChatActivity.Fragments;

import com.example.tradeappv20.Notifications.MyResponse;
import com.example.tradeappv20.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAf1RL3zc:APA91bERd3kj3SlrPWWL09AJq6jsEa7oCHOgs1vmbOjmkNya2MOPyVkAFj8PysD0WX_foVPQDGv-TgJnSzOk5vJ83tawqmWiPC4ejHdB_jnN813sWaTwMneMIpyh2fNkVxu-mzO_8HoW"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotfication(@Body Sender body);
}
