package com.tubb.smrv.swiper;

import android.view.View;
import android.widget.OverScroller;

/**
 * Created by tubingbing on 16/4/11.
 */
public abstract class Swiper {

    protected static final int BEGIN_DIRECTION = 1;
    protected static final int END_DIRECTION = -1;

    private int direction;
    private View menuView;
    protected Checker mChecker;

    public Swiper(int direction, View menuView){
        this.direction = direction;
        this.menuView = menuView;
        mChecker = new Checker();
    }

    public boolean isSwiping(final int scrollDis) {
        int absScrollDis = Math.abs(scrollDis);
        return absScrollDis > 0 && absScrollDis < getMenuView().getWidth();
    }
    public abstract boolean isMenuOpened(final int scrollDis);
    public abstract boolean isMenuOpenedNotEqual(final int scrollDis);
    public abstract void autoOpenMenu(OverScroller scroller, int scrollDis, int duration);
    public abstract void autoCloseMenu(OverScroller scroller, int scrollDis, int duration);
    public abstract Checker checkXY(int x, int y);
    public abstract boolean isClickOnContentView(View contentView, float clickPoint);

    public boolean isNotInPlace(final int scrollDis) {
        return scrollDis != 0;
    }

    public int getDirection() {
        return direction;
    }

    public View getMenuView() {
        return menuView;
    }

    public int getMenuWidth(){
        return getMenuView().getWidth();
    }

    public int getMenuHeight(){
        return getMenuView().getHeight();
    }

    public static final class Checker{
        public int x;
        public int y;
        public boolean shouldResetSwiper;
    }

}
