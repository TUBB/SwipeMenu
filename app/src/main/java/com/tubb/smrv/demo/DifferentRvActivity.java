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
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.smrv.SwipeMenuLayout;
import com.tubb.smrv.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DifferentRvActivity extends Activity {

    private Context mContext;
    private List<User> users;
    private AppAdapter mAdapter;
    private SwipeMenuRecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mContext = this;
        users = getUsers();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(mContext, "Refresh success", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        mRecyclerView = (SwipeMenuRecyclerView) findViewById(R.id.listView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // interpolator setting
        mRecyclerView.setOpenInterpolator(new BounceInterpolator());
        mRecyclerView.setCloseInterpolator(new BounceInterpolator());
        mAdapter = new AppAdapter(this, users);
        mRecyclerView.setAdapter(mAdapter);
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

    class AppAdapter extends RecyclerView.Adapter {

        private static final int VIEW_TYPE_SIMPLE = 0;
        private static final int VIEW_TYPE_DIFFERENT = 1;

        List<User> users;

        public AppAdapter(Context context, List<User> users){
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
            User user = users.get(position);
            if(user.userId % 2 == 0){
                return VIEW_TYPE_SIMPLE;
            }else{
                return VIEW_TYPE_DIFFERENT;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case VIEW_TYPE_SIMPLE:
                    View simpleView = LayoutInflater.from(mContext).inflate(R.layout.item_simple, parent, false);
                    return new NormalViewHolder(simpleView);
                case VIEW_TYPE_DIFFERENT:
                    View differentView = LayoutInflater.from(mContext).inflate(R.layout.item_different, parent, false);
                    return new DifferentViewHolder(differentView);
            }

            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder vh, final int position) {
            final User user = users.get(position);
            int viewType = getItemViewType(position);
            switch (viewType){
                case VIEW_TYPE_SIMPLE:
                    fillSimpleView(vh, user);
                    break;
                case VIEW_TYPE_DIFFERENT:
                    fillDifferentView((DifferentViewHolder) vh, user);
                    break;
            }

        }

        private void fillDifferentView(DifferentViewHolder vh, final User user) {
            final DifferentViewHolder differentViewHolder = vh;
            SwipeMenuLayout itemView = (SwipeMenuLayout) differentViewHolder.itemView;
            differentViewHolder.tvName.setText(user.userName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Hi " + user.userName, Toast.LENGTH_SHORT).show();
                }
            });
            differentViewHolder.btGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(differentViewHolder.itemView.getContext(), "Good", Toast.LENGTH_SHORT).show();
                }
            });
            differentViewHolder.btFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(differentViewHolder.itemView.getContext(), "Favorite", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void fillSimpleView(final RecyclerView.ViewHolder vh, final User user) {
            final NormalViewHolder normalViewHolder = (NormalViewHolder)vh;
            SwipeMenuLayout itemView = (SwipeMenuLayout) normalViewHolder.itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Hi " + user.userName, Toast.LENGTH_SHORT).show();
                }
            });
            normalViewHolder.btGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(normalViewHolder.itemView.getContext(), "Good", Toast.LENGTH_SHORT).show();
                }
            });
            normalViewHolder.btOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Open " + user.userName, Toast.LENGTH_SHORT).show();
                }
            });
            normalViewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    users.remove(vh.getAdapterPosition());
                    mAdapter.notifyItemRemoved(vh.getAdapterPosition());
                }
            });
            normalViewHolder.tvName.setText(user.userName);
        }
    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvSwipeEnable;
        View btGood;
        View btOpen;
        View btDelete;
        public NormalViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSwipeEnable = (TextView) itemView.findViewById(R.id.tvSwipeEnable);
            btGood = itemView.findViewById(R.id.btGood);
            btOpen = itemView.findViewById(R.id.btOpen);
            btDelete = itemView.findViewById(R.id.btDelete);
        }
    }

    public static class DifferentViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        View btGood;
        View btFavorite;
        public DifferentViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            btGood = itemView.findViewById(R.id.btGood);
            btFavorite = itemView.findViewById(R.id.btFavorite);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_left) {
            mRecyclerView.setSwipeDirection(SwipeMenuRecyclerView.DIRECTION_LEFT);
            return true;
        }
        if (id == R.id.action_right) {
            mRecyclerView.setSwipeDirection(SwipeMenuRecyclerView.DIRECTION_RIGHT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class User{
        public int userId;
        public String userName;
    }
}
