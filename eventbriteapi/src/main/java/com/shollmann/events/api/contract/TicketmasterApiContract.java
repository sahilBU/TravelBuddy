package com.shollmann.events.api.contract;

import com.shollmann.events.api.model.PaginatedEvents;
import com.shollmann.events.helper.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TicketmasterApiContract {
    String apikey = "nYOir7P11knKKGYONw1a6jVWuXb63J2o";

    @GET("/discovery/v2/events")
    Call<PaginatedEvents> getEvents(
            @Query("location.latitude") double latitude,
            @Query("location.longitude") double longitude,
            @Query("apikey") String apikey);
}
//https://www.eventbriteapi.com/v3/categories/103/