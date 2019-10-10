package com.shollmann.events.ui.activity;


import com.google.firebase.codelab.friendlychat.model.User;
import com.google.firebase.codelab.friendlychat.model.FirebaseModel;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shollmann.events.R;
import com.shollmann.events.api.EventbriteApi;
import com.shollmann.events.api.baseapi.ApiClient;
import com.shollmann.events.api.baseapi.CallId;
import com.shollmann.events.api.baseapi.CallOrigin;
import com.shollmann.events.api.baseapi.CallType;
import com.shollmann.events.api.baseapi.TicketmasterApiCall;
import com.shollmann.events.api.contract.TicketmasterApiContract;
import com.shollmann.events.api.model.Event;
import com.shollmann.events.api.model.PaginatedEvents;
import com.shollmann.events.helper.Constants;
import com.shollmann.events.helper.PreferencesHelper;
import com.shollmann.events.helper.ResourcesHelper;
import com.shollmann.events.ui.EventbriteApplication;
import com.shollmann.events.ui.TicketmasterApplication;
import com.shollmann.events.ui.adapter.EventAdapter;
import com.shollmann.events.ui.event.LoadMoreEvents;
import com.shollmann.events.api.TicketmasterApi;
import com.shollmann.events.api.model.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.json.*;


public class EventsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final int NO_FLAGS = 0;
    private static final int ACCESS_COARSE_LOCATION_PERMISSION_REQUEST = 7001;
    private FirebaseModel model = new FirebaseModel();
    private String curCat;
    private Toolbar toolbar;
    private TextView txtNoResults;
    private TextView txtWaitForResults;
    private SearchView searchView;
    private MenuItem menuSearch;
    private CoordinatorLayout coordinatorLayout;
    private EventbriteApi eventbriteApi;
    private TicketmasterApi ticketmasterApi;
    private Location location;
    private RecyclerView recyclerEvents;
    private EventAdapter eventAdapter;
    private PaginatedEvents lastPageLoaded;
    private String currentQuery;
    private CallId getEventsCallId;
    private TextView mTMResults;

    private Double curLat;
    private Double curLong;
    private Map<String, String> catDictionary = new HashMap<String, String>() {{
        put("Music", "103");
        put("TicketMaster","999");
        put("Business & Professional", "101");
        put("Food & Drink","110");
        put("Community & Culture","113");
        put("Performing & Visual Arts","105");
        put("Film, Media & Entertainment","104");
        put("Sports & Fitness","108");
        put("Health & Wellness","107");
        put("Science & Technology","102");
        put("Travel & Outdoor","109");
        put("Charity & Causes","111");
        put("Religion & Spirituality","114");
        put("Family & Education","115");
        put("Seasonal & Holiday","116");
        put("Government & Politics","112");
        put("Fashion & Beauty","106");
        put("Home & Lifestyle","117");
        put("Auto, Boat & Air","118");
        put("Hobbies & Special Interest","119");
        put("Other","199");
        put("","");
    }};
    // C
    boolean mainAppFlag = false;
    ArrayList<HashMap> listTickets = new ArrayList<HashMap>();
    JSONArray events = null;
    JSONObject embedded = null;
    String curEvents;

    public static double getShorterCoordinate(double coordinate) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat(Constants.COORDINATES_FORMAT, dfs);

        return Double.parseDouble(decimalFormat.format(coordinate));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        curCat= getIntent().getStringExtra("message");

        model.getSingleUser(model.getUid(), new FirebaseModel.MyCallBack() {
            @Override
            public void onCallback(Object object) {
                User this_user = (User) object;
                curLat=(this_user.getLatitude());
                curLong=(this_user.getLongitute());
                String cat;
                try {
                    cat = catDictionary.get(curCat);
                    curCat= cat;
                    if (curCat.equals("999")){
                        Intent i = new Intent(getApplicationContext(), EventsTicketmaster.class);
                        i.putExtra("location", curLat.toString()+","+curLong.toString());
                        startActivity(i);
                        finish();
                    }
                }
                catch (Exception e){
                    cat= "";
                    curCat= cat;
                }
                eventbriteApi = EventbriteApplication.getApplication().getApiEventbrite();

                findViews();
                setupTaskDescription();
                setupToolbar();
                setupRecyclerView();
                checkForLocationPermission();
            }
        });

    }

    public EventAdapter onCreateHelpers(String query){
//        setupRecyclerView();
        mainAppFlag = true;
        eventbriteApi = EventbriteApplication.getApplication().getApiEventbrite();
        getEventsCallId = new CallId(CallOrigin.HOME, CallType.GET_EVENTS);
        Callback<PaginatedEvents> callback = generateGetEventsCallback();
        eventbriteApi.registerCallback(getEventsCallId, callback);
        try {
            eventbriteApi.getEvents(query, curLat, curLong
            , curCat, "date", "venue", lastPageLoaded, getEventsCallId, callback);
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }
        PreferencesHelper.setLastSearch(query);

        return eventAdapter;
    }

    private void setupRecyclerView() {
        recyclerEvents.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerEvents.setLayoutManager(layoutManager);
        eventAdapter = new EventAdapter(new ArrayList<Event>());
        recyclerEvents.setAdapter(eventAdapter);

        recyclerEvents.setVisibility(View.GONE);
    }

    private void updateEventsList(List<Event> eventList) {
        eventAdapter.add(eventList);
        eventAdapter.notifyDataSetChanged();

        if (recyclerEvents.getVisibility() != View.VISIBLE) {
            recyclerEvents.setVisibility(View.VISIBLE);
        }
    }

    private void checkForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_PERMISSION_REQUEST);

        } else {
            LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            getEvents(null);
        }
    }


    private void getEvents(String query) {
        Snackbar.make(coordinatorLayout, R.string.getting_events, Snackbar.LENGTH_SHORT).show();
        getEventsCallId = new CallId(CallOrigin.HOME, CallType.GET_EVENTS);
        Callback<PaginatedEvents> callback = generateGetEventsCallback();
        eventbriteApi.registerCallback(getEventsCallId, callback);
        try {
            eventbriteApi.getEvents(query, curLat, curLong
            , curCat, "date", "venue", lastPageLoaded, getEventsCallId, callback);
        } catch (IOException e) {
            //TODO
            System.out.println("HOTESJFOISJEGOIRS");
            e.printStackTrace();
        }
        PreferencesHelper.setLastSearch(query);
    }


    private Callback<PaginatedEvents> generateGetEventsCallback() {
        return new Callback<PaginatedEvents>() {

            @Override
            public void onResponse(Call<PaginatedEvents> call, Response<PaginatedEvents> response) {
                PaginatedEvents paginatedEvents = response.body();
                System.out.println(response.body());
                if (paginatedEvents.getEvents().isEmpty()) {
                    eventAdapter.setKeepLoading(false);
                    if (eventAdapter.getItemCount() == 0) {
                        txtNoResults.setVisibility(View.VISIBLE);
                        return;
                    }
                }
//                txtNoResults.setVisibility(View.GONE);
                if (!mainAppFlag){
                    txtNoResults.setVisibility(View.GONE);
                }
                System.out.println(response.body());
                updateEventsList(paginatedEvents.getEvents());
                lastPageLoaded = paginatedEvents;
            }

            @Override
            public void onFailure(Call<PaginatedEvents> call, Throwable t) {
                handleGetEventsFailure();
            }
        };
    }

    private void handleGetEventsFailure() {
        txtWaitForResults.setVisibility(View.GONE);
        txtNoResults.setVisibility(View.VISIBLE);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void findViews() {
        toolbar = findViewById(R.id.toolbar);
        txtNoResults = findViewById(R.id.home_txt_no_results);
        txtWaitForResults = findViewById(R.id.home_txt_wait_first_time);
        coordinatorLayout = findViewById(R.id.home_coordinator_layout);
        recyclerEvents = findViewById(R.id.home_events_recycler);
    }

    private void setupTaskDescription() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap icon = BitmapFactory.decodeResource(ResourcesHelper.getResources(),
                    R.mipmap.ic_launcher);
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(ResourcesHelper.getString(R.string.app_name), icon, ResourcesHelper.getResources().getColor(R.color.colorPrimary));
            this.setTaskDescription(taskDescription);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        menuSearch = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuSearch);
        EditText edtSearch = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        ImageView searchClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_action_close);
        edtSearch.setTextColor(Color.WHITE);
        edtSearch.setHintTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(this);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        resetSearch();
        currentQuery = query.trim();
        getEvents(currentQuery);
        searchView.setQuery(Constants.EMPTY_STRING, false);
        searchView.setIconified(true);
        hideKeyboard();
        return true;
    }

    private void resetSearch() {
        lastPageLoaded = null;
        eventAdapter.reset();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) EventbriteApplication.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), NO_FLAGS);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoadMoreEvents event) {
        getEvents(currentQuery);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventbriteApi.unregisterCallback(getEventsCallId);
        EventBus.getDefault().unregister(this);
    }
}

