package com.example.feng.xgs.main.area.record.like;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.utils.SpannableUtil;

import java.util.List;

/**
 * Created by feng on 2018/6/6 0006.
 */

public class RecordLikeAdapter extends BaseEntityAdapter {

    public RecordLikeAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        TextView tvName = holder.getView(R.id.tv_record_vote_item);
        String text = entity.getField(EntityKeys.NAME)
                + "\n" + entity.getField(EntityKeys.INFO);
        SpannableStringBuilder builder =
                SpannableUtil.setTextAndColor(mContext, text,
                        "\n", R.color.price_color, 12);
        tvName.setText(builder);

        ImageView ivHead = holder.getView(R.id.iv_record_vote_item);
        loadImg(entity.getField(EntityKeys.IMG_URL), ivHead);
    }
}
