package com.example.android.sunshine.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.sunshine.Database.AppDatabase;
import com.example.android.sunshine.Database.WeatherModel;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<WeatherModel>> weatherEntityList;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(getApplication());
        weatherEntityList = appDatabase.weatherDAO().loadAllWeatherData();
    }

    public LiveData<List<WeatherModel>> getWeatherEntityList() {
        return weatherEntityList;
    }


}
