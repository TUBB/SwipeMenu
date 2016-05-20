package com.tubb.smrv.demo.normal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tubb.smrv.demo.R;
import com.tubb.smrv.demo.rv.RvMainActivity;

public class NormalMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_main);
    }

    public void onClick(View v){
        if (v.getId() == R.id.button1) {
            startActivity(new Intent(this, SimpleActivity.class));
        }else if(v.getId() == R.id.button2){
            startActivity(new Intent(this, SimpleVerticalActivity.class));
        }
    }
}
