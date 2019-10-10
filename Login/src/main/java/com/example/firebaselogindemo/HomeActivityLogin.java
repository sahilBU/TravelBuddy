package com.example.firebaselogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class HomeActivityLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnSearch;
    private Button btnEvent;
    private  Button btnSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_login);

        mAuth = FirebaseAuth.getInstance();

        btnSearch = findViewById(R.id.btnSearch);
        btnSettings = findViewById(R.id.btnSettings);

        btnEvent = findViewById(R.id.btnEvent);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivityLogin.this, SettingsActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
