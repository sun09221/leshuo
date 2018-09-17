package com.example.feng.xgs.main.mine.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.main.mine.activity.detail.ActivitiesEnrollActivity;

import java.util.List;

/**
 * Created by feng on 2018/5/7 0007.
 */

public class ActivityAdapter extends BaseEntityAdapter {

    private String mType;
    public ActivityAdapter(int layoutResId, @Nullable List<BaseEntity> data, String type) {
        super(layoutResId, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder holder, final BaseEntity entity) {
        holder.setText(R.id.tv_activity_item_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_activity_item_time, entity.getField(EntityKeys.TIME))
                .setText(R.id.tv_activity_item_info, entity.getField(EntityKeys.INFO))
                .setText(R.id.tv_activity_item_address, "地址: " + entity.getField(EntityKeys.ADDRESS));

        loadImg(entity.getField(EntityKeys.IMG_URL), (ImageView) holder.getView(R.id.iv_activity_item));

        TextView tvPrice = holder.getView(R.id.tv_activity_item_price);
        TextView tvSure = holder.getView(R.id.tv_activity_item_sure);
        String price = entity.getField(EntityKeys.PRICE);
        if (TextUtils.isEmpty(price)) {
            tvPrice.setVisibility(View.GONE);
        } else {
            tvPrice.setText(String.format("￥%s", price));
            tvPrice.setVisibility(View.VISIBLE);
        }

        if(ContentKeys.FIND_ACTIVITY.equals(mType)){

        }else {
            tvSure.setVisibility(View.GONE);
        }

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivitiesEnrollActivity.class);
                intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.ID_ONLY));
                mContext.startActivity(intent);

            }
        });


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, ActivitiesDetailActivity.class);
//                intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.ID_ONLY));
//                intent.putExtra(ContentKeys.ACTIVITY_TYPE, entity.getField(EntityKeys.TYPE));
//                mContext.startActivity(intent);
//            }
//        });


    }
}
