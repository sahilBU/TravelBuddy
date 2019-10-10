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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class UberMainActivity extends AppCompatActivity {
    Double lang1;
    Double lat1;
    Double lang2;
    Double lat2;
    String eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uber_activity_main);
        try {
            String in = getIntent().getStringExtra("message");
            String[] parts = in.split(",");
            String string0 = parts[0];
            String string1 = parts[1];
            String string2 = parts[2];
            String string3 = parts[3];
            String string4 = parts[4];



            lang1 = getShorterCoordinate(Double.parseDouble(string3));
            lat1 = getShorterCoordinate(Double.parseDouble(string2));
            lang2 = getShorterCoordinate(Double.parseDouble(string1));
            lat2 = getShorterCoordinate(Double.parseDouble(string0));
            eventName = string4;
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
                .setPickupLocation(lat2, lang2, "Uber HQ", "1455 Market Street, San Francisco")
                .setDropoffLocation(lat1, lang1, "Embarcadero", "One Embarcadero Center, San Francisco") // Price estimate will only be provided if this is provided.
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
    public static double getShorterCoordinate(double coordinate) {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("###.######", dfs);

        return Double.parseDouble(decimalFormat.format(coordinate));
    }
}
