package com.tubb.smrv.demo.normal;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.tubb.smrv.SwipeMenuLayout;
import com.tubb.smrv.demo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        
        final SwipeMenuLayout sml = (SwipeMenuLayout) findViewById(R.id.sml);
        findViewById(R.id.btLeftOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sml.smoothOpenLeftMenu();
            }
        });

        findViewById(R.id.btLeftClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sml.smoothCloseLeftMenu();
            }
        });

        findViewById(R.id.btRightOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sml.smoothOpenRightMenu();
            }
        });

        findViewById(R.id.btRightClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sml.smoothCloseRightMenu();
            }
        });

        findViewById(R.id.smContentView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SimpleActivity.this, "content view onclick", Toast.LENGTH_SHORT).show();
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
    }

}
