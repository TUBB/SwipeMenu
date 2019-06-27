package com.tubb.smrv;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

import com.tubb.smrv.listener.SwipeFractionListener;
import com.tubb.smrv.listener.SwipeSwitchListener;
import com.tubb.smrv.swiper.Swiper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public abstract class SwipeMenuLayout extends FrameLayout {

    public static final int DEFAULT_SCROLLER_DURATION = 250;
    public static final float DEFAULT_AUTO_OPEN_PERCENT = 0.5f;
    protected float mAutoOpenPercent = DEFAULT_AUTO_OPEN_PERCENT;
    protected int mScrollerDuration = DEFAULT_SCROLLER_DURATION;

    protected int mScaledTouchSlop;
    protected int mLastX;
    protected int mLastY;
    protected int mDownX;
    protected int mDownY;
    protected View mContentView;
    protected Swiper mBeginSwiper;
    protected Swiper mEndSwiper;
    protected Swiper mCurrentSwiper;
    protected boolean shouldResetSwiper;
    protected boolean mDragging;
    protected boolean swipeEnable = true;
    protected OverScroller mScroller;
    protected Interpolator mInterpolator;
    protected VelocityTracker mVelocityTracker;
    protected int mScaledMinimumFlingVelocity;
    protected int mScaledMaximumFlingVelocity;
    protected SwipeSwitchListener mSwipeSwitchListener;
    protected SwipeFractionListener mSwipeFractionListener;
    protected NumberFormat mDecimalFormat = new DecimalFormat("#.00", new DecimalFormatSymbols(Locale.US));
    protected boolean isRtl = false;

    public SwipeMenuLayout(Context context) {
        this(context, null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeMenu, 0, defStyle);
            int interpolatorId = a.getResourceId(R.styleable.SwipeMenu_sml_scroller_interpolator, -1);
            if (interpolatorId > 0)
                mInterpolator = AnimationUtils.loadInterpolator(getContext(), interpolatorId);
            mAutoOpenPercent = a.getFloat(R.styleable.SwipeMenu_sml_auto_open_percent, DEFAULT_AUTO_OPEN_PERCENT);
            mScrollerDuration = a.getInteger(R.styleable.SwipeMenu_sml_scroller_duration, DEFAULT_SCROLLER_DURATION);
            a.recycle();
        }
        isRtl = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL;
        init();
    }

    protected boolean handleActionUpOfIntercept(float mv) {
        boolean isIntercepted = false;
        // menu view opened and click on content view,
        // we just close the menu view and intercept the up event
        boolean isMenuOpened = isMenuOpened();
        boolean isClickOnContentView = false;
        if (mCurrentSwiper != null) {
            isClickOnContentView = mCurrentSwiper.isClickOnContentView(this, mv);
        }
        if (isMenuOpened && isClickOnContentView) {
            smoothCloseMenu();
            isIntercepted = true;
        }
        return isIntercepted;
    }

    public void smoothOpenBeginMenu() {
        if (mBeginSwiper == null) throw new IllegalArgumentException("No begin menu!");
        mCurrentSwiper = mBeginSwiper;
        smoothOpenMenu();
    }

    public void smoothOpenEndMenu() {
        if (mEndSwiper == null) throw new IllegalArgumentException("No end menu!");
        mCurrentSwiper = mEndSwiper;
        smoothOpenMenu();
    }

    public void smoothCloseBeginMenu() {
        if (mBeginSwiper == null) throw new IllegalArgumentException("No begin menu!");
        mCurrentSwiper = mBeginSwiper;
        smoothCloseMenu();
    }

    public void smoothCloseEndMenu() {
        if (mEndSwiper == null) throw new IllegalArgumentException("No end menu!");
        mCurrentSwiper = mEndSwiper;
        smoothCloseMenu();
    }

    public void openBeginMenuWithoutAnimation() {
        if (mBeginSwiper == null) throw new IllegalArgumentException("No begin menu!");
        mCurrentSwiper = mBeginSwiper;
        smoothOpenMenu(0);
    }

    public void openEndMenuWithoutAnimation() {
        if (mEndSwiper == null) throw new IllegalArgumentException("No end menu!");
        mCurrentSwiper = mEndSwiper;
        smoothOpenMenu(0);
    }

    public void closeBeginMenuWithoutAnimation() {
        if (mBeginSwiper == null) throw new IllegalArgumentException("No begin menu!");
        mCurrentSwiper = mBeginSwiper;
        smoothCloseMenu(0);
    }

    public void closeEndMenuWithoutAnimation() {
        if (mEndSwiper == null) throw new IllegalArgumentException("No end menu!");
        mCurrentSwiper = mEndSwiper;
        smoothCloseMenu(0);
    }

    public abstract void smoothOpenMenu(int duration);

    public void smoothOpenMenu() {
        smoothOpenMenu(mScrollerDuration);
    }

    public abstract void smoothCloseMenu(int duration);

    public void smoothCloseMenu() {
        smoothCloseMenu(mScrollerDuration);
    }

    protected abstract boolean isMenuOpened();

    protected abstract boolean isSwiping();

    protected abstract boolean isMenuOpenedNotEqual();

    /**
     * Not in the place, the swipe menu is swiping
     * @return in the place or not
     */
    public abstract boolean isNotInPlace();

    public void init() {
        ViewConfiguration mViewConfig = ViewConfiguration.get(getContext());
        mScaledTouchSlop = mViewConfig.getScaledTouchSlop();
        mScroller = new OverScroller(getContext(), mInterpolator);
        mScaledMinimumFlingVelocity = mViewConfig.getScaledMinimumFlingVelocity();
        mScaledMaximumFlingVelocity = mViewConfig.getScaledMaximumFlingVelocity();
    }

    public void setSwipeEnable(boolean swipeEnable) {
        this.swipeEnable = swipeEnable;
    }

    public boolean isSwipeEnable() {
        return swipeEnable;
    }

    public void setSwipeListener(SwipeSwitchListener swipeSwitchListener) {
        mSwipeSwitchListener = swipeSwitchListener;
    }

    public void setSwipeFractionListener(SwipeFractionListener swipeFractionListener) {
        mSwipeFractionListener = swipeFractionListener;
    }

    abstract int getMoveLen(MotionEvent event);

    abstract int getLen();

    /**
     * compute finish duration
     *
     * @param ev       up event
     * @param velocity velocity
     * @return finish duration
     */
    int getSwipeDuration(MotionEvent ev, int velocity) {
        int moveLen = getMoveLen(ev);
        final int len = getLen();
        final int halfLen = len / 2;
        final float distanceRatio = Math.min(1f, 1.0f * Math.abs(moveLen) / len);
        final float distance = halfLen + halfLen *
                distanceInfluenceForSnapDuration(distanceRatio);
        int duration;
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        } else {
            final float pageDelta = (float) Math.abs(moveLen) / len;
            duration = (int) ((pageDelta + 1) * 100);
        }
        duration = Math.min(duration, mScrollerDuration);
        return duration;
    }

    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * Math.PI / 2.0f;
        return (float) Math.sin(f);
    }
}
