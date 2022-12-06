package com.example.raifbeyrestaurant.API;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsdAPI {
    @GET("embed/doviz.json")
    Call<Usd> getDolar();
}
