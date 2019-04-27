package com.shollmann.events.api;

import com.shollmann.events.api.baseapi.Api;
import com.shollmann.events.api.baseapi.BaseApiCall;
import com.shollmann.events.api.baseapi.Cache;
import com.shollmann.events.api.baseapi.CallId;
import com.shollmann.events.api.contract.EventbriteApiContract;
import com.shollmann.events.api.contract.TicketmasterApiContract;
import com.shollmann.events.api.model.PaginatedEvents;

import java.io.IOException;

import retrofit2.Callback;

public class TicketmasterApi extends Api<TicketmasterApiContract> {

    public String apikey= "nYOir7P11knKKGYONw1a6jVWuXb63J2o";
    public TicketmasterApi(Builder builder) {
        super(builder);
    }

    public void getEvents(double lat, double lon, String apikey, PaginatedEvents lastPageLoaded, CallId callId, Callback<PaginatedEvents> callback) {
        int pageNumber = lastPageLoaded != null ? lastPageLoaded.getPagination().getPageNumber() + 1 : 1;
        System.out.println(lat+lon+apikey);
        Cache cache = new Cache.Builder()
                .policy(Cache.Policy.CACHE_ELSE_NETWORK)
                .ttl(Cache.Time.ONE_MINUTE)
                .key(String.format("get_events_%1$s_%2$s_%3$s_%4$s", lat, lon, apikey, pageNumber))
                .build();

        BaseApiCall<PaginatedEvents> apiCall = registerCall(callId, cache, callback, PaginatedEvents.class);

        if (apiCall != null && apiCall.requiresNetworkCall()) {
            System.out.println("APICALLNULL TICKETMASTERAPI.GETEVETNS");
            getService().getEvents(lat, lon, apikey).enqueue(apiCall);
        }
    }

    public static class Builder extends Api.Builder {
        @Override
        public TicketmasterApi build() {
            super.validate();
            return new TicketmasterApi(this);
        }
    }

}
