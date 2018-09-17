package com.example.feng.xgs.main.nearby.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/7.
 */

public class CustomCheckBox extends LinearLayout {

    /**
     * 原始每行最多显示的个数
     */
    private static final int INITIAL_MAX_COUNT = 3;

    private Context mContext;
    /**
     * CheckBox 列表
     */
    private List<CheckBox> mCheckBoxs;
    /**
     * 每一个CheckBox 显示的内容
     */
    private List<String> mSelectedBoxContents;

    public CustomCheckBox(Context context) {
        this(context, null);
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public CustomCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        this.setOrientation(VERTICAL);
        mCheckBoxs = new ArrayList<CheckBox>();
        mSelectedBoxContents = new ArrayList<String>();
    }

    private void initOrientation(int orientation) {
        this.setOrientation(orientation);
    }

    /**
     * 获取已选中的项
     *
     * @return
     */
    public List<String> getSelectedBoxContents() {
        return mSelectedBoxContents;
    }

    public void setCheckBoxs(List<String> checkBoxContents) {
        if (checkBoxContents == null || !mCheckBoxs.isEmpty())
            return;

        int checkBoxCount = checkBoxContents.size();
        if (checkBoxCount > INITIAL_MAX_COUNT) {
            initOrientation(LinearLayout.VERTICAL);
        } else {
            initOrientation(LinearLayout.HORIZONTAL);
        }
        if (this.getOrientation() == LinearLayout.VERTICAL) {// 竖直显示
            int checkBoxLines = checkBoxCount % INITIAL_MAX_COUNT == 0 ? checkBoxCount
                    / INITIAL_MAX_COUNT
                    : checkBoxCount / INITIAL_MAX_COUNT + 1;
            for (int i = 0; i < checkBoxLines; i++) {
                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams lyParams = new LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                /** 最后一行显示余下的个数，其余每行显示最大的显示数目 */
                int count = (i == (checkBoxLines - 1) ? checkBoxCount
                        % INITIAL_MAX_COUNT : INITIAL_MAX_COUNT);
                for (int j = 0; j < count; j++) {
                    String checkBoxText = checkBoxContents.get(i
                            * INITIAL_MAX_COUNT + j);
                    initCheckBox(linearLayout, checkBoxText, i
                            * INITIAL_MAX_COUNT + j);
                }

                this.addView(linearLayout, lyParams);
            }
        } else {// 水平显示
            for (int i = 0; i < checkBoxCount; i++) {
                String checkBoxText = checkBoxContents.get(i);
                initCheckBox(this, checkBoxText, i);
            }
        }

    }

    private void initCheckBox(LinearLayout linearLayout, String checkBoxText,
                              int index) {
        CheckBox checkBox = new CheckBox(mContext);
        checkBox.setText(checkBoxText);
        checkBox.setTextSize(15.0f);
        checkBox.setTextColor(Color.parseColor("#88000000"));
        checkBox.setOnCheckedChangeListener(listener);
        checkBox.setTag(index);
        mCheckBoxs.add(checkBox);
        LinearLayout.LayoutParams checkBoxParams = new LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        checkBoxParams.gravity = Gravity.CENTER_HORIZONTAL;
        checkBoxParams.weight = 1.0f;
        checkBoxParams.setMargins(160,20,0,0);
        linearLayout.addView(checkBox, checkBoxParams);
    }

    private CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            if (isChecked)// 添加已选中的复选框
                mSelectedBoxContents.add(buttonView.getText().toString());
            else
                mSelectedBoxContents.remove(buttonView.getText().toString());
        }
    };

}