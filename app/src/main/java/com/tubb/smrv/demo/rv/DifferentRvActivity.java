package com.tubb.smrv.demo.rv;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tubb.smrv.SwipeHorizontalMenuLayout;
import com.tubb.smrv.demo.AbstractRvActivity;
import com.tubb.smrv.demo.R;
import com.tubb.smrv.demo.User;

import java.util.List;

public class DifferentRvActivity extends AbstractRvActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected AbstractRvAdapter createAppAdapter(AbstractRvActivity baseRvActivity, List<User> users) {
        return new DifferentRvAppAdapter(baseRvActivity, users);
    }

    private class DifferentRvAppAdapter extends AbstractRvAdapter {

        private static final int VIEW_TYPE_SIMPLE = 0;
        private static final int VIEW_TYPE_DIFFERENT = 1;

        public DifferentRvAppAdapter(Context context, List<User> users){
            super(context, users);
        }

        @Override
        protected RecyclerView.ViewHolder createViewHolder(View itemView) {
            return null;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_SIMPLE) {
                View simpleView = LayoutInflater.from(mContext).inflate(R.layout.item_simple, parent, false);
                return new NormalViewHolder(simpleView);
            } else if(viewType == VIEW_TYPE_DIFFERENT){
                View differentView = LayoutInflater.from(mContext).inflate(R.layout.item_different, parent, false);
                return new DifferentViewHolder(differentView);
            }

            return null;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder vh, final int position) {
            final User user = users.get(position);
            int viewType = getItemViewType(position);
            if (viewType == VIEW_TYPE_SIMPLE) {
                fillSimpleView(vh, user);
            } else if(viewType == VIEW_TYPE_DIFFERENT) {
                fillDifferentView((DifferentViewHolder) vh, user);
            }

        }

        private void fillDifferentView(DifferentViewHolder vh, final User user) {
            final DifferentViewHolder differentViewHolder = vh;
            SwipeHorizontalMenuLayout itemView = (SwipeHorizontalMenuLayout) differentViewHolder.itemView;
            differentViewHolder.tvName.setText(user.getUserName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Hi " + user.getUserName(), Toast.LENGTH_SHORT).show();
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
            final SwipeHorizontalMenuLayout itemView = (SwipeHorizontalMenuLayout) normalViewHolder.itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Hi " + user.getUserName(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "Open " + user.getUserName(), Toast.LENGTH_SHORT).show();
                }
            });
            normalViewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // must close normal
                    itemView.smoothCloseMenu();
                    users.remove(vh.getAdapterPosition());
                    mAdapter.notifyItemRemoved(vh.getAdapterPosition());
                }
            });
            normalViewHolder.btLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Left click", Toast.LENGTH_SHORT).show();
                }
            });
            normalViewHolder.tvName.setText(user.getUserName());
        }

        @Override
        public int getItemViewType(int position) {
            User user = users.get(position);
            if(user.getUserId() % 2 == 0){
                return VIEW_TYPE_SIMPLE;
            }else{
                return VIEW_TYPE_DIFFERENT;
            }
        }

    }

    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvSwipeEnable;
        View btGood;
        View btOpen;
        View btDelete;
        View btLeft;
        public NormalViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSwipeEnable = (TextView) itemView.findViewById(R.id.tvSwipeEnable);
            btGood = itemView.findViewById(R.id.btGood);
            btOpen = itemView.findViewById(R.id.btOpen);
            btDelete = itemView.findViewById(R.id.btDelete);
            btLeft = itemView.findViewById(R.id.btLeft);
        }
    }

    public static class DifferentViewHolder extends RecyclerView.ViewHolder {
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

}
