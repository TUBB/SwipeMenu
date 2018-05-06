package com.tubb.smrv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewParent;

import com.tubb.smrv.swiper.BottomVerticalSwiper;
import com.tubb.smrv.swiper.Swiper;
import com.tubb.smrv.swiper.TopVerticalSwiper;

public class SwipeVerticalMenuLayout extends SwipeMenuLayout {

    protected int mPreScrollY;
    protected float mPreTopMenuFraction = -1;
    protected float mPreBottomMenuFraction = -1;

    public SwipeVerticalMenuLayout(Context context) {
        super(context);
    }

    public SwipeVerticalMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeVerticalMenuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = super.onInterceptTouchEvent(ev);
        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = mLastX = (int) ev.getX();
                mDownY = mLastY = (int) ev.getY();
                isIntercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int disX = (int) (ev.getX() - mDownX);
                int disY = (int) (ev.getY() - mDownY);
                isIntercepted = Math.abs(disY) > mScaledTouchSlop && Math.abs(disY) > Math.abs(disX);
                break;
            case MotionEvent.ACTION_UP:
                isIntercepted = handleActionUpOfIntercept(ev.getY());
                break;
            case MotionEvent.ACTION_CANCEL:
                isIntercepted = false;
                if (!mScroller.isFinished())
                    mScroller.forceFinished(false);
                break;
        }
        return isIntercepted;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(ev);
        int dx;
        int dy;
        int action = ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isSwipeEnable()) break;
                int disX = (int) (mLastX - ev.getX());
                int disY = (int) (mLastY - ev.getY());
                if (!mDragging
                        && Math.abs(disY) > mScaledTouchSlop
                        && Math.abs(disY) > Math.abs(disX)) {
                    ViewParent parent = getParent();
                    if(parent!= null){
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mDragging = true;
                }
                if (mDragging) {
                    if (mCurrentSwiper == null || shouldResetSwiper) {
                        if (disY < 0) {
                            if (mBeginSwiper != null)
                                mCurrentSwiper = mBeginSwiper;
                            else
                                mCurrentSwiper = mEndSwiper;
                        } else {
                            if (mEndSwiper != null)
                                mCurrentSwiper = mEndSwiper;
                            else
                                mCurrentSwiper = mBeginSwiper;
                        }
                    }
                    scrollBy(0, disY);
                    mLastX = (int) ev.getX();
                    mLastY = (int) ev.getY();
                    shouldResetSwiper = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                ViewParent parent = getParent();
                if(parent!= null){
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                dx = (int) (mDownX - ev.getX());
                dy = (int) (mDownY - ev.getY());
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
                int velocityY = (int) mVelocityTracker.getYVelocity();
                int velocity = Math.abs(velocityY);
                if (velocity > mScaledMinimumFlingVelocity) {
                    if (mCurrentSwiper != null) {
                        int duration = getSwipeDuration(ev, velocity);
                        if (mCurrentSwiper instanceof BottomVerticalSwiper) {
                            if (velocityY < 0) {
                                smoothOpenMenu(duration);
                            } else {
                                smoothCloseMenu(duration);
                            }
                        } else {
                            if (velocityY > 0) {
                                smoothOpenMenu(duration);
                            } else {
                                smoothCloseMenu(duration);
                            }
                        }
                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                } else {
                    judgeOpenClose(dx, dy);
                }
                mVelocityTracker.clear();
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                if (Math.abs(mDownY - ev.getY()) > mScaledTouchSlop
                        || Math.abs(mDownX - ev.getX()) > mScaledTouchSlop
                        || isMenuOpened()
                        || isSwiping()) { // ignore click listener
                    MotionEvent motionEvent = MotionEvent.obtain(ev);
                    motionEvent.setAction(MotionEvent.ACTION_CANCEL);
                    return super.onTouchEvent(motionEvent);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(false);
                } else {
                    dx = (int) (mDownX - ev.getX());
                    dy = (int) (mDownY - ev.getY());
                    judgeOpenClose(dx, dy);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void judgeOpenClose(int dx, int dy) {
        if (mCurrentSwiper != null) {
            if (Math.abs(getScrollY()) >= (mCurrentSwiper.getMenuView().getHeight() * mAutoOpenPercent)) {
                if (Math.abs(dx) > mScaledTouchSlop || Math.abs(dy) > mScaledTouchSlop) {
                    if (isMenuOpenedNotEqual())
                        smoothCloseMenu();
                    else
                        smoothOpenMenu();
                } else { // normal up
                    if (isMenuOpened())
                        smoothCloseMenu();
                    else
                        smoothOpenMenu();
                }
            } else { // auto close
                smoothCloseMenu();
            }
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        Swiper.Checker checker = mCurrentSwiper.checkXY(x, y);
        shouldResetSwiper = checker.shouldResetSwiper;
        if (checker.y != getScrollY()) {
            super.scrollTo(checker.x, checker.y);
        }

        if (getScrollY() != mPreScrollY) {
            int absScrollY = Math.abs(getScrollY());
            if (mCurrentSwiper instanceof TopVerticalSwiper) {
                if (mSwipeSwitchListener != null) {
                    if (absScrollY == 0) mSwipeSwitchListener.beginMenuClosed(this);
                    else if (absScrollY == mBeginSwiper.getMenuHeight())
                        mSwipeSwitchListener.beginMenuOpened(this);
                }
                if (mSwipeFractionListener != null) {
                    float fraction = (float) absScrollY / mBeginSwiper.getMenuHeight();
                    fraction = Float.parseFloat(mDecimalFormat.format(fraction));
                    if (fraction != mPreTopMenuFraction) {
                        mSwipeFractionListener.beginMenuSwipeFraction(this, fraction);
                    }
                    mPreTopMenuFraction = fraction;
                }
            } else {
                if (mSwipeSwitchListener != null) {
                    if (absScrollY == 0) mSwipeSwitchListener.endMenuClosed(this);
                    else if (absScrollY == mEndSwiper.getMenuHeight())
                        mSwipeSwitchListener.endMenuOpened(this);
                }
                if (mSwipeFractionListener != null) {
                    float fraction = (float) absScrollY / mEndSwiper.getMenuHeight();
                    fraction = Float.parseFloat(mDecimalFormat.format(fraction));
                    if (fraction != mPreBottomMenuFraction) {
                        mSwipeFractionListener.endMenuSwipeFraction(this, fraction);
                    }
                    mPreBottomMenuFraction = fraction;
                }
            }
        }
        mPreScrollY = getScrollY();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int currY = Math.abs(mScroller.getCurrY());
            if (mCurrentSwiper instanceof BottomVerticalSwiper) {
                scrollTo(0, currY);
                invalidate();
            } else {
                scrollTo(0, -currY);
                invalidate();
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setClickable(true);
        mContentView = findViewById(R.id.smContentView);
        if (mContentView == null) {
            throw new IllegalArgumentException("Not find contentView by id smContentView");
        }
        View menuViewTop = findViewById(R.id.smMenuViewTop);
        View menuViewBottom = findViewById(R.id.smMenuViewBottom);
        if (menuViewTop == null && menuViewBottom == null) {
            throw new IllegalArgumentException("Not find menuView by id (smMenuViewTop, smMenuViewBottom)");
        }
        if (menuViewTop != null) mBeginSwiper = new TopVerticalSwiper(menuViewTop);
        if (menuViewBottom != null) mEndSwiper = new BottomVerticalSwiper(menuViewBottom);
    }

    @Override
    protected boolean isMenuOpened() {
        return (mBeginSwiper != null && mBeginSwiper.isMenuOpened(getScrollY()))
                || (mEndSwiper != null && mEndSwiper.isMenuOpened(getScrollY()));
    }

    @Override
    protected boolean isSwiping() {
        return (mBeginSwiper != null && mBeginSwiper.isSwiping(getScrollY()))
                || (mEndSwiper != null && mEndSwiper.isSwiping(getScrollY()));
    }

    @Override
    protected boolean isMenuOpenedNotEqual() {
        return (mBeginSwiper != null && mBeginSwiper.isMenuOpenedNotEqual(getScrollY()))
                || (mEndSwiper != null && mEndSwiper.isMenuOpenedNotEqual(getScrollY()));
    }

    @Override
    public boolean isNotInPlace() {
        return (mBeginSwiper != null && mBeginSwiper.isNotInPlace(getScrollY()))
                || (mEndSwiper != null && mEndSwiper.isNotInPlace(getScrollY()));
    }

    @Override
    public void smoothOpenMenu(int duration) {
        if (mCurrentSwiper != null) {
            mCurrentSwiper.autoOpenMenu(mScroller, getScrollY(), duration);
            invalidate();
        }
    }

    @Override
    public void smoothCloseMenu(int duration) {
        if (mCurrentSwiper != null) {
            mCurrentSwiper.autoCloseMenu(mScroller, getScrollY(), duration);
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int parentViewHeight = getMeasuredHeightAndState();
        int contentViewWidth = mContentView.getMeasuredWidthAndState();
        int contentViewHeight = mContentView.getMeasuredHeightAndState();
        LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
        int lGap = getPaddingLeft() + lp.leftMargin;
        int tGap = getPaddingTop() + lp.topMargin;
        mContentView.layout(lGap,
                tGap,
                lGap + contentViewWidth,
                tGap + contentViewHeight);

        if (mEndSwiper != null) {
            int menuViewWidth = mEndSwiper.getMenuView().getMeasuredWidthAndState();
            int menuViewHeight = mEndSwiper.getMenuView().getMeasuredHeightAndState();
            lp = (LayoutParams) mEndSwiper.getMenuView().getLayoutParams();
            lGap = getPaddingLeft() + lp.leftMargin;
            mEndSwiper.getMenuView().layout(
                    lGap,
                    parentViewHeight,
                    lGap + menuViewWidth,
                    parentViewHeight + menuViewHeight);
        }
        if (mBeginSwiper != null) {
            int menuViewWidth = mBeginSwiper.getMenuView().getMeasuredWidthAndState();
            int menuViewHeight = mBeginSwiper.getMenuView().getMeasuredHeightAndState();
            lp = (LayoutParams) mBeginSwiper.getMenuView().getLayoutParams();
            lGap = getPaddingLeft() + lp.leftMargin;
            mBeginSwiper.getMenuView().layout(
                    lGap,
                    -menuViewHeight,
                    menuViewWidth,
                    0);
        }
    }

    int getLen() {
        return mCurrentSwiper.getMenuHeight();
    }

    int getMoveLen(MotionEvent ev) {
        int sy = getScrollY();
        return (int) (ev.getY() - sy);
    }

}
