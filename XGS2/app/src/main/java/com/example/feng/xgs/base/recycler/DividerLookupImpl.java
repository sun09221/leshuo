package com.example.feng.xgs.base.recycler;

import android.graphics.Color;

import com.choices.divider.Divider;
import com.choices.divider.DividerItemDecoration;

/**
 * Created by feng on 2018/3/16 0016.
 * recyclerView添加divider
 */

public class DividerLookupImpl implements DividerItemDecoration.DividerLookup {

    private int mSize = 1;
    private int mColor = Color.parseColor("#EEEEEE");
    public DividerLookupImpl(){}

    public DividerLookupImpl(int size, int color){
        this.mSize = size;
        this.mColor = color;
    }
    @Override
    public Divider getVerticalDivider(int position) {
        return new Divider.Builder()
                .size(mSize)
                .color(mColor)
                .build();
    }

    @Override
    public Divider getHorizontalDivider(int position) {
        return new Divider.Builder()
                .size(mSize)
                .color(mColor)
                .build();
    }
}
