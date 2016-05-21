package com.tubb.smrv.demo.normal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tubb.smrv.SwipeMenuLayout;
import com.tubb.smrv.SwipeVerticalMenuLayout;
import com.tubb.smrv.demo.R;
import com.tubb.smrv.listener.SwipeSwitchListener;

public class SimpleVerticalActivity extends Activity {

    private static final String TAG = "sml";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_vertical);
        
        final SwipeVerticalMenuLayout sml = (SwipeVerticalMenuLayout) findViewById(R.id.sml);
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
                Toast.makeText(SimpleVerticalActivity.this, "content view onclick", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.smMenuViewTop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleVerticalActivity.this, "top menu view onclick", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.smMenuViewBottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleVerticalActivity.this, "bottom menu view onclick", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleVerticalActivity.this, "top button onclick", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleVerticalActivity.this, "bottom button onclick", Toast.LENGTH_SHORT).show();
            }
        });

        sml.setSwipeListener(new SwipeSwitchListener() {
            @Override
            public void beginMenuClosed(SwipeMenuLayout swipeMenuLayout) {
                Log.e(TAG, "top menu closed");
            }

            @Override
            public void beginMenuOpened(SwipeMenuLayout swipeMenuLayout) {
                Log.e(TAG, "top menu opened");
            }

            @Override
            public void endMenuClosed(SwipeMenuLayout swipeMenuLayout) {
                Log.e(TAG, "bottom menu closed");
            }

            @Override
            public void endMenuOpened(SwipeMenuLayout swipeMenuLayout) {
                Log.e(TAG, "bottom menu opened");
            }
        });

//        sml.setSwipeFractionListener(new SwipeFractionListener() {
//            @Override
//            public void beginMenuSwipeFraction(SwipeMenuLayout swipeMenuLayout, float fraction) {
//                Log.e(TAG, "top menu swipe fraction:"+fraction);
//
//            }
//
//            @Override
//            public void endMenuSwipeFraction(SwipeMenuLayout swipeMenuLayout, float fraction) {
//                Log.e(TAG, "bottom menu swipe fraction:"+fraction);
//            }
//        });
//        sml.setSwipeFractionListener(new SwipeFractionListener() {
//            @Override
//            public void beginMenuSwipeFraction(SwipeMenuLayout swipeMenuLayout, float fraction) {
//                Log.e(TAG, "top menu swipe fraction:"+fraction);
//            }
//
//            @Override
//            public void endMenuSwipeFraction(SwipeMenuLayout swipeMenuLayout, float fraction) {
//                Log.e(TAG, "bottom menu swipe fraction:"+fraction);
//            }
//        });
//        sml.setSwipeFractionListener(new SimpleSwipeFractionListener(){
//            @Override
//            public void beginMenuSwipeFraction(SwipeMenuLayout swipeMenuLayout, float fraction) {
//                Log.e(TAG, "top menu swipe fraction:"+fraction);
//            }
//        });
    }

}
