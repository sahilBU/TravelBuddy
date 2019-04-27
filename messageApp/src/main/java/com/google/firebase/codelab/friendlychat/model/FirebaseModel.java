package com.google.firebase.codelab.friendlychat.model;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/*
store in hash map array
    - user info
    - comment
    - addfriend
    - pwd check (see demo)
 */
public class FirebaseModel {
    public FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;

    public DatabaseReference mUserDatabase;
    public DatabaseReference mFriendDatabase;
    public DatabaseReference mChatDatabase;
    public DatabaseReference mCommentDatabase;

    private ArrayList<User> users;
    private ArrayList<String> friend_ids;
    private ArrayList<Comment> comments;
    private User user;

    private boolean updateFlag = false;

    public FirebaseModel(){
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference("friend");
        mChatDatabase = FirebaseDatabase.getInstance().getReference("p2pChat");
        mCommentDatabase = FirebaseDatabase.getInstance().getReference("comment");

        users = new ArrayList<User>();
        friend_ids = new ArrayList<String>();
        comments = new ArrayList<Comment>();
    }

    public void userSignOut(){
        mFirebaseAuth.signOut();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    public boolean isLogIn(){
        return mFirebaseUser != null;
    }

    public void getUserPhoto(final MyCallBack myCallback){
        Query query = mUserDatabase.orderByChild("uid").equalTo(mFirebaseUser.getUid());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        user = snapshot.getValue(User.class);
                    }

                    myCallback.onCallback(user.getPhotoUrl());
                }
                else
                    myCallback.onCallback(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fetchFriends(String uid, final MyCallBack myCallback){
        friend_ids.clear();
        Query query = mFriendDatabase.orderByChild("uid1").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        friend_ids.add(snapshot.child("uid2").getValue().toString());
                    }

                    myCallback.onCallback(friend_ids);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return;
    }

    public interface MyCallBack{
        void onCallback(Object object);
    }

    // update user table, retuchatroomrn  id
    public String addFriend(String uid1, String uid2){
        // create chatroom
        mChatDatabase = FirebaseDatabase.getInstance().getReference("p2pChat");
        String chatroom_id = mChatDatabase.push().getKey();

        mChatDatabase.child(chatroom_id).child("uid1").setValue(uid1);
        mChatDatabase.child(chatroom_id).child("uid2").setValue(uid2);

        String inital_chat_key = mFriendDatabase.child(chatroom_id).child("messages").push().getKey();
        mChatDatabase.child(chatroom_id).child("messages").child(inital_chat_key).child("name").setValue("");
        mChatDatabase.child(chatroom_id).child("messages").child(inital_chat_key).child("photoUrl").setValue("");
        mChatDatabase.child(chatroom_id).child("messages").child(inital_chat_key).child("text").setValue("say hey to each other!");

        // update friends
        mFriendDatabase = FirebaseDatabase.getInstance().getReference("friend");
        String new_id1 = mFriendDatabase.push().getKey();

        mFriendDatabase.child(new_id1).child("uid1").setValue(uid1);
        mFriendDatabase.child(new_id1).child("uid2").setValue(uid2);
        mFriendDatabase.child(new_id1).child("chatroom_id").setValue(chatroom_id);

        String new_id2 = mFriendDatabase.push().getKey();
        mFriendDatabase.child(new_id2).child("uid1").setValue(uid2);
        mFriendDatabase.child(new_id2).child("uid2").setValue(uid1);
        mFriendDatabase.child(new_id2).child("chatroom_id").setValue(chatroom_id);

        return chatroom_id;
    }

    public void getComments(String uid, final MyCallBack myCallback){
        comments.clear();
        Query firebaseSearchQuery = mCommentDatabase.child(uid);

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Comment comment = snapshot.getValue(Comment.class);
                        comments.add(comment);
                    }

                    myCallback.onCallback(comments);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return;
    }


    public void getUsers(String search_word, final MyCallBack myCallback){
        users.clear();
        Query firebaseSearchQuery = mUserDatabase.orderByChild("uname").startAt(search_word).endAt(search_word+"\uf8ff");

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        users.add(user);
                    }

                    myCallback.onCallback(users);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return;
    }

    public void afterLogin(final MyCallBack myCallback){
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // new user -> upload structured data
        Query query = mUserDatabase.orderByChild("uid").startAt(mFirebaseUser.getUid()).endAt(mFirebaseUser.getUid()+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    myCallback.onCallback(true);
                }
                else
                    myCallback.onCallback(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
