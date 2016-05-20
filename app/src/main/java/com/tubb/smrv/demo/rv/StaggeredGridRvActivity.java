package com.tubb.smrv.demo.rv;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.tubb.smrv.demo.AbstractRvActivity;
import com.tubb.smrv.demo.R;
import com.tubb.smrv.demo.User;

import java.util.List;

public class StaggeredGridRvActivity extends AbstractRvActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new StaggeredSpaceItemDecoration(15, 0, 15, 45));
    }

    @Override
    protected AbstractRvAdapter createAppAdapter(AbstractRvActivity baseRvActivity, List<User> users) {
        return new StaggeredGridRvAdapter(baseRvActivity, users);
    }

    private class StaggeredGridRvAdapter extends AbstractRvAdapter {

        public StaggeredGridRvAdapter(Context context, List<User> users) {
            super(context, users);
        }

        @Override
        protected RecyclerView.ViewHolder createViewHolder(View itemView) {
            return new StaggeredGridRvViewHolder(itemView);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_staggered, parent, false);
            return createViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder vh, final int position) {
            super.onBindViewHolder(vh, position);
            final StaggeredGridRvViewHolder myViewHolder = (StaggeredGridRvViewHolder)vh;
            final SwipeHorizontalMenuLayout itemView = (SwipeHorizontalMenuLayout) myViewHolder.itemView;
            final User user = users.get(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Hi " + user.getUserName(), Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.btLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Left click", Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.ivPhoto.setImageResource(user.getPhotoRes());
        }
    }

    public static class StaggeredGridRvViewHolder extends AbstractRvActivity.AbstractViewHolder {
        ImageView ivPhoto;
        View btLeft;
        public StaggeredGridRvViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            btLeft = itemView.findViewById(R.id.btLeft);
        }
    }

}
