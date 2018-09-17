package com.example.feng.xgs.main.nearby.dynamic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/4/28 0028.
 */

public class NearbyDynamicDataConverter extends BaseDataConverter{
    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();

        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String time = object.getString("pushtime");
            String idOnly = object.getString("id");
            String content = object.getString("content");
            String imgUrl = object.getString("imagepath");
            String state = object.getString("state");
            String circle = object.getString("circle");
            String peopleId = object.getString("peopleid");

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, content)
                    .setField(EntityKeys.TIME, time)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.ID_ONLY, idOnly)
                    .build();
            ENTITIES.add(entity);
        }

        return ENTITIES;
    }
}
