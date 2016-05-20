package com.tubb.smrv.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.tubb.smrv.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractRvActivity extends Activity {

    protected Context mContext;
    protected List<User> users;
    protected AbstractRvAdapter mAdapter;
    protected SwipeMenuRecyclerView mRecyclerView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    private Random random = new Random();

    protected List<User> getUsers() {
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
        mAdapter = createAppAdapter(this, users);
        mRecyclerView.setAdapter(mAdapter);
    }

    protected abstract AbstractRvAdapter createAppAdapter(AbstractRvActivity baseRvActivity, List<User> users);

    protected abstract class AbstractRvAdapter extends RecyclerView.Adapter {

        protected static final int VIEW_TYPE_ENABLE = 0;
        protected static final int VIEW_TYPE_DISABLE = 1;

        List<User> users;

        public AbstractRvAdapter(Context context, List<User> users) {
            this.users = users;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_simple, parent, false);
            return createViewHolder(itemView);
        }

        protected boolean swipeEnableByViewType(int viewType) {
            if(viewType == VIEW_TYPE_ENABLE)
                return true;
            else
                return viewType != VIEW_TYPE_DISABLE;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder vh, int position) {
            final AbstractViewHolder myViewHolder = (AbstractViewHolder)vh;
            final User user = users.get(position);
            final SwipeHorizontalMenuLayout itemView = (SwipeHorizontalMenuLayout) myViewHolder.itemView;
            myViewHolder.getBtOpen().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Open " + user.getUserName(), Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.getBtDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // must close normal
                    itemView.smoothCloseMenu();
                    users.remove(vh.getAdapterPosition());
                    mAdapter.notifyItemRemoved(vh.getAdapterPosition());
                }
            });
            myViewHolder.getTvName().setText(user.getUserName());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return users.size();
        }
        protected abstract RecyclerView.ViewHolder createViewHolder(View itemView);
    }

    protected static abstract class AbstractViewHolder extends RecyclerView.ViewHolder {
        private View btOpen;
        private View btDelete;
        private TextView tvName;

        public AbstractViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            btOpen = itemView.findViewById(R.id.btOpen);
            btDelete = itemView.findViewById(R.id.btDelete);
        }

        public View getBtOpen() {
            return btOpen;
        }

        public View getBtDelete() {
            return btDelete;
        }

        public TextView getTvName() {
            return tvName;
        }
    }
}

