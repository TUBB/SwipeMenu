package com.tubb.smrv.demo;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by bingbing.tu
 * 2015/12/25.
 */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration{

    private final int mVerticalSpaceHeight;
    private final int mHorizontalSpaceHeight;

    public GridSpaceItemDecoration(int verticalSpaceHeight, int horizontalSpaceHeight) {
        this.mVerticalSpaceHeight = verticalSpaceHeight;
        this.mHorizontalSpaceHeight = horizontalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) % 2 == 1) outRect.left = mHorizontalSpaceHeight;
        outRect.bottom = mVerticalSpaceHeight;
    }

}
