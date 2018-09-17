package com.example.feng.xgs.main.find.store;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/5/7 0007.
 */

public class StoreAdapter extends BaseEntityAdapter {

    public StoreAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        loadImg(entity.getField(EntityKeys.IMG_URL), ((ImageView) holder.getView(R.id.iv_store_item)));
        holder.setText(R.id.tv_store_item_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_store_item_distance, entity.getField(EntityKeys.DISTANCE))
                .setText(R.id.tv_store_item_info, entity.getField(EntityKeys.INFO))
                .setText(R.id.tv_store_item_address, "地址: " + entity.getField(EntityKeys.ADDRESS_ALL))
                .setText(R.id.tv_store_item_mobile, "联系电话: " + entity.getField(EntityKeys.MOBILE));
    }
}
