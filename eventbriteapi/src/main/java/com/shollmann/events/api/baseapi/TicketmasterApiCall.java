package com.shollmann.events.api.baseapi;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.shollmann.events.ui.activity.EventsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class TicketmasterApiCall extends AsyncTask<String , Void ,ArrayList<HashMap>> {
    String server_response;
    String ticketMasterJson;

    TicketmasterApiCall context;
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
            embedded = jsonObj.getJSONObject("_embedded");
            JSONObject jsonObjEvent = new JSONObject(embedded.toString());
            System.out.println(jsonObjEvent.toString());
            events= jsonObjEvent.getJSONArray("events");
            for (int i = 0; i < events.length(); i++) {
                try {
                    HashMap<String, String> tixList = new HashMap<>();
                    JSONObject eventJSON = events.getJSONObject(i);
                    String nameEvent = eventJSON.getString("name");
                    tixList.put("name", nameEvent);
                    String urlEvent = eventJSON.getString("url");
                    tixList.put("url", urlEvent);
                    String idEvent = eventJSON.getString("id");
                    tixList.put("id", idEvent);
                    String localeEvent = eventJSON.getString("locale");
                    tixList.put("locale", localeEvent);

                    JSONArray imageEvent = eventJSON.getJSONArray("images");
                    JSONObject imageJSON = imageEvent.getJSONObject(1);
                    String imageUrlEvent = imageJSON.getString("url");
                    tixList.put("imageUrl", imageUrlEvent);

                    JSONObject datesJSON = eventJSON.getJSONObject("dates");
                    JSONObject startJSON = datesJSON.getJSONObject("start");

                    String localDateEvent = startJSON.getString("localDate");
                    tixList.put("localDate", localDateEvent);
                    try {
                        String localTimeEvent = startJSON.getString("localTime");
                        tixList.put("localTime", localTimeEvent);
                    } catch (Exception e) {
                        System.out.println("nologo");
                    }
                    try {
                        String dateTimeEvent = startJSON.getString("dateTime");
                        tixList.put("dateTime", dateTimeEvent);
                    } catch (Exception e) {
                        System.out.println("nologo");

                    }
                    try {
                        String distanceEvent = eventJSON.getString("distance");
                        tixList.put("distance", distanceEvent);
                    } catch (Exception e) {
                        System.out.println("nologo");

                    }
                    try {
                        String unitsEvent = eventJSON.getString("units");
                        tixList.put("units", unitsEvent);
                    } catch (Exception e) {
                        System.out.println("nologo");

                    }

                    try {
                        String infoEvent = eventJSON.getString("info");
                        tixList.put("info", infoEvent);
                    } catch (Exception e) {
                        System.out.println("nologo");

                    }
                    try {
                        String pleaseNoteEvent = eventJSON.getString("pleaseNote");
                        tixList.put("pleaseNote", pleaseNoteEvent);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                    JSONObject embeddedJSON = eventJSON.getJSONObject("_embedded");
                    JSONArray venuesJSON = embeddedJSON.getJSONArray("venues");
                    JSONObject venuesTestJSON = venuesJSON.getJSONObject(0);
                    String venueNameEvent = venuesTestJSON.getString("name");
                    tixList.put("venueName", venueNameEvent);
                    JSONObject venueLocationEvent = venuesTestJSON.getJSONObject("location");

                    String venueLocLatEvent = venueLocationEvent.getString("latitude");
                    tixList.put("venueLat", venueLocLatEvent);
                    String venueLocLonEvent = venueLocationEvent.getString("longitude");
                    tixList.put("venueLon", venueLocLonEvent);
//                    System.out.println("TICKETMASTERAPICALL: " + venueLocLatEvent);
//                    this.listTickets.add(tixList);
                    listTickets.add(tixList);
                }
                catch (JSONException e){
                    Log.e("MYAPP", "unexpected JSON exception", e);
//                    System.out.println("THIS IS TICKETMASTERAPICALLERROR"+this.listTickets.get(0));
                }
            }
        }
        catch (JSONException e){
            Log.e("MYAPP", "unexpected JSON exception", e);
            System.out.println("THIS IS TICKETMASTERAPICALL"+this.listTickets.get(0));
        }
        System.out.println(listTickets.size());

        System.out.println(listTickets.get(0));

        System.out.println(this.listTickets.get(0));
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



