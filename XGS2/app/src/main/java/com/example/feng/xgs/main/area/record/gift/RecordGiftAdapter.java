package com.example.feng.xgs.main.area.record.gift;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/6/6 0006.
 */

public class RecordGiftAdapter extends BaseEntityAdapter {

    public RecordGiftAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        holder.setText(R.id.tv_record_gift_item_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_record_gift_item_info, entity.getField(EntityKeys.INFO))
                .setText(R.id.tv_record_gift_item_price, "￥" + entity.getField(EntityKeys.PRICE));

        ImageView ivGiftHead = holder.getView(R.id.iv_record_gift_item);
        loadImg(entity.getField(EntityKeys.IMG_URL), ivGiftHead);

//        ImageView ivGiftDetail = holder.getView(R.id.iv_record_gift_ite);
//        loadImg(entity.getField(EntityKeys.IMG_URL), ivGiftHead);
    }
}
