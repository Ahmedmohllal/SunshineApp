package com.example.android.sunshine.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DAO {

    @Query("SELECT * FROM weather")
    LiveData<List<WeatherModel>> loadAllWeatherData();

    @Insert
    void insertWeather(WeatherModel weatherEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateWeather(WeatherModel weatherEntity);

    @Delete
    void deleteAllData(WeatherModel weatherModel);

    @Query("SELECT * FROM weather WHERE id = :id")
    LiveData <WeatherModel> loadWeatherById(int id);

}
