package com.example.feng.xgs.main.nearby.dynamic;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.List;

/**
 * Created by feng on 2018/4/28 0028.
 */

public class NearbyDynamicAdapter extends BaseEntityAdapter{

    public NearbyDynamicAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        holder.setText(R.id.tv_nearby_dynamic_item_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_nearby_dynamic_item_time, entity.getField(EntityKeys.TIME));

        String imgUrl = entity.getField(EntityKeys.IMG_URL);
        ImageView imageView = holder.getView(R.id.iv_nearby_dynamic_item);
        loadImg(imgUrl, imageView);

    }
}
