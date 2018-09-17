package com.example.feng.xgs.main.move.detail;

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
 * Created by feng on 2018/4/28 0028.
 */

public class DynamicDetailAdapter extends BaseEntityAdapter{

    public DynamicDetailAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        holder.addOnClickListener(R.id.iv_dynamic_detail_item);
//        try {
//            holder.setText(R.id.tv_dynamic_detail_item_content, URLDecoder.decode(entity.getField(EntityKeys.INFO),"UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        holder.setText(R.id.tv_dynamic_detail_item_content, entity.getField(EntityKeys.INFO));
        TextView tvName = holder.getView(R.id.tv_dynamic_detail_item_name);
        String name = entity.getField(EntityKeys.NAME);
        if (entity.getField(EntityKeys.CREATETIME).equals("")||entity.getField(EntityKeys.CREATETIME)==null){
            holder.setGone(R.id.tv_dynamic_detail_item_time,false);
        }else {
            holder.setText(R.id.tv_dynamic_detail_item_time,entity.getField(EntityKeys.CREATETIME));
            holder.setGone(R.id.tv_dynamic_detail_item_time,true);
        }
        SpannableStringBuilder builder = SpannableUtil.setTextAndColor(mContext, name, "\n", R.color.gray_light, 12);
        tvName.setText(builder);

        ImageView imageView = holder.getView(R.id.iv_dynamic_detail_item);
        loadImg(entity.getField(EntityKeys.IMG_URL), imageView);
    }
}
