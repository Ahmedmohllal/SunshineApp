package com.example.android.sunshine.Activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.Database.AppDatabase;
import com.example.android.sunshine.R;
import com.example.android.sunshine.ViewModel.DetailViewModel;
import com.example.android.sunshine.ViewModel.DetailViewModelFactory;
import com.example.android.sunshine.data.SharedPrefManager;
import com.example.android.sunshine.Database.WeatherModel;

public class DetailActivity extends AppCompatActivity {

    private TextView result;
    int item_id;
    WeatherModel weatherModel;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        result = (TextView)findViewById(R.id.resultText);

        appDatabase = AppDatabase.getInstance(DetailActivity.this);
        item_id = getIntent().getExtras().getInt("Item_id");

        DetailViewModelFactory viewModelFactory = new DetailViewModelFactory(appDatabase, item_id);
        DetailViewModel detailViewModel = ViewModelProviders.of(DetailActivity.this, viewModelFactory)
                .get(DetailViewModel.class);
        detailViewModel.getWeatherModelLiveData().observe(DetailActivity.this, new Observer<WeatherModel>() {
            @Override
            public void onChanged(@Nullable WeatherModel weatherModel) {

                populateUI(weatherModel);
            }
        });

    }

    private void populateUI(WeatherModel weatherModel){
        result.setText(weatherModel.getDay()+"   " + weatherModel.getDescription()+"  "
        +weatherModel.getHighAndLow());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detailshare, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemSelected = item.getItemId();
        if (itemSelected == R.id.shares){




        }

        switch (itemSelected){
            case  R.id.shares:
                String dayTyp = "text/plain";
                String title = "This is the day which selected from Main Activity";

                break;
            case R.id.logout:
                SharedPrefManager.getInstance(DetailActivity.this).clear();
                goToSignUpActivity();

        }
        return super.onOptionsItemSelected(item);
    }

    private void goToSignUpActivity() {
        Intent i = new Intent(DetailActivity.this, SignupActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
