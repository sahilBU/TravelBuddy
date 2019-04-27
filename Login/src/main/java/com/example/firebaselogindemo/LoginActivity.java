package com.example.firebaselogindemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebaselogindemo.com.example.firebaselogindemo.model.userInfo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.codelab.friendlychat.model.FirebaseModel;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.auth.api.Auth;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private CallbackManager mCallbackManager;
    private static final String TAG = "FACELOG" ;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private String uname;
    private String ugender;
    private String uemail;
    private String upicUrl;

    private Button btnSignup;
    private Button btnlogin;
    private EditText edtUsername;
    private EditText edtPassword;

    private FirebaseModel model = new FirebaseModel();

    private static final int RC_SIGN_IN = 9001;

    private SignInButton mSignInButton;

    private GoogleApiClient mGoogleApiClient;

    private boolean emailSignInFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_login);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser != null){
            mainpageUI();
        }

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
//        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                getLoginInfo(loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });


        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnlogin = findViewById(R.id.btnlogin);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUserName = edtUsername.getText().toString();
                String inputPassWord = edtPassword.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(inputUserName, inputPassWord)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Authentication Failed",
                                            Toast.LENGTH_LONG).show();
                                } else{
                                    model.afterLogin(new FirebaseModel.MyCallBack() {
                                        @Override
                                        public void onCallback(Object object) {
                                            boolean new_user = (boolean) object;

                                            if(emailSignInFlag == false){
                                                emailSignInFlag = true;
                                                // navigate to profile change
                                                if(new_user == true){
                                                    startActivity(new Intent(LoginActivity.this, CreateProfileActivity.class));
                                                }
                                                else{   // navigate to main page
                                                    try {
                                                        Intent myIntent = new Intent(LoginActivity.this, Class.forName("com.example.myapplication.MainActivity"));
                                                        startActivity(myIntent);
                                                    } catch (ClassNotFoundException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });

        // Assign fields
        mSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);

        // Set click listeners
        mSignInButton.setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    //get the user info when login
    public void getLoginInfo(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if(object!= null){
                    //get user info
                    uname = object.optString("name");
                    ugender = object.optString("gender");
                    uemail = object.optString("email");

                    //get user profile picture
                    JSONObject object_pic = object.optJSONObject("picture");
                    JSONObject object_data = object_pic.optJSONObject("data");
                    upicUrl = object_data.optString("url");

                    Toast toast = Toast.makeText(getApplicationContext(),""+object.toString(),Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        });
        //make a bundle of infos you want to obtain from the request
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,gender,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    //after loggin --> go to "Create Profile" page
    private void mainpageUI(){
        Toast.makeText(LoginActivity.this,"you are logged in",Toast.LENGTH_LONG).show();


        model.afterLogin(new FirebaseModel.MyCallBack() {
            @Override
            public void onCallback(Object object) {
                boolean new_user = (boolean) object;

                // navigate to profile change
                if(new_user == true){
                    startActivity(new Intent(LoginActivity.this, CreateProfileActivity.class));
                }
                // navigate to main page
                else{
                    try {
                        Intent myIntent = new Intent(LoginActivity.this, Class.forName("com.example.myapplication.MainActivity"));
                        startActivity(myIntent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

                // store user info into database
                mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }





                model.afterLogin(new FirebaseModel.MyCallBack() {
                    @Override
                    public void onCallback(Object object) {
                        boolean new_user = (boolean) object;

                        // navigate to profile change
                        if(new_user == true){
                            startActivity(new Intent(LoginActivity.this, CreateProfileActivity.class));
                        }
                        // navigate to main page
                        else{
                            try {
                                Intent myIntent = new Intent(LoginActivity.this, Class.forName("com.example.myapplication.MainActivity"));
                                startActivity(myIntent);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                finish();
            } else {
                // Google Sign-In failed
                Log.e(TAG, "Google Sign-In failed.");
            }
        }
        else
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        //sign into firebase with facebook account
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();

                            //store user info based on facebook account profile
                            mFirebaseUser = mFirebaseAuth.getCurrentUser();
                            userInfo userInfo = new userInfo();
                            userInfo.setUid(mFirebaseUser.getUid());
                            userInfo.setUserName(uname);
                            userInfo.setGender(ugender);
                            userInfo.setEmail(uemail);
                            userInfo.setPicUrl(upicUrl);

                            //upload structured user info
                            FirebaseDatabase.getInstance().getReference().child("Users").push().setValue(userInfo);

                            mainpageUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.google_sign_in_button) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
