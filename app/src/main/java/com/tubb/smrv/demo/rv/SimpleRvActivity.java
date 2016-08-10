package com.tubb.smrv.demo.rv;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.tubb.smrv.demo.AbstractRvActivity;
import com.tubb.smrv.demo.R;
import com.tubb.smrv.demo.User;

import java.util.List;

public class SimpleRvActivity extends AbstractRvActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(3));
    }

    @Override
    protected AbstractRvAdapter createAppAdapter(AbstractRvActivity baseRvActivity, List<User> users) {
        return new SimpleRvAppAdapter(baseRvActivity, users);
    }

    private class SimpleRvAppAdapter extends AbstractRvAdapter {

        public SimpleRvAppAdapter(Context context, List<User> users) {
            super(context, users);
        }

        @Override
        protected RecyclerView.ViewHolder createViewHolder(View itemView) {
            return new SimpleRvViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder vh, final int position) {
            final User user = users.get(position);
            final SimpleRvViewHolder myViewHolder = (SimpleRvViewHolder)vh;
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Hi " + user.getUserName(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "Open " + user.getUserName(), Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // must close normal
                    myViewHolder.sml.smoothCloseMenu();
                    users.remove(vh.getAdapterPosition());
                    mAdapter.notifyItemRemoved(vh.getAdapterPosition());
                }
            });

            myViewHolder.btLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Left click", Toast.LENGTH_SHORT).show();
                }
            });

            myViewHolder.tvName.setText(user.getUserName());
            boolean swipeEnable = swipeEnableByViewType(getItemViewType(position));
            myViewHolder.tvSwipeEnable.setText(swipeEnable ? "swipe on" : "swipe off");
            myViewHolder.sml.setSwipeEnable(swipeEnable);
        }
    }

    public static class SimpleRvViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvSwipeEnable;
        View btGood;
        View btOpen;
        View btDelete;
        View btLeft;
        SwipeHorizontalMenuLayout sml;
        public SimpleRvViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSwipeEnable = (TextView) itemView.findViewById(R.id.tvSwipeEnable);
            btGood = itemView.findViewById(R.id.btGood);
            btOpen = itemView.findViewById(R.id.btOpen);
            btDelete = itemView.findViewById(R.id.btDelete);
            btLeft = itemView.findViewById(R.id.btLeft);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
        }
    }

}
