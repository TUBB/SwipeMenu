package com.tubb.smrv;

import android.view.View;
import android.widget.OverScroller;

/**
 * Created by tubingbing on 16/4/11.
 */
public abstract class HorizontalSwiper {

    protected static final int LEFT_DIRECTION = 1;
    protected static final int RIGHT_DIRECTION = -1;

    private int direction;
    private View menuView;
    protected Checker mChecker;

    public HorizontalSwiper(int direction, View menuView){
        this.direction = direction;
        this.menuView = menuView;
        mChecker = new Checker();
    }

    public abstract boolean isMenuOpen(final int scrollX);
    public abstract boolean isMenuOpenNotEqual(final int scrollX);
    public abstract void autoOpenMenu(OverScroller scroller, int scrollX, int duration);
    public abstract void autoCloseMenu(OverScroller scroller, int scrollX, int duration);
    public abstract Checker checkXY(int x, int y);
    public abstract boolean isClickOnContentView(int contentViewWidth, float x);

    public int getDirection() {
        return direction;
    }

    public View getMenuView() {
        return menuView;
    }

    public int getMenuWidth(){
        return getMenuView().getWidth();
    }

    public static final class Checker{
        public int x;
        public int y;
        public boolean shouldResetSwiper;
    }

}
