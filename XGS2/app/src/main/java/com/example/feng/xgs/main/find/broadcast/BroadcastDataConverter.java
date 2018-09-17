package com.example.feng.xgs.main.find.broadcast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/7 0007.
 * 广播
 */

public class BroadcastDataConverter extends BaseDataConverter{
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("nickname");
            String sex = object.getString("sex");
            String imgUrl = object.getString("header");
            String peopleId = object.getString("peopleid");
            String time = object.getString("appointmenttime");
            String address = object.getString("appointmenaddress");
            String info = object.getString("content");

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.PEOPLE_ID, peopleId)
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.SEX, sex)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.TIME, time)
                    .setField(EntityKeys.ADDRESS, address)
                    .setField(EntityKeys.INFO, info)
                    .build();
            ENTITIES.add(entity);

        }

        return ENTITIES;
    }
}
