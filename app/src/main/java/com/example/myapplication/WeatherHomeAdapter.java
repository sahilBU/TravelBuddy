package com.example.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.myapplication.WeatherHomeViewHolder;

public class WeatherHomeAdapter extends RecyclerView.Adapter<WeatherHomeViewHolder> {
    private ArrayList<HashMap> listEvents;
    private boolean isKeepLoading;

    public WeatherHomeAdapter(ArrayList<HashMap> listEvents) {
        this.listEvents = listEvents;
        this.isKeepLoading = true;
    }

    @Override
    public WeatherHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_event_home, parent, false);
        WeatherHomeViewHolder weathertHomeViewHolder = new WeatherHomeViewHolder(view);
        return weathertHomeViewHolder;
    }

    @Override
    public void onBindViewHolder(WeatherHomeViewHolder holder, int position) {
        holder.setEvent(listEvents.get(position));
    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    public void add(List<HashMap> eventList) {
        listEvents.addAll(eventList);
    }

    public void setKeepLoading(boolean keepLoading) {
        isKeepLoading = keepLoading;
    }

    public void reset() {
        isKeepLoading = true;
        listEvents.clear();
        notifyDataSetChanged();
    }
}
