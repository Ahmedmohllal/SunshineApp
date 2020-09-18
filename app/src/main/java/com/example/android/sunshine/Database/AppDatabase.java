package com.example.android.sunshine.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {WeatherModel.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;
    private static final String DATABASE_NAME = "WeatherData";

    public static synchronized AppDatabase getInstance(Context context){
        if (sInstance == null){
            sInstance = Room.databaseBuilder(context.getApplicationContext()
                    , AppDatabase.class
                    , DATABASE_NAME)
                    .build();

        }
        return sInstance;
    }

    public abstract DAO weatherDAO();
}
