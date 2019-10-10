package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.firebaselogindemo.LoginActivity;
import com.example.firebaselogindemo.SettingsActivity;
import com.example.weatherappfortravelapp.WeatherMainActivity;
import com.google.firebase.codelab.friendlychat.model.User;
import com.shollmann.events.api.EventbriteApi;
import com.shollmann.events.api.baseapi.TicketmasterApiCall;
import com.shollmann.events.ui.EventbriteApplication;
import com.shollmann.events.ui.EventbriteApplication;

import com.shollmann.events.ui.activity.EventsActivity;
import com.google.firebase.codelab.friendlychat.model.FirebaseModel;
import com.shollmann.events.ui.adapter.TicketAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private TextView mTMResults;
    private Button message_bth;
    private Button weather_btn;
    private Button eventbrite_btn;
    private Button cardview_btn;
    private Button login_btn;
    private Location current_location;
    private LocationManager locationManager;
    private LocationListener locationListener; 
    private static final int ACCESS_COARSE_LOCATION_PERMISSION_REQUEST = 7001;

    private ActionBar toolbar;
    private TextView cityCur;
    private TextView tempCur;
    private  TextView descCur;
    private TextView dateCur;
//    private ImageView foreCur;
    private ArrayList<HashMap> forecast = new ArrayList<HashMap>();

    private FirebaseModel model = new FirebaseModel();

    private MenuItem profile_image;

    private boolean read_loc_once = false;
    private double homeLat;
    private double homeLong;

    private double homeLatt =42.3601;
    private double homeLongg =-71.0589;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

//    HashSet<String> friendsDataModelHashSet = new HashSet<String>();
//    String friendsDataModelHashSet = "";
//    ArrayList<User> friendsDataModelArrayList = new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForLocationPermission();
//        getFriendsList();

        if(model.isLogIn() == false){
            try {
                Intent myIntent = new Intent(MainActivity.this, Class.forName("com.example.firebaselogindemo.LoginActivity"));
                startActivity(myIntent);
                finish();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            finish();
            return;
        }

        model.afterLogin(new FirebaseModel.MyCallBack() {
            @Override
            public void onCallback(Object object) {
                boolean new_user = (boolean) object;

                // navigate to profile change
                if(new_user == true){
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    finish();
                }
            }
        });

        toolbar = getSupportActionBar();
        tempCur = findViewById(R.id.weather_home_temp);
        cityCur = findViewById(R.id.weather_home_city);
        descCur= findViewById(R.id.weather_home_desc);
        dateCur= findViewById(R.id.weather_home_date);
//        foreCur = findViewById(R.id.weather_home_img_cover);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        model.getSingleUser(model.getUid(), new FirebaseModel.MyCallBack() {
            @Override
            public void onCallback(Object object) {
                User this_user = (User) object;
                homeLat=this_user.getLatitude();
                homeLong=this_user.getLongitute();
                updateWeather(homeLat,homeLong);
                updateEvents();
            }
        });
    }

    private void checkForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    ACCESS_COARSE_LOCATION_PERMISSION_REQUEST);

        } else {
            // Acquire a reference to the system Location Manager
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
// Define a listener that responds to location updates
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    if(read_loc_once == false) {
                        read_loc_once = true;
                        current_location = location;
                        homeLat=location.getLatitude();
                        homeLong = location.getLongitude();
//                        updateWeather(homeLat,homeLong);
//                        updateEvents();
                        System.out.println(location.getLatitude() + location.getLongitude());
                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };

// Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        }
    }


    private void updateWeather(Double curLat, Double curLong){
        RecyclerView recyclerView;

        WeatherMainActivity weather_model =  new WeatherMainActivity();

        forecast=  weather_model.fetchInfo(curLat,curLong);

//        forecast=  weather_model.fetchInfo(location.getLatitude(),location.getLongitude());
        cityCur.setText(forecast.get(0).get("city").toString());
        tempCur.setText(forecast.get(0).get("temp").toString());
        dateCur.setText("Today");
        descCur.setText(forecast.get(0).get("description").toString());

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.weather_linear);
        String main = forecast.get(0).get("main").toString();

        if (main.equals("Clear")){
            linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.clear2));
        }
        else if(main.equals("Clouds")){
            linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.cloudy1));
        }
        else if(main.equals("Rain") || main.equals("Drizzle")){
            linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.rain));
        }
        else if(main.equals("Thunderstorm")){
            linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.thunder));
        }
        else if(main.equals("Snow")){
            linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.snow));
        }
        else if(main.equals("Mist") || main.equals("Fog") || main.equals("Haze")){
            linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.misty));
        }
        else {
            linearLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.dust));
        }


        ArrayList<HashMap> forecastSplice = new ArrayList<HashMap>(forecast.subList(1,forecast.size()));
        WeatherHomeAdapter weatherAdapter = new WeatherHomeAdapter(forecastSplice);
        recyclerView = findViewById(R.id.weather_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(weatherAdapter);

    }

    private void updateEvents(){
        ArrayList<HashMap> listTickets = new ArrayList<HashMap>();
        RecyclerView recyclerView;

        String url = "https://www.eventbriteapi.com/v3/events/search/?q=&location.latitude="+homeLat+"&location.longitude="+homeLong+"&sort_by=date&expand=venue&page=1&token=VBEQ2ZP7SOEWDHH3PVOI ";
        Log.e("UPDATEEVENTS",Double.toString(homeLat)+Double.toString(homeLong));

        try {
            listTickets=new EventbriteHomeApiCall().execute(url).get();
//            System.out.println(listTickets.get(1));
        }
        catch (
                ExecutionException e){
            System.out.println(e);
        }
        catch (InterruptedException e){
            System.out.println(e);
        }

        // Initializing list view with the custom adapter
        EventHomeAdapter ticketAdapter = new EventHomeAdapter(listTickets);
        recyclerView = findViewById(R.id.events_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(ticketAdapter);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_events:
                    toolbar.setTitle("Events");

                    try {
//                        Intent myIntent = new Intent(MainActivity.this, Class.forName("com.shollmann.events.ui.activity.EventsActivity"));
                        Intent myIntent = new Intent(MainActivity.this, Class.forName("com.shollmann.events.ui.activity.EventsHome"));
                        startActivity(myIntent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    return true;
                case R.id.navigation_message:
                    toolbar.setTitle("Messages");

                    try {
                        Intent myIntent = new Intent(MainActivity.this, Class.forName("com.google.firebase.codelab.friendlychat.MessageMainActivity"));
                        startActivity(myIntent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.navigation_search:
                    toolbar.setTitle("Search");

                    try {
//                        getFriendsList();
                        Intent myIntent = new Intent(getApplicationContext(),SwipeMainActivity.class);
//                        myIntent.putExtra("friendsList", friendsDataModelHashSet);
                        startActivity(myIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.profile:
                    toolbar.setTitle("Profile");

                    try {
                        Intent myIntent = new Intent(getApplicationContext(),Class.forName("com.example.firebaselogindemo.SettingsActivity"));
                        startActivity(myIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
            }




            return false;
        }
    };

//    private void getFriendsList(){
//        model.fetchFriends(model.getUid(), new FirebaseModel.MyCallBack() {
//            @Override
//            public void onCallback(Object object) {
//                friendsDataModelArrayList = (ArrayList<User>) object;
//                Log.e("FRIENDSMODELARRAYLISTR",friendsDataModelArrayList.get(0).toString());
//                if (friendsDataModelArrayList== null){
//                    friendsDataModelHashSet.add(model.getUid());
////                    friendsDataModelHashSet+=model.getUid();
//                }
//                else{
//                    for (int i=0; i < friendsDataModelArrayList.size(); i++) {
//                        friendsDataModelHashSet.add(friendsDataModelArrayList.get(i).getUid());
////                        friendsDataModelHashSet+=friendsDataModelArrayList.get(i).getUid();
//                    }
////                    friendsDataModelHashSet.add(model.getUid());
//                }
////                friendsDataModelHashSet+=model.getUid();
//                friendsDataModelHashSet.add(model.getUid());
//            }
//        });
//    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.top_right_menu, menu );

        if(model.isLogIn()){
            model.getUserPhoto(new FirebaseModel.MyCallBack() {
                @Override
                public void onCallback(Object object) {
                    String userPhotoUrl = (String) object;
                    profile_image = menu.findItem(R.id.profile_image);

                    Tasks task = new Tasks();

                    try {
                        String result = task.execute(userPhotoUrl).get();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sign_out_menu){
            model.userSignOut();
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // user created class
    // asynctask to do threads in background with speed
    class Tasks extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                Bitmap myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream()); //get your bitmap
                profile_image.setIcon(new BitmapDrawable(getResources(), myBitmap));

                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        // dobackground result is shown here
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }


    public static class EventbriteHomeApiCall extends AsyncTask<String , Void ,ArrayList<HashMap>> {
        String server_response;
        String ticketMasterJson;

//        com.shollmann.events.api.baseapi.TicketmasterApiCall context;
        ArrayList<HashMap> listTickets = new ArrayList<HashMap>();
        JSONArray events = null;
        JSONObject embedded = null;

        EventsActivity parent;

        @Override
        protected ArrayList<HashMap> doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                    try {
                        JSONObject jsonObj = new JSONObject(server_response);
                        events= jsonObj.getJSONArray("events");
                        System.out.println(jsonObj.toString());
                        for (int i = 0; i < events.length(); i++) {
                            try {
                                HashMap<String, String> tixList = new HashMap<>();
                                JSONObject eventJSON = events.getJSONObject(i);
                                try{
                                String nameEvent = eventJSON.getJSONObject("name").getString("text");
                                System.out.println(nameEvent);
                                tixList.put("name", nameEvent);
                                }
                                catch (Exception e){
                                    Log.e("EVENTBRITE",e.toString());
                                }
                                try{
                                String nameDescription = eventJSON.getJSONObject("description").getString("text");
                                tixList.put("description", nameDescription);
                                }
                                catch (Exception e){
                                    Log.e("EVENTBRITE",e.toString());
                                }
                                try{
                                String urlEvent = eventJSON.getString("url");
                                tixList.put("url", urlEvent);                                }
                                catch (Exception e){
                                    Log.e("EVENTBRITE",e.toString());
                                }
                                try{
                                String idEvent = eventJSON.getString("id");
                                tixList.put("id", idEvent);
                                }
                                catch (Exception e){
                                    Log.e("EVENTBRITE",e.toString());
                                }
                                try{
                                String isFree = eventJSON.getString("is_free");
                                tixList.put("is_free", isFree.equals("TRUE") ? "FREE" : "PAID");
                                }
                                catch (Exception e){
                                    Log.e("EVENTBRITE",e.toString());
                                }
                                try {
                                    JSONObject imageJSON = eventJSON.getJSONObject("logo");
                                    String imageUrlEvent = imageJSON.getString("url");
                                    tixList.put("imageUrl", imageUrlEvent);
                                }
                                catch (Exception e){
                                    Log.e("EVENTBRITE",e.toString());
                                }
                                try{

                                JSONObject datesJSON = eventJSON.getJSONObject("start");

                                String localDateEvent = datesJSON.getString("local");

                                tixList.put("localDate", localDateEvent);
                                }
                                catch (Exception e){
                                    Log.e("EVENTBRITE",e.toString());
                                }
                                try{
                                JSONObject venuesTestJSON = eventJSON.getJSONObject("venue");

                                String venueLocLatEvent = venuesTestJSON.getString("latitude");
                                tixList.put("venueLat", venueLocLatEvent);
                                String venueLocLonEvent = venuesTestJSON.getString("longitude");
                                tixList.put("venueLon", venueLocLonEvent);
                                listTickets.add(tixList);
                                }
                                catch (Exception e){
                                    Log.e("EVENTBRITE",e.toString());
                                }
                            }
                            catch (JSONException e){
                                Log.e("MYAPP", "unexpected JSON exception", e);
                                System.out.println("THIS IS TICKETMASTERAPICALLERROR"+this.listTickets.get(0));
                            }
                        }
                    }
                    catch (JSONException e){
                        Log.e("MYAPP", "unexpected JSON exception", e);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        return ticketMasterJson;
//        return server_response;
            return listTickets;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap> s) {
            super.onPostExecute(s);
            Log.e("Response", "" + s);
//        processValue(s);
            ticketMasterJson = server_response;
//        parent.ReturnThreadResult(s);
        }

        void processValue(String s) {
            //handle value
            //Update GUI, show toast, etc..


        }

        // Converting InputStream to String

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }
    }




}
