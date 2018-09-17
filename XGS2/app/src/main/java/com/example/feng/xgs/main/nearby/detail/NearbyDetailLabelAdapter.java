package com.example.feng.xgs.main.nearby.detail;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.adapter.BaseEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.ui.weight.LabelLayout;

import java.util.List;

/**
 * Created by feng on 2018/4/28 0028.
 */

public class NearbyDetailLabelAdapter extends BaseEntityAdapter{

    public NearbyDetailLabelAdapter(int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BaseEntity entity) {
        holder.setImageResource(R.id.iv_nearby_detail_label_item, (Integer) entity.getFieldObject(EntityKeys.IMG_URL));

        int position = holder.getLayoutPosition();
        int textSize = 12;
        int textColor = R.color.main;
        int backGround = R.drawable.gray_light_solid_radius_shape;
        switch (position){
            case 0:
                textColor = R.color.label_campaign_dark;
                backGround = R.drawable.label_camaign_solid_radius_shape;
                break;
            case 1:
                textColor = R.color.label_music_dark;
                backGround = R.drawable.label_music_solid_radius_shape;
                break;
            case 2:
                textColor = R.color.label_food_dark;
                backGround = R.drawable.label_food_solid_radius_shape;
                break;
            case 3:
                textColor = R.color.label_movie_dark;
                backGround = R.drawable.label_movie_solid_radius_shape;
                break;
            case 4:
                textColor = R.color.label_writer_dark;
                backGround = R.drawable.label_writer_solid_radius_shape;
                break;
            case 5:
                textColor = R.color.label_city_dark;
                backGround = R.drawable.label_city_solid_radius_shape;
                break;
                default:
                    break;
        }

        LabelLayout labelLayout = holder.getView(R.id.label_nearby_detail_label_item);
        labelLayout.setBackground(backGround);
        labelLayout.setTextColor(textColor);
        String[] likeArray = entity.getFieldObject(EntityKeys.ENTITY);
//        List<String> titles = ;
        labelLayout.setLabelData(likeArray);
    }
}
