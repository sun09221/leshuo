package com.example.feng.xgs.base.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.feng.xgs.core.GlideApp;

import butterknife.ButterKnife;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by feng on 2017/12/18.
 */

public abstract class BaseActivity extends AppCompatActivity{

    public String TAG = getClass().getSimpleName();

    public abstract Object setLayout();
    public abstract void onBindView(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBeforeLayout();

        if(setLayout() instanceof View){
            setContentView((View) setLayout());

        }else if(setLayout() instanceof  Integer){
            setContentView((int)setLayout());
        }else {
            throw new NullPointerException("setLayout must be int or View");
        }

        //顶部栏透明
        StatusBarCompat.translucentStatusBar(this, true);
//        int statusHeight = DensityUtils.getStatusBarHeight(this);

        ButterKnife.bind(this);
        baseInitEmptyView();
        onBindView(savedInstanceState);

    }

    public void baseInitEmptyView(){

    }


    public void initBeforeLayout(){

    }

    public void loadImg(String imgUrl, ImageView view){
        GlideApp.with(this)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .centerCrop()
                .dontAnimate()
                .into(view);
    }

    public void loadImg(String imgUrl, ImageView view, int drawable){
        GlideApp.with(this)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .centerCrop()
                .error(drawable)
                .dontAnimate()
                .into(view);
    }


}
