package com.example.android.sunshine.utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.Database.WeatherModel;
import com.example.android.sunshine.R;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter <WeatherAdapter.WeatherHolder>{

    List<WeatherModel> weatherList;
    String day, highLow, desc;
    private Context context;
    final private ItemClickListener mItemClickListener;



    public WeatherAdapter(Context context, ItemClickListener mItemClickListener) {
        this.context = context;
        this.mItemClickListener = mItemClickListener;
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    @NonNull
    @Override
    public WeatherHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        WeatherHolder holder = new WeatherHolder(mView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WeatherHolder holder, final int position) {

       final Context  mContext = holder.itemView.getContext();


       day = weatherList.get(position).getDay();
       highLow = weatherList.get(position).getHighAndLow();
       desc = weatherList.get(position).getDescription();

        holder.dateView.setText(day);
        holder.highlowView.setText(highLow);
        holder.descView.setText(desc);


    }

    public List<WeatherModel> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<WeatherModel> weatherList) {
        this.weatherList = weatherList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (weatherList == null){
            return 0;
        }
        return weatherList.size();
    }

    class WeatherHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView dateView, highlowView, descView;

        public WeatherHolder(@NonNull View itemView) {
            super(itemView);
            dateView = (TextView)itemView.findViewById(R.id.date);
            highlowView = (TextView)itemView.findViewById(R.id.high_low);
            descView = (TextView)itemView.findViewById(R.id.desc);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            int elementId = weatherList.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
