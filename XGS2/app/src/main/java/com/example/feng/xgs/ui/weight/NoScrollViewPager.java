package com.example.feng.xgs.ui.weight;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by feng on 2018/4/26 0026.
 * 禁止ViewPager左右滑动
 */

public class NoScrollViewPager extends ViewPager{


    private final String TAG = NoScrollViewPager.class.getSimpleName();
//    private boolean scroll = true;//false 禁止viewpager左右滑动

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    /**
//     * @param scroll
//     */
//    public void setScroll(boolean scroll) {
//        this.scroll = scroll;
//    }
//
//    @Override
//    public void scrollTo(int x, int y) {
//        super.scrollTo(x, y);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }

}
