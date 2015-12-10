package com.tubb.smrv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class SwipeMenuRecyclerViewAdapter extends RecyclerView.Adapter
		implements SwipeMenuView.OnSwipeItemClickListener {

	private Context mContext;
	private SwipeMenuRecyclerView mRecyclerView;

	public SwipeMenuRecyclerViewAdapter(Context context, SwipeMenuRecyclerView recyclerView) {
		mContext = context;
		mRecyclerView = recyclerView;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View itemView = onCreateItemView(viewGroup, viewType);
        SwipeMenu menu = new SwipeMenu(mContext);
        menu.setViewType(viewType);
        createMenu(menu);
        SwipeMenuView menuView = new SwipeMenuView(menu);
        menuView.setClickable(true);
        menuView.setOnSwipeItemClickListener(this);
        SwipeMenuLayout layout = new SwipeMenuLayout(itemView, menuView,
                mRecyclerView.getCloseInterpolator(),
                mRecyclerView.getOpenInterpolator());
        // be sure its parent able to intercept touch event
        layout.setClickable(true);
		return onCreateWrapViewHolder(layout, viewType);
	}

    public abstract View onCreateItemView(ViewGroup viewGroup, int viewType);
    public abstract RecyclerView.ViewHolder onCreateWrapViewHolder(View itemView, int viewType);
    public abstract void onBindWrapViewHolder(RecyclerView.ViewHolder vh, int position);
    public boolean swipeEnableByViewType(int viewType){
        return true;
    }

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        SwipeMenuLayout layout = (SwipeMenuLayout) vh.itemView;
        int viewType = getItemViewType(position);
        layout.getMenuView().getMenu().setViewType(viewType);
        layout.setSwipeEnable(swipeEnableByViewType(viewType));
        layout.closeMenu();
        layout.setPosition(vh);
        onBindWrapViewHolder(vh, position);
	}

    @Override
    public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
        boolean flag = false;
        SwipeMenuRecyclerView.OnMenuItemClickListener onMenuItemClickListener = mRecyclerView.getOnMenuItemClickListener();
        if (onMenuItemClickListener != null) {
            flag = onMenuItemClickListener.onMenuItemClick(
                    view.getPosition().getAdapterPosition(), menu, index);
        }
        SwipeMenuLayout touchView = mRecyclerView.getTouchView();
        if (touchView != null && !flag) {
            touchView.smoothCloseMenu();
        }
    }

	private void createMenu(SwipeMenu menu) {
        SwipeMenuCreator menuCreator = mRecyclerView.getMenuCreator();
		if (menuCreator != null) {
            menuCreator.create(menu);
		}
	}
}
