package com.example.feng.xgs.main.area.record.comment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.base.recycler.entity.BaseDataConverter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;

import java.util.ArrayList;

/**
 * Created by feng on 2018/6/7 0007.
 */

public class RecordCommentDataConverter extends BaseDataConverter {


    @Override
    public ArrayList<BaseEntity> convert() {

        JSONArray array = JSON.parseObject(getJsonData()).getJSONArray("datalist");
        int size = array.size();
        for (int i = 0; i < size; i++) {
            JSONObject object = JSON.parseObject(array.getString(i));
            String name = object.getString("nickname");
            String imgUrl = object.getString("header");
            String info = object.getString("content");
            String time = object.getString("createtime");

            BaseEntity entity = BaseEntity.builder()
                    .setField(EntityKeys.NAME, name)
                    .setField(EntityKeys.IMG_URL, imgUrl)
                    .setField(EntityKeys.INFO, info)
                    .setField(EntityKeys.TIME, time)
                    .build();
            ENTITIES.add(entity);

        }
        return ENTITIES;
    }
}
