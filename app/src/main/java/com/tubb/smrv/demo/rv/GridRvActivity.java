package com.tubb.smrv.demo.rv;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.smrv.SwipeMenuLayout;
import com.tubb.smrv.demo.AbstractRvActivity;
import com.tubb.smrv.demo.R;
import com.tubb.smrv.demo.User;

import java.util.List;

public class GridRvActivity extends AbstractRvActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new GridSpaceItemDecoration(3, 3));
    }

    @Override
    protected AbstractRvAdapter createAppAdapter(AbstractRvActivity baseRvActivity, List<User> users) {
        return new GridRvAppAdapter(baseRvActivity, users);
    }

    private class GridRvAppAdapter extends AbstractRvAdapter {

        public GridRvAppAdapter(Context context, List<User> users){
            super(context, users);
        }

        @Override
        protected RecyclerView.ViewHolder createViewHolder(View itemView) {
            return new GridRvViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder vh, final int position) {
            super.onBindViewHolder(vh, position);
            final GridRvViewHolder myViewHolder = (GridRvViewHolder)vh;
            final SwipeMenuLayout itemView = (SwipeMenuLayout) myViewHolder.itemView;
            final User user = users.get(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Hi " + user.getUserName(), Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.btGood.setVisibility(View.GONE);
            myViewHolder.tvSwipeEnable.setVisibility(View.GONE);
        }
    }

    public static class GridRvViewHolder extends AbstractRvActivity.AbstractViewHolder {
        TextView tvSwipeEnable;
        View btGood;
        public GridRvViewHolder(View itemView) {
            super(itemView);
            tvSwipeEnable = (TextView) itemView.findViewById(R.id.tvSwipeEnable);
            btGood = itemView.findViewById(R.id.btGood);
        }
    }

}
