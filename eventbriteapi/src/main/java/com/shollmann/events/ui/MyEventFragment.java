//package com.shollmann.events.ui;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.shollmann.events.R;
//import com.shollmann.events.api.model.Event;
//import com.shollmann.events.ui.activity.EventsActivity;
//
//public class MyEventFragment extends Fragment {
//
//    LinearLayout linearLayout;
//    View rootView;
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        EventsActivity eventsActivity = new EventsActivity();
//        Button b = (Button) rootView.findViewById(R.id.);
//        btnGoogleMaps= rootView.findViewById(R.id.event_but_google_maps);
//        b.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                addPlaces();
//            }
//
//        });
//        linearLayout = (LinearLayout) rootView.findViewById(R.id.eventsFragLinear);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.activity_home, container, false);
//        return rootView;
//    }
//
//    public void addPlaces() {
//        Button button = new Button(getActivity()); // needs activity context
//        button.setText("button name");
//        linearLayout.addView(button);
//    }
//}
//
////
////public class MyEventFragment extends Fragment {
////
////    LinearLayout linearLayout;
////    View rootView;
////    TextView txtTitle;
////    TextView txtDate;
////    TextView txtIsFree;
////    TextView txtDesc;
////    ImageView imgCover;
////    Button btnUrl;
////    Button btnUber;
////    Button btnGoogleMaps;
////
////    @Override
////    public void onActivityCreated(Bundle savedInstanceState) {
////        super.onActivityCreated(savedInstanceState);
////        Button b = (Button) rootView.findViewById(R.id.);
////        txtTitle = rootView.findViewById(R.id.event_txt_title);
////        txtDate = rootView.findViewById(R.id.event_txt_date);
////        txtIsFree = rootView.findViewById(R.id.event_txt_address);
////        txtTitle = rootView.findViewById(R.id.event_txt_title);
////        txtDesc = rootView.findViewById(R.id.event_txt_desc);
////        imgCover = rootView.findViewById(R.id.event_img_cover);
////        btnUrl = rootView.findViewById(R.id.event_but_url);
////        btnUber = rootView.findViewById(R.id.event_but_uber);
////        btnGoogleMaps= rootView.findViewById(R.id.event_but_google_maps);
////        b.setOnClickListener(new View.OnClickListener() {
////
////            @Override
////            public void onClick(View v) {
////                // TODO Auto-generated method stub
////                addPlaces();
////            }
////
////        });
////        linearLayout = (LinearLayout) rootView.findViewById(R.id.eventsFragLinear);
////    }
////
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                             Bundle savedInstanceState) {
////        rootView = inflater.inflate(R.layout.activity_home, container, false);
////        return rootView;
////    }
////
////    public void addPlaces() {
////        Button button = new Button(getActivity()); // needs activity context
////        button.setText("button name");
////        linearLayout.addView(button);
////    }
////}
