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

public class SimpleRvActivity extends Activity {

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
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(3));
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

        private static final int VIEW_TYPE_ENABLE = 0;
        private static final int VIEW_TYPE_DISABLE = 1;

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
                return VIEW_TYPE_DISABLE;
            }else{
                return VIEW_TYPE_ENABLE;
            }
        }

        public boolean swipeEnableByViewType(int viewType) {
            if(viewType == VIEW_TYPE_ENABLE) return true;
            else if(viewType == VIEW_TYPE_DISABLE) return false;
            else return true; // default
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_simple_nest, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder vh, final int position) {
            final User user = users.get(position);
            final MyViewHolder myViewHolder = (MyViewHolder)vh;
            View itemView = myViewHolder.itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Hi " + user.userName, Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.btGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(myViewHolder.itemView.getContext(), "Good", Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.btOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Open " + user.userName, Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    users.remove(vh.getAdapterPosition());
                    mAdapter.notifyItemRemoved(vh.getAdapterPosition());
                }
            });
            myViewHolder.tvName.setText(user.userName);
            boolean swipeEnable = swipeEnableByViewType(getItemViewType(position));
            myViewHolder.tvSwipeEnable.setText(swipeEnable ? "swipe on" : "swipe off");

            /**
             * optional
             */

            myViewHolder.swipeMenuLayout.setSwipeEnable(swipeEnable);
            myViewHolder.swipeMenuLayout.setOpenInterpolator(mRecyclerView.getOpenInterpolator());
            myViewHolder.swipeMenuLayout.setCloseInterpolator(mRecyclerView.getCloseInterpolator());
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvSwipeEnable;
        View btGood;
        View btOpen;
        View btDelete;
        SwipeMenuLayout swipeMenuLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSwipeEnable = (TextView) itemView.findViewById(R.id.tvSwipeEnable);
            btGood = itemView.findViewById(R.id.btGood);
            btOpen = itemView.findViewById(R.id.btOpen);
            btDelete = itemView.findViewById(R.id.btDelete);
            swipeMenuLayout = (SwipeMenuLayout) itemView.findViewById(R.id.sml);
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
