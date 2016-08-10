package com.tubb.smrv.demo.lv;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.tubb.smrv.demo.R;
import com.tubb.smrv.demo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tubingbing on 16/8/10.
 */

public class SimpleLvActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv_simple);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(SimpleLvActivity.this, "Refresh success", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new MyAdapter(this));
    }

    private static class MyAdapter extends BaseAdapter{

        static final int VIEW_TYPE_ENABLE = 0;
        static final int VIEW_TYPE_DISABLE = 1;
        Context mContext;
        List<User> users;

        MyAdapter(Context context){
            mContext = context;
            users = getUsers();
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public Object getItem(int position) {
            return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final User user = users.get(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_simple, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Hi " + user.getUserName(), Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.btGood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Good", Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.btOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Open " + user.getUserName(), Toast.LENGTH_SHORT).show();
                }
            });
            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // must close normal
                    finalViewHolder.sml.smoothCloseMenu();
                    users.remove(position);
                    notifyDataSetChanged();
                }
            });

            viewHolder.btLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Left click", Toast.LENGTH_SHORT).show();
                }
            });

            viewHolder.tvName.setText(user.getUserName());
            boolean swipeEnable = swipeEnableByViewType(getItemViewType(position));
            viewHolder.tvSwipeEnable.setText(swipeEnable ? "swipe on" : "swipe off");
            viewHolder.sml.setSwipeEnable(swipeEnable);
            return convertView;
        }

        boolean swipeEnableByViewType(int viewType) {
            if(viewType == VIEW_TYPE_ENABLE)
                return true;
            else
                return viewType != VIEW_TYPE_DISABLE;
        }

        class ViewHolder{
            TextView tvName;
            TextView tvSwipeEnable;
            View btGood;
            View btOpen;
            View btDelete;
            View btLeft;
            SwipeHorizontalMenuLayout sml;
            ViewHolder(View itemView) {
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvSwipeEnable = (TextView) itemView.findViewById(R.id.tvSwipeEnable);
                btGood = itemView.findViewById(R.id.btGood);
                btOpen = itemView.findViewById(R.id.btOpen);
                btDelete = itemView.findViewById(R.id.btDelete);
                btLeft = itemView.findViewById(R.id.btLeft);
                sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
            }
        }

        List<User> getUsers() {
            Random random = new Random();
            List<User> userList = new ArrayList<>();
            for (int i=0; i<100; i++){
                User user = new User();
                user.setUserId(i+1000);
                user.setUserName("Pobi "+(i+1));
                int num = random.nextInt(4);
                int photoRes = 0;
                if(num == 0){
                    photoRes = R.drawable.one;
                }else if(num == 1){
                    photoRes = R.drawable.two;
                }else if(num == 2){
                    photoRes = R.drawable.three;
                }else if(num == 3){
                    photoRes = R.drawable.four;
                }
                user.setPhotoRes(photoRes);
                userList.add(user);
            }
            return userList;
        }
    }
}
