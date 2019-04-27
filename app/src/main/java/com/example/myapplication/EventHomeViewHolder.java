package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shollmann.events.helper.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shollmann.events.helper.DateUtils;
import com.shollmann.events.ui.EventbriteApplication;
import com.shollmann.events.ui.activity.EventsActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EventHomeViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTitle;
    private TextView txtDate;
    private TextView txtIsFree;
    private TextView txtDesc;
    private ImageView imgCover;
    private Button btnUrl;
    private Button btnUber;
    private Button btnGoogleMaps;

    public EventHomeViewHolder(View view) {
        super(view);
        txtTitle = view.findViewById(R.id.event_home_txt_title);
        txtDate = view.findViewById(R.id.event_home_txt_date);
        txtIsFree = view.findViewById(R.id.event_home_txt_address);
        txtTitle = view.findViewById(R.id.event_home_txt_title);
//        txtDesc = view.findViewById(R.id.event_home_txt_desc);
        imgCover = view.findViewById(R.id.event_home_img_cover);
        btnUrl = view.findViewById(R.id.event_home_but_url);
        btnUber = view.findViewById(R.id.event_home_but_uber);
        btnGoogleMaps = view.findViewById(R.id.event_home_but_google_maps);
        return;
    }

    public void setEvent(HashMap event) {
        txtTitle.setText(event.get("name").toString());
        txtDate.setText(com.shollmann.events.helper.DateUtils.getEventDate(event.get("localDate").toString()));
//        txtDesc.setText(event.get("description").toString());
        txtIsFree.setText(event.get("is_free").toString());
        if (event.get("imageUrl") != null) {
            Picasso.get().load(event.get("imageUrl").toString()).into(imgCover);
//            Picasso.with(EventbriteApplication.getApplication()).load(event.get("imageUrl").toString()).into(imgCover);

        }
        final String curUrl=event.get("url").toString();
        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = curUrl;
                Intent i = new Intent(Intent.ACTION_VIEW);
//                Intent i = new Intent(v.getContext(), EventsHome.class);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            }
        });

        final String lat = event.get("venueLat").toString();
        final String lon = event.get("venueLon").toString();

        btnUber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                System.out.println(lat+lon);
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
//                intent.setComponent(new ComponentName("packagename//com.shollmann.events",
//                        "classname//com.example.ubertest.UberMainActivity"));
//                startActivity(intent);
//                Intent i = new Intent(v.getContext(), EventsActivity.class);
                try {
//                    Intent i = new Intent(this, Class.forName("com.journaldev.MapsInAction.GoogleMapsMainActivity"));
                    Intent i = new Intent(v.getContext(), com.example.ubertest.UberMainActivity.class);
                    System.out.println(lat + lon);
                    i.putExtra("message", lat + "," + lon);
                    v.getContext().startActivity(i);
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
        });
        btnGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.println(lat+lon);
                try {
//                    Intent i = new Intent(this, Class.forName("com.journaldev.MapsInAction.GoogleMapsMainActivity"));
                    Intent i = new Intent(v.getContext(), com.journaldev.MapsInAction.GoogleMapsMainActivity.class);
                    System.out.println(lat + lon);
                    i.putExtra("message", lat + "," + lon);
                    v.getContext().startActivity(i);
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
        });
    }

}
