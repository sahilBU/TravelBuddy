package com.journaldev.MapsInAction;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class input_location extends Activity {


   public  input_location(){

    }


    EditText input;
    EditText output;
    Button button;
    ArrayList<Double> location  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_location);

        input = findViewById(R.id.input);
        output = findViewById(R.id.output);
        button = findViewById(R.id.button);



        button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                final  String loc1 = input.getText().toString();
                final String loc2 = output.getText().toString();
                location = getLocationFromAddress(loc1 , loc2);

                Intent intent = new Intent(input_location.this, GoogleMapsMainActivity.class);
                intent.putExtra("location_lat1", location.get(0));
                intent.putExtra("location_lang1", location.get(1));
                intent.putExtra("location_lat2", location.get(2));
                intent.putExtra("location_lang2", location.get(3));

                startActivity(intent);



            }
        });
    }

    public ArrayList getLocationFromAddress(String strAddress1, String strAddress2) {

        Geocoder coder = new Geocoder(getApplicationContext());
        List<Address> address1;
        List<Address> address2;
        ArrayList<Double> result = new ArrayList<>();

        try {
            address1 = coder.getFromLocationName(strAddress1, 1);
            address2 = coder.getFromLocationName(strAddress2, 1);

            if (address1 == null || address2 == null) {
                return null;
            }
            Address location1 = address1.get(0);
            Address location2 = address2.get(0);
            double lat1 = location1.getLatitude();
            double lng1 = location1.getLongitude();

            double lat2 = location2.getLatitude();
            double lng2 = location2.getLongitude();


            result.add(lat1);
            result.add(lng1);
            result.add(lat2);
            result.add(lng2);

            return result; }

            catch (Exception e) {
            return null;
        }
    }



//
//
//

}


