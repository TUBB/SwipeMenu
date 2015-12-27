/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 baoyongzhang <baoyz94@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.tubb.smrv.demo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.smrv.SwipeMenu;
import com.tubb.smrv.SwipeMenuCreator;
import com.tubb.smrv.SwipeMenuItem;
import com.tubb.smrv.SwipeMenuRecyclerView;
import com.tubb.smrv.SwipeMenuRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * SwipeMenuListView
 * Created by baoyz on 15/6/29.
 */
public class DifferentMenuRvActivity extends Activity {

    private static final int VIEW_TYPE_1 = 0;
    private static final int VIEW_TYPE_2 = 1;
    private static final int VIEW_TYPE_3 = 2;

    private static final int ACTION_FAVORITE = 1;
    private static final int ACTION_GOOD = 2;
    private static final int ACTION_IMPORTANT = 3;
    private static final int ACTION_DISCARD = 4;
    private static final int ACTION_ABOUT = 5;
    private static final int ACTION_SHARE = 6;

    private SwipeRefreshLayout swipeRefreshLayout;
    private List<User> users;
    private AppAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        users = getUsers();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(DifferentMenuRvActivity.this, "Refresh success", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        final SwipeMenuRecyclerView listView = (SwipeMenuRecyclerView) findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AppAdapter(this, listView, users);
        listView.setAdapter(mAdapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case VIEW_TYPE_1:
                        createMenu1(menu);
                        break;
                    case VIEW_TYPE_2:
                        createMenu2(menu);
                        break;
                    case VIEW_TYPE_3:
                        createMenu3(menu);
                        break;
                }
            }

            private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());
                item1.setType(ACTION_FAVORITE);
                item1.setBackground(new ColorDrawable(Color.rgb(0xE5, 0x18,
                        0x5E)));
                item1.setWidth(dp2px(90));
                item1.setIcon(R.drawable.ic_action_favorite);
                menu.addMenuItem(item1);

                SwipeMenuItem item2 = new SwipeMenuItem(
                        getApplicationContext());
                item2.setType(ACTION_GOOD);
                item2.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                item2.setWidth(dp2px(90));
                item2.setIcon(R.drawable.ic_action_good);
                menu.addMenuItem(item2);
            }

            private void createMenu2(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());
                item1.setType(ACTION_IMPORTANT);
                item1.setBackground(new ColorDrawable(Color.rgb(0xE5, 0xE0,
                        0x3F)));
                item1.setWidth(dp2px(90));
                item1.setIcon(R.drawable.ic_action_important);
                menu.addMenuItem(item1);

                SwipeMenuItem item2 = new SwipeMenuItem(
                        getApplicationContext());
                item2.setType(ACTION_DISCARD);
                item2.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                item2.setWidth(dp2px(90));
                item2.setIcon(R.drawable.ic_action_discard);
                menu.addMenuItem(item2);
            }

            private void createMenu3(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());
                item1.setType(ACTION_ABOUT);
                item1.setBackground(new ColorDrawable(Color.rgb(0x30, 0xB1,
                        0xF5)));
                item1.setWidth(dp2px(90));
                item1.setIcon(R.drawable.ic_action_about);
                menu.addMenuItem(item1);
                SwipeMenuItem item2 = new SwipeMenuItem(
                        getApplicationContext());
                item2.setType(ACTION_SHARE);
                item2.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                item2.setWidth(dp2px(90));
                item2.setIcon(R.drawable.ic_action_share);
                menu.addMenuItem(item2);
            }
        };
        // set creator
        listView.setMenuCreator(creator);

        // step 2. listener item click event
        listView.setOnMenuItemClickListener(new SwipeMenuRecyclerView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                String userName = users.get(position).userName;
                SwipeMenuItem swipeMenuItem = menu.getMenuItem(index);
                switch (swipeMenuItem.getType()) {
                    case ACTION_FAVORITE:
                        Toast.makeText(DifferentMenuRvActivity.this, "favorite " + userName, Toast.LENGTH_SHORT).show();
                        break;
                    case ACTION_ABOUT:
                        Toast.makeText(DifferentMenuRvActivity.this, "about " + userName, Toast.LENGTH_SHORT).show();
                        break;
                    case ACTION_DISCARD:
                        Toast.makeText(DifferentMenuRvActivity.this, "discard " + userName, Toast.LENGTH_SHORT).show();
                        break;
                    case ACTION_GOOD:
                        Toast.makeText(DifferentMenuRvActivity.this, "good " + userName, Toast.LENGTH_SHORT).show();
                        break;
                    case ACTION_IMPORTANT:
                        Toast.makeText(DifferentMenuRvActivity.this, "important " + userName, Toast.LENGTH_SHORT).show();
                        break;
                    case ACTION_SHARE:
                        Toast.makeText(DifferentMenuRvActivity.this, "share " + userName, Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

    }

    public static class AppAdapter extends SwipeMenuRecyclerViewAdapter {

        List<User> users;

        public AppAdapter(Context context, SwipeMenuRecyclerView rv, List<User> users){
            super(context, rv);
            this.users = users;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        @Override
        public int getItemViewType(int position) {
            int left = position % 3;
            if(left == 0) return VIEW_TYPE_1;
            else if(left == 1) return VIEW_TYPE_2;
            else if(left == 2) return VIEW_TYPE_3;
            return left;
        }

        @Override
        public View onCreateItemView(ViewGroup viewGroup, int viewType) {
            return LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_app, viewGroup, false);
        }

        @Override
        public RecyclerView.ViewHolder onCreateWrapViewHolder(View itemView, int viewType) {
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindWrapViewHolder(RecyclerView.ViewHolder vh, int position) {
            User user = users.get(position);
            final MyViewHolder myViewHolder = (MyViewHolder)vh;
            myViewHolder.tvName.setText(user.userName);
            myViewHolder.btGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(myViewHolder.itemView.getContext(), "Good", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        View btGood;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            btGood = itemView.findViewById(R.id.btGood);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        for (int i=0; i<100; i++){
            User user = new User();
            user.userId = i+1000;
            user.userName = "Pobi "+(i+1);
            userList.add(user);
        }
        return userList;
    }

    class User{
        public int userId;
        public String userName;
    }
}
