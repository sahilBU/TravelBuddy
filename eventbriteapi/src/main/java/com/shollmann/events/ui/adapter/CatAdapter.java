package com.shollmann.events.ui.adapter;


import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.shollmann.events.R;
import com.shollmann.events.ui.activity.EventsActivity;
import com.shollmann.events.ui.activity.EventsHome;
import com.shollmann.events.ui.viewholder.CatViewHolder;
import com.shollmann.events.ui.adapter.CatAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<CatViewHolder> itemList;
    public String curName;
    // Constructor of the class
    public CatAdapter(int layoutId, ArrayList<CatViewHolder> itemList) {
        listItemLayout = layoutId;
        this.itemList = itemList;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        ImageView itemPic = holder.itemPic;
        item.setText(itemList.get(listPosition).getName());
        if (listPosition == 0) {
            itemPic.getLayoutParams().height = 350;
            itemPic.getLayoutParams().width = 650;
            itemPic.setAdjustViewBounds(true);
            itemPic.setBackgroundResource(R.drawable.ticketmaster_music_crowd);
        }
        if (listPosition == 1) {
//            Picasso.get().load("https://cdn.downunderendeavours.com/wp-content/uploads/2014/08/experience-outdoor-hero-1-new-zealand-hiking-queenstown-2000x837.jpg").centerCrop().into(itemPic);
            itemPic.getLayoutParams().height = 350;
            itemPic.getLayoutParams().width = 650;

            itemPic.setAdjustViewBounds(true);
            itemPic.setBackgroundResource(R.drawable.music);
        }
        if (listPosition == 2) {
            itemPic.getLayoutParams().height = 350;
            itemPic.getLayoutParams().width = 650;

            itemPic.setAdjustViewBounds(true);
            itemPic.setBackgroundResource(R.drawable.food);
        }
        if (listPosition == 3) {
            itemPic.getLayoutParams().height = 350;
            itemPic.getLayoutParams().width = 650;

            itemPic.setAdjustViewBounds(true);
            itemPic.setBackgroundResource(R.drawable.film);
        }
        if (listPosition == 4) {
            itemPic.getLayoutParams().height = 350;
            itemPic.setAdjustViewBounds(true);
            itemPic.setBackgroundResource(R.drawable.community);
        }
        if (listPosition == 5) {
            itemPic.getLayoutParams().height = 350;
            itemPic.getLayoutParams().width = 650;
            itemPic.setAdjustViewBounds(true);
            itemPic.setBackgroundResource(R.drawable.outdoors);
        }
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView item;
        public ImageView itemPic;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            final RelativeLayout background = (RelativeLayout) itemView.findViewById(R.id.row_relLayout);
            item = itemView.findViewById(R.id.row_item);
            itemPic = itemView.findViewById(R.id.row_image);
        }
        @Override
        public void onClick(View view) {
            Intent i = new Intent(view.getContext(), EventsActivity.class);
            System.out.println(item.getText());
            i.putExtra("message", item.getText());
            view.getContext().startActivity(i);
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + item.getText());
        }
    }
}