package com.example.android.sunshine.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAuthSinglton {
    public static final String Auth_BASE_URL = "https://tracksystem7.000webhostapp.com/";
    private static RetrofitAuthSinglton retrofitAuthSinglton;
    private Retrofit retrofit;



    private RetrofitAuthSinglton(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Auth_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitAuthSinglton getInstance(){
        if (retrofitAuthSinglton == null){
            retrofitAuthSinglton = new RetrofitAuthSinglton();
        }
        return retrofitAuthSinglton;
    }

    public API getAPI(){
        return retrofit.create(API.class);
    }
}
