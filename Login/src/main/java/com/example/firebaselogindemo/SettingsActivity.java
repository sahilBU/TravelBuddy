package com.example.firebaselogindemo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.codelab.friendlychat.ChatRoom;
import com.google.firebase.codelab.friendlychat.model.FirebaseModel;
import com.google.firebase.codelab.friendlychat.model.FriendlyMessage;
import com.google.firebase.codelab.friendlychat.model.User;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.journaldev.MapsInAction.input_location;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SettingsActivity extends AppCompatActivity {

    private Button btnLogout;
    private SeekBar sbRadius;
    private Button btnPhoto;
    private ImageView ivPhoto;
    private Button btnUpload;
    private RadioButton current;
    private RadioButton own;
    //private EditText useraddress;
    private double homeLat;
    private double homeLong;
    private boolean read_loc_once = false;
    private Location current_location;

    private EditText edtName;
    private EditText edtGender;
    private EditText edtAge;
    private EditText edtPhone;
    private EditText edtEmail;
    private EditText edtLocation;
    private TextView tvLocation;
    private static final int ALBUM_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int CROP_REQUEST_CODE = 2;
    public static final int REQUEST_PERMISSION = 3;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int ACCESS_COARSE_LOCATION_PERMISSION_REQUEST = 7001;
    ArrayList<Double> lt_lng = new ArrayList<>();
    String cityname;

    private File tempFile;
    private String imageFilePath = "";
    private String TAG = "Set Radius";

    private FirebaseAuth mAuth;
    private FirebaseStorage mystorage;
    private StorageReference storageReference;
    private StorageReference ref;
    input_location input_location1 = new input_location();

    private RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.btnLogout);
        btnPhoto = findViewById(R.id.btnPhoto);
//        sbRadius = findViewById(R.id.sbRadius);
        ivPhoto = findViewById(R.id.ivPhoto);
        btnUpload = findViewById(R.id.btnUpload);
        tvLocation = findViewById(R.id.tvLocation);
        edtName = findViewById(R.id.edtName);
        edtGender = findViewById(R.id.edtAge);
        edtAge = findViewById(R.id.edtAge);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtLocation = findViewById(R.id.edtLocation);
        current = findViewById(R.id.current);
        own = findViewById(R.id.userown);
        //useraddress = findViewById(R.id.useraddress);
        edtLocation.setVisibility(View.GONE);
        tvLocation.setVisibility(View.GONE);

        radioGroup = (RadioGroup) findViewById(R.id.radioSex);

        FirebaseModel model = new FirebaseModel();
        model.getSingleUser(model.getUid(), new FirebaseModel.MyCallBack() {
            @Override
            public void onCallback(Object object) {
                if(object != null) {
                    User user = (User) object;

                    edtName.setText(user.getUname());
                    edtGender.setText(user.getGender());
                    edtAge.setText(user.getAge());
                    edtPhone.setText(user.getPhone());
                    edtEmail.setText(user.getEmail());
                    edtLocation.setText(user.getLocation());
                    String userImgUrl = user.getPhotoUrl();
                    Picasso.get().load(userImgUrl).into(ivPhoto);
                }

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        boolean isChecked =  own.isChecked();

                        if(isChecked)
                        {
                            edtLocation.setVisibility(View.VISIBLE);
                            tvLocation.setVisibility(View.VISIBLE);


                            //GEOCODE from map should come which converts address to lang and lat.

                        }

                        else {
                            current.setChecked(true);
                            edtLocation.setVisibility(View.GONE);
                            tvLocation.setVisibility(View.GONE);



                            // current location code.
                        }

                    }

                });






                //give permission
                if (ContextCompat.checkSelfPermission(SettingsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_PERMISSION);
                }


                //setup firebase storage for image files
                mystorage = FirebaseStorage.getInstance();
                storageReference = mystorage.getReference();

                btnLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseModel model = new FirebaseModel();
                        model.userSignOut();
                        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                        finish();
                    }
                });

                btnUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final User myuser = new User();
                        myuser.setUname(edtName.getText().toString());
                        myuser.setGender(edtGender.getText().toString());
                        myuser.setAge(edtAge.getText().toString());
                        myuser.setPhone(edtPhone.getText().toString());
                        myuser.setEmail(edtEmail.getText().toString());
                        myuser.setUid(mAuth.getCurrentUser().getUid());

                        if(own.isChecked()) {
                            myuser.setLocation(edtLocation.getText().toString().toUpperCase());
                            cityname =  edtLocation.getText().toString();


                            lt_lng    = getlatlongfromAddress(getApplicationContext(),cityname);

                            myuser.setLatitude(lt_lng.get(0));
                            myuser.setLongitute(lt_lng.get(1));

                            uploadPic(ivPhoto, myuser);
                        }
                        else{
                            checkForLocationPermission(new FirebaseModel.MyCallBack() {
                                @Override
                                public void onCallback(Object object) {
                                    lt_lng = (ArrayList<Double>) object;

                                    double lati = lt_lng.get(0);
                                    double longi = lt_lng.get(1);


                                    cityname = getRegionName(getApplicationContext(),lati,  longi);

                                    myuser.setLocation(cityname.toUpperCase());

                                    myuser.setLatitude(lt_lng.get(0));
                                    myuser.setLongitute(lt_lng.get(1));

                                    uploadPic(ivPhoto, myuser);
                                }
                            });
                            // Grab current location

                        }

                        //ArrayList<Double> latlong = new ArrayList<>();
                        //latlong = input_location1.getlatLong(getApplicationContext(), myuser.getLocation());


                    }
                });

//                sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                        Log.i(TAG, "Radius is set to " + progress);
//                        Toast.makeText(getBaseContext(),
//                                "Radius is set to" + progress, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//
//                    }
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//
//                    }
//                });


                btnPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChoosePicDialog();
                    }
                });
            }
        });
    }


    private void uploadPic(ImageView v, final User user) {
        // Get the data from an ImageView as bytes
//        v.setDrawingCacheEnabled(true);
//        v.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) v.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        ref = storageReference.child("/Profile Photo/1.jpg");

        ref.putBytes(data).addOnCompleteListener(SettingsActivity.this,
                new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            String myUrl = task.getResult().getMetadata().getDownloadUrl().toString();

                            User new_user = user;
                            new_user.setPhotoUrl(myUrl);

                            FirebaseModel model = new FirebaseModel();
                            model.updateUser(new_user, new FirebaseModel.MyCallBack() {
                                @Override
                                public void onCallback(Object object) {
                                    boolean success = (boolean) object;
                                    if(success == true){
                                        try {
                                            Intent myIntent = new Intent(SettingsActivity.this, Class.forName("com.example.myapplication.MainActivity"));
                                            startActivity(myIntent);
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            //FirebaseDatabase.getInstance().getReference().child("users").push().setValue(new_user);

                        } else {
                            Log.w(TAG, "Image upload task was not successful.",
                                    task.getException());
                        }
                    }
                });

    }







    private static String getRegionName(Context c,double lati, double longi){
        String cityName = "";
        Geocoder geocoder = new Geocoder(c, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);
//        String cityName = addresses.get(0).getAddressLine(0); //ADDRESS
            cityName = addresses.get(0).getLocality().toString(); //CITY NAME
            Log.e("MAINACTIVITY_LOCATION", cityName);
            String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);
        } catch (Exception e) {
            Log.e("MAINACTIVITY_LOCATION", e.toString());

        }
        return cityName;
    }

    public ArrayList getlatlongfromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address1;

        ArrayList<Double> result = new ArrayList<>();

        try {
            address1 = coder.getFromLocationName(strAddress, 1);


            if (address1 == null ) {
                return null;
            }
            Address location1 = address1.get(0);

            double lat1 = location1.getLatitude();
            double lng1 = location1.getLongitude();





            result.add(lat1);
            result.add(lng1);


            return result;
        }

        catch (Exception e) {
            return null;
        }
    }
    private void  checkForLocationPermission(final FirebaseModel.MyCallBack myCallBack) {
        //ArrayList<Double>   result = new ArrayList<>();;


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    ACCESS_COARSE_LOCATION_PERMISSION_REQUEST);

//            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            double longitude = location.getLongitude();
//            double latitude = location.getLatitude();
//            homeLat = latitude;
//            homeLong = longitude;

        } else {
            // Acquire a reference to the system Location Manager
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//            homeLat=location.getLatitude();
//            homeLong = location.getLongitude();
//            System.out.println(location.getLatitude() + location.getLongitude());
// Define a listener that responds to location updates
            locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider.
                    if(read_loc_once == false) {
                        read_loc_once = true;
                        current_location = location;
//                        updateWeather("Boston");
//                        updateEvents();
                        ArrayList<Double>   result = new ArrayList<>();;

                        homeLat=location.getLatitude();
                        homeLong = location.getLongitude();
                        result.add(homeLat);
                        result.add(homeLong);

                        myCallBack.onCallback(result);

                        // System.out.println(location.getLatitude() + location.getLongitude());
                    }
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {}

                public void onProviderEnabled(String provider) {}

                public void onProviderDisabled(String provider) {}
            };

// Register the listener with the Location Manager to receive location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
        }
//        result.add(homeLat);
//        result.add(homeLong);
//
//        return result;

    }







    //display the ChoosePicDialog
    private void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Profile Picture");
        String[] items = {"choose from local", "camera"};
        builder.setNegativeButton("cancel", null);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // choose local pic
                        //getPicFromAlbum();
                        openAlbum();
                        break;
                    case 1: // take a picture
                        //getPicFromCamera();
                        openCameraIntent();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }


    //handle case 0: choose pic from album
    private void openAlbum() {
//        File galleryFile = new File(getExternalCacheDir(),"default.jpg");
//        try {
//            if (galleryFile.exists()) {
//                galleryFile.delete();
//            }
//            galleryFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Intent intentAlbum = new Intent("android.intent.action.GET_CONTENT");
//        intentAlbum.addCategory(Intent.CATEGORY_OPENABLE);
//        intentAlbum.setType("image/*");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
//            Uri uriForFile = FileProvider.getUriForFile
//                    (this, getPackageName() +".provider", galleryFile);
//            intentAlbum.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
//            intentAlbum.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            intentAlbum.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        } else {
//            intentAlbum.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(galleryFile));
//        }
//        startActivityForResult(intentAlbum, ALBUM_REQUEST_CODE);
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }


    //handle case 1: take pic by camera
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, CAMERA_REQUEST_CODE);
        }
    }


    //store the image taken by camera
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();

        return image;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ivPhoto.setImageURI(Uri.parse(imageFilePath));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == ALBUM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                imageFilePath = c.getString(columnIndex);
                ivPhoto.setImageURI(Uri.parse(imageFilePath));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //old version --- pic from camera
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

    //old version --- pic from album
    private void getPicFromAlbum() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }


    //old version --- crop picture
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


    //old version --- onActivityResult
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if(resultCode != RESULT_CANCELED) {
//            switch (requestCode) {
//                case CAMERA_REQUEST_CODE:
//                    if (resultCode == RESULT_OK) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            Uri contentUri = FileProvider.getUriForFile(SettingsActivity.this, "com.example.firebaselogindemo", tempFile);
//                            //cropPhoto(contentUri);
//                        } else {
//                            //cropPhoto(Uri.fromFile(tempFil));
////                        }
////                    }
////                    break;
////                case ALBUM_REQUEST_CODE:
////                    if (resultCode == RESULT_OK) {
////                        Uri uri = intent.getData();
////                        //cropPhoto(uri);
////                    }
////                    break;
////                case CROP_REQUEST_CODE:
////                    Bundle bundle = intent.getExtras();
////                    if (bundle != null) {
////                        Bitmap image = bundle.getParcelable("data");
////                        ivPhoto.setImageBitmap(image);
////                    }
////                    break;
////            }
////        }
////    }
//
//
//}
//
//
//
////        Query fbSearchQuery = FirebaseDatabase.getInstance().getReference().orderByChild("picUrl");
////        fbSearchQuery.addValueEventListener(new ValueEventListener() {
////
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                if(dataSnapshot.exists()){
////                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
////                        if(dataSnapshot.child("userName").getValue().equals("BowenJia")){
////                            url = snapshot.child("picUrl").getValue().toString();
////                        }
////                    }
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
//
//// old version --- pic load ( Picasso API)
////        String pic_jeff = "https://platform-lookaside.fbsbx.com/platform/profilepic" +
////                "/?asid=668089376952860&height=50&width=50&ext=1557976673&hash=AeS_PhCjyJq0Oc0g";
////                Picasso.get().load(pic_jeff)
////                .into(imgPhoto);e
}