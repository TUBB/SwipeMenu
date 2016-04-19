package com.tubb.smrv.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tubb.smrv.demo.normal.SimpleActivity;
import com.tubb.smrv.demo.rv.RvMainActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        if (v.getId() == R.id.button1) {
            startActivity(new Intent(this, SimpleActivity.class));
        } else if(v.getId() == R.id.button2) {
            startActivity(new Intent(this, RvMainActivity.class));
        }
    }
}
