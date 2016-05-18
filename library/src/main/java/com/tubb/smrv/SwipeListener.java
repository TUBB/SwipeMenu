package com.tubb.smrv;

/**
 * Created by tubingbing on 16/5/18.
 */
public interface SwipeListener {

    void leftMenuClosed();
    void leftMenuOpened();
    void rightMenuClosed();
    void rightMenuOpened();
    void leftMenuSwipeFraction(float fraction);
    void rightMenuSwipeFraction(float fraction);

}
