package com.example.raifbeyrestaurant.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usd {
    @SerializedName("USD")
    @Expose
    private UsdData USD;
    public String getDolar() {
        return String.valueOf(USD.getSatis());
    }

   /* public String getDolar() {
        return dolar;
    }
    public void setDolar(String dolar) {
        this.dolar = dolar;
    }*/
}
