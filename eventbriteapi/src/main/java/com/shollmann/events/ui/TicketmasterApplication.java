package com.shollmann.events.ui;

import android.app.Application;

import com.shollmann.events.api.EventbriteApi;
import com.shollmann.events.api.TicketmasterApi;
import com.shollmann.events.api.contract.EventbriteApiContract;
import com.shollmann.events.api.contract.TicketmasterApiContract;
import com.shollmann.events.db.CachingDbHelper;
import com.shollmann.events.helper.Constants;

public class TicketmasterApplication extends Application {
    private static TicketmasterApplication instance;
//    private static TicketmasterApplication context;
    private CachingDbHelper cachingDbHelper;
    private TicketmasterApi apiTicketmaster;

//    public static TicketmasterApplication getApplication() {
//        System.out.println("TICKETMASTERAPPLICATION.GETAPPLICATION" + instance); return instance;
//    }
    public static TicketmasterApplication getApplication() {
        System.out.println("TICKETMASTERAPPLICATION.GETAPPLICATION" + instance); return instance;
//        System.out.println("TICKETMASTERAPPLICATION.GETAPPLICATION" + instance.getApplicationContext()); return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        this.context = context;
        System.out.println("wtf"+instance);
        this.apiTicketmaster = (TicketmasterApi) new TicketmasterApi.Builder()
                .baseUrl(Constants.TicketmasterApi.URL)
                .contract(TicketmasterApiContract.class)
                .build();

        this.cachingDbHelper = new CachingDbHelper(getApplicationContext());
    }

    public CachingDbHelper getCachingDbHelper() {
        return cachingDbHelper;
    }

    public TicketmasterApi getApiTicketmaster() {
        return apiTicketmaster;
    }

}
