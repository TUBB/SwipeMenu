package com.tubb.smrv.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tubb.smrv.demo.lv.SimpleGvActivity;
import com.tubb.smrv.demo.lv.SimpleLvActivity;
import com.tubb.smrv.demo.normal.NormalMainActivity;
import com.tubb.smrv.demo.rv.RvMainActivity;
import com.tubb.smrv.demo.sv.ScrollViewActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        if (v.getId() == R.id.button1) {
            startActivity(new Intent(this, NormalMainActivity.class));
        } else if(v.getId() == R.id.button2) {
            startActivity(new Intent(this, RvMainActivity.class));
        } else if (v.getId() == R.id.button3) {
            startActivity(new Intent(this, SimpleLvActivity.class));
        } else if (v.getId() == R.id.button4) {
            startActivity(new Intent(this, SimpleGvActivity.class));
        } else if (v.getId() == R.id.button5) {
            startActivity(new Intent(this, ScrollViewActivity.class));
        }
    }
}
