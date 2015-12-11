package com.tubb.smrv;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;

public class SwipeMenuRecyclerView extends RecyclerView {

	private static final int TOUCH_STATE_NONE = 0;
	private static final int TOUCH_STATE_X = 1;
	private static final int TOUCH_STATE_Y = 2;

	public static final int DIRECTION_LEFT = 1;
	public static final int DIRECTION_RIGHT = -1;
	private int mDirection = DIRECTION_LEFT;//swipe from right to left by default

	private float mDownX;
	private float mDownY;
	private int mTouchState;
	private int mTouchPosition;
	private SwipeMenuLayout mTouchView;
	private OnSwipeListener mOnSwipeListener;

	private SwipeMenuCreator mMenuCreator;
	private OnMenuItemClickListener mOnMenuItemClickListener;
	private Interpolator mCloseInterpolator;
	private Interpolator mOpenInterpolator;

	private LinearLayoutManager mLlm;
    private ViewConfiguration mViewConfiguration;
    private long startClickTime;
    private float dx;
    private float dy;

    public SwipeMenuRecyclerView(Context context) {
		super(context);
		init();
	}

	public SwipeMenuRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SwipeMenuRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mTouchState = TOUCH_STATE_NONE;
		mViewConfiguration = ViewConfiguration.get(getContext());
	}

	@Override
	public void setAdapter(RecyclerView.Adapter adapter) {
        if (!(adapter instanceof SwipeMenuRecyclerViewAdapter))
            throw new IllegalArgumentException("Your adapter must extends SwipeMenuRecyclerViewAdapter");
        super.setAdapter(adapter);
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
//                Log.e("XX", "down event");
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
                View view = getChildAt(mTouchPosition - mLlm.findFirstVisibleItemPosition());
                // is not touched the opened menu view, so we intercept this touch event
                if (mTouchPosition != oldPos && mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                    mTouchView = null;
                    // try to cancel the touch event
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(cancelEvent);
//                    Log.e("XX", "intercept down event");
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
//                Log.e("XX", "up evnet");
                if (mTouchState == TOUCH_STATE_X && mTouchView.isSwipeEnable()) {
                    isCloseOnUpEvent = !mTouchView.onSwipe(ev);
                    if (!mTouchView.isOpen()) {
                        mTouchPosition = -1;
                        mTouchView = null;
                    }
                    if (mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipeEnd(mTouchPosition);
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                }
                long clickDuration = System.currentTimeMillis() - startClickTime;
                boolean isOutDuration = clickDuration > ViewConfiguration.getLongPressTimeout();
                boolean isOutX = dx > mViewConfiguration.getScaledTouchSlop();
                boolean isOutY = dy > mViewConfiguration.getScaledTouchSlop();
//                Log.e("XX", "isOutDuration:"+isOutDuration+" isOutX:"+isOutX+" isOutY:"+isOutY);
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
                        SwipeMenuView menuView = smView.getMenuView();
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

    public void smoothCloseMenu(int position) {
        View view = mLlm.findViewByPosition(position);
        if (view instanceof SwipeMenuLayout) {
            // close the opened swipe menu
            if (mTouchView != null && mTouchView.isOpen()) {
                mTouchView.smoothCloseMenu();
            }
        }
    }


    public void setMenuCreator(SwipeMenuCreator menuCreator) {
		this.mMenuCreator = menuCreator;
	}

	public SwipeMenuCreator getMenuCreator() {
		return mMenuCreator;
	}

	public void setOnMenuItemClickListener(
			OnMenuItemClickListener onMenuItemClickListener) {
		this.mOnMenuItemClickListener = onMenuItemClickListener;
	}

	public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
		this.mOnSwipeListener = onSwipeListener;
	}

    public SwipeMenuLayout getTouchView() {
        return mTouchView;
    }

	public OnMenuItemClickListener getOnMenuItemClickListener() {
		return mOnMenuItemClickListener;
	}

	public interface OnMenuItemClickListener {
		boolean onMenuItemClick(int position, SwipeMenu menu, int index);
	}

	public interface OnSwipeListener {
		void onSwipeStart(int position);

		void onSwipeEnd(int position);
	}

	public void setSwipeDirection(int direction) {
		mDirection = direction;
	}

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if(!(layout instanceof LinearLayoutManager)){
            throw new IllegalArgumentException("not support " + layout.getClass().getSimpleName() + " yet.");
        }
        mLlm = (LinearLayoutManager)layout;
    }
}
