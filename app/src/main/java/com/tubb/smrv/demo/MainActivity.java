package com.tubb.smrv.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button1:
                startActivity(new Intent(this, SimpleRvActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(this, DifferentRvActivity.class));
                break;
            case R.id.button3:
                startActivity(new Intent(this, GridRvActivity.class));
                break;
            case R.id.button4:
                startActivity(new Intent(this, StaggeredGridRvActivity.class));
                break;
        }
    }
}
