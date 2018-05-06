package com.tubb.smrv.swiper;

import android.view.View;
import android.widget.OverScroller;

/**
 * Created by tubingbing on 16/4/11.
 */
public class BottomVerticalSwiper extends Swiper {

    public BottomVerticalSwiper(View menuView) {
        super(END_DIRECTION, menuView);
    }

    @Override
    public boolean isMenuOpened(int scrollY) {
        return scrollY >= -getMenuView().getHeight() * getDirection();
    }

    @Override
    public boolean isMenuOpenedNotEqual(int scrollY) {
        return scrollY > -getMenuView().getHeight() * getDirection();
    }

    @Override
    public void autoOpenMenu(OverScroller scroller, int scrollY, int duration) {
        scroller.startScroll(0, Math.abs(scrollY), 0, getMenuView().getHeight()-Math.abs(scrollY), duration);
    }

    @Override
    public void autoCloseMenu(OverScroller scroller, int scrollY, int duration) {
        scroller.startScroll(0, -Math.abs(scrollY), 0, Math.abs(scrollY), duration);
    }

    @Override
    public Checker checkXY(int x, int y) {
        mChecker.x = x;
        mChecker.y = y;
        mChecker.shouldResetSwiper = false;
        if(mChecker.y == 0){
            mChecker.shouldResetSwiper = true;
        }
        if (mChecker.y < 0){
            mChecker.y = 0;
        }
        if (mChecker.y > getMenuView().getHeight()){
            mChecker.y = getMenuView().getHeight();
        }
        return mChecker;
    }

    @Override
    public boolean isClickOnContentView(View contentView, float y) {
        return y < (contentView.getHeight() - getMenuView().getHeight());
    }
}
