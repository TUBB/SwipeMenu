SwipeMenuRecyclerView
=================
A swipe menu for RecyclerView, low coupling, can fast rapid integration into your project

DEMO
====
![DEMO](https://github.com/TUBB/SwipeMenuRecyclerView/blob/master/art/demo.gif)

Usage
======
Add to dependencies
-------------------
```
dependencies {
    compile 'com.tubb.smrv:swipemenu-recyclerview:3.0.5'
}
```

Step 1
------
Use SwipeMenuLayout, we use SwipeMenuLayout ViewGroup to combine item content view and swipe menu
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.tubb.smrv.SwipeMenuLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <!-- item content view, the id must be (smContentView) -->
    <include android:id="@id/smContentView" layout="@layout/item_simple_content"/>
    <!-- item swipe menu, the id must be (smMenuView) -->
    <include android:id="@id/smMenuView" layout="@layout/item_simple_menu"/>
</com.tubb.smrv.SwipeMenuLayout>
```

Step 2
------
Just use our custom RecyclerView
```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.v4.widget.SwipeRefreshLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="${relativePackage}.${activityClass}"
    android:id="@+id/swipeRefreshLayout">

    <com.tubb.smrv.SwipeMenuRecyclerView
        android:id="@+id/listView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</android.support.v4.widget.SwipeRefreshLayout>
```
More details please see the demo project.

Custom
======
You can on or off swipe feature, and set open/close Interpolator in onBindViewHolder(RecyclerView.ViewHolder vh, int position) method
```java
itemView.setSwipeEnable(swipeEnable);
itemView.setOpenInterpolator(mRecyclerView.getOpenInterpolator());
itemView.setCloseInterpolator(mRecyclerView.getCloseInterpolator());
```
We add anim_duration attr to custom swipe animation duration, default is 500ms
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.tubb.smrv.SwipeMenuLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:swipemenu="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    swipemenu:anim_duration="300">

</com.tubb.smrv.SwipeMenuLayout>
```

Features
=======
    Support LinearLayoutManager、GridLayoutManager and StaggeredGridLayoutManager for RecyclerView
    On-off swipe ability
    Not intercept item touch event
    Good expansibility, we only override onInterceptTouchEvent(MotionEvent ev) method of RecyclerView