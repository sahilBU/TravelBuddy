package com.example.firebaselogindemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnLogout;
    private SeekBar sbRadius;
    private Button btnPhoto;
    private ImageView ivPhoto;

    private static final int ALBUM_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int CROP_REQUEST_CODE = 2;
    private File tempFile;
    private String TAG = "Set Radius";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        btnPhoto = findViewById(R.id.btnPhoto);
        sbRadius = findViewById(R.id.sbRadius);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG,"Radius is set to " + progress);
                Toast.makeText(getBaseContext(),
                        "Radius is set to"+ progress,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });

    }


    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Profile Picture");
        String[] items = { "choose from local", "camera" };
        builder.setNegativeButton("cancel", null);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // choose local pic
                        getPicFromAlbm();
                        break;
                    case 1: // take a picture
                        getPicFromCamera();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    //pic from camera
    private void getPicFromCamera() {
        //save the photo
        tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        //go call system camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //check sdk version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //if higher than Android7.0,use FileProvider to get Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(SettingsActivity.this, "com.example.firebaselogindemo.FileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            Log.e("get uri", contentUri.toString());
        } else {    //or use Uri.fromFile(file)method to get Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    //pic from album
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }


    //crop picture
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Uri contentUri = FileProvider.getUriForFile(SettingsActivity.this, "com.example.firebaselogindemo", tempFile);
                            cropPhoto(contentUri);
                        } else {
                            cropPhoto(Uri.fromFile(tempFile));
                        }
                    }
                    break;
                case ALBUM_REQUEST_CODE:
                    if (resultCode == RESULT_OK) {
                        Uri uri = intent.getData();
                        cropPhoto(uri);
                    }
                    break;
                case CROP_REQUEST_CODE:
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        Bitmap image = bundle.getParcelable("data");
                        //set to image view
                        ivPhoto.setImageBitmap(image);
                    }
                    break;
            }
        }
    }


}



//        Query fbSearchQuery = FirebaseDatabase.getInstance().getReference().orderByChild("picUrl");
//        fbSearchQuery.addValueEventListener(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()){
//                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
//                        if(dataSnapshot.child("userName").getValue().equals("BowenJia")){
//                            url = snapshot.child("picUrl").getValue().toString();
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        String pic_jeff = "https://platform-lookaside.fbsbx.com/platform/profilepic" +
//                "/?asid=668089376952860&height=50&width=50&ext=1557976673&hash=AeS_PhCjyJq0Oc0g";
//                Picasso.get().load(pic_jeff)
//                .into(imgPhoto);
