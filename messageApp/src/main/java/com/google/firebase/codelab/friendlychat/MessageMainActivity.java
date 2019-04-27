/**
 * Copyright Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.firebase.codelab.friendlychat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.codelab.friendlychat.model.Comment;
import com.google.firebase.codelab.friendlychat.model.FirebaseModel;
import com.google.firebase.codelab.friendlychat.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageMainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private String mPhotoUrl;

    private RecyclerView mUserList;
    FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter;

    private EditText searchText;

    private SharedPreferences mSharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private DatabaseReference mUserDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mChatDatabase;

    FirebaseModel model;
    ArrayList<String> friend_list;
    boolean is_remove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mUsername = mFirebaseUser.getDisplayName();
        if (mFirebaseUser.getPhotoUrl() != null) {
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        mUserDatabase = FirebaseDatabase.getInstance().getReference("users");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference("friend");
        mChatDatabase = FirebaseDatabase.getInstance().getReference("p2pChat");

        mUserList = findViewById(R.id.user_list);
        mUserList.setHasFixedSize(true);
        mUserList.setLayoutManager(new LinearLayoutManager(this));

        model = new FirebaseModel();

        FirebaseModel model = new FirebaseModel();
        model.getUsers("", new FirebaseModel.MyCallBack(){
            @Override
            public void onCallback(Object object) {
                ArrayList<User> users = (ArrayList<User>) object;
                System.out.println(users.toString());
            }
        });

        model.getComments(mFirebaseUser.getUid(), new FirebaseModel.MyCallBack(){
            @Override
            public void onCallback(Object object) {
                ArrayList<Comment> comments = (ArrayList<Comment>) object;
                System.out.println(comments.toString());
            }
        });

        firebaseUserLoad(mFirebaseUser.getUid());
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        View mView;
        String user_name;
        String user_id;
        String user_profile;
        String chatroom_id;
        Boolean search1, search2;

        public UserViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            search1 = false;
            search2 = true;

            //listener set on ENTIRE ROW, you may set on individual components within a row.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String uid1 = mFirebaseUser.getUid();
                    final String uid2 = user_id;
                    chatroom_id = "None";
                    Log.w("debugging", uid1);
                    Log.w("debugging", uid2);
                    mFriendDatabase = FirebaseDatabase.getInstance().getReference("friend");

                    Query query_1 = mFriendDatabase.orderByChild("uid1").startAt(uid1).endAt(uid1+"\uf8ff");
                    query_1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            search1 = true;
                            if(dataSnapshot.exists()){
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    //Toast.makeText(getApplicationContext(), "id = " + snapshot.getKey(), Toast.LENGTH_LONG).show();
                                    if(snapshot.child("uid2").getValue().equals(uid2))
                                        chatroom_id = snapshot.child("chatroom_id").getValue().toString();
                                }

//                                if(search1 && search2 && chatroom_id.equals("None")){
//                                    chatroom_id = model.addFriend(uid1, uid2);
//                                }

                                if(!chatroom_id.equals("None")) {
                                    Log.w("debugging", chatroom_id + "---");
                                    Intent chatroom = new Intent(getBaseContext(), ChatRoom.class);
                                    chatroom.putExtra("mUsername", mUsername);
                                    chatroom.putExtra("mPhotoUrl", mPhotoUrl);
                                    chatroom.putExtra("chatroom_id", chatroom_id);
                                    chatroom.putExtra("friend_id", user_id);
                                    chatroom.putExtra("friend_name", user_name);
                                    startActivity(chatroom);
                                    chatroom_id = "None";
                                    search1 = false;
                                    //search2 = false;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

//                    Query query_2 = mFriendDatabase.orderByChild("uid2").startAt(uid1).endAt(uid1+"\uf8ff");
//                    query_2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            search2 = true;
//                            if(dataSnapshot.exists()){
//                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    //Toast.makeText(getApplicationContext(), "id = " + snapshot.getKey(), Toast.LENGTH_LONG).show();
//                                    if(snapshot.child("uid1").getValue().equals(uid2))
//                                        chatroom_id = snapshot.child("chatroom_id").getValue().toString();
//                                }
//
//                                if(search1 && search2 && chatroom_id.equals("None")){
//                                    chatroom_id = model.createChatroom(uid1, uid2);
//                                    model.addFriend(uid1, uid2, chatroom_id);
//                                }
//
//                                if(!chatroom_id.equals("None")) {
//                                    Log.w("debugging", chatroom_id);
//                                    Intent chatroom = new Intent(getBaseContext(), ChatRoom.class);
//                                    chatroom.putExtra("mUsername", mUsername);
//                                    chatroom.putExtra("mPhotoUrl", mPhotoUrl);
//                                    chatroom.putExtra("chatroom_id", chatroom_id);
//                                    chatroom.putExtra("friend_id", user_id);
//                                    chatroom.putExtra("friend_name", user_name);
//                                    startActivity(chatroom);
//                                    chatroom_id = "None";
//                                    search1 = false;
//                                    search2 = false;
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
                }
            });
        }

        public void setDetails(String uname, String uprofile, String uid){
            TextView userName = mView.findViewById(R.id.user_name);
            TextView userId = mView.findViewById(R.id.user_id);
            ImageView userProfile = mView.findViewById(R.id.user_profile);

            user_name = uname;
            user_profile = uprofile;
            user_id = uid;
            userName.setText(uname);
            userId.setText(user_id);

            Glide.with(getApplicationContext()).load(uprofile).into(userProfile);

            if(is_remove == true){
                mView.setVisibility(View.GONE);
                mView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                is_remove = false;
            }
        }
    }

    private void firebaseUserLoad(final String user_id){
        model.fetchFriends(user_id, new FirebaseModel.MyCallBack() {
            @Override
            public void onCallback(Object object) {
                final ArrayList<String> friend_list = (ArrayList<String>)  object;

                Query firebaseSearchQuery = mUserDatabase.orderByChild("uname");

                FirebaseRecyclerOptions<User> options =
                        new FirebaseRecyclerOptions.Builder<User>()
                                .setQuery(firebaseSearchQuery, new SnapshotParser<User>() {
                                    @NonNull @Override
                                    public User parseSnapshot(@NonNull DataSnapshot snapshot) {
                                        User user = snapshot.getValue(User.class);

                                        is_remove = false;
                                        if(!friend_list.contains(user.getUid()))
                                            is_remove = true;
                                        return user;
                                    }
                                }).build();

                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
                        holder.setDetails(model.getUname(), model.getPhotoUrl(), model.getUid());
                    }

                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                        return new UserViewHolder(inflater.inflate(R.layout.list_layout, parent, false));
                    }
                };

                mUserList.setAdapter(firebaseRecyclerAdapter);
                firebaseRecyclerAdapter.startListening();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}
