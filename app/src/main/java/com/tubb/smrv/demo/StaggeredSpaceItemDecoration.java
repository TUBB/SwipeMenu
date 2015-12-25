package com.tubb.smrv.demo;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by bingbing.tu
 * 2015/12/25.
 */
public class StaggeredSpaceItemDecoration extends RecyclerView.ItemDecoration{

    private int left;
    private int top;
    private int right;
    private int bottom;

    public StaggeredSpaceItemDecoration(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = left;
        outRect.top = top;
        outRect.right = right;
        outRect.bottom = bottom;
    }

}
