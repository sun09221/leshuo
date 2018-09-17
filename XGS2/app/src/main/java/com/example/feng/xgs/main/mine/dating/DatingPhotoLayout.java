package com.example.feng.xgs.main.mine.dating;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.feng.xgs.R;
import com.example.feng.xgs.core.GlideApp;
import com.example.feng.xgs.utils.DensityUtils;

/**
 * Created by feng on 2018/5/10 0010.
 */

public class DatingPhotoLayout extends ViewGroup{

    private Context mContext;

    //子控件水平间距
    private int mHorizontalMargin;
    //子控件竖直间距
    private int mVerticalMargin;
    private int mScreenWidth;
    //布局左右间距
//    private int mLayoutMargin;


    public DatingPhotoLayout(Context context) {
        this(context, null);
    }

    public DatingPhotoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatingPhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
        addChildView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mScreenWidth,mScreenWidth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void init(Context context){
        this.mContext = context;

        mHorizontalMargin = dp2px(10);
        mVerticalMargin = dp2px(10);
    }


    public void addChildView(){
        removeAllViews();

        mScreenWidth = DensityUtils.getScreenWidth(mContext);
//        int mLayoutHeight =

        //图片的宽度 等于 屏幕宽度 - 左右布局边距 - 子控件间间距 再除以3
        int imageWidth = (int) ((mScreenWidth - mHorizontalMargin * 2 - dp2px(10) * 2) / 3);
        int bigImageWidth = (imageWidth * 2) + mHorizontalMargin;
        Log.d("TAG", "addChildView: 图片宽度" + imageWidth);
        int left = 0, right = 0, top = 0, bottom = 0;
        for (int i = 0; i < 6; i++) {
            final ImageView image = new ImageView(mContext);
            //第一张图片大图显示(占屏幕宽度2/3)
            if(i == 0){
                left = 0;
                right = left + bigImageWidth;
                top = 0;
                bottom = top + bigImageWidth;
                image.setLayoutParams(new LinearLayout.LayoutParams(bigImageWidth, bigImageWidth));
            }
            //第二三张图片在大图右边，等比例纵向显示，宽度为屏幕1/3
            else if(i < 3){
                left = bigImageWidth + mHorizontalMargin;
                right = left + imageWidth;
                if(i == 1){
                    top = 0;
                }else {
                    top = imageWidth + mVerticalMargin;
                }
                bottom = top + imageWidth;
                image.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
            //后三张图片 在大图下面，等比例横向显示，宽度为屏幕1/3
            }else {
                left = imageWidth * (i % 3) + mHorizontalMargin * (i %3);
                right = left + imageWidth;
                top = bigImageWidth + mVerticalMargin;
                bottom = top + imageWidth;
                image.setLayoutParams(new ViewGroup.LayoutParams(imageWidth, imageWidth));
            }

            Log.d("TAG", "addChildView: 第几次循环" + i);
            Log.d("TAG", "addChildView: left:" + left + "top:" + top + "right: " + right + "bottom" + bottom);
            image.setImageResource(R.mipmap.icon_dating_add);
            image.setScaleType(ImageView.ScaleType.CENTER);
            image.setBackgroundResource(R.color.white_dark);
            addView(image);

            final int position = i;
            image.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onItemClick(position, image);
                    }
                }
            });

            image.layout(left, top, right, bottom);
        }


    }

    public void setData(String imgUrls){
        if(TextUtils.isEmpty(imgUrls)){
            return;
        }

        String[] imgUrlArray = imgUrls.split(",");
        setData(imgUrlArray);
    }

    public void setData(String[] imgUrls){
        if(imgUrls == null) return;

        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            if(i < imgUrls.length){
                ImageView imageView = (ImageView) getChildAt(i);
                String imgUrl = imgUrls[i];
                loadImage(imageView, imgUrl);
            }

        }
    }

    private void loadImage(ImageView imageView, String imgUrl){
        GlideApp.with(mContext)
                .load(imgUrl)
                .placeholder(R.mipmap.icon_dating_add)
                .error(R.mipmap.icon_dating_add)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }


    private IDatingLayoutPhotoListener mListener;

    public interface IDatingLayoutPhotoListener{
        void onItemClick(int position, ImageView imageView);
    }

    public void setOnLayoutPhotoItemListener(IDatingLayoutPhotoListener listener){
        this.mListener = listener;
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, mContext.getResources().getDisplayMetrics());
    }
}
