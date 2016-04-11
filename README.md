SwipeMenuRecyclerView
=================
A swipe menu for cross slip, support left and right direction, low coupling, can fast rapid integration into your project

Preview
=======
![DEMO](https://github.com/TUBB/SwipeMenuRecyclerView/blob/master/art/demo.gif)

Usage
=====

Add to dependencies
```
dependencies {
    compile 'com.tubb.smrv:swipemenu-recyclerview:4.0.2'
}
```

Just use `SwipeMenuLayout`, we use `SwipeMenuLayout` ViewGroup to combine item content view and `left/right(at least one)` swipe menu
```xml
<com.tubb.smrv.SwipeMenuLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:sml="http://schemas.android.com/apk/res-auto"
    android:id="@+id/sml"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    sml:sml_scroller_interpolator="@android:anim/bounce_interpolator"
    sml:sml_auto_open_percent="0.5"
    sml:sml_scroller_duration="250">

    <include android:id="@id/smContentView" layout="@layout/item_simple_content"/>
    <include android:id="@id/smMenuViewLeft" layout="@layout/item_simple_left_menu"/>
    <include android:id="@id/smMenuViewRight" layout="@layout/item_simple_right_menu"/>

</com.tubb.smrv.SwipeMenuLayout>
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
More details please see the demo project.

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

Features
======== 
 
 * Support LinearLayoutManager、GridLayoutManager and StaggeredGridLayoutManager for RecyclerView
 * On-off swipe ability
 * Not intercept item touch event
 * Left/Right menu support, free switch

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