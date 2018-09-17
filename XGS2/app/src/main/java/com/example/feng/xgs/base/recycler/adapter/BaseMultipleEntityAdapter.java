package com.example.feng.xgs.base.recycler.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.core.GlideApp;

import java.util.List;


/**
 * Created by feng on 2017/12/18.
 */
public abstract class BaseMultipleEntityAdapter extends BaseMultiItemQuickAdapter<BaseEntity,
        BaseViewHolder> implements BaseQuickAdapter.SpanSizeLookup {


    private static final RequestOptions OPTIONS = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .dontAnimate();

    public BaseMultipleEntityAdapter(List<BaseEntity> data) {
        super(data);
        init();
    }

    private void init() {
        //设置宽度监听
        setSpanSizeLookup(this);
        //打开加载的动画效果
        openLoadAnimation();
        //多次执行动画
        isFirstOnly(true);
//        setLoadMoreView(new CustomLoadMoreView());
    }

    public void loadImg(String imgUrl, ImageView view){
        GlideApp.with(mContext)
                .load(imgUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public void loadImg(String imgUrl, ImageView view, int drawable){
        GlideApp.with(mContext)
                .load(imgUrl)
                .placeholder(drawable)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    @Override
    public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
        BaseEntity entity = getData().get(position);
        if(entity != null){
            int spanSize = entity.getFieldObject(EntityKeys.SPAN_SIZE);
//            if(spanSize != null){
//                return Integer.valueOf(spanSize);
//            }
            return spanSize;
        }

        return 0;
    }
}
