package com.example.android.sunshine.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.sunshine.Database.AppDatabase;
import com.example.android.sunshine.Database.WeatherModel;

public class DetailViewModel extends ViewModel {

    private LiveData<WeatherModel> weatherModelLiveData;

    public DetailViewModel(AppDatabase mDp, int id){
        weatherModelLiveData = mDp.weatherDAO().loadWeatherById(id);
    }

    public LiveData<WeatherModel> getWeatherModelLiveData()
    {
        return weatherModelLiveData;
    }
}
