package com.tubb.smrv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

public class SwipeMenuGridView extends GridView implements SwipeMenuHelper.Callback{

    protected SwipeMenuHelper mHelper;

    public SwipeMenuGridView(Context context) {
        super(context);
        init();
    }

    public SwipeMenuGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeMenuGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        mHelper = new SwipeMenuHelper(getContext(), this);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = super.onInterceptTouchEvent(ev);
        // ignore Multi-Touch
        if (ev.getActionIndex() != 0) return true;
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isIntercepted = mHelper.handleListDownTouchEvent(ev, isIntercepted);
                break;
        }
        return isIntercepted;
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
        return touchingView;
    }
}
