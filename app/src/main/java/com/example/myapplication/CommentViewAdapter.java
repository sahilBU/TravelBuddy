package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class CommentViewAdapter extends ArrayAdapter<CommentsAndRatings> {

    private AppCompatActivity activity;
    private ArrayList<CommentsAndRatings> CommentsAndRatingsList;

    public CommentViewAdapter(AppCompatActivity context, int resource, ArrayList<CommentsAndRatings> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.CommentsAndRatingsList = objects;
    }

    @Override
    public CommentsAndRatings getItem(int position) {
        return CommentsAndRatingsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.comment_listview, parent, false);
            convertView = inflater.inflate(R.layout.comment_listview, parent, false);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            //holder.ratingBar.getTag(position);
        }

        holder.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(holder, position));

        holder.ratingBar.setTag(position);
        holder.ratingBar.setRating(getItem(position).getRatingStar());
        holder.commentsAndRatingsName.setText(getItem(position).getName());
//        holder.commentsAndRatingsPhotoUrl.setImage(getItem(position).getPhotoUrl());
        Picasso.get().load(getItem(position).getPhotoUrl()).into(holder.commentsAndRatingsPhotoUrl);
//        holder.commentsAndRatingsPhotoUrl.setImageDrawable(Picasso.get().load(getItem(position).getPhotoUrl()).into(commentsAndRatingsPhotoUrl));
        return convertView;
    }

    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                CommentsAndRatings item = getItem(position);
                item.setRatingStar(v);
                Log.i("Adapter", "star: " + v);
            }
        };
    }

    private static class ViewHolder {
        private RatingBar ratingBar;
        private TextView commentsAndRatingsName;
        private ImageView commentsAndRatingsPhotoUrl;

        public ViewHolder(View view) {
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            commentsAndRatingsName = (TextView) view.findViewById(R.id.commentsAndRatingsName);
            commentsAndRatingsPhotoUrl = (ImageView) view.findViewById(R.id.commentsAndRatingsPhotoUrl);
        }
    }
}

