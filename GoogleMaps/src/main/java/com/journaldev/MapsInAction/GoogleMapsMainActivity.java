package com.journaldev.MapsInAction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public  class GoogleMapsMainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int ACCESS_COARSE_LOCATION_PERMISSION_REQUEST = 7001;

        SupportMapFragment mapFragment;
        Location location;
        private GoogleMap mMap;
        CameraUpdateFactory cm;
        UiSettings ui;
        private Context context;
        Double lang1;
        Double lat1;
        Double lang2;
        Double lat2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.googlemaps_activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            try {
                String in = getIntent().getStringExtra("message");

                System.out.println(in+"HFOEWFIOEJ");

                String[] parts = in.split(",");
                String string1 = parts[0];
                String string2 = parts[1];

                System.out.println(string1);  // prints name1
//                System.out.println(string2);

                lang1 = Double.parseDouble(string2);
                lat1 = Double.parseDouble(string1);

                lat2 = Double.parseDouble("42.3584");
                lang2 = Double.parseDouble("-71.0598");

//                lat2 = location.getLatitude();
//                lang2 = location.getLongitude();

            }

            catch (Exception e){
                System.out.println(e);
            }

            System.out.println(lang1);
            if(lang1 ==0.0 || lat1 ==0.0 || lang2 == 0.0 || lat2 ==0.0) {
                Intent intent = new Intent(GoogleMapsMainActivity.this, input_location.class);
                startActivity(intent);
                finish();
            }
//        float zoomLevel = 16.0f; //This goes up to 21
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));



else {

                mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

                FloatingActionButton fab = findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                                googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat1, lang1))
                                        .title("Destination")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat2, lang2))
                                        .title("Current Location")
                                        );


                                    MapsInitializer.initialize(getApplicationContext());

                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat1, lang1), 5));
                            }

                        });

                    }
                });

            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat1, lang1))
                    .title("LinkedIn")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat2, lang2))
                    .title("Facebook")
                    .snippet("Facebook HQ: Menlo Park"));

//            googleMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(37.3092293, -122.1136845))
//                    .title("Apple"));
            MapsInitializer.initialize(getApplicationContext());

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat1, lang1), 8));

        }

        private void checkForLocationPermission() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        ACCESS_COARSE_LOCATION_PERMISSION_REQUEST);

            } else {
                LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                System.out.println(location.getLatitude()+location.getLongitude());
            }
        }
    }

