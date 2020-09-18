package com.example.android.sunshine.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitSinglton {

    public static final String BASE_URL = "https://andfun-weather.udacity.com/";
    private static RetrofitSinglton retrofitSinglton;
    private Retrofit retrofit;


    private RetrofitSinglton(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitSinglton getInstance(){
        if (retrofitSinglton == null){
            retrofitSinglton = new RetrofitSinglton();
        }
        return retrofitSinglton;
    }

    public API getAPI(){
        return retrofit.create(API.class);
    }
}
