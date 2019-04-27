package com.shollmann.events.ui.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import android.net.Uri;
import com.shollmann.events.R;
import com.shollmann.events.api.model.Event;
import com.shollmann.events.api.model.ExpandableTextView;
import com.shollmann.events.helper.DateUtils;
import com.squareup.picasso.Picasso;
import android.widget.Button;


public class EventViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTitle;
    private TextView txtDate;
    private TextView txtIsFree;
//    private TextView txtDesc;
    private ImageView imgCover;
    private Button btnUrl;
    private Button btnUber;
    private Button btnGoogleMaps;
    private boolean isTextViewClicked;
    Spannable.Factory spannableFactory;
    ExpandableTextView txtDesc;

    public EventViewHolder(View view) {
        super(view);
        txtTitle = view.findViewById(R.id.event_txt_title);
        txtDate = view.findViewById(R.id.event_txt_date);
        txtIsFree = view.findViewById(R.id.event_txt_address);
        txtTitle = view.findViewById(R.id.event_txt_title);
//        txtDesc = view.findViewById(R.id.event_txt_desc);
        imgCover = view.findViewById(R.id.event_img_cover);
        btnUrl = view.findViewById(R.id.event_but_url);
        btnUber = view.findViewById(R.id.event_but_uber);
        btnGoogleMaps= view.findViewById(R.id.event_but_google_maps);
        txtDesc = view.findViewById(R.id.event_txt_desc);
        return;
    }

    public void setEvent(Event event) {
        spannableFactory = Spannable.Factory
                .getInstance();

        txtTitle.setText(event.getName().getText());
        txtDate.setText(DateUtils.getEventDate(event.getStart().getLocal()));
        txtDesc.setText(event.getDescription().getText());

        txtIsFree.setText(event.getIsFree() ? R.string.free : R.string.paid);
        if (event.getLogo() != null) {
//            Picasso.with(EventbriteApplication.getApplication()).load(event.getLogo().getUrl()).into(imgCover);
            Picasso.get().load(event.getLogo().getUrl()).into(imgCover);

        }
        final String curUrl=event.getUrl();
        final String lat = event.getVenue().getAddress().getLatitude();
        final String lon = event.getVenue().getAddress().getLongitude();
        btnUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = curUrl;
                Intent i = new Intent(Intent.ACTION_VIEW);
                System.out.println(lat+lon);
                i.setData(Uri.parse(url));
                v.getContext().startActivity(i);
            }
        });
        btnUber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(lat+lon);
                Intent i = new Intent(v.getContext(), com.example.ubertest.UberMainActivity.class);
//                System.out.println(lat + lon);
                i.putExtra("message", lat + "," + lon);
                v.getContext().startActivity(i);
            }
        });
        btnGoogleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(lat+lon);
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
