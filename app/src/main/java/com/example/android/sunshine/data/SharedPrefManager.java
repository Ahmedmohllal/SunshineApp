package com.example.android.sunshine.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.sunshine.utilities.UserModel;

public class SharedPrefManager {
    private static SharedPrefManager sharedPrefManager;
    private Context mCtx;

    private SharedPrefManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx) {
        if (sharedPrefManager == null) {
            sharedPrefManager = new SharedPrefManager(mCtx);
        }
        return sharedPrefManager;
    }

    public void saveUser(UserModel userModel) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("my_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstname", userModel.getFirstName());
        editor.putString("lastname", userModel.getLastName());
        editor.putString("email", userModel.getEmail());
        editor.putString("mobile", userModel.getPhoneNumber());
        editor.apply();
    }

    public boolean checkReg() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("my_preference", Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", null) != null;

    }

    public UserModel getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("my_preference", Context.MODE_PRIVATE);
        UserModel userModel = new UserModel(
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("firstname", null),
                sharedPreferences.getString("lastname", null),
                sharedPreferences.getString("mobile", null)
        );
        return userModel;
    }


    public void clear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences("my_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
