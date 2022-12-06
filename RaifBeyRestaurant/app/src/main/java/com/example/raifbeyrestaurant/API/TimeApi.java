package com.example.raifbeyrestaurant.API;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TimeApi {
    @GET("Europe/Istanbul")
    Call<TimeTurkey> getTime();
}
