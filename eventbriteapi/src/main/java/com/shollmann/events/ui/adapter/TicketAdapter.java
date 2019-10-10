package com.shollmann.events.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shollmann.events.R;
import com.shollmann.events.ui.viewholder.TicketViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketViewHolder> {
    private ArrayList<HashMap> listEvents;
    private boolean isKeepLoading;

    public TicketAdapter(ArrayList<HashMap> listEvents) {
        this.listEvents = listEvents;
        this.isKeepLoading = true;
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_event, parent, false);
        TicketViewHolder eventViewHolder = new TicketViewHolder(view);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(TicketViewHolder holder, int position) {
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
