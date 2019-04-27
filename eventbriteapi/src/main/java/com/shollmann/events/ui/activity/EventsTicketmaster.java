package com.shollmann.events.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shollmann.events.R;
import com.shollmann.events.api.baseapi.TicketmasterApiCall;
import com.shollmann.events.ui.adapter.TicketAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class EventsTicketmaster extends AppCompatActivity {
    ArrayList<HashMap> listTickets = new ArrayList<HashMap>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        String url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=nYOir7P11knKKGYONw1a6jVWuXb63J2o&latlong=42.360082,-71.057083";
        try {
            listTickets=new TicketmasterApiCall().execute(url).get();
//            curEvents = new TicketmasterApiCall().execute(url).get();
            System.out.println(listTickets.get(1));
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
}


