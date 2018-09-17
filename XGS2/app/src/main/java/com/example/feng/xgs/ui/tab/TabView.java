package com.example.feng.xgs.ui.tab;

/**
 * TabItem
 * Created by D on 2017/8/25.
 */
public interface TabView {
    void setText(String text);

    void setPadding(int padding);

    void setNumber(String text, int visibility);

    void notifyData(boolean focus);

    void onScroll(float factor);

    void setTitleTextSize(int size);

    void setTitleColorNormal(int color);

    void setTitleColorSelect(int color);
}
