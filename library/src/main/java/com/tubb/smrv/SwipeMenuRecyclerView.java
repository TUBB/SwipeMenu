package com.tubb.smrv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SwipeMenuRecyclerView extends RecyclerView {

    private static final int INVALID_POSITION = -1;

    protected ViewConfiguration mViewConfig;
    protected SwipeMenuLayout mOldSwipedView;
    protected int mOldTouchedPosition = INVALID_POSITION;
    private int mDownX;
    private int mDownY;

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

    private View getSwipeMenuView(ViewGroup itemView) {
        if(itemView instanceof SwipeMenuLayout) return itemView;
        List<View> unvisited = new ArrayList<>();
        unvisited.add(itemView);
        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            if (!(child instanceof ViewGroup)) { // view
                continue;
            }
            if(child instanceof SwipeMenuLayout) return child;
            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i=0; i<childCount; i++) unvisited.add(group.getChildAt(i));
        }
        return itemView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = super.onInterceptTouchEvent(ev);
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                isIntercepted = false;
                int touchingPosition = getChildAdapterPosition(findChildViewUnder((int) ev.getX(), (int) ev.getY()));
                if(touchingPosition != mOldTouchedPosition && mOldSwipedView != null){
                    // already one swipe menu is opened, so we close it and intercept the event
                    if(mOldSwipedView.isMenuOpen()){
                        mOldSwipedView.smoothCloseMenu();
                        isIntercepted = true;
                    }
                }
                ViewHolder vh = findViewHolderForAdapterPosition(touchingPosition);
                if(vh != null){
                    View itemView = getSwipeMenuView((ViewGroup) vh.itemView);
                    if(itemView != null && itemView instanceof SwipeMenuLayout){
                        mOldSwipedView = (SwipeMenuLayout) itemView;
                        mOldTouchedPosition = touchingPosition;
                    }
                }
                // if we intercept the event, just reset
                if(isIntercepted){
                    mOldSwipedView = null;
                    mOldTouchedPosition = INVALID_POSITION;
                }
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int disX = (int) (mDownX - ev.getX());
                int disY = (int) (mDownY - ev.getY());
                // swipe
                if(Math.abs(disX) > mViewConfig.getScaledTouchSlop())
                    isIntercepted = false;
                // click
                if(Math.abs(disY) < mViewConfig.getScaledTouchSlop()
                        && Math.abs(disX) < mViewConfig.getScaledTouchSlop())
                    isIntercepted = false;
                break;
        }
        return isIntercepted;
    }
}
