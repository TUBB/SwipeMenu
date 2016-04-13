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
            int interpolatorId = a.getResourceId(R.styleable.SwipeMenu_sml_scroller_interpolator, android.R.anim.linear_interpolator);
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
                if(Math.abs(disX) > mScaledTouchSlop)
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

		int action = ev.getAction();
		switch (action){
			case MotionEvent.ACTION_DOWN:
				mLastX = (int) ev.getX();
                break;
			case MotionEvent.ACTION_MOVE:
                if(!isSwipeEnable()) break;
				int disX = (int) (mLastX - ev.getX());
                if (!mDragging && Math.abs(disX) > mScaledTouchSlop){
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
                    shouldResetSwiper = false;
                }
                break;
			case MotionEvent.ACTION_UP:
                mDragging = false;
                mVelocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) mVelocityTracker.getXVelocity();
                if(Math.abs(velocityX) > mScaledMinimumFlingVelocity){
                    if(mCurrentSwiper != null){
                        int distance = mCurrentSwiper.getMenuWidth() - Math.abs(getScrollX());
                        int duration = distance * Math.abs(velocityX) / 5000;
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
                        invalidate();
                    }
                }else{
                    judgeOpenClose();
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
                    judgeOpenClose();
                }
				break;
		}
        return super.onTouchEvent(ev);
    }

    private void judgeOpenClose(){
        if(mCurrentSwiper != null){
            if(Math.abs(getScrollX()) >= (mCurrentSwiper.getMenuView().getWidth() * mAutoOpenPercent)){ // auto open
                if(isMenuOpen()) smoothCloseMenu();
                else smoothOpenMenu();
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
