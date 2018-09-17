package com.example.feng.xgs.ui.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.feng.xgs.R;
import com.example.feng.xgs.utils.manager.LogUtils;


/**
 * TabViewGroup
 * Created by D on 2017/8/25.
 */
public class TabViewGroup extends RelativeLayout implements TabView {
    private Context context;
    private TextView tvTitle, tvNumber;
    private int mColorNormal, mColorSelect;

    public TabViewGroup(Context context) {
        this(context, null);
    }

    public TabViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        View root = LayoutInflater.from(context).inflate(R.layout.lib_ui_stab_view_tab, this);
        tvTitle = (TextView) root.findViewById(R.id.tv_title);
        tvNumber = (TextView) root.findViewById(R.id.tv_number);


    }

    @Override
    public void setText(String text) {
        LogUtils.d("setText: " + text);
        tvTitle.setText(text);
    }

    @Override
    public void setPadding(int padding) {
        setPadding(padding, 0, padding, 0);
    }

    @Override
    public void setNumber(String text, int visibility) {
        tvNumber.setText(text);
        tvNumber.setVisibility(visibility);
    }

    @Override
    public void notifyData(boolean focus) {
//        tvTitle.setTextColor(ContextCompat.getColor(context, focus ? mColorSelect : mColorNormal));
        tvTitle.setTextColor(focus ? mColorSelect : mColorNormal);
//        tvTitle.setTextColor(ContextCompat.getColor(context, R.color.green));
    }

    @Override
    public void onScroll(float factor) {

    }

    @Override
    public void setTitleTextSize(int size) {
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    @Override
    public void setTitleColorNormal(int color) {
        mColorNormal = color;
    }

    @Override
    public void setTitleColorSelect(int color) {
        mColorSelect = color;
    }


}
