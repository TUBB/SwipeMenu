package com.tubb.smrv;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;

public class SwipeMenuRecyclerView extends RecyclerView {

	public static final int TOUCH_STATE_NONE = 0;
    public static final int TOUCH_STATE_X = 1;
    public static final int TOUCH_STATE_Y = 2;

    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = -1;
    protected int mDirection = DIRECTION_LEFT; // swipe from right to left by default

    protected float mDownX;
    protected float mDownY;
    protected int mTouchState;
    protected int mTouchPosition;
    protected SwipeMenuLayout mTouchView;
    protected OnSwipeListener mOnSwipeListener;

    protected Interpolator mCloseInterpolator;
    protected Interpolator mOpenInterpolator;

    protected LayoutManager mLlm;
    protected ViewConfiguration mViewConfiguration;
    protected long startClickTime;
    protected float dx;
    protected float dy;

    public SwipeMenuRecyclerView(Context context) {
		this(context, null);
	}

	public SwipeMenuRecyclerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

    public SwipeMenuRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
		mTouchState = TOUCH_STATE_NONE;
		mViewConfiguration = ViewConfiguration.get(getContext());
	}

	public void setCloseInterpolator(Interpolator interpolator) {
		mCloseInterpolator = interpolator;
	}

	public void setOpenInterpolator(Interpolator interpolator) {
		mOpenInterpolator = interpolator;
	}

	public Interpolator getOpenInterpolator() {
		return mOpenInterpolator;
	}

	public Interpolator getCloseInterpolator() {
		return mCloseInterpolator;
	}

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null){
            return super.onInterceptTouchEvent(ev);
        }

        int action = ev.getAction();
        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                dx = 0.0f; // reset
                dy = 0.0f; // reset
                startClickTime = System.currentTimeMillis(); // reset
                int oldPos = mTouchPosition;
                mDownX = ev.getX();
                mDownY = ev.getY();
                mTouchState = TOUCH_STATE_NONE;
                mTouchPosition = getChildAdapterPosition(findChildViewUnder((int) ev.getX(), (int) ev.getY()));
                if (mTouchPosition == oldPos && mTouchView != null
                        && mTouchView.isOpen()) {
                    mTouchState = TOUCH_STATE_X;
                    mTouchView.onSwipe(ev);
                }
                // find the touched child view
                View view = null;
                ViewHolder vh = findViewHolderForAdapterPosition(mTouchPosition);
                if(vh != null){
                    view = vh.itemView;
                }
                // is not touched the opened menu view, so we intercept this touch event
                if (mTouchPosition != oldPos && mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                    mTouchView = null;
                    // try to cancel the touch event
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(cancelEvent);
                    return true;
                }
                if (view instanceof SwipeMenuLayout) {
                    mTouchView = (SwipeMenuLayout) view;
                    mTouchView.setSwipeDirection(mDirection);
                }
                if (mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                dy = Math.abs((ev.getY() - mDownY));
                dx = Math.abs((ev.getX() - mDownX));
                if (mTouchState == TOUCH_STATE_X && mTouchView.isSwipeEnable()) {
                    mTouchView.onSwipe(ev);
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                } else if (mTouchState == TOUCH_STATE_NONE && mTouchView.isSwipeEnable()) {
                    if (Math.abs(dy) > mViewConfiguration.getScaledTouchSlop()) {
                        mTouchState = TOUCH_STATE_Y;
                    } else if (dx > mViewConfiguration.getScaledTouchSlop()) {
                        mTouchState = TOUCH_STATE_X;
                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onSwipeStart(mTouchPosition);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                boolean isCloseOnUpEvent = false;
                if (mTouchState == TOUCH_STATE_X && mTouchView.isSwipeEnable()) {
                    isCloseOnUpEvent = !mTouchView.onSwipe(ev);
                    if (mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipeEnd(mTouchPosition);
                    }
                    if (!mTouchView.isOpen()) {
                        mTouchPosition = -1;
                        mTouchView = null;
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                }
                long clickDuration = System.currentTimeMillis() - startClickTime;
                boolean isOutDuration = clickDuration > ViewConfiguration.getLongPressTimeout();
                boolean isOutX = dx > mViewConfiguration.getScaledTouchSlop();
                boolean isOutY = dy > mViewConfiguration.getScaledTouchSlop();
                // long pressed or scaled touch, we just intercept up touch event
                if(isOutDuration || isOutX || isOutY){
                    return true;
                }else{
                    float eX = ev.getX();
                    float eY = ev.getY();
                    View upView = findChildViewUnder(eX, eY);
                    if(upView instanceof SwipeMenuLayout){
                        SwipeMenuLayout smView = (SwipeMenuLayout)upView;
                        int x = (int)eX - smView.getLeft();
                        int y = (int)eY - smView.getTop();
                        View menuView = smView.getMenuView();
                        final float translationX = ViewCompat.getTranslationX(menuView);
                        final float translationY = ViewCompat.getTranslationY(menuView);
                        // intercept the up event when touched on the contentView of the opened SwipeMenuLayout
                        if (!(x >= menuView.getLeft() + translationX &&
                                x <= menuView.getRight() + translationX &&
                                y >= menuView.getTop() + translationY &&
                                y <= menuView.getBottom() + translationY) &&
                                isCloseOnUpEvent) {
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if(mTouchView != null && mTouchView.isSwipeEnable()){
                    // when event has canceled, we just consider as up event
                    ev.setAction(MotionEvent.ACTION_UP);
                    mTouchView.onSwipe(ev);
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * open menu manually
     * @param position the adapter position
     */
	public void smoothOpenMenu(int position) {
        View view = mLlm.findViewByPosition(position);
        if (view instanceof SwipeMenuLayout) {
            mTouchPosition = position;
            // close pre opened swipe menu
            if (mTouchView != null && mTouchView.isOpen()) {
                mTouchView.smoothCloseMenu();
            }
            mTouchView = (SwipeMenuLayout) view;
            mTouchView.setSwipeDirection(mDirection);
            mTouchView.smoothOpenMenu();
        }
	}

    /**
     * close the opened menu manually
     */
    public void smoothCloseMenu() {
        // close the opened swipe menu
        if (mTouchView != null && mTouchView.isOpen()) {
            mTouchView.smoothCloseMenu();
        }
    }

	public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
		this.mOnSwipeListener = onSwipeListener;
	}

    /**
     * get current touched view
     * @return touched view, maybe null
     */
    public SwipeMenuLayout getTouchView() {
        return mTouchView;
    }

	public interface OnSwipeListener {
		void onSwipeStart(int position);
		void onSwipeEnd(int position);
	}

    /**
     * set the swipe direction
     * @param direction swipe direction (left or right)
     */
	public void setSwipeDirection(int direction) {
		mDirection = direction;
	}

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        mLlm = layout;
    }

}
