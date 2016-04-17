package com.tubb.smrv;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.OverScroller;

public class SwipeMenuLayout extends FrameLayout {

    private static final String TAG = "sml";
    private static final int DEFAULT_SCROLLER_DURATION = 300;
    private static final float DEFAULT_AUTO_OPEN_PERCENT = 0.5f;
    private float mAutoOpenPercent = DEFAULT_AUTO_OPEN_PERCENT;
    private int mScrollerDuration = DEFAULT_SCROLLER_DURATION;

    private int mScaledTouchSlop;
    private int mLastX;
    private int mLastY;
    private int mDownX;
    private int mDownY;
	private View mContentView;
    private HorizontalSwiper mLeftSwiper;
    private HorizontalSwiper mRightSwiper;
    private HorizontalSwiper mCurrentSwiper;
    private boolean shouldResetSwiper;
    private boolean mDragging;
    private boolean swipeEnable = true;
    private OverScroller mScroller;
    private Interpolator mInterpolator;
    private VelocityTracker mVelocityTracker;
    private int mScaledMinimumFlingVelocity;
    private int mScaledMaximumFlingVelocity;


    public SwipeMenuLayout(Context context) {
		this(context, null);
	}

	public SwipeMenuLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(!isInEditMode()){
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeMenu, 0, defStyle);
            int interpolatorId = a.getResourceId(R.styleable.SwipeMenu_sml_scroller_interpolator, -1);
            if(interpolatorId > 0)
                mInterpolator = AnimationUtils.loadInterpolator(getContext(), interpolatorId);
			mAutoOpenPercent = a.getFloat(R.styleable.SwipeMenu_sml_auto_open_percent, DEFAULT_AUTO_OPEN_PERCENT);
            mScrollerDuration = a.getInteger(R.styleable.SwipeMenu_sml_scroller_duration, DEFAULT_SCROLLER_DURATION);
            a.recycle();
		}
        init();
	}

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = super.onInterceptTouchEvent(ev);
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mDownX = mLastX = (int) ev.getX();
                mDownY = (int) ev.getY();
                isIntercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int disX = (int) (ev.getX() - mDownX);
                int disY = (int) (ev.getY() - mDownY);
                if(Math.abs(disX) > mScaledTouchSlop
                        && Math.abs(disX) > Math.abs(disY))
                    isIntercepted = true;
                else
                    isIntercepted = false;
                break;
            case MotionEvent.ACTION_UP:
                isIntercepted = false;
                // menu view opened and click on content view,
                // we just close the menu view and intercept the up event
                if(isMenuOpen()
                        && mCurrentSwiper.isClickOnContentView(getWidth(), ev.getX())){
                    smoothCloseMenu();
                    isIntercepted = true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                isIntercepted = false;
                if(!mScroller.isFinished())
                    mScroller.abortAnimation();
                break;
        }
        return isIntercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mVelocityTracker == null) mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(ev);
        int dx = 0;
        int dy = 0;
		int action = ev.getAction();
		switch (action){
			case MotionEvent.ACTION_DOWN:
				mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                break;
			case MotionEvent.ACTION_MOVE:
                if(!isSwipeEnable()) break;
				int disX = (int) (mLastX - ev.getX());
                int disY = (int) (mLastY - ev.getY());
                if (!mDragging
                        && Math.abs(disX) > mScaledTouchSlop
                        && Math.abs(disX) > Math.abs(disY)){
                    mDragging = true;
                }
                if (mDragging) {
                    if(mCurrentSwiper == null || shouldResetSwiper){
                        if(disX < 0){
                            if(mLeftSwiper != null)
                                mCurrentSwiper = mLeftSwiper;
                            else
                                mCurrentSwiper = mRightSwiper;
                        }else{
                            if(mRightSwiper != null)
                                mCurrentSwiper = mRightSwiper;
                            else
                                mCurrentSwiper = mLeftSwiper;
                        }
                    }
                    scrollBy(disX, 0);
                    mLastX = (int) ev.getX();
                    mLastY = (int) ev.getY();
                    shouldResetSwiper = false;
                }
                break;
			case MotionEvent.ACTION_UP:
                dx = (int) (mDownX - ev.getX());
                dy = (int) (mDownY - ev.getY());
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
                int velocityX = (int) mVelocityTracker.getXVelocity();
                int velocity = Math.abs(velocityX);
                if(velocity > mScaledMinimumFlingVelocity){
                    if(mCurrentSwiper != null){
                        int duration = getSwipeDuration(ev, velocity);
                        Log.e(TAG, "duration:"+duration);
                        if(mCurrentSwiper instanceof RightHorizontalSwiper){
                            if(velocityX < 0){ // just open
                                smoothOpenMenu(duration);
                            }else{ // just close
                                smoothCloseMenu(duration);
                            }
                        }else{
                            if(velocityX > 0){ // just open
                                smoothOpenMenu(duration);
                            }else{ // just close
                                smoothCloseMenu(duration);
                            }
                        }
                        ViewCompat.postInvalidateOnAnimation(this);                    }
                }else{
                    judgeOpenClose(dx, dy);
                }
                mVelocityTracker.clear();
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                if(Math.abs(mDownX - ev.getX()) > mScaledTouchSlop
                        || Math.abs(mDownY - ev.getY()) > mScaledTouchSlop
                        || isMenuOpen()){ // ignore click listener
                    return true;
                }
                break;
			case MotionEvent.ACTION_CANCEL:
                mDragging = false;
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }else{
                    dx = (int) (mDownX - ev.getX());
                    dy = (int) (mDownY - ev.getY());
                    judgeOpenClose(dx, dy);
                }
				break;
		}
        return super.onTouchEvent(ev);
    }

    /**
     * compute finish duration
     * @param ev up event
     * @param velocity velocity x
     * @return finish duration
     */
    private int getSwipeDuration(MotionEvent ev, int velocity) {
        int sx = getScrollX();
        int dx = (int) (ev.getX() - sx);
        final int width = mCurrentSwiper.getMenuWidth();
        final int halfWidth = width / 2;
        final float distanceRatio = Math.min(1f, 1.0f * Math.abs(dx) / width);
        final float distance = halfWidth + halfWidth *
                distanceInfluenceForSnapDuration(distanceRatio);
        int duration = 0;
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        } else {
            final float pageDelta = (float) Math.abs(dx) / width;
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

    private void judgeOpenClose(int dx, int dy){
        if(mCurrentSwiper != null){
            Log.e(TAG, "judge open or close:"+getScrollX());
            if(Math.abs(getScrollX()) >= (mCurrentSwiper.getMenuView().getWidth() * mAutoOpenPercent)){ // auto open
                if(Math.abs(dx) > mScaledTouchSlop || Math.abs(dy) > mScaledTouchSlop){
                    if(isMenuOpenNotEqual()) smoothCloseMenu();
                    else smoothOpenMenu();
                }else{
                    if(isMenuOpen()) smoothCloseMenu();
                    else smoothOpenMenu();
                }
            }else{ // auto close
                smoothCloseMenu();
            }
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        HorizontalSwiper.Checker checker = mCurrentSwiper.checkXY(x, y);
        shouldResetSwiper = checker.shouldResetSwiper;
        if (checker.x != getScrollX()){
            super.scrollTo(checker.x, checker.y);
        }
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            if(mCurrentSwiper instanceof RightHorizontalSwiper){
                scrollTo(Math.abs(mScroller.getCurrX()), 0);
                invalidate();
            }else{
                scrollTo(-Math.abs(mScroller.getCurrX()), 0);
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
		View menuViewLeft = findViewById(R.id.smMenuViewLeft);
        View menuViewRight = findViewById(R.id.smMenuViewRight);
		if (menuViewLeft== null && menuViewRight == null) {
			throw new IllegalArgumentException("Not find menuView by id (smMenuViewLeft, smMenuViewRight)");
		}
        if(menuViewLeft != null) mLeftSwiper = new LeftHorizontalSwiper(menuViewLeft);
        if(menuViewRight != null) mRightSwiper = new RightHorizontalSwiper(menuViewRight);
	}

    public boolean isMenuOpen() {
		return (mLeftSwiper != null && mLeftSwiper.isMenuOpen(getScrollX()))
                || (mRightSwiper != null && mRightSwiper.isMenuOpen(getScrollX()));
	}

    public boolean isMenuOpenNotEqual() {
        return (mLeftSwiper != null && mLeftSwiper.isMenuOpenNotEqual(getScrollX()))
                || (mRightSwiper != null && mRightSwiper.isMenuOpenNotEqual(getScrollX()));
    }

    public void smoothOpenLeftMenu(){
        if(mLeftSwiper == null) throw new IllegalArgumentException("Not have left menu!");
        mCurrentSwiper = mLeftSwiper;
        smoothOpenMenu();
    }

    public void smoothOpenRightMenu(){
        if(mRightSwiper == null) throw new IllegalArgumentException("Not have right menu!");
        mCurrentSwiper = mRightSwiper;
        smoothOpenMenu();
    }

    public void smoothCloseLeftMenu(){
        if(mLeftSwiper == null) throw new IllegalArgumentException("Not have left menu!");
        mCurrentSwiper = mLeftSwiper;
        smoothCloseMenu();
    }

    public void smoothCloseRightMenu(){
        if(mRightSwiper == null) throw new IllegalArgumentException("Not have right menu!");
        mCurrentSwiper = mRightSwiper;
        smoothCloseMenu();
    }

    public void smoothOpenMenu(int duration) {
        mCurrentSwiper.autoOpenMenu(mScroller, getScrollX(), duration);
        invalidate();
    }

    public void smoothOpenMenu() {
        smoothOpenMenu(mScrollerDuration);
    }

    public void smoothCloseMenu(int duration) {
        mCurrentSwiper.autoCloseMenu(mScroller, getScrollX(), duration);
        invalidate();
    }

	public void smoothCloseMenu() {
        smoothCloseMenu(mScrollerDuration);
	}

    public void init() {
        ViewConfiguration mViewConfig = ViewConfiguration.get(getContext());
        mScaledTouchSlop = mViewConfig.getScaledTouchSlop();
        mScroller = new OverScroller(getContext(), mInterpolator);
        mScaledMinimumFlingVelocity = mViewConfig.getScaledMinimumFlingVelocity();
        mScaledMaximumFlingVelocity = mViewConfig.getScaledMaximumFlingVelocity();
    }

    @Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int parentViewWidth = ViewCompat.getMeasuredWidthAndState(this);
		int contentViewWidth = ViewCompat.getMeasuredWidthAndState(mContentView);
		int contentViewHeight = ViewCompat.getMeasuredHeightAndState(mContentView);
		LayoutParams lp = (LayoutParams) mContentView.getLayoutParams();
		int lGap = getPaddingLeft() + lp.leftMargin;
		int tGap = getPaddingTop() + lp.topMargin;
		mContentView.layout(lGap,
				tGap,
				lGap + contentViewWidth,
				tGap + contentViewHeight);
        if(mRightSwiper != null){
            int menuViewWidth = ViewCompat.getMeasuredWidthAndState(mRightSwiper.getMenuView());
            int menuViewHeight = ViewCompat.getMeasuredHeightAndState(mRightSwiper.getMenuView());
            lp = (LayoutParams) mRightSwiper.getMenuView().getLayoutParams();
            tGap = getPaddingTop() + lp.topMargin;
            mRightSwiper.getMenuView().layout(parentViewWidth,
                    tGap,
                    parentViewWidth + menuViewWidth,
                    tGap + menuViewHeight);
        }
        if(mLeftSwiper != null){
            int menuViewWidth = ViewCompat.getMeasuredWidthAndState(mLeftSwiper.getMenuView());
            int menuViewHeight = ViewCompat.getMeasuredHeightAndState(mLeftSwiper.getMenuView());
            lp = (LayoutParams) mLeftSwiper.getMenuView().getLayoutParams();
            tGap = getPaddingTop() + lp.topMargin;
            mLeftSwiper.getMenuView().layout(-menuViewWidth,
                    tGap,
                    0,
                    tGap + menuViewHeight);
        }
	}

    public void setSwipeEnable(boolean swipeEnable) {
        this.swipeEnable = swipeEnable;
    }

    public boolean isSwipeEnable() {
        return swipeEnable;
    }
}
