package com.tubb.smrv.demo;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tubingbing on 16/4/11.
 */
public class BaseActivity extends Activity{
    Random random = new Random();
    protected List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        for (int i=0; i<100; i++){
            User user = new User();
            user.userId = i+1000;
            user.userName = "Pobi "+(i+1);
            int num = random.nextInt(4);
            if(num == 0){
                user.photoRes = R.drawable.one;
            }else if(num == 1){
                user.photoRes = R.drawable.two;
            }else if(num == 2){
                user.photoRes = R.drawable.three;
            }else if(num == 3){
                user.photoRes = R.drawable.four;
            }
            userList.add(user);
        }
        return userList;
    }
}
