package com.example.feng.xgs.main.area.model.detail.gift;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/6/1 0001.
 */

public class GiftListAdapter extends BaseEntityAdapter{

    public GiftListAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        int count = entity.getFieldObject(EntityKeys.COUNT);
        TextView tvCount = holder.getView(R.id.tv_gift_list_item_count);
        ImageView ivAdd = holder.getView(R.id.iv_gift_list_item_add);
        ImageView ivCut = holder.getView(R.id.iv_gift_list_item_cut);
        if(count == 0){
            ivCut.setVisibility(View.GONE);
            tvCount.setVisibility(View.GONE);
        }else {
            tvCount.setVisibility(View.VISIBLE);
            ivCut.setVisibility(View.VISIBLE);
        }
        holder.addOnClickListener(R.id.iv_gift_list_item_add);
        holder.addOnClickListener(R.id.iv_gift_list_item_cut);

        holder.setText(R.id.tv_gift_list_item_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_gift_list_item_info, entity.getField(EntityKeys.INFO))
                .setText(R.id.tv_gift_list_item_price, "￥" + entity.getField(EntityKeys.PRICE));

        ImageView imageView = holder.getView(R.id.iv_gift_list_item);
        loadImg(entity.getField(EntityKeys.IMG_URL), imageView);

    }
}
