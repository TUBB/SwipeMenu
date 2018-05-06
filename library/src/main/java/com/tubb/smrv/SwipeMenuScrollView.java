package com.tubb.smrv;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class SwipeMenuScrollView extends ScrollView implements SwipeMenuHelper.Callback{

    protected SwipeMenuHelper mHelper;
    protected ViewGroup realChildContainer;
    protected List<SwipeHorizontalMenuLayout> menuLayoutList;

    public SwipeMenuScrollView(Context context) {
        super(context);
        init();
    }

    public SwipeMenuScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeMenuScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        mHelper = new Helper(getContext(), this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (super.getChildCount() > 0) {
            View rootView = getChildAt(0);
            if (rootView instanceof ViewGroup) {
                realChildContainer = (ViewGroup) rootView;
                menuLayoutList = new ArrayList<>(realChildContainer.getChildCount());
                for (int i = 0, len = realChildContainer.getChildCount(); i < len; i++) {
                    View childView = realChildContainer.getChildAt(i);
                    if (childView instanceof SwipeHorizontalMenuLayout) {
                        menuLayoutList.add((SwipeHorizontalMenuLayout) childView);
                    }
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mHelper.handleDispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = super.onInterceptTouchEvent(ev);
        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isIntercepted = mHelper.handleListDownTouchEvent(ev, isIntercepted);
                break;
        }
        return isIntercepted;
    }

    public int getPositionForView(View touchingView) {
        if (realChildContainer != null) {
            for (int i = 0, len = realChildContainer.getChildCount(); i < len; i++) {
                if (touchingView == realChildContainer.getChildAt(i)) {
                    return i;
                }
            }
        }
        return SwipeMenuHelper.INVALID_POSITION;
    }

    @Override
    public View transformTouchingView(int touchingPosition, View touchingView) {
        return touchingView;
    }

    @Override
    public int getRealChildCount() {
        if (realChildContainer != null) {
            return realChildContainer.getChildCount();
        } else {
            return super.getChildCount();
        }
    }

    @Override
    public View getRealChildAt(int index) {
        if (realChildContainer != null) {
            return realChildContainer.getChildAt(index);
        } else {
            return super.getChildAt(index);
        }
    }

    @Override
    public void reset() {
        mHelper.reset();
    }

    protected class Helper extends SwipeMenuHelper {

        protected Helper(Context context, Callback callback) {
            super(context, callback);
        }

        @Nullable
        @Override
        public View findChildViewUnder(float x, float y) {
            if (menuLayoutList != null) {
                int scrollY = getScrollY();
                float realY = y + scrollY;
                for (SwipeHorizontalMenuLayout menuLayout : menuLayoutList) {
                    int topY = menuLayout.getTop();
                    int height = menuLayout.getHeight();
                    if (realY >= topY && realY <= topY + height) {
                        if (!menuLayout.isMenuOpened()) {
                            closeOpenedMenu();
                        }
                        return menuLayout;
                    }
                }
            }
            return null;
        }

        protected void closeOpenedMenu() {
            for (SwipeHorizontalMenuLayout menuLayout:menuLayoutList) {
                if (menuLayout.isMenuOpened()) {
                    menuLayout.smoothCloseMenu();
                }
            }
        }
    }
}
