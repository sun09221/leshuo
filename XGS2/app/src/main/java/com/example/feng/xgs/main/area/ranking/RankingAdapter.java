package com.example.feng.xgs.main.area.ranking;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.utils.DensityUtils;

import java.util.List;

/**
 * Created by feng on 2018/5/31 0031.
 */

public class RankingAdapter extends BaseEntityAdapter{

    public RankingAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        TextView tvPosition = holder.getView(R.id.tv_ranking_item_position);

        int position = holder.getLayoutPosition();
        tvPosition.setText(String.valueOf(position + 1));
        if(position == 0){
            tvPosition.setBackgroundResource(R.mipmap.icon_find_ranking_first);
        }else if(position == 1){
            tvPosition.setBackgroundResource(R.mipmap.icon_find_ranking_second);
        }else if(position == 2){
            tvPosition.setBackgroundResource(R.mipmap.icon_find_ranking_third);
        }else {
            tvPosition.setTextColor(ContextCompat.getColor(mContext, R.color.red_dark));
            tvPosition.setBackgroundResource(R.drawable.red_stroke_radius_shape);
            tvPosition.setGravity(Gravity.CENTER);
            int width = DensityUtils.dp2px(mContext, 28);
            tvPosition.setLayoutParams(new LinearLayout.LayoutParams(width, width));
        }

        holder.setText(R.id.tv_ranking_item_name, entity.getField(EntityKeys.NAME))
                .setText(R.id.tv_ranking_item_number, entity.getField(EntityKeys.NUMBER))
                .setText(R.id.tv_ranking_item_count, entity.getField(EntityKeys.COUNT));


    }
}
