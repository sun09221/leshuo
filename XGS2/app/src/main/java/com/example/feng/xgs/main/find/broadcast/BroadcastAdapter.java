package com.example.feng.xgs.main.find.broadcast;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.utils.SpannableUtil;

import java.util.List;

/**
 * Created by feng on 2018/5/7 0007.
 * 广播
 */

public class BroadcastAdapter extends BaseEntityAdapter{

    public BroadcastAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        String divider = "\n";
        String name = entity.getField(EntityKeys.NAME) + divider + entity.getField(EntityKeys.SEX);
        SpannableStringBuilder nameBuilder =
                SpannableUtil.setTextAndColor(mContext, name, divider, R.color.gray_light, 12);

        String info = "约会时间: " + entity.getField(EntityKeys.TIME)
                + "\n约会地点:" + entity.getField(EntityKeys.ADDRESS)
                + "\r\n" + entity.getField(EntityKeys.INFO);
        SpannableStringBuilder infoBuilder =
                SpannableUtil.setTextAndColor(mContext, info, "\r\n", R.color.black);

        holder.setText(R.id.tv_broadcast_item_name, nameBuilder)
                .setText(R.id.tv_broadcast_item_info, infoBuilder);

        loadImg(entity.getField(EntityKeys.IMG_URL), (ImageView) holder.getView(R.id.iv_broadcast_item));
    }
}
