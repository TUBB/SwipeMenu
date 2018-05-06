package com.tubb.smrv.swiper;

import android.view.View;
import android.widget.OverScroller;

/**
 * Created by tubingbing on 16/4/11.
 */
public class LeftHorizontalSwiper extends Swiper {

    public LeftHorizontalSwiper(View menuView) {
        super(BEGIN_DIRECTION, menuView);
    }

    @Override
    public boolean isMenuOpened(int scrollX) {
        return scrollX <= -getMenuView().getWidth() * getDirection();
    }

    @Override
    public boolean isMenuOpenedNotEqual(int scrollX) {
        return scrollX < -getMenuView().getWidth() * getDirection();
    }

    @Override
    public void autoOpenMenu(OverScroller scroller, int scrollX, int duration) {
        scroller.startScroll(Math.abs(scrollX), 0, getMenuView().getWidth()-Math.abs(scrollX), 0, duration);
    }

    @Override
    public void autoCloseMenu(OverScroller scroller, int scrollX, int duration) {
        scroller.startScroll(-Math.abs(scrollX), 0, Math.abs(scrollX), 0, duration);
    }

    @Override
    public Checker checkXY(int x, int y) {
        mChecker.x = x;
        mChecker.y = y;
        mChecker.shouldResetSwiper = false;
        if(mChecker.x == 0){
            mChecker.shouldResetSwiper = true;
        }
        if (mChecker.x >= 0){
            mChecker.x = 0;
        }
        if (mChecker.x <= -getMenuView().getWidth()){
            mChecker.x = -getMenuView().getWidth();
        }
        return mChecker;
    }

    @Override
    public boolean isClickOnContentView(View contentView, float x) {
        return x > getMenuView().getWidth();
    }
}
