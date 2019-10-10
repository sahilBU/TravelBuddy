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
    private ArrayList<String> request_ids;
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
        request_ids = new ArrayList<String>();
        comments = new ArrayList<Comment>();
    }

    public String getUid(){
        return mFirebaseUser.getUid();
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

    public void cleanupChat(final String uid){
        Query query_friend = mFriendDatabase;

        query_friend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.child("uid1").getValue().toString().equals(uid) || snapshot.child("uid2").getValue().toString().equals(uid))
                            snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query query_chat = mChatDatabase;

        query_chat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.child("uid1").getValue().toString().equals(uid) || snapshot.child("uid2").getValue().toString().equals(uid))
                            snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return;
    }

    public void cleanupRequest(){
        Query query = mFriendDatabase;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        if(snapshot.child("chatroom_id").getValue().toString().equals("None"))
                            snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return;
    }

    public void fetchFriends(String uid, final MyCallBack myCallback){
        friend_ids.clear();
        Query query = mFriendDatabase.orderByChild("uid1").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        try {
                            if(!snapshot.child("chatroom_id").getValue().toString().equals("None"))
                                friend_ids.add(snapshot.child("uid2").getValue().toString());
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    myCallback.onCallback(friend_ids);
                }
                else
                    myCallback.onCallback(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return;
    }

    public void fetchRequests(String uid, final MyCallBack myCallback){
        request_ids.clear();
        Query query = mFriendDatabase.orderByChild("uid2").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String request_id = snapshot.child("uid1").getValue().toString();
                        if(!friend_ids.contains(request_id))
                            request_ids.add(request_id);
                    }

                    myCallback.onCallback(request_ids);
                }
                else
                    myCallback.onCallback(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return;
    }

    public void deleteRequest(final String uid1, final String uid2, final MyCallBack myCallback){
        Query query = mFriendDatabase.orderByChild("uid1").equalTo(uid1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        try {
                            String id = snapshot.child("uid2").getValue().toString();
                            String chatroom_id = snapshot.child("chatroom_id").getValue().toString();
                            if (id.equals(uid2) && chatroom_id.equals("None"))
                                snapshot.getRef().removeValue();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                    myCallback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface MyCallBack{
        void onCallback(Object object);
    }


    public void sendRequest(String uid1, String uid2){
        String new_id = mFriendDatabase.push().getKey();
        mFriendDatabase.child(new_id).child("uid1").setValue(uid1);
        mFriendDatabase.child(new_id).child("uid2").setValue(uid2);
        mFriendDatabase.child(new_id).child("chatroom_id").setValue("None");
    }

    // update user table, retuchatroomrn  id
    public String addFriend(final String uid1, final String uid2, final MyCallBack myCallback){
        // create chatroom
        mChatDatabase = FirebaseDatabase.getInstance().getReference("p2pChat");
        final String chatroom_id = mChatDatabase.push().getKey();

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
        Query firebaseSearchQuery = mCommentDatabase.child(uid);

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                comments.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Comment comment = snapshot.getValue(Comment.class);
                        comments.add(comment);
                    }

                    myCallback.onCallback(comments);
                }
                else
                    myCallback.onCallback(null);
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

    public void getSingleUser(String uid, final MyCallBack myCallback){
        users.clear();
        Query firebaseSearchQuery = mUserDatabase.orderByChild("uid").equalTo(uid);

        firebaseSearchQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = new User();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        user = snapshot.getValue(User.class);
                    }

                    myCallback.onCallback(user);
                }
                else
                    myCallback.onCallback(null);
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

    public void updateUser(final User user, final MyCallBack myCallback){
        updateFlag = false;
        Query query = mUserDatabase.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(updateFlag == false){
                    updateFlag = true;

                    if(dataSnapshot.exists()){
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            snapshot.getRef().setValue(user);
                        }

                        myCallback.onCallback(true);
                    }
                    else {
                        mUserDatabase.push().setValue(user);
                        myCallback.onCallback(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
