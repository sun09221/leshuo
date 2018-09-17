package com.example.feng.xgs.main.find.shop.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.ContentKeys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2017/6/17.
 * 最近热搜
 */
public class ShopHotSearchLayout extends ViewGroup{

    private Context mContext;
    private float mHorizontalMargin;
    private float mVerticalMargin;
    private int mTextColor;
    private float mTextSize;
    private int mBackGroundResource;

    private List<Integer> mListHeight = new ArrayList<>();
    private List<List<View>> mListChildView = new ArrayList<>();

    private onSelectedListener mListener;

    public interface onSelectedListener{
        void onSelected(int position);
    }

    public ShopHotSearchLayout(Context context) {
        this(context,null);
    }


    public ShopHotSearchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        mContext = context;

        @SuppressLint("CustomViewStyleable")
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.label);
        mVerticalMargin = array.getDimension(R.styleable.label_vertical_margin, dp2px(8));
        mHorizontalMargin = array.getDimension(R.styleable.label_horizontal_margin, dp2px(8));
        mTextSize = array.getDimension(R.styleable.label_text_size, 14);
        mTextColor = array.getColor(R.styleable.label_text_color, ContextCompat.getColor(context, R.color.main));
        mBackGroundResource = array.getResourceId(R.styleable.label_background, R.drawable.gray_light_solid_radius_shape);
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mListHeight.clear();
        mListChildView.clear();
        int lineWidth = 0, lineHeight = 0;

        List<View> listChild = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(0,0);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            /**
             * 主要是判断每行有多少子控件，并添加到集合中，
             * 方便在下面摆放子控件时，判断是否换行
             * */
            if(lineWidth + childWidth > getWidth()){
                mListChildView.add(listChild);
                mListHeight.add(lineHeight);

                lineHeight = 0;
                lineWidth = 0;
                listChild = new ArrayList<>();
            }
            lineWidth = (int) (lineWidth + childWidth + mHorizontalMargin);
            lineHeight = (int) Math.max(lineHeight, childHeight);
            listChild.add(childView);
        }
        mListChildView.add(listChild);
        mListHeight.add(lineHeight);


        /**
         * 先获取每一行的控件集合listChild，和每一行的高度
         * */
        int left = 0, right = 0, top = 0, bottom = 0;
        for (int i = 0; i < mListChildView.size(); i++) {
            listChild = mListChildView.get(i);
            lineHeight = mListHeight.get(i);

            /**
             * 获取当前行的每一个控件
             * */
            for (int j = 0; j < listChild.size(); j++) {
                View childLineView = listChild.get(j);
                int viewWidth = childLineView.getMeasuredWidth();
                right = left + viewWidth;
                bottom = top + childLineView.getMeasuredHeight();
                /**
                 * 根据算出的子控件位置，摆放子控件
                 * */
                childLineView.layout(left,top,right,bottom);
                left = (int) (left + viewWidth + mHorizontalMargin);
            }
            left = 0;
            top = (int) (top + lineHeight  + mVerticalMargin);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //行宽高
        int lineWidth = 0,lineHeight = 0;
        //控件宽高
        int viewWidth = 0,viewHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            //触发子控件的测量
            childView.measure(0,0);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            /**
             * 如果子控件累计宽度大于父控件宽度、换行
             * 记录行宽viewWidth
             * 累积行高viewHeight
             * 清零行宽高
             * */
            if(lineWidth + childWidth > width){
                viewWidth = lineWidth;
                viewHeight = (int) (viewHeight + lineHeight + mVerticalMargin);

                lineHeight = 0;
                lineWidth = 0;
            }
            lineWidth = (int) (lineWidth + childWidth + mHorizontalMargin);
            lineHeight = Math.max(lineHeight,childHeight);
        }
        //添加最后一行的高度
        viewHeight = (int) (viewHeight + lineHeight + mVerticalMargin);
        viewWidth = Math.max(viewWidth , lineWidth);

        if(widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED){
            width = viewWidth;
        }
        if(heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED){
            height = viewHeight;
        }
        setMeasuredDimension(width,height);

    }

    public void setLabelData(List<String> titles){
        removeAllViews();

        if(titles != null && titles.size() > 0){

            for (int i = 0; i < titles.size(); i++) {
                addTextView(titles.get(i), i);
            }
        }else {

//            addNormalTextView();
        }

    }

    public void setLabelData(String[] titles){
        removeAllViews();

        if(titles != null && titles.length > 0){

            for (int i = 0; i < titles.length; i++) {
                addTextView(titles[i], i);
            }
        }else {
//            addNormalTextView();
        }
    }

    public void setLabelData(String result){
        if(TextUtils.isEmpty(result)){
//            addNormalTextView();
        }else {
            String titles[] = result.split(ContentKeys.DELIMIT);
            setLabelData(titles);
        }
    }



    private void addTextView(String title, int position){
        TextView tv = new TextView(mContext);
        tv.setText(title);
        tv.setPadding(dp2px(16), dp2px(10), dp2px(16), dp2px(10));
        tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(ContextCompat.getColor(mContext, R.color.gray));
        tv.setBackgroundResource(R.drawable.gray_light_solid_radius_shape);

        final int j = position;

        //如果没有回调点击事件，则不为标签添加点击事件 ---防止事件冲突
        if(mListener != null){
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(mListener != null){
                        mListener.onSelected(j);
//                    }
                }
            });
        }

        addView(tv);
    }

//    public void setOnClickItemLi

    public void setSelectedListener(onSelectedListener listener){
        mListener = listener;
    }

    public void setHorizontalMargin(int margin){
        mHorizontalMargin = dp2px(margin);
    }

    public void setVerticalMargin(int margin){
        mVerticalMargin = dp2px(margin);
    }

    public void setTextSize(int size){
        mTextSize = dp2px(size);
    }

    public void setTextColor(int color){
        mTextColor = ContextCompat.getColor(mContext, color);
    }

    public void setBackground(int drawable){
        mBackGroundResource = drawable;
    }

    /**
     * dp转换为px
     * */
    private int dp2px(float dpValue){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, mContext.getResources().getDisplayMetrics());
    }

}
