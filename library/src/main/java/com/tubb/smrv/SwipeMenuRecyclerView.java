package com.tubb.smrv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

public class SwipeMenuRecyclerView extends RecyclerView {

    protected ViewConfiguration mViewConfig;
    protected SwipeHorizontalMenuLayout mOldSwipedView;
    protected int mOldTouchedPosition = SwipeMenuHelper.INVALID_POSITION;

    public SwipeMenuRecyclerView(Context context) {
        super(context);
        init();
    }

    public SwipeMenuRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeMenuRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        mViewConfig = ViewConfiguration.get(getContext());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = super.onInterceptTouchEvent(ev);
        // ignore Multi-Touch
        if (ev.getActionIndex() != 0) return true;
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int touchingPosition = getChildAdapterPosition(findChildViewUnder((int) ev.getX(), (int) ev.getY()));
                if (touchingPosition != mOldTouchedPosition && mOldSwipedView != null) {
                    // already one swipe menu is opened, so we close it and intercept the event
                    if (mOldSwipedView.isMenuOpen()) {
                        mOldSwipedView.smoothCloseMenu();
                        isIntercepted = true;
                    }
                }
                ViewHolder vh = findViewHolderForAdapterPosition(touchingPosition);
                if (vh != null) {
                    View itemView = SwipeMenuHelper.getSwipeMenuView((ViewGroup) vh.itemView);
                    if (itemView != null && itemView instanceof SwipeHorizontalMenuLayout) {
                        mOldSwipedView = (SwipeHorizontalMenuLayout) itemView;
                        mOldTouchedPosition = touchingPosition;
                    }
                }
                // if we intercept the event, just reset
                if (isIntercepted) {
                    mOldSwipedView = null;
                    mOldTouchedPosition = SwipeMenuHelper.INVALID_POSITION;
                }
                break;
        }
        return isIntercepted;
    }
}
