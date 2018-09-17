package com.example.feng.xgs.main.mine.activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/7 0007.
 */

public class ActivityDataConverter extends BaseDataConverter {
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String idOnly = object.getString("id");
            String name = object.getString("activityname");
            String address = object.getString("address");
            String time = object.getString("activitytime");
            String price = object.getString("money");
            String information = object.getString("information");
            String detail = object.getString("detailedcontent");
            String imgUrl = object.getString("images");
            String type = object.getString("type");

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.ID_ONLY, idOnly)
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.ADDRESS, address)
                    .setField(EntityKeys.TIME, time)
                    .setField(EntityKeys.PRICE, price)
                    .setField(EntityKeys.INFO, information)
                    .setField(EntityKeys.DETAIL, detail)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.TYPE, type)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
