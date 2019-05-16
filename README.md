SwipeMenu
=========
A swipe menu for `horizontal/vertical`, support `left/right and top/bottom` directions, low coupling, can fast rapid integration into your project

Features
======== 
 
 * Support LinearLayoutManager、GridLayoutManager and StaggeredGridLayoutManager for RecyclerView (`only horizontal`)
 * Support ListView and GridView (`only horizontal`)
 * Support ScrollView (`only horizontal` and the `[SwipeHorizontal/SwipeVertical]MenuLayout` must be the direct child of ScrollView's direct child )
 * On-off swipe ability
 * Not intercept item touch event
 * Left/Right and Top/Bottom menu support, free switch

Preview
=======
![DEMO](https://github.com/TUBB/SwipeMenu/blob/master/art/demo.gif)

Usage
=====

Add to dependencies
```
dependencies {
    compile 'com.tubb.smrv:swipemenu-recyclerview:5.4.8'
}
```

#### Horizontal
Just use `SwipeHorizontalMenuLayout`, we use `SwipeHorizontalMenuLayout` ViewGroup to combine item content view and `left/right(at least one)` swipe menu
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.tubb.smrv.SwipeHorizontalMenuLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:sml="http://schemas.android.com/apk/res-auto"
    android:id="@+id/sml"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    sml:sml_scroller_interpolator="@android:anim/bounce_interpolator"
    sml:sml_auto_open_percent="0.2"
    sml:sml_scroller_duration="250">

    <include android:id="@id/smContentView" layout="@layout/item_simple_content"/>
    <include android:id="@id/smMenuViewLeft" layout="@layout/item_simple_left_menu"/>
    <include android:id="@id/smMenuViewRight" layout="@layout/item_simple_right_menu"/>

</com.tubb.smrv.SwipeHorizontalMenuLayout>
```

If you have so many items, you may be want to use our custom RecyclerView
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

### Vertical
Just use `SwipeVerticalMenuLayout`, we use `SwipeVerticalMenuLayout` ViewGroup to combine item content view and `top/bottom (at least one)` swipe menu
```xml
<com.tubb.smrv.SwipeVerticalMenuLayout
    android:id="@+id/sml"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    sml:sml_scroller_interpolator="@android:anim/bounce_interpolator">

    <LinearLayout
        android:id="@id/smMenuViewTop"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        android:orientation="horizontal"
        android:clickable="true"
        android:background="@android:color/holo_green_light"
        android:gravity="center_horizontal">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="top"
            android:textColor="@color/white"
            android:layout_gravity="top"/>

    </LinearLayout>

    <RelativeLayout
        android:id="@id/smContentView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/red">

        <Button
            android:id="@+id/btLeft"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Top"
            android:layout_centerHorizontal="true"
            />

        <Button
            android:id="@+id/btRight"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Bottom"
            android:layout_centerHorizontal="true"
            android:layout_alignParentBottom="true"/>

    </RelativeLayout>

    <LinearLayout
        android:id="@id/smMenuViewBottom"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        android:orientation="horizontal"
        android:clickable="true"
        android:background="@android:color/holo_blue_light"
        android:gravity="center_horizontal">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="bottom"
            android:textColor="@color/white"
            android:layout_gravity="bottom"/>

    </LinearLayout>
</com.tubb.smrv.SwipeVerticalMenuLayout>
```

More details please see the demo project

## Listeners
We add a [SwipeSwitchListener][2] for actions like open/close
```java
sml.setSwipeListener(new SwipeSwitchListener() {
    @Override
    public void beginMenuClosed(SwipeMenuLayout swipeMenuLayout) {
        Log.e(TAG, "left menu closed");
    }

    @Override
    public void beginMenuOpened(SwipeMenuLayout swipeMenuLayout) {
        Log.e(TAG, "left menu opened");
    }

    @Override
    public void endMenuClosed(SwipeMenuLayout swipeMenuLayout) {
        Log.e(TAG, "right menu closed");
    }

    @Override
    public void endMenuOpened(SwipeMenuLayout swipeMenuLayout) {
        Log.e(TAG, "right menu opened");
    }
});
```

If you case a few actions, just use [SimpleSwipeSwitchListener][3]
```java
sml.setSwipeListener(new SimpleSwipeSwitchListener(){
    @Override
    public void beginMenuClosed(SwipeMenuLayout swipeMenuLayout) {
        Log.e(TAG, "left menu closed");
    }
});
```

We also add a [SwipeFractionListener][4] for complete fraction action
```java
sml.setSwipeFractionListener(new SwipeFractionListener() {
    @Override
    public void beginMenuSwipeFraction(SwipeMenuLayout swipeMenuLayout, float fraction) {
        Log.e(TAG, "top menu swipe fraction:"+fraction);

    }

    @Override
    public void endMenuSwipeFraction(SwipeMenuLayout swipeMenuLayout, float fraction) {
        Log.e(TAG, "bottom menu swipe fraction:"+fraction);
    }
});
```

If you case a few actions, just use [SimpleSwipeFractionListener][5]
```java
sml.setSwipeFractionListener(new SimpleSwipeFractionListener(){
    @Override
    public void beginMenuSwipeFraction(SwipeMenuLayout swipeMenuLayout, float fraction) {
        Log.e(TAG, "top menu swipe fraction:"+fraction);
    }
});
```

Custom
======

Supported custom attrs:

 * `sml_scroller_duration` Scroller duration(ms), `sml:sml_scroller_duration="250"`
 * `sml_auto_open_percent` Swipe menu auto open percent(relative to menu's width), `sml:sml_auto_open_percent="0.5"`
 * `sml_scroller_interpolator` Scroller open/close interpolation, `sml:sml_scroller_interpolator="@android:anim/bounce_interpolator"`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="SwipeMenu">
        <attr name="sml_scroller_duration" format="integer" />
        <attr name="sml_auto_open_percent" format="float"/>
        <attr name="sml_scroller_interpolator" format="reference"/>
    </declare-styleable>
</resources>
```

Thanks
======

Inspired by [baoyongzhang/SwipeMenuListView][1]

License
-------

    Copyright 2016 TUBB

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[1]: https://github.com/baoyongzhang/SwipeMenuListView
[2]: https://github.com/TUBB/SwipeMenu/tree/master/library/src/main/java/com/tubb/smrv/listener/SwipeSwitchListener.java
[3]: https://github.com/TUBB/SwipeMenu/tree/master/library/src/main/java/com/tubb/smrv/listener/SimpleSwipeSwitchListener.java
[4]: https://github.com/TUBB/SwipeMenu/tree/master/library/src/main/java/com/tubb/smrv/listener/SwipeFractionListener.java
[5]: https://github.com/TUBB/SwipeMenu/tree/master/library/src/main/java/com/tubb/smrv/listener/SimpleSwipeFractionListener.java
