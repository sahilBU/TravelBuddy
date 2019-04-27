package com.example.ubertest;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.error.ApiError;

public class UberMainActivity extends AppCompatActivity {
    Double lang1;
    Double lat1;
    Double lang2;
    Double lat2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uber_activity_main);
        try {
            String in = getIntent().getStringExtra("message");

            System.out.println(in+"HFOEWFIOEJ");

            String[] parts = in.split(",");
            String string1 = parts[0];
            String string2 = parts[1];

            System.out.println(string1);  // prints name1
//                System.out.println(string2);

            lang1 = Double.parseDouble(string1);
            lat1 = Double.parseDouble(string2);
            lang2 = Double.parseDouble(string1);
            lat2 = Double.parseDouble(string2);

//                lang1 = in.getDoubleExtra("location_lang1", 42.331967);
//                lat1 = in.getDoubleExtra("location_lat1", -71.0201737);
//                lang2 = in.getDoubleExtra("location_lang2", 0.0);
//                lat2 = in.getDoubleExtra("location_lat2", 0.0);
        }
        catch (Exception e){
            System.out.println(e);
        }

        SessionConfiguration config = new SessionConfiguration.Builder()
                // mandatory
                .setClientId("Dp9Viv-IS3hvXgrGGDiPOZddsrlBfwLz")
                // required for enhanced button features
                .setServerToken("rSplFXVpNFMCyqheqtNl-0sW8QCu-EmaM-8DgbAU")
                // optional: set sandbox as operating environment
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();

        UberSdk.initialize(config);

        RideRequestButton requestButton = (RideRequestButton) findViewById(R.id.requestButton);

        RideParameters rideParams = new RideParameters.Builder()
                .setPickupLocation(37.775304, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
                .setDropoffLocation(37.795079, -122.4397805, "Embarcadero", "One Embarcadero Center, San Francisco") // Price estimate will only be provided if this is provided.
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d") // Optional. If not provided, the cheapest product will be used.
                .build();
        // set parameters for the RideRequestButton instance
        requestButton.setRideParameters(rideParams);

        ServerTokenSession session = new ServerTokenSession(config);
        requestButton.setSession(session);

        requestButton.loadRideInformation();

        RideRequestButtonCallback callback = new RideRequestButtonCallback() {

            @Override
            public void onRideInformationLoaded() {
                // react to the displayed estimates
            }

            @Override
            public void onError(ApiError apiError) {
                // API error details: /docs/riders/references/api#section-errors
            }

            @Override
            public void onError(Throwable throwable) {
                // Unexpected error, very likely an IOException
            }
        };
        requestButton.setCallback(callback);
    }
}
