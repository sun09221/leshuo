package com.example.feng.xgs.base.recycler;

import android.support.annotation.ColorInt;

import com.choices.divider.DividerItemDecoration;

/**
 * Created by feng on 2018/3/16 0016.
 */

public class BaseDecoration extends DividerItemDecoration {

    private BaseDecoration() {
        setDividerLookup(new DividerLookupImpl());
    }

    public static BaseDecoration create() {
        return new BaseDecoration();
    }


    private BaseDecoration(int size, @ColorInt int color) {
        setDividerLookup(new DividerLookupImpl(size, color));
    }

    public static BaseDecoration create(int size, @ColorInt int color) {
        return new BaseDecoration(size, color);
    }
}
