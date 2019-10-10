package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.codelab.friendlychat.model.Comment;
import com.google.firebase.codelab.friendlychat.model.FirebaseModel;
import com.google.firebase.codelab.friendlychat.model.User;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class SwipeMainActivity extends AppCompatActivity {
    int windowwidth;
    int screenCenter;
    int x_cord, y_cord, x, y;
    int Likes = 0;
    public RelativeLayout parentView;
    float alphaValue = 0;
    private Context context;
    private FirebaseModel model = new FirebaseModel();
    String city;

//    ArrayList<UserDataModel> userDataModelArrayList;

    private ArrayList<User> userDataModelArrayList;
    private ArrayList<String> friendsDataModelArrayList = new ArrayList<String>();
    private ArrayList<Comment> userCommentList = new ArrayList<Comment>();
    private ArrayList<CommentsAndRatings> comarrayList = new ArrayList<CommentsAndRatings>();
    //    private ArrayList<CommentsAndRatings> comarrayList = new ArrayList<CommentsAndRatings>();
    private HashSet<String> friendsDataModelHashSet = new HashSet<String>();
    private HashMap<String, ArrayList<CommentsAndRatings>>  userCommentHashMap = new HashMap<>();

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.card_container);

        context = SwipeMainActivity.this;

        parentView = (RelativeLayout) findViewById(R.id.main_layoutview);

        windowwidth = getWindowManager().getDefaultDisplay().getWidth();

        screenCenter = windowwidth / 2;

        userDataModelArrayList = new ArrayList<>();

        getUsersList();

    }

    int userDataModelArrayListSize = 0;
    int friendsDataModelArrayListSize = 0;

    private void getUsersList(){
        model.getUsers("", new FirebaseModel.MyCallBack() {
            @Override
            public void onCallback(Object object) {
                userDataModelArrayList.clear();
                userDataModelArrayList = (ArrayList<User>) object;
                userDataModelArrayListSize=userDataModelArrayList.size();
                model.fetchFriends(model.getUid(), new FirebaseModel.MyCallBack() {
                    @Override
                    public void onCallback(Object object) {
                        friendsDataModelArrayList.clear();
                        friendsDataModelArrayList = (ArrayList<String>) object;
//                        Log.e("FRIENDSMODELARRAYLISTR",friendsDataModelArrayList.get(0).toString());
                        if (friendsDataModelArrayList== null){
                            friendsDataModelHashSet.clear();
                            friendsDataModelHashSet.add(model.getUid());
                            friendsDataModelArrayListSize=friendsDataModelHashSet.size();
                        }
                        else{
                            friendsDataModelHashSet.clear();
                            for (int i=0; i < friendsDataModelArrayList.size(); i++) {
                                friendsDataModelHashSet.add(friendsDataModelArrayList.get(i));
                            }
                            friendsDataModelHashSet.add(model.getUid());
                            friendsDataModelArrayListSize=friendsDataModelHashSet.size();
                        }
                        final ArrayList<User> remUsers = new ArrayList<>();
                        for (int i = 0; i < userDataModelArrayList.size(); i++) {
                            final String curUid = userDataModelArrayList.get(i).getUid();
                            if(friendsDataModelHashSet.contains(curUid) || curUid.equals(model.getUid())){
//                                userDataModelArrayList.remove(i);
                                if(curUid.equals(model.getUid())){
                                    city=userDataModelArrayList.get(i).getLocation();
                                }
                                remUsers.add(userDataModelArrayList.get(i));
                                continue;
                            }
                            else{
                                model.getComments(curUid, new FirebaseModel.MyCallBack() {
                                    @Override
                                    public void onCallback(Object object) {
                                        if (object==null){
                                            userCommentList.clear();
                                        }
                                        else {
//                                            userCommentList.clear();
                                            userCommentList = (ArrayList<Comment>) object;
                                        }
                                        comarrayList = setCommentsAndRatings(userCommentList);
                                        userCommentHashMap.put(curUid,comarrayList);
                                        if(userCommentHashMap.size()>=userDataModelArrayListSize-friendsDataModelArrayListSize){
                                            for(int i=0; i < remUsers.size(); i++){
                                                int remUserIndex = userDataModelArrayList.indexOf(remUsers.get(i));
                                                if (remUserIndex!=-1){
                                                    userDataModelArrayList.remove(remUserIndex);}
                                                else{
//                                                    continue;
                                                    userDataModelArrayList.remove(userDataModelArrayList.size()-1);
                                                }
                                            }
                                            populateCardView(userDataModelArrayList,userCommentHashMap);
                                        }
                                    }
                                });
                            }

                        }
                    }
                });
            }
        });

    }


    private void populateCardView(ArrayList<User> userDataModelArrayList, HashMap<String,ArrayList<CommentsAndRatings>> userCommentHashMap){

        for (int i = 0; i < userDataModelArrayList.size(); i++) {
            if (!city.equals(userDataModelArrayList.get(i).getLocation())) {
                continue;
            } else {
                final String curUid = userDataModelArrayList.get(i).getUid();

                LayoutInflater inflate =
                        (LayoutInflater) SwipeMainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View containerView = inflate.inflate(R.layout.custom_tinder_layout, null);

                ImageView userIMG = (ImageView) containerView.findViewById(R.id.userIMG);
                RelativeLayout relativeLayoutContainer = (RelativeLayout) containerView.findViewById(R.id.relative_container);


                LayoutParams layoutParams = new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                containerView.setLayoutParams(layoutParams);

                containerView.setTag(i);

                try {
                    Picasso.get().load(userDataModelArrayList.get(i).getPhotoUrl()).into(userIMG);
                } catch (Exception e) {
                    Log.e("POPULATECARDVIEW", "NO PHOTOURL");
                    Picasso.get().load("https://forwardsummit.ca/wp-content/uploads/2019/01/avatar-default.png").into(userIMG);
                }

                ListView crlistView = containerView.findViewById(R.id.comListView);

                CommentViewAdapter comAdapter =
                        new CommentViewAdapter(this, R.layout.comment_listview, userCommentHashMap.get(curUid));

                crlistView.setAdapter(comAdapter);

                LayoutParams layoutTvParams = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


                // ADD dynamically like TextView on image.
                final TextView tvLike = new TextView(context);
                tvLike.setLayoutParams(layoutTvParams);
                tvLike.setPadding(10, 10, 10, 10);
                tvLike.setBackgroundDrawable(getResources().getDrawable(R.drawable.btnlikeback));
                tvLike.setText("LIKE");
                tvLike.setGravity(Gravity.CENTER);
                tvLike.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvLike.setTextSize(25);
                tvLike.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                tvLike.setX(20);
                tvLike.setY(100);
                tvLike.setRotation(-50);
                tvLike.setAlpha(alphaValue);
                relativeLayoutContainer.addView(tvLike);


//            ADD dynamically dislike TextView on image.
                final TextView tvUnLike = new TextView(context);
                tvUnLike.setLayoutParams(layoutTvParams);
                tvUnLike.setPadding(10, 10, 10, 10);
                tvUnLike.setBackgroundDrawable(getResources().getDrawable(R.drawable.btnlikeback));
                tvUnLike.setText("UNLIKE");
                tvUnLike.setGravity(Gravity.CENTER);
                tvUnLike.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvUnLike.setTextSize(25);
                tvUnLike.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                tvUnLike.setX(500);
                tvUnLike.setY(150);
                tvUnLike.setRotation(50);
                tvUnLike.setAlpha(alphaValue);
                relativeLayoutContainer.addView(tvUnLike);


                TextView tvName = (TextView) containerView.findViewById(R.id.tvName);
                TextView tvTotLikes = (TextView) containerView.findViewById(R.id.tvTotalLikes);


                tvName.setText(userDataModelArrayList.get(i).getUname());

                try {
                    tvTotLikes.setText(userDataModelArrayList.get(i).getBio());
                } catch (Exception e) {
                    Log.e("POPULATE", "NOBIO");
                    tvTotLikes.setText("No Bio");
                }

                // Touch listener on the image layout to swipe image right or left.
                relativeLayoutContainer.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        x_cord = (int) event.getRawX();
                        y_cord = (int) event.getRawY();

                        containerView.setX(0);
                        containerView.setY(0);

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                x = (int) event.getX();
                                y = (int) event.getY();


                                Log.v("On touch", x + " " + y);
                                break;
                            case MotionEvent.ACTION_MOVE:

                                x_cord = (int) event.getRawX();
                                // smoother animation.
                                y_cord = (int) event.getRawY();

                                containerView.setX(x_cord - x);
                                containerView.setY(y_cord - y);


                                if (x_cord >= screenCenter) {
                                    containerView.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 32)));
                                    if (x_cord > (screenCenter + (screenCenter / 2))) {
                                        tvLike.setAlpha(1);
//                                    model.sendRequest(model.getUid(), curUid);
                                        if (x_cord > (windowwidth - (screenCenter / 4))) {
                                            Likes = 2;
                                        } else {
                                            Likes = 0;
                                        }
                                    } else {
                                        Likes = 0;
                                        tvLike.setAlpha(0);
//                                    model.sendRequest(model.getUid(), curUid);
                                    }
                                    tvUnLike.setAlpha(0);
                                } else {
                                    // rotate image while moving
                                    containerView.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 32)));
                                    if (x_cord < (screenCenter / 2)) {
                                        tvUnLike.setAlpha(1);
                                        if (x_cord < screenCenter / 4) {
                                            Likes = 1;
                                        } else {
                                            Likes = 0;
                                        }
                                    } else {
                                        Likes = 0;
                                        tvUnLike.setAlpha(0);
                                    }
                                    tvLike.setAlpha(0);
//                                model.sendRequest(model.getUid(), curUid);
                                }

                                break;
                            case MotionEvent.ACTION_UP:

                                x_cord = (int) event.getRawX();
                                y_cord = (int) event.getRawY();

                                Log.e("X Point", "" + x_cord + " , Y " + y_cord);
                                tvUnLike.setAlpha(0);
                                tvLike.setAlpha(0);
//                            model.sendRequest(model.getUid(), curUid);

                                if (Likes == 0) {
//                                Toast.makeText(context, "NOTHING", Toast.LENGTH_SHORT).show();
                                    Log.e("Event_Status :-> ", "Nothing");
                                    containerView.setX(0);
                                    containerView.setY(0);
                                    containerView.setRotation(0);
                                } else if (Likes == 1) {
//                                Toast.makeText(context, "UNLIKE", Toast.LENGTH_SHORT).show();
                                    Log.e("Event_Status :-> ", "UNLIKE");
                                    parentView.removeView(containerView);
                                } else if (Likes == 2) {
//                                Toast.makeText(context, "LIKED", Toast.LENGTH_SHORT).show();
                                    Log.e("Event_Status :-> ", "Liked");
                                    model.sendRequest(model.getUid(), curUid);
                                    parentView.removeView(containerView);
                                }
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                parentView.addView(containerView);
            }
        }
    }

//    private void getCommentsList(String uid){
//
//        model.getComments(uid, new FirebaseModel.MyCallBack() {
//            @Override
//            public void onCallback(Object object) {
//                if (userCommentList.size()!=0){
//                    userCommentList.clear();
//                }
//                userCommentList = (ArrayList<Comment>) object;
//                comarrayList = setCommentsAndRatings(userCommentList);
//            }
//        });
//    }

    private ArrayList<CommentsAndRatings> setCommentsAndRatings(ArrayList<Comment> userCommentList) {
        ArrayList<CommentsAndRatings> userComList = new ArrayList<CommentsAndRatings>();
        if (userCommentList.size()==0){
//            comarrayList.clear();
            userComList.add(new CommentsAndRatings(0,"User has no ratings","https://forwardsummit.ca/wp-content/uploads/2019/01/avatar-default.png" ));
        }
        else {
//            comarrayList.clear();
            for (int i = 0; i < userCommentList.size(); i++) {
                userComList.add(new CommentsAndRatings(Math.round(userCommentList.get(i).getComment_rate()), userCommentList.get(i).getComment_text(), userCommentList.get(i).getCommenter_photoUrl()));
            }
        }
        return userComList;

    }
}