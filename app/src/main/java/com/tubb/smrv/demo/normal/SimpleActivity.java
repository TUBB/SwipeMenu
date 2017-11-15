package com.tubb.smrv.demo.normal;

import android.app.Activity;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.OverScroller;
import android.widget.Toast;

import com.tubb.smrv.SwipeMenuLayout;
import com.tubb.smrv.listener.SwipeFractionListener;
import com.tubb.smrv.listener.SwipeSwitchListener;
import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.tubb.smrv.demo.R;

public class SimpleActivity extends Activity {

    private static final String TAG = "sml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
//        RippleDrawable
        final SwipeHorizontalMenuLayout sml = (SwipeHorizontalMenuLayout) findViewById(R.id.sml);
        findViewById(R.id.btLeftOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sml.smoothOpenBeginMenu();
            }
        });

        findViewById(R.id.btLeftClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sml.smoothCloseBeginMenu();
            }
        });

        findViewById(R.id.btRightOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sml.smoothOpenEndMenu();
            }
        });

        findViewById(R.id.btRightClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sml.smoothCloseEndMenu();
            }
        });

        findViewById(R.id.smContentView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleActivity.this, "content view onclick", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.smContentView).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SimpleActivity.this, "content view long click", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        findViewById(R.id.smMenuViewLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleActivity.this, "left menu view onclick", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.smMenuViewRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleActivity.this, "right menu view onclick", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleActivity.this, "left button onclick", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleActivity.this, "right button onclick", Toast.LENGTH_SHORT).show();
            }
        });
        sml.setSwipeListener(new SwipeSwitchListener() {
            @Override
            public void beginMenuClosed(SwipeMenuLayout swipeMenuLayout) {
                Log.e(TAG, "left menu closed");
            }

            @Override
            public void beginMenuOpened(SwipeMenuLayout swipeMenuLayout) {
                Log.e(TAG, "left menu opened");
            }

            @Override
            public void endMenuClosed(SwipeMenuLayout swipeMenuLayout) {
                Log.e(TAG, "right menu closed");
            }

            @Override
            public void endMenuOpened(SwipeMenuLayout swipeMenuLayout) {
                Log.e(TAG, "right menu opened");
            }


        });

//        sml.setSwipeListener(new SimpleSwipeSwitchListener(){
//            @Override
//            public void beginMenuClosed(SwipeMenuLayout swipeMenuLayout) {
//                Log.e(TAG, "left menu closed");
//            }
//        });

        sml.setSwipeFractionListener(new SwipeFractionListener() {
            @Override
            public void beginMenuSwipeFraction(SwipeMenuLayout swipeMenuLayout, float fraction) {
                Log.e(TAG, "left menu swipe fraction:"+fraction);
            }

            @Override
            public void endMenuSwipeFraction(SwipeMenuLayout swipeMenuLayout, float fraction) {
                Log.e(TAG, "right menu swipe fraction:"+fraction);
            }
        });
    }

}
