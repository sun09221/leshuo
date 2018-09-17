package com.example.feng.xgs.main.find.shop.detail.image;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/5/15 0015.
 */

public class ShopImageAdapter extends BaseEntityAdapter {

    public ShopImageAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        ImageView imageView = holder.getView(R.id.iv_shop_detail_image_item);
        loadImg1(entity.getField(EntityKeys.IMG_URL), imageView);
    }
}
