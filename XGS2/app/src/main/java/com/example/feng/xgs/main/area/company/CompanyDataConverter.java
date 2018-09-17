package com.example.feng.xgs.main.area.company;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/31 0031.
 */

public class CompanyDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("imglist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            String imgUrl = array.getString(i);
            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
