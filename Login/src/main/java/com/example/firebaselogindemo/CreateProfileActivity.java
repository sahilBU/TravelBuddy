package com.example.firebaselogindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.firebaselogindemo.com.example.firebaselogindemo.model.profile;
import com.google.firebase.codelab.friendlychat.model.FirebaseModel;
import com.google.firebase.codelab.friendlychat.model.User;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProfileActivity extends AppCompatActivity {

    private Button btnUpdate;
    private EditText edtName;
    private EditText edtGender;
    private EditText edtAge;
    private EditText edtPhone;
    private EditText edtEmail;
    private EditText edtLocation;

    private ImageView imgPhoto;
    private String url = "";

    private FirebaseModel model = new FirebaseModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        btnUpdate = findViewById(R.id.btnUpdate);
        edtName = findViewById(R.id.edtName);
        edtGender = findViewById(R.id.edtAge);
        edtAge = findViewById(R.id.edtAge);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtLocation= findViewById(R.id.edtLocation);

        imgPhoto = findViewById(R.id.imgPhoto);

        if(model.mFirebaseUser.getPhotoUrl() != null)
            url = model.mFirebaseUser.getPhotoUrl().toString();
        if(model.mFirebaseUser.getEmail() != null)
            edtEmail.setText(model.mFirebaseUser.getEmail());
        if(model.mFirebaseUser.getPhoneNumber() != null)
            edtPhone.setText(model.mFirebaseUser.getPhoneNumber());
        if(model.mFirebaseUser.getDisplayName() != null)
            edtName.setText(model.mFirebaseUser.getDisplayName());

        //update user profile to firebase database
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setUname(edtName.getText().toString());
                user.setGender(edtGender.getText().toString());
                user.setAge(edtAge.getText().toString());
                user.setPhone(edtPhone.getText().toString());
                user.setEmail(edtEmail.getText().toString());
                user.setLocation(edtLocation.getText().toString());
                user.setPhotoUrl(url);
                user.setUid(model.mFirebaseUser.getUid());

                model.mUserDatabase.push().setValue(user);

                try {
                    Intent myIntent = new Intent(CreateProfileActivity.this, Class.forName("com.example.myapplication.MainActivity"));
                    startActivity(myIntent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                finish();
            }
        });

    }
}
