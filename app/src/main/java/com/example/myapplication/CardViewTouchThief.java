package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class CardViewTouchThief extends CardView {

    public CardViewTouchThief(Context context) {
        super(context);
    }
    public CardViewTouchThief(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CardViewTouchThief(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * This method determines whether we want to intercept the motion.
         * If we return true, onTouchEvent will be called.
         */
        return true;
    }

}