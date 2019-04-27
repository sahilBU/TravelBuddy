package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

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
import com.squareup.picasso.Target;

import java.util.HashMap;

public class WeatherHomeViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTemp;
    private TextView txtDate;
    private TextView txtDesc;
    private ImageView imgCover;

    public WeatherHomeViewHolder(View view) {
        super(view);
        txtTemp = view.findViewById(R.id.weather_home_txt_temp);
        txtDesc = view.findViewById(R.id.weather_home_txt_desc);
        txtDate = view.findViewById(R.id.weather_home_txt_date);
        imgCover = view.findViewById(R.id.weather_home_img_cover);
        return;
    }

    public void setEvent(HashMap event) {
        txtTemp.setText(event.get("temp").toString());
        txtDesc.setText(event.get("description").toString());
        txtDate.setText(com.shollmann.events.helper.DateUtils.getWeatherDate(event.get("date").toString()));

        String main = event.get("main").toString();
        String icon = event.get("icon").toString();
        String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
        if (iconUrl != null) {
            Picasso.get().load(iconUrl).into(imgCover);

        }

    }

}
