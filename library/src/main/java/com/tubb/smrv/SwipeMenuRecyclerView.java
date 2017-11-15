package com.tubb.smrv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SwipeMenuRecyclerView extends RecyclerView implements SwipeMenuHelper.Callback{

    protected SwipeMenuHelper mHelper;

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
        mHelper = new SwipeMenuHelper(getContext(), this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mHelper.handleDispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean isIntercepted = super.onInterceptTouchEvent(event);
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isIntercepted = mHelper.handleListDownTouchEvent(event, isIntercepted);
                break;
        }
        return isIntercepted;
    }

    public int getPositionForView(View touchView) {
        return getChildAdapterPosition(touchView);
    }

    @Override
    public int getRealChildCount() {
        return getChildCount();
    }

    @Override
    public View getRealChildAt(int index) {
        return getChildAt(index);
    }

    @Override
    public View transformTouchingView(int touchingPosition, View touchingView) {
        ViewHolder vh = findViewHolderForAdapterPosition(touchingPosition);
        if (vh != null) {
            return vh.itemView;
        }
        return touchingView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mHelper.reset();
        super.setAdapter(adapter);
    }

    @Override
    public void reset() {
        mHelper.reset();
    }
}
