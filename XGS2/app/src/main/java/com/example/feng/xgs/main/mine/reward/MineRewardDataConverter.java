package com.example.feng.xgs.main.mine.reward;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/9 0009.
 */

public class MineRewardDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        if(array != null){
            int size = array.size();
            for (int i = 0; i < size; i++) {
                JSONObject object = JSON.parseObject(array.getString(i));
                String idOnly = object.getString("id");
                String count = object.getString("num");
                String price = object.getString("money");
                String countGive = object.getString("givenum");//增送

                BaseEntity entity = BaseEntity.builder()
                        .setField(EntityKeys.ID_ONLY, idOnly)
                        .setField(EntityKeys.COUNT, count)
                        .setField(EntityKeys.PRICE, price)
                        .setField(EntityKeys.COUNT_GIVE, countGive)
                        .build();
                ENTITIES.add(entity);

            }
        }


        return ENTITIES;
    }
}
