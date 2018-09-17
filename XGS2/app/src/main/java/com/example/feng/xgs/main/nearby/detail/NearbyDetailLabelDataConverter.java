package com.example.feng.xgs.main.nearby.detail;

import android.text.TextUtils;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by feng on 2018/4/28 0028.
 */

public class NearbyDetailLabelDataConverter extends BaseDataConverter{


    private List<String> mLikeList = new ArrayList<>();

    @Override
    public ArrayList<BaseEntity> convert() {


        int[] arrayImg = new int[]{R.mipmap.icon_dating_campaign, R.mipmap.icon_dating_music,
                R.mipmap.icon_dating_food, R.mipmap.icon_dating_movie,
                R.mipmap.icon_dating_writer, R.mipmap.icon_dating_city,};

        for (int i = 0; i < 6; i++) {
            String[] likeArray = null;
            if(mLikeList.size() > i){
                String like = mLikeList.get(i);
                if(!TextUtils.isEmpty(like)){
                    likeArray = like.split(",");
                }
            }

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.IMG_URL, arrayImg[i])
                    .setField(EntityKeys.ENTITY, likeArray)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }

    public NearbyDetailLabelDataConverter setTitleList(List<String> list){
        mLikeList = list;
        return this;
    }
}
