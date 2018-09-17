package com.example.feng.xgs.main.move.reward;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/2 0002.
 */

public class RewardDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("rewardname");
            String price = object.getString("price");
            String idOnly = object.getString("rewardid");
            String imgUrl = object.getString("imagepath");

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.PRICE, price)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.ID_ONLY, idOnly)
                    .build();

            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
