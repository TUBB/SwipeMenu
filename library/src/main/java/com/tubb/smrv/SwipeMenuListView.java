package com.tubb.smrv;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SwipeMenuListView extends ListView {

    private static final int INVALID_POSITION = -1;

    protected ViewConfiguration mViewConfig;
    protected SwipeHorizontalMenuLayout mOldSwipedView;
    protected int mOldTouchedPosition = INVALID_POSITION;
    private int mDownX;
    private int mDownY;

    public SwipeMenuListView(Context context) {
        super(context);
        init();
    }

    public SwipeMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        mViewConfig = ViewConfiguration.get(getContext());
    }

    private View getSwipeMenuView(ViewGroup itemView) {
        if (itemView instanceof SwipeHorizontalMenuLayout) return itemView;
        List<View> unvisited = new ArrayList<>();
        unvisited.add(itemView);
        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            if (!(child instanceof ViewGroup)) { // view
                continue;
            }
            if (child instanceof SwipeHorizontalMenuLayout) return child;
            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i = 0; i < childCount; i++) unvisited.add(group.getChildAt(i));
        }
        return itemView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = super.onInterceptTouchEvent(ev);
        // ignore Multi-Touch
        if (ev.getActionIndex() != 0) return true;
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                View touchView = findChildViewUnder((int) ev.getX(), (int) ev.getY());
                int touchingPosition = getPositionForView(touchView);
                if (touchingPosition != mOldTouchedPosition && mOldSwipedView != null) {
                    // already one swipe menu is opened, so we close it and intercept the event
                    if (mOldSwipedView.isMenuOpen()) {
                        mOldSwipedView.smoothCloseMenu();
                        isIntercepted = true;
                    }
                }
                if (touchView != null) {
                    View itemView = getSwipeMenuView((ViewGroup) touchView);
                    if (itemView != null && itemView instanceof SwipeHorizontalMenuLayout) {
                        mOldSwipedView = (SwipeHorizontalMenuLayout) itemView;
                        mOldTouchedPosition = touchingPosition;
                    }
                }
                // if we intercept the event, just reset
                if (isIntercepted) {
                    mOldSwipedView = null;
                    mOldTouchedPosition = INVALID_POSITION;
                }
                break;
        }
        return isIntercepted;
    }

    /**
     * Find the topmost view under the given point.
     *
     * @param x Horizontal position in pixels to search
     * @param y Vertical position in pixels to search
     * @return The child view under (x, y) or null if no matching child is found
     */
    protected View findChildViewUnder(float x, float y) {
        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            final float translationX = ViewCompat.getTranslationX(child);
            final float translationY = ViewCompat.getTranslationY(child);
            if (x >= child.getLeft() + translationX &&
                    x <= child.getRight() + translationX &&
                    y >= child.getTop() + translationY &&
                    y <= child.getBottom() + translationY) {
                return child;
            }
        }
        return null;
    }
}
