package com.tubb.smrv.demo.rv;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.smrv.SwipeMenuLayout;
import com.tubb.smrv.SwipeMenuRecyclerView;
import com.tubb.smrv.demo.BaseActivity;
import com.tubb.smrv.demo.R;
import com.tubb.smrv.demo.User;

import java.util.List;

public class GridRvActivity extends BaseActivity {

    private Context mContext;
    private AppAdapter mAdapter;
    private List<User> users;
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
        mRecyclerView.addItemDecoration(new GridSpaceItemDecoration(3, 3));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new AppAdapter(this, users);
        mRecyclerView.setAdapter(mAdapter);
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
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_simple, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder vh, final int position) {
            final User user = users.get(position);
            final MyViewHolder myViewHolder = (MyViewHolder)vh;
            final SwipeMenuLayout itemView = (SwipeMenuLayout) myViewHolder.itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Hi " + user.userName, Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.btGood.setVisibility(View.GONE);
            myViewHolder.btOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Open " + user.userName, Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // must close normal
                    itemView.smoothCloseMenu();
                    users.remove(vh.getAdapterPosition());
                    mAdapter.notifyItemRemoved(vh.getAdapterPosition());
                }
            });
            myViewHolder.tvName.setText(user.userName);
            myViewHolder.tvSwipeEnable.setVisibility(View.GONE);

        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvSwipeEnable;
        View btGood;
        View btOpen;
        View btDelete;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSwipeEnable = (TextView) itemView.findViewById(R.id.tvSwipeEnable);
            btGood = itemView.findViewById(R.id.btGood);
            btOpen = itemView.findViewById(R.id.btOpen);
            btDelete = itemView.findViewById(R.id.btDelete);
        }
    }

}
