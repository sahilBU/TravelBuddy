package com.shollmann.events.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shollmann.events.R;
import com.shollmann.events.api.baseapi.TicketmasterApiCall;
import com.shollmann.events.ui.adapter.TicketAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class EventsTicketmaster extends AppCompatActivity {
    ArrayList<HashMap> listTickets = new ArrayList<HashMap>();
    RecyclerView recyclerView;
    String location;
//    Double curLat;
//    Double curLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        location = getIntent().getStringExtra("location");
//        List<String> items = Arrays.asList(location.split(","));
//        curLat =Double.parseDouble(items.get(0));
//        curLong=Double.parseDouble(items.get(1));
        String url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=nYOir7P11knKKGYONw1a6jVWuXb63J2o&latlong="+location+"&sort=distance,date,asc";
        try {
            listTickets=new TicketmasterApiCall().execute(url).get();
        }
        catch (ExecutionException e){
            System.out.println(e);
        }
        catch (InterruptedException e){
            System.out.println(e);
        }

        // Initializing list view with the custom adapter
        TicketAdapter ticketAdapter = new TicketAdapter(listTickets);
        recyclerView = findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ticketAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(EventsTicketmaster.this, EventsHome.class));
        finish();
    }
}


