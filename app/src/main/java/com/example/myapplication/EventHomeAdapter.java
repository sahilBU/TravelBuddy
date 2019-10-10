package com.example.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.myapplication.EventHomeViewHolder;


public class EventHomeAdapter extends RecyclerView.Adapter<EventHomeViewHolder> {
    private ArrayList<HashMap> listEvents;
    private boolean isKeepLoading;

    public EventHomeAdapter(ArrayList<HashMap> listEvents) {
        this.listEvents = listEvents;
        this.isKeepLoading = true;
    }

    @Override
    public EventHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_event_home, parent, false);
        EventHomeViewHolder eventViewHolder = new EventHomeViewHolder(view);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(EventHomeViewHolder holder, int position) {
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
