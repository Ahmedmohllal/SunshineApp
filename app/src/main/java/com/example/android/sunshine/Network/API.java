package com.example.android.sunshine.Network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    @GET("staticweather?q=94043%2CUSA&mode=json&units=metric&cnt=14")
    Call<ResponseBody> getWeatherData();


    @POST("check_register2.php")
    Call<ResponseBody> register(
            @Query("firstName") String fName,
            @Query("lastName") String lastname,
            @Query("email") String email,
            @Query("phoneNumber") String mobile,
            @Query("password") String password,
            @Query("gpluse") String Gplus
    );


}
