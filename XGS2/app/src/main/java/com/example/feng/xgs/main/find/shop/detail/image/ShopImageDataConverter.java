package com.example.feng.xgs.main.find.shop.detail.image;

import android.text.TextUtils;

import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/15 0015.
 */

public class ShopImageDataConverter extends BaseDataConverter {

    private String mResult;

    @Override
    public ArrayList<BaseEntity> convert() {

        if(TextUtils.isEmpty(mResult)){
            return ENTITIES;
        }

        String[] imageArray = mResult.split(",");
        for (int i = 0; i < imageArray.length; i++) {
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.IMG_URL, imageArray[i])
                    .build();
            ENTITIES.add(entity);
        }
        return ENTITIES;
    }

    public ShopImageDataConverter setImageResult(String result){
        this.mResult = result;
        return this;
    }
}
