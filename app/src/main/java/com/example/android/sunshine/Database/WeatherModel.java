package com.example.android.sunshine.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Weather")
public class WeatherModel {

    @PrimaryKey(autoGenerate = true)
    private int id ;
    String day;
    String highAndLow;
    String description;

    public WeatherModel(int id, String day, String highAndLow, String description) {
        this.id = id;
        this.day = day;
        this.highAndLow = highAndLow;
        this.description = description;
    }

    @Ignore
    public WeatherModel(String day, String highAndLow, String description) {
        this.day = day;
        this.highAndLow = highAndLow;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHighAndLow() {
        return highAndLow;
    }

    public void setHighAndLow(String highAndLow) {
        this.highAndLow = highAndLow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
