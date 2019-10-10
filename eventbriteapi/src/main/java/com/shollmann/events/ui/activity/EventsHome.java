package com.shollmann.events.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shollmann.events.R;
import com.shollmann.events.ui.adapter.CatAdapter;
import com.shollmann.events.ui.viewholder.CatViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class EventsHome extends AppCompatActivity {
    private ArrayList<String> listCat = new ArrayList<>(Arrays.asList("TicketMaster","Music","Food & Drink","Film, Media & Entertainment","Community & Culture","Travel & Outdoor"));
    private ArrayList<Integer> picCat = new ArrayList<>(Arrays.asList(R.drawable.ticketmaster_music_crowd, R.drawable.music, R.drawable.food, R.drawable.film,R.drawable.community,R.drawable.outdoors));

    //    private ArrayList<String> listCat = new ArrayList<>(Arrays.asList("Music","Food & Drink","Film, Media & Entertainment","Sports & Fitness","Travel & Outdoor","Performing & Visual Arts","Seasonal & Holiday","Hobbies & Special Interest","Community & Culture","Charity & Causes","Health & Wellness","Fashion & Beauty","Business & Professional","Science & Technology","Government & Politics","Auto, Boat & Air","Family & Education","Religion & Spirituality","Home & Lifestyle","School Activities","Other"));
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Initializing list view with the custom adapter
        ArrayList<CatViewHolder> itemList = new ArrayList<CatViewHolder>();
        ArrayList<Integer> imageList = new ArrayList<Integer>();

        CatAdapter catAdapter = new CatAdapter(R.layout.view_cat, itemList);
        recyclerView = findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(catAdapter);
        // Populating list items
        for (int i = 0; i < 6; i++) {
            System.out.println(picCat.get(2));
            System.out.println(R.drawable.music);
            itemList.add(new CatViewHolder(listCat.get(i)));
        }

    }
}


