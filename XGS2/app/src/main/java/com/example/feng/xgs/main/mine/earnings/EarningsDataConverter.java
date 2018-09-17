package com.example.feng.xgs.main.mine.earnings;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/8 0008.
 */

public class EarningsDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        if(array != null){
            int size = array.size();
            for (int i = 0; i < size; i++) {
                JSONObject object = JSON.parseObject(array.getString(i));
                String time = object.getString("finishtime");
                String price = object.getString("money");

                BaseEntity entity = BaseEntity.builder()
                        .setField(EntityKeys.TIME, time)
                        .setField(EntityKeys.PRICE, price)
                        .build();
                ENTITIES.add(entity);
            }
        }
        return ENTITIES;
    }
}
