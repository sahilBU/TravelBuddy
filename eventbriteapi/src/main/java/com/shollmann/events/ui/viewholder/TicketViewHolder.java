package com.shollmann.events.ui.viewholder;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.codelab.friendlychat.model.FirebaseModel;
import com.google.firebase.codelab.friendlychat.model.User;
import com.shollmann.events.R;
import com.shollmann.events.helper.DateUtils;
import com.shollmann.events.ui.EventbriteApplication;
import com.shollmann.events.ui.activity.EventsActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class TicketViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTitle;
    private TextView txtDate;
    private TextView txtIsFree;
    private TextView txtDesc;
    private ImageView imgCover;
    private Button btnUrl;
    private Button btnUber;
    private Button btnGoogleMaps;
    private FirebaseModel model = new FirebaseModel();



    public TicketViewHolder(View view) {
        super(view);
        txtTitle = view.findViewById(R.id.event_txt_title);
        txtDate = view.findViewById(R.id.event_txt_date);
        txtIsFree = view.findViewById(R.id.event_txt_address);
        txtTitle = view.findViewById(R.id.event_txt_title);
        txtDesc = view.findViewById(R.id.event_txt_desc);
        imgCover = view.findViewById(R.id.event_img_cover);
        btnUrl = view.findViewById(R.id.event_but_url);
        btnUber = view.findViewById(R.id.event_but_uber);
        btnGoogleMaps = view.findViewById(R.id.event_but_google_maps);
        return;
    }

    public void setEvent(final HashMap event) {
        try {
            txtTitle.setText(event.get("name").toString());
        }
        catch (Exception e){
            Log.e("TICKETMASTER","NULL_NO PLEASENOTE"+e.toString());
            txtTitle.setText("No additional event information");
        }
        try {
            txtDate.setText(DateUtils.getEventDate(event.get("dateTime").toString()));
        }
        catch (Exception e){
            Log.e("TICKETMASTER","NULL_NO PLEASENOTE"+e.toString());
            txtDate.setText("No additional event information");
        }

        try {
            txtDesc.setText(event.get("pleaseNote").toString());
        }
        catch (Exception e){
            Log.e("TICKETMASTER","NULL_NO PLEASENOTE"+e.toString());
            txtDesc.setText("No additional event information");
        }
        try {
            txtIsFree.setText(event.get("distance").toString() + " " + event.get("units").toString());
        }
        catch (Exception e){
            Log.e("TICKETMASTER","NULL_NO_DISTANCE"+e.toString());
            txtIsFree.setText("No additional event information");
        }
        if (event.get("imageUrl") != null) {
            Picasso.get().load(event.get("imageUrl").toString()).into(imgCover);

        }
        final String curUrl=event.get("url").toString();
        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = curUrl;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            }
        });

        final String lat = event.get("venueLat").toString();
        final String lon = event.get("venueLon").toString();

        btnUber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                model.getSingleUser(model.getUid(), new FirebaseModel.MyCallBack() {
                    @Override
                    public void onCallback(Object object) {
                        User this_user = (User) object;
                        Intent i = new Intent(v.getContext(), com.example.ubertest.UberMainActivity.class);
                        i.putExtra("message", lat + "," + lon +","+ String.valueOf(this_user.getLatitude())+ "," + String.valueOf(this_user.getLongitute())+","+event.get("name"));
                        v.getContext().startActivity(i);
                    }
                });
            }
        });
        btnGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                model.getSingleUser(model.getUid(), new FirebaseModel.MyCallBack() {
                    @Override
                    public void onCallback(Object object) {
                        try {
                            User this_user = (User) object;
                            Intent i = new Intent(v.getContext(), com.journaldev.MapsInAction.GoogleMapsMainActivity.class);
                            i.putExtra("message", lat + "," + lon +","+ String.valueOf(this_user.getLatitude())+ "," + String.valueOf(this_user.getLongitute())+","+event.get("name"));
                            v.getContext().startActivity(i);

                        }
                        catch (Exception e){
                            System.out.println(e);
                        }
                    }
                });
            }
        });
    }
    private void expandTextView(TextView tv){
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", tv.getLineCount());
        animation.setDuration(200).start();
    }

    private void collapseTextView(TextView tv, int numLines){
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", numLines);
        animation.setDuration(200).start();
    }
}
