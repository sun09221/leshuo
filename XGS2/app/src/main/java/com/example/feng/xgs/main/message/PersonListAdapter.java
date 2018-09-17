package com.example.feng.xgs.main.message;

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
 * Created by feng on 2018/5/4 0004.
 */

public class PersonListAdapter extends BaseEntityAdapter {

    public PersonListAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        holder.addOnClickListener(R.id.tv_person_list_item_private_letter);
        String divider = "\n";
//        String content = entity.getField(EntityKeys.NAME) + divider + entity.getField(EntityKeys.INFO);
        String content = entity.getField(EntityKeys.NAME) + divider + entity.getField("content");
        SpannableStringBuilder builder =
                SpannableUtil.setTextAndColor(mContext, content, divider, R.color.gray, 12);
        holder.setText(R.id.tv_person_list_item_name, builder);
        ImageView ivHead = holder.getView(R.id.iv_person_list_item);
        loadImg(entity.getField(EntityKeys.IMG_URL), ivHead, R.mipmap.icon_head_default);

    }
}
