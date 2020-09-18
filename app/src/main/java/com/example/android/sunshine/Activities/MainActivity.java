
package com.example.android.sunshine.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.sunshine.AppExecutors;
import com.example.android.sunshine.Database.AppDatabase;
import com.example.android.sunshine.Network.RetrofitSinglton;
import com.example.android.sunshine.R;
import com.example.android.sunshine.ViewModel.MainViewModel;
import com.example.android.sunshine.data.SharedPrefManager;
import com.example.android.sunshine.utilities.WeatherAdapter;
import com.example.android.sunshine.Database.WeatherModel;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements WeatherAdapter.ItemClickListener {

    private ProgressBar mprogressBar;
    RecyclerView recyclerView;
    ArrayList<WeatherModel> weatherList;
    WeatherAdapter weatherAdapter;
    WeatherModel weatherModel;
    boolean connected = false;
    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        appDatabase = AppDatabase.getInstance(MainActivity.this);


        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.weatherRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        weatherList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(MainActivity.this, this);
        recyclerView.setAdapter(weatherAdapter);


        if (SharedPrefManager.getInstance(MainActivity.this).checkReg() == false) {
           // goToSignUpActivity();
        }

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() != NetworkInfo.State.CONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() != NetworkInfo.State.CONNECTED) {
            connected = false;
            mprogressBar.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Database Have Data",Toast.LENGTH_LONG).show();
            ReturnMainViewModel();
        } else{
            Toast.makeText(MainActivity.this, "Network is connected",Toast.LENGTH_LONG).show();
            connected = true;
            CallNetwork();
        }
    }

    private void CallNetwork() {
        mprogressBar.setVisibility(View.VISIBLE);
            long localDate = System.currentTimeMillis();
            long utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate);
            final long startDay = SunshineDateUtils.normalizeDate(utcDate);

            Call<ResponseBody> call = RetrofitSinglton
                    .getInstance()
                    .getAPI()
                    .getWeatherData();

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String result = response.body().string();
                        if (result == null) {

                            mprogressBar.setVisibility(View.INVISIBLE);
                        } else {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String date;
                                long dateTimeMillis;

                                /*
                                 * We ignore all the datetime values embedded in the JSON and assume that
                                 * the values are returned in-order by day (which is not guaranteed to be correct).
                                 */
                                dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * i;
                                date = SunshineDateUtils.getFriendlyDateString(MainActivity.this, dateTimeMillis, false);


                                /* These are the values that will be collected */
                                String description;
                                String highAndLow;
                                double min;
                                double max;

                                JSONObject firstObject = jsonArray.getJSONObject(i);
                                JSONObject TempObject = firstObject.getJSONObject("temp");
                                min = TempObject.getDouble("min");
                                max = TempObject.getDouble("max");
                                highAndLow = SunshineWeatherUtils.formatHighLows(MainActivity.this, max, min);


                                JSONArray weatherArray = firstObject.getJSONArray("weather");
                                JSONObject firstWeatherJO = weatherArray.getJSONObject(0);
                                description = firstWeatherJO.getString("main");
                                weatherModel = new WeatherModel(date, highAndLow, description);

                                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            appDatabase.weatherDAO().insertWeather(weatherModel);
                                        }
                                    });

                                mprogressBar.setVisibility(View.INVISIBLE);
                            }
                            ReturnMainViewModel();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }

    private void ReturnMainViewModel(){
        MainViewModel mainViewModel = ViewModelProviders.of(MainActivity.this).get(MainViewModel.class);
        mainViewModel.getWeatherEntityList().observe( MainActivity.this, new Observer<List<WeatherModel>>() {
            @Override
            public void onChanged(List<WeatherModel> weatherlist) {
                weatherAdapter.setWeatherList(weatherlist);
            }
        });
        mprogressBar.setVisibility(View.INVISIBLE);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();

        switch (itemSelected) {
            case R.id.refreshAction:
                mprogressBar.setVisibility(View.VISIBLE);
                CallNetwork();
                break;
            case R.id.deleteData:
                mprogressBar.setVisibility(View.VISIBLE);
                deleteData();
                break;
            case R.id.logout:
                SharedPrefManager.getInstance(MainActivity.this).clear();
                goToSignUpActivity();


        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteData(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<WeatherModel> y = weatherAdapter.getWeatherList();
                WeatherModel model;
                for (int i = 0 ; i < y.size() ; i++){
                    model = y.get(i);
                    appDatabase.weatherDAO().deleteAllData(model);
                }
                mprogressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void goToSignUpActivity() {
        Intent i = new Intent(MainActivity.this, SignupActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onItemClickListener(int itemId) {

        Intent i = new Intent(MainActivity.this, DetailActivity.class);
        i.putExtra("Item_id", itemId);
        startActivity(i);

    }
}